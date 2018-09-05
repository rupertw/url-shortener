package me.www.urlshortener.service.impl;

import me.www.urlshortener.constant.RedisConsts;
import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.repository.ShortUrlRepository;
import me.www.urlshortener.service.ShortUrlService;
import me.www.urlshortener.util.Base62;
import me.www.urlshortener.util.SnowFlake;
import me.www.urlshortener.vo.ShortUrlVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 短地址Service
 *
 * @author www
 * @since 2018/7/21 22:25
 */
@Service
public class ShortUrlServiceImpl implements ShortUrlService, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 清理缓存时每次最多清理数
     */
    private final static Integer LR_SHORTEN_URL_CLEAR_PER_LIMIT = 10;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${url.shortener.service.host}")
    private String serviceHost;

    @Override
    public ShortUrl shortenUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        /* 查询是否已简化url */
        String shortUrlKey = (String) stringRedisTemplate.opsForHash().get(RedisConsts.LR_SHORTEN_URL_HASH, url);
        if (StringUtils.isNotEmpty(shortUrlKey)) {
            Optional<ShortUrl> optional = shortUrlRepository.findById(shortUrlKey.split(":")[1]);
            if (optional.isPresent()) {
                stringRedisTemplate.opsForZSet().add(RedisConsts.LR_SHORTEN_URL_ZSET, url, System.currentTimeMillis());
                return optional.get();
            }
        }

        /* 简化url */
        String code = Base62.encode(snowFlake.nextId());
        ShortUrl shortUrl = new ShortUrl(code, url);
        shortUrlRepository.save(shortUrl);
        // 保存到最近简化url缓存
        stringRedisTemplate.opsForZSet().add(RedisConsts.LR_SHORTEN_URL_ZSET, url, System.currentTimeMillis());
        stringRedisTemplate.opsForHash().put(RedisConsts.LR_SHORTEN_URL_HASH, url, RedisConsts.SHORT_URL_KEY_PREFIX + code);

        return shortUrl;
    }

    @Override
    public void afterPropertiesSet() {
        // 线程处理：采用lru算法清除最近简化url缓存
        threadPoolTaskExecutor.execute(() -> {
            while (true) {
                stringRedisTemplate.watch(RedisConsts.LR_SHORTEN_URL_ZSET);
                Long zsetSize = stringRedisTemplate.opsForZSet().size(RedisConsts.LR_SHORTEN_URL_ZSET);
                if (zsetSize <= RedisConsts.LR_SHORTEN_URL_LIMIT) {
                    stringRedisTemplate.unwatch();
                    try {
                        // 每分钟执行一次批量清理（高并发下，每秒执行一次批量清理，注意要保证清理速度大于产生速度）
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                Long end_index = Long.min(zsetSize - RedisConsts.LR_SHORTEN_URL_LIMIT, LR_SHORTEN_URL_CLEAR_PER_LIMIT); // 每次最多清理10个
                Set<String> urlSet = stringRedisTemplate.opsForZSet().range(RedisConsts.LR_SHORTEN_URL_ZSET, 0, end_index - 1); // 注：查询redis数据，必须在multi()之前
                stringRedisTemplate.multi();
                stringRedisTemplate.opsForHash().delete(RedisConsts.LR_SHORTEN_URL_HASH, urlSet.toArray());
                stringRedisTemplate.opsForZSet().removeRange(RedisConsts.LR_SHORTEN_URL_ZSET, 0, end_index - 1);
                List<Object> results = stringRedisTemplate.exec();
                // empty response indicates that the transaction was aborted due to the watched key changing.
                if (results.isEmpty()) {
                    //logger.info("");
                } else {
                    logger.info("采用lru算法清除最近简化url缓存" + end_index + "个元素.");
                }
            }
        });
    }

    @Override
    public ShortUrl getShortUrl(String code, Boolean toVisit) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        Optional<ShortUrl> optional = shortUrlRepository.findById(code);
        if (optional.isPresent()) {
            ShortUrl shortUrl = optional.get();
            if (toVisit) {
                // 增加访问次数
                stringRedisTemplate.opsForZSet().incrementScore(RedisConsts.VISIT_COUNT_KEY, RedisConsts.SHORT_URL_KEY_PREFIX + shortUrl.getCode(), 1);
            }
            return shortUrl;
        } else {
            return null;
        }

    }

    @Override
    public List<ShortUrlVO> topnVisit(Integer topn) {
        // 查询数据
        Set<ZSetOperations.TypedTuple<String>> topnSet = stringRedisTemplate.opsForZSet().reverseRangeWithScores(RedisConsts.VISIT_COUNT_KEY, 0, topn - 1);
        if (topnSet.isEmpty()) {
            return Collections.emptyList();
        }

        // 数据转换
        List<ShortUrlVO> topnList = new ArrayList<>();
        topnSet.forEach(objectTypedTuple -> {
            String shortUrlKey = objectTypedTuple.getValue();
            Integer visitCount = objectTypedTuple.getScore().intValue();

            Optional<ShortUrl> optional = shortUrlRepository.findById(shortUrlKey.split(":")[1]);
            if (optional.isPresent()) {
                ShortUrl shortUrl = optional.get();
                ShortUrlVO vo = new ShortUrlVO();
                vo.setSurl(serviceHost + "/" + shortUrl.getCode());
                vo.setLurl(shortUrl.getUrl());
                vo.setVisitCount(visitCount);
                topnList.add(vo);
            }
        });

        return topnList;
    }

}
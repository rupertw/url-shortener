package me.www.urlshortener.service.impl;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: www
 * @date: 2018/7/21 22:25
 * @description: 短地址Service
 */
@Service
public class ShortUrlServiceImpl implements ShortUrlService, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * redis key: ShortUrl存储key前缀 (redis类型: hash)
     */
    public final static String SHORT_URL_KEY_PREFIX = "short_url:";

    /**
     * redis key: 保存最近简化url相关
     */
    public final static String LR_SHORTEN_URL_ZSET = "lr_shorten_url_zset";
    public final static String LR_SHORTEN_URL_HASH = "lr_shorten_url_hash";
    public final static Integer LR_SHORTEN_URL_LIMIT = 100; // 缓存容量限制
    public final static Integer LR_SHORTEN_URL_CLEAR_PER_LIMIT = 10; // 清理缓存时每次最多清理数

    /**
     * redis key: 访问计数(redis类型: ZSet)
     */
    public final static String VISIT_COUNT_KEY = "visit_count";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
        String shortUrlKey = (String) redisTemplate.opsForHash().get(LR_SHORTEN_URL_HASH, url);
        if (StringUtils.isNotEmpty(shortUrlKey)) {
            Optional<ShortUrl> optional = shortUrlRepository.findById(shortUrlKey.split(":")[1]);
            if (optional.isPresent()) {
                redisTemplate.opsForZSet().add(LR_SHORTEN_URL_ZSET, url, System.currentTimeMillis());
                return optional.get();
            }
        }

        /* 简化url */
        String code = Base62.encode(snowFlake.nextId());
        ShortUrl shortUrl = new ShortUrl(code, url);
        shortUrlRepository.save(shortUrl);
        // 保存到最近简化url缓存
        redisTemplate.opsForZSet().add(LR_SHORTEN_URL_ZSET, url, System.currentTimeMillis());
        redisTemplate.opsForHash().put(LR_SHORTEN_URL_HASH, url, SHORT_URL_KEY_PREFIX + code);

        return shortUrl;
    }

    @Override
    public void afterPropertiesSet() {
        // 线程处理：采用lru算法清除最近简化url缓存
        threadPoolTaskExecutor.execute(() -> {
            while (true) {
                redisTemplate.watch(LR_SHORTEN_URL_ZSET);
                Long zsetSize = redisTemplate.opsForZSet().size(LR_SHORTEN_URL_ZSET);
                if (zsetSize <= LR_SHORTEN_URL_LIMIT) {
                    redisTemplate.unwatch();
                    try {
                        // 每分钟执行一次批量清理（高并发下，每秒执行一次批量清理，注意要保证清理速度大于产生速度）
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                Long end_index = Long.min(zsetSize - LR_SHORTEN_URL_LIMIT, LR_SHORTEN_URL_CLEAR_PER_LIMIT); // 每次最多清理10个
                Set<Object> urlSet = redisTemplate.opsForZSet().range(LR_SHORTEN_URL_ZSET, 0, end_index - 1); // 注：查询redis数据，必须在multi()之前
                redisTemplate.multi();
                redisTemplate.opsForHash().delete(LR_SHORTEN_URL_HASH, urlSet.toArray());
                redisTemplate.opsForZSet().removeRange(LR_SHORTEN_URL_ZSET, 0, end_index - 1);
                List<Object> results = redisTemplate.exec();
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
                redisTemplate.opsForZSet().incrementScore(VISIT_COUNT_KEY, SHORT_URL_KEY_PREFIX + shortUrl.getCode(), 1);
            }
            return shortUrl;
        } else {
            return null;
        }

    }

    /**
     * 查询访问计数排行
     *
     * @param topn
     * @return
     */
    @Override
    public List<ShortUrlVO> topnVisit(Integer topn) {
        // 查询数据
        Set<ZSetOperations.TypedTuple<Object>> topnSet = redisTemplate.opsForZSet().reverseRangeWithScores(VISIT_COUNT_KEY, 0, topn - 1);
        if (topnSet.isEmpty()) {
            return Collections.emptyList();
        }

        // 数据转换
        List<ShortUrlVO> topnList = new ArrayList<>();
        topnSet.forEach(objectTypedTuple -> {
            String shortUrlKey = (String) objectTypedTuple.getValue();
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

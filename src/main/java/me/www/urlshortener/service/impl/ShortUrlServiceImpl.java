package me.www.urlshortener.service.impl;

import me.www.urlshortener.constant.RedisConsts;
import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.repository.ShortUrlRepository;
import me.www.urlshortener.service.ShortUrlService;
import me.www.urlshortener.util.Base62;
import me.www.urlshortener.util.SnowFlake;
import me.www.urlshortener.vo.ShortUrlVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 短地址Service
 *
 * @author www
 * @since 2018/7/21 22:25
 */
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private SnowFlake snowFlake;

    @Value("${url.shortener.service.host}")
    private String serviceHost;

    /**
     * @param url
     * @return
     * @see me.www.urlshortener.listener.ApplicationContextListener#onApplicationEvent(ContextRefreshedEvent)
     */
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
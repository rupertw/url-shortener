package me.www.urlshortener.service.impl;

import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.repository.ShortUrlRepository;
import me.www.urlshortener.service.ShortUrlService;
import me.www.urlshortener.util.Base62;
import me.www.urlshortener.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author: www
 * @date: 2018/7/21 22:25
 * @description: 短地址Service
 */
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    /**
     * redis key: ShortUrl存储key前缀 (redis类型: hash)
     */
    public final static String SHORT_URL_KEY_PREFIX = "short_url:";

    /**
     * redis key: 保存最近简化url相关
     */
    public final static String LR_SHORTEN_URL_ZSET = "lr_shorten_url_zset";
    public final static String LR_SHORTEN_URL_HASH = "lr_shorten_url_hash";
    public final static Integer LR_SHORTEN_URL_NUM = 2;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private SnowFlake snowFlake;

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
        // 采用lru算法清除缓存(事务处理，失败重试)
        for (int i = 0; i < 5; i++) {
            redisTemplate.watch(LR_SHORTEN_URL_ZSET);
            Long zsetSize = redisTemplate.opsForZSet().size(LR_SHORTEN_URL_ZSET);
            if (zsetSize <= LR_SHORTEN_URL_NUM) {
                redisTemplate.unwatch();
                break;
            }
            Set<Object> urlSet = redisTemplate.opsForZSet().range(LR_SHORTEN_URL_ZSET, 0, zsetSize - LR_SHORTEN_URL_NUM - 1); // 注：查询redis数据，必须在multi()之前
            redisTemplate.multi();
            redisTemplate.opsForHash().delete(LR_SHORTEN_URL_HASH, urlSet.toArray());
            redisTemplate.opsForZSet().removeRange(LR_SHORTEN_URL_ZSET, 0, zsetSize - LR_SHORTEN_URL_NUM - 1);
            List<Object> results = redisTemplate.exec();
            // empty response indicates that the transaction was aborted due to the watched key changing.
            if (results.isEmpty()) {
                continue;
            } else {
                break;
            }
        }

        return shortUrl;
    }

    @Override
    public ShortUrl getShortUrl(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        Optional<ShortUrl> optional = shortUrlRepository.findById(code);
        return optional.isPresent() ? optional.get() : null;
    }

}

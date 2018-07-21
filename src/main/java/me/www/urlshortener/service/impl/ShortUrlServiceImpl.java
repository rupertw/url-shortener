package me.www.urlshortener.service.impl;

import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.service.ShortUrlService;
import me.www.urlshortener.util.Base62;
import me.www.urlshortener.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author: www
 * @date: 2018/7/21 22:25
 * @description: TODO
 */
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private HashMap<String, ShortUrl> cacheMap = new HashMap<>();

    @Autowired
    private SnowFlake snowFlake;

    @Override
    public ShortUrl generateShortUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        String code = Base62.encode(snowFlake.nextId());

        ShortUrl shortUrl = new ShortUrl(code, url);
        cacheMap.put(code, shortUrl);

        return shortUrl;
    }

    @Override
    public ShortUrl getShortUrl(String code) {
        return StringUtils.isEmpty(code) ? null : cacheMap.get(code);
    }

}

package me.www.urlshortener.service.impl;

import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.repository.ShortUrlRepository;
import me.www.urlshortener.service.ShortUrlService;
import me.www.urlshortener.util.Base62;
import me.www.urlshortener.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: www
 * @date: 2018/7/21 22:25
 * @description: 短地址Service
 */
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private SnowFlake snowFlake;

    @Override
    public ShortUrl generateShortUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        String code = Base62.encode(snowFlake.nextId());

        ShortUrl shortUrl = new ShortUrl(code, url);
        shortUrlRepository.save(shortUrl);

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

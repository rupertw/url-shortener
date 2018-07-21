package me.www.urlshortener.service;

import me.www.urlshortener.domain.ShortUrl;

/**
 * @author: www
 * @date: 2018/7/21 22:19
 * @description: TODO
 */
public interface ShortUrlService {

    ShortUrl generateShortUrl(String url);

    ShortUrl getShortUrl(String code);

}
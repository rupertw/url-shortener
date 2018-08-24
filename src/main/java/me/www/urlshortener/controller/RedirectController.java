package me.www.urlshortener.controller;

import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.service.ShortUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * 短地址访问重定向
 *
 * @author www
 * @since 2018/5/20 13:56
 */
@RestController
public class RedirectController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping(value = "/{scode}")
    public ResponseEntity<Void> redirect(@PathVariable String scode) {
        logger.info("request for redirect: " + scode);

        // 获取原网址
        ShortUrl shortUrl = shortUrlService.getShortUrl(scode, true);

        if (shortUrl == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders respHeader = new HttpHeaders();
        respHeader.setLocation(URI.create(shortUrl.getUrl()));
        respHeader.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
        ResponseEntity<Void> resp = new ResponseEntity<>(respHeader, HttpStatus.FOUND);
        return resp;
    }

}

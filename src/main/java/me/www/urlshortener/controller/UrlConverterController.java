package me.www.urlshortener.controller;

import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.rest.IllegalParamsException;
import me.www.urlshortener.rest.RestResult;
import me.www.urlshortener.rest.RestResultGenerator;
import me.www.urlshortener.service.ShortUrlService;
import me.www.urlshortener.util.DomainUtil;
import me.www.urlshortener.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: www
 * @date: 2018/5/20 14:29
 * @description: 长短地址转换
 */
@RestController
public class UrlConverterController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShortUrlService shortUrlService;

    @Value("${url.shortener.domain.name}")
    private String domain;

    private Pattern pattern;

    @PostConstruct
    private void init() {
        String patternStr = "^http://www\\.me/([a-zA-Z0-9]+)$";
        pattern = Pattern.compile(patternStr);
    }

    /**
     * 缩短网址
     *
     * @return
     */
    @GetMapping("/shorten")
    public RestResult<String> shorten(String url) {
        // 1、参数验证
        if (!UrlUtil.isValid(url)) {
            throw new IllegalParamsException();
        }
        url = UrlUtil.normalizeUrl(url);
        if (DomainUtil.isInBlackList(UrlUtil.getDomain(url))) {
            throw new IllegalParamsException();
        }

        // 2、生成短网址
        ShortUrl shortUrl = shortUrlService.generateShortUrl(url);
        String surl = domain + "/" + shortUrl.getCode();

        return RestResultGenerator.genResult(surl);
    }

    /**
     * 网址还原
     *
     * @return
     */
    @GetMapping("/original")
    public ResponseEntity<RestResult<String>> original(String surl) {
        // 1、参数验证
        if (!UrlUtil.isValid(surl)) {
            throw new IllegalParamsException();
        }
        surl = UrlUtil.normalizeUrl(surl);
        // 正则验证
        Matcher matcher = pattern.matcher(surl);
        if (!matcher.find()) {
            throw new IllegalParamsException();
        }

        // 2、获取原网址
        String scode = matcher.group(1);
        ShortUrl shortUrl = shortUrlService.getShortUrl(scode);

        ResponseEntity<RestResult<String>> resp;
        if (shortUrl == null) {
            resp = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            resp = ResponseEntity.ok(RestResultGenerator.genResult(shortUrl.getUrl()));
            // resp = new ResponseEntity<>(RestResultGenerator.genResult(shortUrl.getUrl()), HttpStatus.OK);
        }

        return resp;
    }

}

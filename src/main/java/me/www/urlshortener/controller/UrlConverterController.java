package me.www.urlshortener.controller;

import io.swagger.annotations.*;
import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.rest.IllegalParamsException;
import me.www.urlshortener.rest.RestErrorEnum;
import me.www.urlshortener.rest.RestResult;
import me.www.urlshortener.rest.RestResultGenerator;
import me.www.urlshortener.service.ShortUrlService;
import me.www.urlshortener.util.DomainUtil;
import me.www.urlshortener.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 长短地址转换接口
 *
 * @author www
 * @since 1.0.0
 */
@RestController
public class UrlConverterController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShortUrlService shortUrlService;

    @Value("${url.shortener.service.host}")
    private String serviceHost;

    /**
     * 短网址匹配正则表达式
     */
    private Pattern surlPattern;

    @PostConstruct
    private void init() {
        // "^http://www\\.me/([a-zA-Z0-9]+)$"
        String patternStr = "^" + serviceHost.replace(".", "\\.") + "/([a-zA-Z0-9]+)$";
        surlPattern = Pattern.compile(patternStr);
    }

    /**
     * 短网址生成
     *
     * @param url 长网址
     * @return
     */
    @ApiOperation("短网址生成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "长网址", required = true, dataTypeClass = String.class, paramType = "query", allowEmptyValue = false)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "", response = RestResult.class)
    })
    @PostMapping("/shorten")
    public RestResult<String> shorten(String url) {
        logger.info("request for /shorten: " + url);

        // 1、参数验证
        if (StringUtils.isEmpty(url)) {
            throw new IllegalParamsException();
        }
        if (!UrlUtil.isValid(url)) {
            return RestResultGenerator.genErrorResult(RestErrorEnum.URL_NOT_VALID);
        }
        url = UrlUtil.normalizeUrl(url);
        if (DomainUtil.isInBlackList(UrlUtil.getDomain(url))) {
            return RestResultGenerator.genErrorResult(RestErrorEnum.URL_IN_BLACKLIST);
        }

        // 2、生成短网址
        ShortUrl shortUrl = shortUrlService.shortenUrl(url);
        String surl = serviceHost + "/" + shortUrl.getCode();

        return RestResultGenerator.genResult(surl);
    }

    /**
     * 短网址还原
     *
     * @param surl 短网址
     * @return
     */
    @ApiOperation("短网址还原")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "surl", value = "短网址", required = true, dataTypeClass = String.class, paramType = "query", allowEmptyValue = false)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "", response = RestResult.class)
    })
    @GetMapping("/original")
    public ResponseEntity<RestResult<String>> original(String surl) {
        logger.info("request for /original: " + surl);

        // 1、参数验证
        if (StringUtils.isEmpty(surl)) {
            throw new IllegalParamsException();
        }
        if (!UrlUtil.isValid(surl)) {
            return new ResponseEntity<>(RestResultGenerator.genErrorResult(RestErrorEnum.URL_NOT_VALID), HttpStatus.OK);
        }
        surl = UrlUtil.normalizeUrl(surl);
        // 正则验证
        Matcher matcher = surlPattern.matcher(surl);
        if (!matcher.find()) {
            throw new IllegalParamsException();
        }

        // 2、获取原网址
        String scode = matcher.group(1);
        ShortUrl shortUrl = shortUrlService.getShortUrl(scode, false);

        ResponseEntity<RestResult<String>> resp;
        if (shortUrl == null) {
            resp = new ResponseEntity<>(RestResultGenerator.genErrorResult(RestErrorEnum.NOT_EXIST), HttpStatus.OK);
        } else {
            resp = ResponseEntity.ok(RestResultGenerator.genResult(shortUrl.getUrl()));
        }

        return resp;
    }

}

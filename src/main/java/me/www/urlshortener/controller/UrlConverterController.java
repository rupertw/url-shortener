package me.www.urlshortener.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: www
 * @date: 2018/5/20 14:29
 * @description: 长短地址转换
 */
@RestController
public class UrlConverterController {

    /**
     * 缩短网址
     *
     * @return
     */
    @RequestMapping("/surl")
    public String surl(String lurl) {
        // TODO
        return null;
    }

    /**
     * 网址还原
     *
     * @return
     */
    @RequestMapping("/lurl")
    public String lurl(String surl) {
        // TODO
        return null;
    }

}

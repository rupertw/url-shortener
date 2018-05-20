package me.www.urlshortener.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: www
 * @date: 2018/5/20 13:56
 * @description: 短地址访问重定向
 */
@Controller
public class RedirectController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/{surlCode}")
    public ResponseEntity<Void> redirect(@PathVariable String surlCode) {
        // TODO
        return null;
    }
}

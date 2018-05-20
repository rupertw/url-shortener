package me.www.urlshortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: www
 * @date: 2018/5/20 13:56
 * @description: 短地址访问重定向
 */
@RestController
public class RedirectController {

    @GetMapping(value = "/{surlCode}")
    public ResponseEntity<Void> redirect(@PathVariable String surlCode) {
        // TODO
        return null;
    }
}

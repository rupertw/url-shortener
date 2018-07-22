package me.www.urlshortener.rest;

/**
 * @author: www
 * @date: 2018/7/22 11:24
 * @description: 非法参数
 */
public class IllegalParamsException extends RuntimeException {

    public IllegalParamsException() {
        super();
    }

    public IllegalParamsException(String message) {
        super(message);
    }
}

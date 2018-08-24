package me.www.urlshortener.rest;

/**
 * 非法参数异常
 *
 * @author www
 * @since 2018/7/22 11:24
 */
public class IllegalParamsException extends RuntimeException {

    public IllegalParamsException() {
        super();
    }

    public IllegalParamsException(String message) {
        super(message);
    }
}

package me.www.urlshortener.controller;

import me.www.urlshortener.rest.IllegalParamsException;
import me.www.urlshortener.rest.RestErrorEnum;
import me.www.urlshortener.rest.RestResult;
import me.www.urlshortener.rest.RestResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author: www
 * @date: 2018/7/22 11:11
 * @description: 异常处理
 */
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 非法参数异常
     *
     * @see me.www.urlshortener.rest.IllegalParamsException
     */
    @ExceptionHandler(IllegalParamsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private RestResult illegalParamsExceptionHandler(IllegalParamsException e) {
        logger.error("--------->请求参数不合法!", e);
        return RestResultGenerator.genErrorResult(RestErrorEnum.ILLEGAL_PARAMS);
    }

}

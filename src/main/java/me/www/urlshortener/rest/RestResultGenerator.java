package me.www.urlshortener.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: www
 * @date: 2018/7/22 10:29
 * @description: 生成Rest接口响应体
 */
public class RestResultGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResultGenerator.class);

    /**
     * 生成响应成功的结果
     *
     * @param data 结果
     * @return RestResult
     */
    public static <T> RestResult<T> genResult(T data) {
        RestResult<T> result = RestResult.newInstance();
        result.setSuccess(true);
        result.setData(data);
        /*if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--------> result:{}",  writeValueAsString(result));
        }*/
        return result;
    }

    /**
     * 生成响应失败的结果
     *
     * @param restErrorEnum 失败信息
     * @return RestResult
     */
    public static RestResult genErrorResult(RestErrorEnum restErrorEnum) {
        RestResult result = RestResult.newInstance();
        result.setSuccess(false);
        result.setErrorInfo(restErrorEnum);

        /*if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--------> result:{}", JacksonMapper.toJsonString(result));
        }*/

        return result;
    }

}

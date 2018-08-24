package me.www.urlshortener.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.www.urlshortener.util.WebObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成Rest接口响应体
 *
 * @author www
 * @since 2018/7/22 10:29
 */
public class RestResultGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResultGenerator.class);

    private static final ObjectMapper JSON_MAPPER;

    static {
        JSON_MAPPER = WebObjectUtil.getRootWebApplicationContext().getBean(ObjectMapper.class);
    }

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

        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug("--------> result:{}", JSON_MAPPER.writeValueAsString(result));
            } catch (JsonProcessingException e) {
                // nothing to do.
            }
        }

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

        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug("--------> result:{}", JSON_MAPPER.writeValueAsString(result));
            } catch (JsonProcessingException e) {
                // nothing to do.
            }
        }

        return result;
    }

}

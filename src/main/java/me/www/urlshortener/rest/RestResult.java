package me.www.urlshortener.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Rest接口响应体
 *
 * @author www
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestResult<T> {

    private boolean success;

    private T data;

    private String errorCode;

    private String errorMsg;

    private RestResult() {
        super();
    }

    public static <T> RestResult<T> newInstance() {
        return new RestResult<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrorInfo(RestErrorEnum restErrorEnum) {
        this.errorCode = restErrorEnum.getErrorCode();
        this.errorMsg = restErrorEnum.getErrorMsg();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
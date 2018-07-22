package me.www.urlshortener.rest;

/**
 * @author: www
 * @date: 2018/7/22 10:47
 * @description: 响应错误信息
 */
public enum RestErrorEnum {

    ILLEGAL_PARAMS("101", "请求参数不合法!"),

    NOT_EXIST("401", "查询资源不存在!");

    private String errorCode;

    private String errorMsg;

    RestErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}

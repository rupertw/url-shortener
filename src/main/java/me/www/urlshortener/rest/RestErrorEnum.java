package me.www.urlshortener.rest;

/**
 * 响应错误信息
 *
 * @author www
 * @since 1.0.0
 */
public enum RestErrorEnum {

    ILLEGAL_PARAMS("101", "请求参数不合法"),

    URL_NOT_VALID("102", "网址不合法"),

    URL_IN_BLACKLIST("103", "长网址在黑名单中，不允许注册"),

    NOT_EXIST("401", "请求资源不存在");

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

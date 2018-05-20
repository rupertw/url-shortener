package me.www.urlshortener.util;

import org.apache.commons.validator.routines.UrlValidator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: www
 * @date: 2018/5/19 23:35
 * @description: url工具类
 */
public class UrlUtil {

    /**
     * url验证器
     */
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(new String[]{"http", "https"});


    public static boolean isValid(String url) {
        return URL_VALIDATOR.isValid(url);
    }

    /**
     * 优化地址表示形式（将协议、主机名（域名）部分转化为小写）（URL地址格式：scheme://host:port/path）
     *
     * @param url
     * @return
     */
    public static String normalizeUrl(String url) {
        if (url == null) {
            return null;
        }
        url = url.trim();
        if (!isValid(url)) {
            return null;
        }

        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        url = urlObj.toString();
        String host = urlObj.getHost();
        url = url.replaceFirst(host, host.toLowerCase());

        return url;
    }

    /**
     * 获取域名（主机名）
     */
    public static String getDomain(String url) {
        if (url == null) {
            return null;
        }
        url = url.trim();
        if (!isValid(url)) {
            return null;
        }

        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlObj.getHost().toLowerCase();
    }

}

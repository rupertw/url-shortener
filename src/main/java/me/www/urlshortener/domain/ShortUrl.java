package me.www.urlshortener.domain;

import java.util.Objects;

/**
 * @author: www
 * @date: 2018/5/19 23:11
 * @description: 短地址
 */
public class ShortUrl {

    private String code;

    private String url;

    public ShortUrl(String code, String url) {
        this.code = code;
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        return this.url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ShortUrl shortUrl = (ShortUrl) obj;
        return Objects.equals(this.url, shortUrl.getUrl());
    }

    @Override
    public String toString() {
        return this.url;
    }

}

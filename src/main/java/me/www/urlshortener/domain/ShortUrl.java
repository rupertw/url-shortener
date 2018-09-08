package me.www.urlshortener.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Objects;

/**
 * 短地址
 *
 * @author www
 * @since 1.0.0
 */
@RedisHash("short_url")
public class ShortUrl implements Serializable {

    @Id
    private String code;

    private String url;

    public ShortUrl() {
        super();
    }

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

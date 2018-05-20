package me.www.urlshortener.domain;

import java.util.Objects;

/**
 * @author: www
 * @date: 2018/5/19 23:11
 * @description: 长地址（原地址）
 */
public class LongUrl {

    private final String url;

    public LongUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
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
        LongUrl longUrl = (LongUrl) obj;
        return Objects.equals(this.url, longUrl.getUrl());
    }

    @Override
    public String toString() {
        return this.url;
    }

}

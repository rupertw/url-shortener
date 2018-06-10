package me.www.urlshortener.util;

import org.junit.Test;

/**
 * @author: www
 * @date: 2018/6/10 19:32
 * @description: 单元测试
 */
public class HashFunctionTest {

    @Test
    public void digestWithSHA256() {
        System.out.println(HashFunction.digestWithSHA256(new String[]{"url"}));
        System.out.println(HashFunction.digestWithSHA256(new String[]{"url", "urlshortenner"}));
    }
}
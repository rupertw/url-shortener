package me.www.urlshortener.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * DomainUtil单元测试
 *
 * @author www
 * @date 2018/6/10 19:21
 */
public class DomainUtilTest {

    @Test
    public void isInBlackList() {
        assertEquals(false, DomainUtil.isInBlackList("xxx.com"));
        assertEquals(true, DomainUtil.isInBlackList("yyy.com"));
        assertEquals(true, DomainUtil.isInBlackList("zzz.yyy.com"));
    }
}
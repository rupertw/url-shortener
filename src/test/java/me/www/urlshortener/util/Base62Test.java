package me.www.urlshortener.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author: www
 * @date: 2018/6/10 19:10
 * @description: 单元测试
 */
public class Base62Test {

    @Test
    public void encode() {
        assertEquals("a", Base62.encode(0));
        assertEquals("999999", Base62.encode((long) Math.pow(62, 6) - 1));
    }

    @Test
    public void decode() {
        assertEquals(0, Base62.decode("a"));
        assertEquals((long) Math.pow(62, 6) - 1, Base62.decode("999999"));
    }

}
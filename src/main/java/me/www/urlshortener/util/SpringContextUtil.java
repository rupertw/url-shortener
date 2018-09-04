package me.www.urlshortener.util;

import org.springframework.context.ApplicationContext;

/**
 * 应用上下文工具类
 *
 * @author wwww
 * @since 2018/9/4
 */
public class SpringContextUtil {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

}
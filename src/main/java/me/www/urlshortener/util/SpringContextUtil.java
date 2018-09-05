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
        if (applicationContext == null) {
            // 项目启动时为字段applicationContext赋值
            applicationContext = context;
        } else {
            // 项目运行时不可修改字段applicationContext
            throw new UnsupportedOperationException();
        }
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

}
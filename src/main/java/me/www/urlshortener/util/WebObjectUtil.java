package me.www.urlshortener.util;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author www
 * @date: 2018/7/22 16:13
 * @description 用于web应用中，网络请求中 获取当前线程相关对象
 * @see org.springframework.web.context.request.RequestContextHolder
 */
public class WebObjectUtil {

    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) requestAttributes;
    }

    /**
     * 当前请求对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 当前请求响应对象
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 当前web会话
     *
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * servlet上下文
     *
     * @return the ServletContext to which this session belongs.
     */
    public static ServletContext getServletContext() {
        return getSession().getServletContext();
    }

    /**
     * Find the root WebApplicationContext for this web app.
     *
     * @return
     */
    public static WebApplicationContext getRootWebApplicationContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

}

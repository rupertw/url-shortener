package me.www.urlshortener;

import me.www.urlshortener.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author www
 * @see 1.0.0
 */
@SpringBootApplication
public class UrlShortenerApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(UrlShortenerApplication.class, args);
        SpringContextUtil.setApplicationContext(applicationContext);
    }
}

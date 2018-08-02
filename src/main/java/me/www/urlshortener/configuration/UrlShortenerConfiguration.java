package me.www.urlshortener.configuration;

import me.www.urlshortener.util.SnowFlake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: www
 * @date: 2018/7/21 22:29
 * @description: Bean Configuration
 */
@Configuration
public class UrlShortenerConfiguration {

    @Bean
    public SnowFlake snowFlake(@Value("${snowflake.datacenterId}") Long datacenterId, @Value("${snowflake.machineId}") Long machineId) {
        return new SnowFlake(datacenterId, machineId);
    }

}

package me.www.urlshortener.config;

import me.www.urlshortener.util.SnowFlake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: www
 * @date: 2018/7/21 22:29
 * @description: TODO
 */
@Configuration
public class UrlShortenerConfig {

    @Value("${snowflake.datacenterId}")
    private Long datacenterId;

    @Value("${snowflake.machineId}")
    private Long machineId;

    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(datacenterId, machineId);
    }

}

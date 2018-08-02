package me.www.urlshortener.repository;

import me.www.urlshortener.domain.ShortUrl;
import org.springframework.data.repository.CrudRepository;

/**
 * @author: www
 * @date: 2018/8/2 23:06
 * @description: repository
 */
public interface ShortUrlRepository extends CrudRepository<ShortUrl, String> {
}

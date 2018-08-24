package me.www.urlshortener.repository;

import me.www.urlshortener.domain.ShortUrl;
import org.springframework.data.repository.CrudRepository;

/**
 * repository
 *
 * @author www
 * @since 2018/8/2 23:06
 */
public interface ShortUrlRepository extends CrudRepository<ShortUrl, String> {
}

package me.www.urlshortener.repository;

import me.www.urlshortener.domain.ShortUrl;
import org.springframework.data.repository.CrudRepository;

/**
 * repository
 *
 * @author www
 * @since 1.0.0
 */
public interface ShortUrlRepository extends CrudRepository<ShortUrl, String> {
}

package me.www.urlshortener.constant;

/**
 * redis存储相关常量
 *
 * @author wwww
 * @since 1.0.0
 */
public class RedisConsts {

    /**
     * redis key: ShortUrl存储key前缀 (redis类型: hash)
     */
    public final static String SHORT_URL_KEY_PREFIX = "short_url:";

    /**
     * redis key: 保存最近简化url相关
     */
    public final static String LR_SHORTEN_URL_ZSET = "lr_shorten_url_zset";
    public final static String LR_SHORTEN_URL_HASH = "lr_shorten_url_hash";

    /**
     * 最近简化url缓存容量限制
     */
    public final static Integer LR_SHORTEN_URL_LIMIT = 100;

    /**
     * redis key: 访问计数 (redis类型: ZSet)
     */
    public final static String VISIT_COUNT_KEY = "visit_count";

}

package me.www.urlshortener.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 主要提供分布式锁相关方法
 *
 * @author wwww
 * @since 1.0.0
 */
public class RedisUtil {

    /**
     * 获取分布式锁
     *
     * @param lockName 锁名称
     * @return 若获取锁成功，返回持锁标识符，否则，返回null
     */
    public static String acquireLock(String lockName) {
        return acquireLock(lockName, 1500, 1000);
    }

    /**
     * 获取分布式锁
     *
     * @param lockName       锁名称
     * @param acquireTimeout 获取锁超时限制（单位毫秒）
     * @param lockTimeout    锁超时时间（单位毫秒）
     * @return 若获取锁成功，返回持锁标识符，否则，返回null
     */
    public static String acquireLock(String lockName, long acquireTimeout, long lockTimeout) {
        // 参数验证
        Objects.requireNonNull(lockName);
        if (acquireTimeout <= 0 || lockTimeout <= 0) {
            throw new IllegalArgumentException();
        }

        StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);

        String identifier = UUID.randomUUID().toString();
        String lockKey = "lock:" + lockName;

        long end = System.currentTimeMillis() + acquireTimeout;
        while (System.currentTimeMillis() < end) {
            // 尝试获取锁
            if (redisTemplate.opsForValue().setIfAbsent(lockKey, identifier)) {
                // 获取锁成功，设置锁超时时间
                redisTemplate.expire(lockKey, lockTimeout, TimeUnit.MILLISECONDS);
                return identifier;
            }
            // 获取锁失败，若锁未设置超时时间，则设置超时时间（此处应对的问题：某进程获取锁成功，但是由于进程中断等原因，未设置超时时间，这样，其他进程将永远无法获得锁，形成死锁）
            // 备注：关于redisTemplate.getExpire返回值，若key不存在，返回-2，若key存在且未设置过期时间，返回-1，否则，返回过期时间（大于0）
            if (redisTemplate.getExpire(lockKey, TimeUnit.MILLISECONDS) == -1) {
                redisTemplate.expire(lockKey, lockTimeout, TimeUnit.MILLISECONDS);
            }
        }

        // null indicates that the lock was not acquired
        return null;
    }

    /**
     * 释放分布式锁
     *
     * @param lockName   锁名称
     * @param identifier 持锁标识符
     * @return 若释放锁成功，返回true，否则，返回false
     */
    public static boolean releaseLock(String lockName, String identifier) {
        // 参数验证
        Objects.requireNonNull(lockName);
        Objects.requireNonNull(identifier);

        StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);

        String lockKey = "lock:" + lockName;

        while (true) {
            // 释放锁时应以事务的方式进行（此处应对的问题：释放锁过程中，锁过期超时自动删除或其他进程重新获得锁）
            redisTemplate.watch(lockKey);
            if (identifier.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.multi();
                redisTemplate.delete(lockKey);
                List<Object> results = redisTemplate.exec();
                // empty response indicates that the transaction was aborted due to the watched key changing.
                if (results.isEmpty()) {
                    continue;
                }
                return true;
            }

            redisTemplate.unwatch();
            break;
        }

        return false;
    }

}

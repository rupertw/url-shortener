package me.www.urlshortener.listener;

import me.www.urlshortener.constant.RedisConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Spring容器初始化完成事件
 *
 * @author wwww
 * @since 1.0.0
 */
@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 清理最近简化url缓存时每次最多清理数
     */
    private final static Integer LR_SHORTEN_URL_CLEAR_PER_LIMIT = 10;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     * @see me.www.urlshortener.service.impl.ShortUrlServiceImpl#shortenUrl(String)
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("启动后台线程任务（采用lru算法清理最近简化url缓存）");

        // 线程处理：采用lru算法清理最近简化url缓存
        threadPoolTaskExecutor.execute(() -> {
            while (true) {
                stringRedisTemplate.watch(RedisConsts.LR_SHORTEN_URL_ZSET);
                Long zsetSize = stringRedisTemplate.opsForZSet().size(RedisConsts.LR_SHORTEN_URL_ZSET);
                if (zsetSize <= RedisConsts.LR_SHORTEN_URL_LIMIT) {
                    stringRedisTemplate.unwatch();
                    try {
                        // 每分钟执行一次批量清理（高并发下，需每秒执行一次批量清理，注意要保证清理速度大于产生速度）
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                Long end_index = Long.min(zsetSize - RedisConsts.LR_SHORTEN_URL_LIMIT, LR_SHORTEN_URL_CLEAR_PER_LIMIT);
                Set<String> urlSet = stringRedisTemplate.opsForZSet().range(RedisConsts.LR_SHORTEN_URL_ZSET, 0, end_index - 1); // 注：查询redis数据，必须在multi()之前
                stringRedisTemplate.multi();
                stringRedisTemplate.opsForHash().delete(RedisConsts.LR_SHORTEN_URL_HASH, urlSet.toArray());
                stringRedisTemplate.opsForZSet().removeRange(RedisConsts.LR_SHORTEN_URL_ZSET, 0, end_index - 1);
                List<Object> results = stringRedisTemplate.exec();
                // empty response indicates that the transaction was aborted due to the watched key changing.
                if (results.isEmpty()) {
                    //logger.info("");
                } else {
                    logger.info("采用lru算法清理最近简化url缓存[" + end_index + "]个元素");
                }
            }
        });

    }

}

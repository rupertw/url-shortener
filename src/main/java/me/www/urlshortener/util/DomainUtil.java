package me.www.urlshortener.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: www
 * @date: 2018/5/20 0:28
 * @description: domain工具类
 */
public class DomainUtil {

    private static final Logger logger = LoggerFactory.getLogger(DomainUtil.class);

    private static final String BLACKLIST_FILENAME = "blacklist";

    private static final Set<String> blacklist;

    static {
        blacklist = new HashSet<>();

        try (final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(BLACKLIST_FILENAME);
             final InputStreamReader isr = new InputStreamReader(is, "UTF-8");
             final BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                blacklist.add(line);
            }
        } catch (final IOException e) {
            logger.error("An error occurred when reading from domain-blacklist file.", e);
        }
    }

    /**
     * 判断域名是否在黑名单，支持上下级包含（如xxx.com在黑名单中，则yyy.xxx.com也在黑名单中）
     *
     * @param domain
     * @return
     */
    public static boolean isInBlackList(String domain) {
        if (blacklist == null || domain == null || domain.isEmpty()) {
            return false;
        }

        do {
            if (blacklist.contains(domain)) {
                return true;
            }

            int index = domain.indexOf(".");
            if (index == -1) {
                break;
            } else {
                domain = domain.substring(index + 1);
            }
        } while (true);

        return false;
    }
}

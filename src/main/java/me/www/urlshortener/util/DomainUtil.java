package me.www.urlshortener.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * 域名工具类
 *
 * @author www
 * @since 1.0.0
 */
public class DomainUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainUtil.class);

    private static final String BLACKLIST_FILENAME = "blacklist";

    private static final Set<String> BLACKLIST = new HashSet<>();

    static {
        try (final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(BLACKLIST_FILENAME);
             final InputStreamReader isr = new InputStreamReader(is, "UTF-8");
             final BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                BLACKLIST.add(line);
            }
        } catch (final IOException e) {
            LOGGER.error("An error occurred when reading from domain-blacklist file.", e);
        }
    }

    /**
     * 判断域名是否在黑名单，支持上下级包含（如xxx.com在黑名单中，则yyy.xxx.com也在黑名单中）
     *
     * @param domain
     * @return
     */
    public static boolean isInBlackList(String domain) {
        if (BLACKLIST.isEmpty() || StringUtils.isEmpty(domain)) {
            return false;
        }

        do {
            if (BLACKLIST.contains(domain)) {
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

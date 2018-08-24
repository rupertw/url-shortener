package me.www.urlshortener.service;

import me.www.urlshortener.domain.ShortUrl;
import me.www.urlshortener.vo.ShortUrlVO;

import java.util.List;

/**
 * 短地址Service
 *
 * @author www
 * @since 2018/7/21 22:19
 */
public interface ShortUrlService {

    /**
     * 简化url
     *
     * @param url
     * @return
     */
    ShortUrl shortenUrl(String url);

    /**
     * 根据code查询简化结果
     *
     * @param code
     * @param toVisit 查询结果是否用于访问
     * @return
     */
    ShortUrl getShortUrl(String code, Boolean toVisit);

    /**
     * 查询访问计数排行
     *
     * @param topn
     * @return
     */
    List<ShortUrlVO> topnVisit(Integer topn);

}
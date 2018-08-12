package me.www.urlshortener.controller;

import me.www.urlshortener.rest.RestResult;
import me.www.urlshortener.rest.RestResultGenerator;
import me.www.urlshortener.service.ShortUrlService;
import me.www.urlshortener.vo.ShortUrlVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: www
 * @date: 2018/8/12 17:51
 * @description: 统计数据
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShortUrlService shortUrlService;

    /**
     * 访问计数排行
     *
     * @param topn
     * @return
     */
    @GetMapping("/topnVisit")
    public RestResult<List<ShortUrlVO>> topnVisit(Integer topn) {
        logger.info("request for /statistics/topnVisit: " + topn);

        if (topn == null || topn <= 0) {
            topn = 10;
        }

        return RestResultGenerator.genResult(shortUrlService.topnVisit(topn));
    }
}

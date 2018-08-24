package me.www.urlshortener.vo;

/**
 * 视图VO类
 *
 * @author www
 * @since 2018/8/12 19:36
 */
public class ShortUrlVO {

    /**
     * 短网址
     */
    private String surl;

    /**
     * 原网址
     */
    private String lurl;

    /**
     * 访问计数
     */
    private Integer visitCount;

    public ShortUrlVO() {
        super();
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getLurl() {
        return lurl;
    }

    public void setLurl(String lurl) {
        this.lurl = lurl;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }
}

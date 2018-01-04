package android.webcrawler.osori.hungryosori.Model;

/**
 * Created by Owner on 2018-01-03.
 */

public class KeywordInfo {
    private String pushToken;
    private String crawler;
    private String keyword;
    private String url;
    private boolean funct;

    public KeywordInfo(String pushToken, String keyword, String crawler, String url, boolean funct)
    {
        this.pushToken      = pushToken;
        this.keyword        = keyword;
        this.crawler        = crawler;
        this.url            = url;
        this.funct          = funct;
    }

    public String getPushToken(){
        return this.pushToken;
    }

    public String getKeyword(){
        return this.keyword;
    }

    public String getCrawler(){
        return this.crawler;
    }

    public String getUrl(){
        return this.url;
    }

    public boolean getFunct() { return this.funct; }

}

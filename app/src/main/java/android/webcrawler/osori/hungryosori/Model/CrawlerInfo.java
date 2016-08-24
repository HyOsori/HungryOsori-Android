package android.webcrawler.osori.hungryosori.Model;

/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerInfo {
    private String id;
    private String title;
    private String description;
    private String url;

    public CrawlerInfo(String id, String title, String description, String url)
    {
        this.title          = title;
        this.description    = description;
        this.url            = url;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getUrl(){
        return url;
    }

}

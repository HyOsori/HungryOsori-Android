package android.webcrawler.osori.hungryosori.Model;

/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerInfo {
    private String id;
    private String title;
    private String description;
    private String url;
    private boolean subscription;

    public CrawlerInfo(String id, String title, String description, String url, boolean subscription)
    {
        this.id             = id;
        this.title          = title;
        this.description    = description;
        this.url            = url;
        this.subscription   = subscription;
    }

    public String getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public String getUrl(){
        return this.url;
    }

    public boolean getSubscription(){
        return this.subscription;
    }

    public void setSubscription(boolean subscription){
        this.subscription = subscription;
    }
}

package android.webcrawler.osori.hungryosori.CrawlerInfo;

import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;

import java.util.ArrayList;

/**
 * Created by kunju on 2016-12-17.
 * 싱글톤 객체로 구현
 */
public class CrawlerInfos extends Subject{
    private static CrawlerInfos crawlerInfos        = null;
    private ArrayList<CrawlerInfo> crawlerInfoList  = new ArrayList<>();
    private ArrayList<CrawlerInfo> subscriptionList = new ArrayList<>();

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    private boolean initialized = false;

    private CrawlerInfos(){
    }

    public static CrawlerInfos getInstance(){
        if(crawlerInfos == null){
            crawlerInfos = new CrawlerInfos();
        }
        return crawlerInfos;
    }

    public void addCrawlerInfo(CrawlerInfo crawlerInfo){
        crawlerInfoList.add(crawlerInfo);
        notifyObservers();
    }

    public void subscriptionCrawler(String id){
        for (CrawlerInfo crawlerInfo : crawlerInfoList) {
            if (crawlerInfo.getId().equals(id) && crawlerInfo.getSubscription() == false) {
                crawlerInfo.setSubscription(true);
                subscriptionList.add(crawlerInfo);
                notifyObservers();
                break;
            }
        }
    }

    public void unSubscriptionCrawler(String id){
        for (CrawlerInfo crawlerInfo : crawlerInfoList) {
            if (crawlerInfo.getId().equals(id) && crawlerInfo.getSubscription() == true) {
                crawlerInfo.setSubscription(false);
                subscriptionList.remove(crawlerInfo);
                notifyObservers();
                break;
            }
        }
    }

    public ArrayList<CrawlerInfo> getCrawlerInfoList(){
        return this.crawlerInfoList;
    }

    public ArrayList<CrawlerInfo> getSubscriptionList(){
        return this.subscriptionList;
    }
}

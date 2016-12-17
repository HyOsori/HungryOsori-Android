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

    public void removeCrawlerInfo(CrawlerInfo crawlerInfo){
        crawlerInfoList.remove(crawlerInfo);
        notifyObservers();
    }

    public void changeSubscription(String id){
        for (CrawlerInfo crawlerInfo : crawlerInfoList) {
            if (crawlerInfo.getId().equals(id)) {
                crawlerInfo.setSubscription(!crawlerInfo.getSubscription());
                notifyObservers();
                break;
            }
        }
    }

    public ArrayList<CrawlerInfo> getCrawlerInfoList(){
        return this.crawlerInfoList;
    }
}

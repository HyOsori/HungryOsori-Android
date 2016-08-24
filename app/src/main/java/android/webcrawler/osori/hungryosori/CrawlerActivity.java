package android.webcrawler.osori.hungryosori;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webcrawler.osori.hungryosori.Adapter.CrawlerViewPagerAdapter;
import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;
import android.webcrawler.osori.hungryosori.common.Constant;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{
    public static ArrayList<CrawlerInfo> crawlerInfosAll;
    public static ArrayList<CrawlerInfo> crawlerInfosMy;

    private ArrayList<String> myCrawlerIDs;

    private ViewPager viewPager;
    private CrawlerViewPagerAdapter viewPagerAdapter;

    private ToggleButton button_all, button_my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawler);

        /** 객체 설정 */
        viewPager   = (ViewPager)findViewById(R.id.crawler_viewPager);
        button_all  = (ToggleButton)findViewById(R.id.crawler_button_all);
        button_my   = (ToggleButton)findViewById(R.id.crawler_button_my);

        crawlerInfosAll = new ArrayList<>();
        crawlerInfosMy  = new ArrayList<>();
        myCrawlerIDs = new ArrayList<>();

        /** Toggle 버튼 초기값 설정 */
        button_my.setChecked(true);
        button_all.setChecked(false);

        // ViewPager 어댑터 생성
        viewPagerAdapter = new CrawlerViewPagerAdapter(getSupportFragmentManager());

        // ViewPager 어댑터 설정
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(this);

        getCrawlerInfo();
    }

    private void getCrawlerInfo() {
        getEntireList();
        getSubscriptionList();

        addToList();
    }

    private void getEntireList(){
        CrawlerInfo info1 = new CrawlerInfo("wf42i", "DCinside",
                "디시 힛겔 크롤러", "http://wstatic.dcinside.com/main/main2011/dcmain/logo_swf/top_logo_160718.png", false);
        CrawlerInfo info2 = new CrawlerInfo("xvio31", "steam",
                "스팀 할인 정보 크롤러", "http://steamcommunity-a.akamaihd.net/public/shared/images/header/globalheader_logo.png", false);
        crawlerInfosAll.add(info1);
        crawlerInfosAll.add(info2);
    }

    private void getSubscriptionList(){
        myCrawlerIDs.add("wf42i");
    }

    private void addToList()
    {
        for(String id : myCrawlerIDs){
            for(CrawlerInfo crawlerInfo : crawlerInfosAll)
            {
                if(crawlerInfo.getId().equals(id))
                {
                    crawlerInfo.setSubscription(true);
                    crawlerInfosMy.add(crawlerInfo);
                    break;
                }
            }
        }
    }

    /* view pager 페이지가 바뀌면 호출된다 */
    public void onPageSelected(int position) {
        if(position == Constant.PAGE_MY){
            button_my.setChecked(true);
            button_all.setChecked(false);
        }else if(position == Constant.PAGE_ALL){
            button_my.setChecked(false);
            button_all.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.crawler_button_my:
                viewPager.setCurrentItem(Constant.PAGE_MY);
                break;
            case R.id.crawler_button_all:
                viewPager.setCurrentItem(Constant.PAGE_ALL);
                break;
        }
    }

}

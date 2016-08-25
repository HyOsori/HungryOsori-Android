package android.webcrawler.osori.hungryosori;

import android.graphics.Typeface;
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
 * Created by 고건주 on 2016-08-25.
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

        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        button_all.setTypeface(fontArial);
        button_my.setTypeface(fontArial);

        crawlerInfosAll = new ArrayList<>();
        crawlerInfosMy  = new ArrayList<>();
        myCrawlerIDs = new ArrayList<>();
        getCrawlerInfo();

        /** Toggle 버튼 초기값 설정 */
        button_my.setChecked(true);
        button_all.setChecked(false);
    }

    private void getCrawlerInfo() {
       getEntireList();
       getSubscriptionList();

       setViewPagerAdapter();
    }

    private void getEntireList(){

    }

    private void getSubscriptionList(){

    }

    private void setViewPagerAdapter()
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
        // ViewPager 어댑터 생성
        viewPagerAdapter = new CrawlerViewPagerAdapter(getSupportFragmentManager());

        // ViewPager 어댑터 설정
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(this);
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

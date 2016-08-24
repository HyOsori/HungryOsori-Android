package android.webcrawler.osori.hungryosori;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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

        /** Toggle 버튼 초기값 설정 */
        button_my.setChecked(true);
        button_all.setChecked(false);

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

}

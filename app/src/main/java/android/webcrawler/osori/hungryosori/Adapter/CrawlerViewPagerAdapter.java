package android.webcrawler.osori.hungryosori.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.webcrawler.osori.hungryosori.Fragment.CrawlerFragment;
import android.webcrawler.osori.hungryosori.common.Constant;


/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerViewPagerAdapter extends FragmentPagerAdapter {

    private int count = 2;          // viewPager의 수

    public static CrawlerFragment fragmentMy;
    public static CrawlerFragment fragmentALL;

    public CrawlerViewPagerAdapter(FragmentManager fm){
        super(fm);
        fragmentMy = CrawlerFragment.newInstance(Constant.PAGE_MY);
        fragmentALL = CrawlerFragment.newInstance(Constant.PAGE_ALL);
    }

    public Fragment getItem(int i){
        if(i == Constant.PAGE_MY){
            return fragmentMy;
        }else if(i == Constant.PAGE_ALL){
            return fragmentALL;
        }
        return null;
    }

    public int getCount() {
        return count;
    }

}

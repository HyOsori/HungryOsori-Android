package android.webcrawler.osori.hungryosori.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.webcrawler.osori.hungryosori.Fragment.CrawlerFragment;
import android.webcrawler.osori.hungryosori.Common.Constant;


/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerViewPagerAdapter extends FragmentPagerAdapter {

    private int count = 2;          // viewPager 수

    public static CrawlerFragment fragmentMy    = CrawlerFragment.newInstance(Constant.PAGE_MY);
    public static CrawlerFragment fragmentALL   = CrawlerFragment.newInstance(Constant.PAGE_ALL);

    public CrawlerViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public Fragment getItem(int i){
        if(i == Constant.PAGE_MY && fragmentMy != null){
            return fragmentMy;
        }else if(i == Constant.PAGE_ALL && fragmentALL != null){
            return fragmentALL;
        }
        return null;
    }

    public int getCount() {
        return count;
    }

    public static void notifyMyCrawlerInfoListChanged()
    {
        if(fragmentMy != null && fragmentMy.listAdapter != null){
            fragmentMy.listAdapter.notifyDataSetChanged();
        }
    }

    public static void notifyAllCrawlerInfoListChanged()
    {
        if(fragmentALL != null && fragmentALL.listAdapter != null){
            fragmentALL.listAdapter.notifyDataSetChanged();
        }
    }

}

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

    private final int count = 2;          // viewPager 수

    public CrawlerViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public Fragment getItem(int i){
        Fragment f = CrawlerFragment.newInstance(i);
        return f;
    }

    public int getCount() {
        return count;
    }

}

package android.webcrawler.osori.hungryosori.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.webcrawler.osori.hungryosori.Fragment.CrawlerFragment;


/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerViewPagerAdapter extends FragmentPagerAdapter {

    private int count = 2;          // viewPager의 수

    public CrawlerViewPagerAdapter(FragmentManager fm){
        super(fm);

    }

    public Fragment getItem(int i){
        CrawlerFragment fragment = CrawlerFragment.newInstance(i);
        return fragment;
    }

    public int getCount() {
        return count;
    }

}

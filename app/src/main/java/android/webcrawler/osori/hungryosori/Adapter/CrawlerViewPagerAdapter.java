package android.webcrawler.osori.hungryosori.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerViewPagerAdapter extends FragmentPagerAdapter {

    private int count = 2;          // viewPager의 수

    public CrawlerViewPagerAdapter(FragmentManager fm){
        super(fm);

    }

    public Fragment getItem(int i){
        if(i == 0){
            /** MY인 경우 */
        }
        else if(i == 1){
            /** ALL인 경우 */
        }
        return null;
    }

    public int getCount() {
        return count;
    }

}

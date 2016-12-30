package android.webcrawler.osori.hungryosori;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.webcrawler.osori.hungryosori.Adapter.CrawlerViewPagerAdapter;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.webcrawler.osori.hungryosori.CrawlerInfo.CrawlerInfos;
import android.webcrawler.osori.hungryosori.Method.GetMethod;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.widget.ToggleButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by 고건주 on 2016-08-25.
 */
public class CrawlerActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private CrawlerInfos crawlerInfos;
    private ViewPager viewPager;
    private CrawlerViewPagerAdapter viewPagerAdapter;

    private ToggleButton button_all, button_my;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawler);

        crawlerInfos = CrawlerInfos.getInstance();
        /** 객체 설정 */
        viewPager = (ViewPager) findViewById(R.id.crawler_viewPager);
        button_all = (ToggleButton) findViewById(R.id.crawler_button_all);
        button_my = (ToggleButton) findViewById(R.id.crawler_button_my);
        navigationView = (NavigationView)findViewById(R.id.activity_crawler_navigationView);

        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        button_all.setTypeface(fontArial);
        button_my.setTypeface(fontArial);

        /** 헤더 추가 */
        View header = LayoutInflater.from(this).inflate(R.layout.header_navigation, null);
        navigationView.addHeaderView(header);

        /** ViewPagerAdapter 설정 */
        setViewPagerAdapter();

        /** 서버에서 정보 가져오기 */
        if(crawlerInfos.isInitialized() == false) {
            getCrawlerInfo();
        }
    }
    private void getCrawlerInfo() {
        getEntireList();
    }

    /**
     * Crawler 전체 정보를 가져오는 함수
     */
    private void getEntireList() {
        String url = Constant.SERVER_URL + "/crawlers/";

        ParamModel params = new ParamModel();
        params.setUrl(url);

        new getEntireListTask().execute(params);
    }


    private class getEntireListTask extends AsyncTask<ParamModel, Void, Boolean> {

        private ArrayList<CrawlerInfo> entireCralwerInfos;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            entireCralwerInfos = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub

            String result = GetMethod.getInstance().send(params[0]);

            try {
                JSONObject jsonObject = new JSONObject(result);

                int error = jsonObject.getInt("ErrorCode");
                if (error == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("crawlers");

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String id = object.getString("crawler_id");
                        String url = object.getString("thumbnail_url");
                        String description = object.getString("description");
                        String title = object.getString("title");

                        entireCralwerInfos.add(new CrawlerInfo(id, title, description, url, false));
                    }
                    return true;
                }
            } catch (Exception e) {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if (success) {
                // 성공시 구독 정보를 가져온다.
                for(CrawlerInfo c : entireCralwerInfos){
                    crawlerInfos.addCrawlerInfo(c);
                }
                crawlerInfos.setInitialized(true);
                getSubscriptionList();
            } else {
                // 실패
            }
        }
    }

    /**
     * 사용자가 구독 중인 CrawlerID를 가져오는 함수
     */
    private void getSubscriptionList() {
        String url = Constant.SERVER_URL + "/subscription/";
        ParamModel params = new ParamModel();

        params.setUrl(url);
        params.addParameter("user_id", Constant.userID);
        params.addParameter("user_key", Constant.userKey);

        new getSubscriptionListTask().execute(params);
    }

    private class getSubscriptionListTask extends AsyncTask<ParamModel, Void, Boolean> {
        private ArrayList<String> subscriptionIDs;

        public getSubscriptionListTask() {
            subscriptionIDs = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
           String result = PostMethod.getInstance().send(params[0]);

            try {
                JSONObject jsonObject = new JSONObject(result);

                int error = jsonObject.getInt("ErrorCode");
                if (error == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("subscriptions");

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String subscriptionsID = object.getString("crawler_id");
                        subscriptionIDs.add(subscriptionsID);
                    }

                    return true;
                }
            } catch (Exception e) {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if (success) {
                // 성공
                for (String id : subscriptionIDs) {
                    crawlerInfos.subscriptionCrawler(id);
                }
            }
        }
    }

    private void setViewPagerAdapter() {
        // ViewPager 어댑터 생성
        viewPagerAdapter = new CrawlerViewPagerAdapter(getSupportFragmentManager());

        // ViewPager 어댑터 설정
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    /* view pager 페이지가 바뀌면 호출된다 */
    public void onPageSelected(int position) {
        if (position == Constant.PAGE_MY) {
            button_my.setChecked(true);
            button_all.setChecked(false);
        } else if (position == Constant.PAGE_ALL) {
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


    public void onClick(View v) {
       switch (v.getId()) {
            case R.id.crawler_button_my:
                ((ToggleButton)v).setChecked(true);
                viewPager.setCurrentItem(Constant.PAGE_MY);
                break;
            case R.id.crawler_button_all:
                ((ToggleButton)v).setChecked(true);
                viewPager.setCurrentItem(Constant.PAGE_ALL);
                break;

            case R.id.header_navigation_textView_logout:
                if(Pref.resetLogin() == true) {
                    Intent intent = new Intent(CrawlerActivity.this, LoginActivity.class);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;

            case R.id.header_navigation_textView_change_password: {
                Intent intent = new Intent(CrawlerActivity.this, ChangePwActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

}

package android.webcrawler.osori.hungryosori;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webcrawler.osori.hungryosori.Adapter.CrawlerViewPagerAdapter;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Http;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by 고건주 on 2016-08-25.
 */
public class CrawlerActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    public static ArrayList<CrawlerInfo> allCrawlerInfoList;
    public static ArrayList<CrawlerInfo> myCrawlerInfoList;

    private ArrayList<String> subscriptionIDs;

    private ViewPager viewPager;
    private CrawlerViewPagerAdapter viewPagerAdapter;

    private ToggleButton button_all, button_my;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawler);

        /** 객체 설정 */
        viewPager = (ViewPager) findViewById(R.id.crawler_viewPager);
        button_all = (ToggleButton) findViewById(R.id.crawler_button_all);
        button_my = (ToggleButton) findViewById(R.id.crawler_button_my);

        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        button_all.setTypeface(fontArial);
        button_my.setTypeface(fontArial);

        /** 리스트 초기화 */
        allCrawlerInfoList = new ArrayList<>();
        myCrawlerInfoList = new ArrayList<>();
        subscriptionIDs = new ArrayList<>();

        /** 서버에서 정보 가져오기 */
        getCrawlerInfo();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void getCrawlerInfo() {
        setViewPagerAdapter();
        getEntireList();
        getSubscriptionList();
    }

    /**
     * Crawler 전체 정보를 가져오는 함수
     */
    private void getEntireList() {
        String url = Constant.SERVER_URL + "/req_entire_list";

        ParamModel params = new ParamModel();

        params.setUrl(url);
        params.setParamStr("user_id", Constant.userID);
        params.setParamStr("user_key", Constant.userKey);

        new getEntireListTask(this).execute(params);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Crawler Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://android.webcrawler.osori.hungryosori/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Crawler Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://android.webcrawler.osori.hungryosori/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class getEntireListTask extends AsyncTask<ParamModel, Void, Boolean> {

        private Context mContext;

        public getEntireListTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
            Http http = new Http(mContext);

            String result = http.send(params[0], false);

            if (result == null) {
                return false;
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String message = jsonObject.getString(Constant.MESSAGE);
                    if (message.equals(Constant.MESSAGE_SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("crawlers");

                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("crawler_id");
                            String url = object.getString("thumbnail_url");
                            String description = object.getString("description");
                            String title = object.getString("title");

                            allCrawlerInfoList.add(new CrawlerInfo(id, title, description, url, false));
                        }
                        return true;
                    }
                } catch (Exception e) {

                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if (success) {
                // 성공
                CrawlerViewPagerAdapter.notifyAllCrawlerInfoListChanged();
            } else {
                // 실패
            }
        }
    }

    /**
     * 사용자가 구독 중인 CrawlerID를 가져오는 함수
     */
    private void getSubscriptionList() {
        String url = Constant.SERVER_URL + "/req_subscription_list";

        ParamModel params = new ParamModel();

        params.setUrl(url);
        params.setParamStr("user_id", Constant.userID);
        params.setParamStr("user_key", Constant.userKey);

        new getSubscriptionListTask(this).execute(params);
    }

    private class getSubscriptionListTask extends AsyncTask<ParamModel, Void, Boolean> {

        private Context mContext;

        public getSubscriptionListTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
            Http http = new Http(mContext);

            String result = http.send(params[0], false);

            if (result == null) {
                return false;
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String message = jsonObject.getString(Constant.MESSAGE);
                    if (message.equals(Constant.MESSAGE_SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("subscriptions");

                        for (int i = 0; i < jsonArray.length(); ++i) {
                            String subscriptionsID = jsonArray.getString(i);
                            subscriptionIDs.add(subscriptionsID);
                        }

                        return true;
                    }
                } catch (Exception e) {

                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if (success) {
                // 성공
                for (String id : subscriptionIDs) {
                    for (CrawlerInfo crawlerInfo : allCrawlerInfoList) {
                        if (crawlerInfo.getId().equals(id)) {
                            crawlerInfo.setSubscription(true);
                            myCrawlerInfoList.add(crawlerInfo);
                            break;
                        }
                    }
                }
                CrawlerViewPagerAdapter.notifyMyCrawlerInfoListChanged();
                CrawlerViewPagerAdapter.notifyAllCrawlerInfoListChanged();
            } else {
                // 실패
            }
        }
    }

    private void setViewPagerAdapter() {
        // ViewPager 어댑터 생성
        viewPagerAdapter = new CrawlerViewPagerAdapter(getSupportFragmentManager());

        // ViewPager 어댑터 설정
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(this);
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
                viewPager.setCurrentItem(Constant.PAGE_MY);
                break;
            case R.id.crawler_button_all:
                viewPager.setCurrentItem(Constant.PAGE_ALL);
                break;

            case R.id.templogout:
                Toast.makeText(CrawlerActivity.this, "임시 로그아웃", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CrawlerActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.temppw:
                Toast.makeText(CrawlerActivity.this, "임시임시", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(CrawlerActivity.this,ChangePwActivity.class);
                startActivity(intent2);
                break;
        }
    }

}

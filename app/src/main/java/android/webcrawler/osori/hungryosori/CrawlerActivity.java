package android.webcrawler.osori.hungryosori;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webcrawler.osori.hungryosori.Adapter.CrawlerViewPagerAdapter;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.webcrawler.osori.hungryosori.CrawlerInfo.CrawlerInfos;
import android.webcrawler.osori.hungryosori.Method.DeleteMethod;
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
 * Created by 고건주,김규민 on 2016-08-25.
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
        navigationView = (NavigationView) findViewById(R.id.activity_crawler_navigationView);

        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        button_all.setTypeface(fontArial);
        button_my.setTypeface(fontArial);


        /** ViewPagerAdapter 설정 */
        setViewPagerAdapter();

        /** 서버에서 정보 가져오기 */
        if (crawlerInfos.isInitialized() == false) {
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
            String token = Pref.getUserKey();
            //String token = Pref.getPushToken();
            String result = GetMethod.getInstance().send(params[0], token);
            Log.e("token_activity", token);
            Log.e("result_activity", result);
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
                for (CrawlerInfo c : entireCralwerInfos) {
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
        Log.e("getsubscriptionListTask", "exe");
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
            String token = Pref.getUserKey();
            String result = GetMethod.getInstance().send(params[0], token);
            Log.e("result:", result.toString());
            try {
                JSONObject jsonObject = new JSONObject(result);

                int error = jsonObject.getInt("ErrorCode");
                if (error == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("subscriptions");

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String subscriptionsID = object.getString("crawler");
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
    @Override
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
                ((ToggleButton) v).setChecked(true);
                viewPager.setCurrentItem(Constant.PAGE_MY);
                break;
            case R.id.crawler_button_all:
                ((ToggleButton) v).setChecked(true);
                viewPager.setCurrentItem(Constant.PAGE_ALL);
                break;

        }
    }

    /* 예상치 못한 로그아웃 방지 */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("로그아웃 하시겠습니까?");

        // 왼쪽
        alert.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //로그아웃POST
                logoutUser();
                //푸시 토큰 삭제 DELETE

                Log.e("onbbackPressed", "postLogout");
                Pref.resetLogin();
                finishAffinity();
            }
        });

        // 오른쪽
        alert.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do Nothing
            }
        });
        alert.setTitle("로그아웃");
        AlertDialog alert_view = alert.create();
        alert_view.show();
    }

    private void logoutUser() {
        String url = Constant.SERVER_URL + "/logout/";

        ParamModel params = new ParamModel();
        params.setUrl(url);

        new logoutUserTask().execute(params);
    }

    private class logoutUserTask extends AsyncTask<ParamModel, Void, Boolean> {
        public logoutUserTask() {
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
            String token = Pref.getUserKey();
            //String token = Pref.getPushToken();
            String result = PostMethod.getInstance().send(params[0], token);

            Log.e("logout_token", token);
            try {
                JSONObject jsonObject = new JSONObject(result);
                int error = jsonObject.getInt("ErrorCode");
                Log.e("logout_doinback", "errorCode: " + error);
                if (error == 0) {
                    return true;
                }
            } catch (Exception e) {
                Log.e("logout_doinback_catch", e.toString());
            }

            return false;
        }
        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 성공
                Log.e("logout_success", "onPostExecute: success");
                deleteToken();
            }else{

            }

        }

    }

    private void deleteToken(){
        String url = Constant.SERVER_URL + "/push_token/";

        ParamModel params = new ParamModel();
        params.setUrl(url);

        new deleteTokenTask().execute(params);
    }

    private class deleteTokenTask extends AsyncTask<ParamModel, Void, Boolean> {


        public deleteTokenTask(){

        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
            String token = Pref.getUserKey();
            String result = DeleteMethod.getInstance().send(params[0], token);

            try {
                JSONObject jsonObject = new JSONObject(result);

                int error = jsonObject.getInt("ErrorCode");
                if (error == 0) {
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
                Log.e("token_delete", "success");
            } else {

            }
        }
    }

}

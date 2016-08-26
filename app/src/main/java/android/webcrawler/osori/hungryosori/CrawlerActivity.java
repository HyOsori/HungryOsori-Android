package android.webcrawler.osori.hungryosori;


import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webcrawler.osori.hungryosori.Adapter.CrawlerViewPagerAdapter;
import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Http;
import android.widget.ToggleButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by 고건주 on 2016-08-25.
 */
public class CrawlerActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{

    public static ArrayList<CrawlerInfo> allCrawlerInfoList;
    public static ArrayList<CrawlerInfo> myCrawlerInfoList;

    private ArrayList<String> subscriptionIDs;

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

        /** 리스트 초기화 */
        allCrawlerInfoList = new ArrayList<>();
        myCrawlerInfoList = new ArrayList<>();
        subscriptionIDs = new ArrayList<>();

        /** 서버에서 정보 가져오기 */
        getCrawlerInfo();
    }

    private void getCrawlerInfo() {
        setViewPagerAdapter();
        getEntireList();
        getSubscriptionList();
    }

    /** Crawler 전체 정보를 가져오는 함수 */
    private void getEntireList(){
        String url = Constant.SERVER_URL + "/req_entire_list";

        ParamModel params = new ParamModel();

        params.setUrl(url);
        params.setParamStr("user_id",Constant.userID);
        params.setParamStr("user_key", Constant.userKey);

        new getEntireListTask(this).execute(params);
    }

    private class getEntireListTask extends AsyncTask<ParamModel, Void, Boolean> {

        private Context mContext;

        public getEntireListTask(Context context){
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

            if(result == null){
                return false;
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String message = jsonObject.getString(Constant.MESSAGE);
                    if(message.equals(Constant.MESSAGE_SUCCESS)){
                        JSONArray jsonArray = jsonObject.getJSONArray("crawlers");

                        for(int i=0; i<jsonArray.length(); ++i){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("crawler_id");
                            String url = object.getString("thumbnail_url");
                            String description = object.getString("description");
                            String title = object.getString("title");

                            allCrawlerInfoList.add(new CrawlerInfo(id, title, description, url, false));
                        }
                        return true;
                    }
                }catch(Exception e){

                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 성공
                CrawlerViewPagerAdapter.fragmentALL.listAdapter.notifyDataSetChanged();
            }else{
                // 실패
            }
        }
    }

    /** 사용자가 구독 중인 CrawlerID를 가져오는 함수 */
    private void getSubscriptionList(){
        String url = Constant.SERVER_URL + "/req_subscription_list";

        ParamModel params = new ParamModel();

        params.setUrl(url);
        params.setParamStr("user_id", Constant.userID);
        params.setParamStr("user_key", Constant.userKey);

        new getSubscriptionListTask(this).execute(params);
    }

    private class getSubscriptionListTask extends AsyncTask<ParamModel, Void, Boolean> {

        private Context mContext;

        public getSubscriptionListTask(Context context){
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

            if(result == null){
                return false;
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String message = jsonObject.getString(Constant.MESSAGE);
                    if(message.equals(Constant.MESSAGE_SUCCESS)){
                        JSONArray jsonArray = jsonObject.getJSONArray("subscriptions");

                        for(int i=0; i<jsonArray.length(); ++i){
                            String subscriptionsID = jsonArray.getString(i);
                            subscriptionIDs.add(subscriptionsID);
                        }

                        return true;
                    }
                }catch(Exception e){

                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 성공
                for(String id : subscriptionIDs){
                    for(CrawlerInfo crawlerInfo : allCrawlerInfoList)
                    {
                        if(crawlerInfo.getId().equals(id))
                        {
                            crawlerInfo.setSubscription(true);
                            myCrawlerInfoList.add(crawlerInfo);
                            break;
                        }
                    }
                }
                CrawlerViewPagerAdapter.fragmentMy.listAdapter.notifyDataSetChanged();
            }else{
                // 실패
            }
        }
    }

    private void setViewPagerAdapter()
    {
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

package android.webcrawler.osori.hungryosori;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

/**
 * Created by 고건주 on 2016-08-18.
 * 어플리케이션 시작 시 처음으로 보여지는 화면으로 DELAY_TIME 동안 실행됨
 * Shared preference 에 저장된 값들을 Constant 변수에 저장한다.
 * ImageLoader 를 초기화 한다.
 */
public class StartActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FirebaseMessaging.getInstance().subscribeToTopic("test");
 //     FirebaseInstanceId.getInstance().getToken();

        /** Preference 값을 저장 */
        Pref.init(this);
        Constant.keepLogin      = Pref.getKeepLogin();
        Constant.userKey        = Pref.getUserKey();
        Constant.cookie         = Pref.getCookie();
        Constant.userID         = Pref.getUserID();
        Constant.userPassword   = Pref.getUserPassword();
        Constant.pushToken      = Pref.getPushToken();

        /** 이미지 로더 등록 */
        initImageLoader(this);

        Handler handler = new Handler();

        if(Constant.keepLogin) {
            /** 이미 로그인 된 경우 */
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryLogin();
                    //Intent intent = new Intent(StartActivity.this, CrawlerActivity.class);
                    //startActivity(intent);
                }
            }, Constant.DELAY_TIME);
        }else{
            /** 로그인 되지 않은 경우에 로그인 페이지로 이동한다 */
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }, Constant.DELAY_TIME);
        }

    }
    public void initImageLoader(Context context) {

        /** This configuration tuning is custom. You can tune every option, you may tune some of them,
             or you can create default configuration by
             ImageLoaderConfiguration.createDefault(this);
             method. */

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    // 로그인 시도
    private void tryLogin(){
        String url = Constant.SERVER_URL + "/signin/";
        String pushToken = Pref.getPushToken();

        ParamModel params = new ParamModel();
        params.setUrl(url);
        params.addParameter("email", Constant.userID);
        params.addParameter("password", Constant.userPassword);
        params.addParameter("push_token", Constant.pushToken);

        new StartActivity.TryLoginTask().execute(params);
    }

    // 로그인을 시도하는 AsyncTask
    private class TryLoginTask extends AsyncTask<ParamModel, Void, Boolean> {
        private String  userKey  = Pref.DEFAULT_STRING_VALUE;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
//            String result = GetMethod.getInstance().send(params[0]);
            String result = PostMethod.getInstance().send(params[0]);
            try {
                JSONObject jsonObject = new JSONObject(result);
                int error = jsonObject.getInt("ErrorCode");
                if (error == 0) {
                    userKey = jsonObject.getString("token");
                    return true;
                }
            } catch (Exception e) {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 로그인 성공
                if(userKey != Pref.DEFAULT_STRING_VALUE) {
                    //지워도 될듯
//                    Pref.setUserKey(userKey);
//                    Pref.setUserID(Constant.userID);
//                    Pref.setUserPassword(Constant.userPassword);
//                    Pref.setKeepLogin(true);
                    //*******************************************
                    Log.d("login", ": success");
                    Intent intent = new Intent(StartActivity.this, CrawlerActivity.class);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }else{
                // 로그인 실패
                Log.d("login", ": fail");
                Intent intent = new Intent(StartActivity.this, AutoFailActivity.class);
                intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(StartActivity.this, "로그인 실패: ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}



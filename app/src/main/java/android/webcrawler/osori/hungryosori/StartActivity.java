package android.webcrawler.osori.hungryosori;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Pref;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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

        FirebaseMessaging.getInstance().subscribeToTopic("test");
 //       FirebaseInstanceId.getInstance().getToken();

        setContentView(R.layout.activity_start);

        /** Preference 값을 저장 */
        Constant.keepLogin      = Pref.getKeepLogin(this);
        Constant.userKey        = Pref.getUserKey(this);
        Constant.cookie         = Pref.getCookie(this);
        Constant.userID         = Pref.getUserID(this);
        Constant.userPassword   = Pref.getUserPassword(this);

        /** 이미지 로더 등록 */
        initImageLoader(this);

        if(Constant.keepLogin) {
            /** 이미 로그인 된 경우 */
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartActivity.this, CrawlerActivity.class);
                    startActivity(intent);
                }
            }, Constant.DELAY_TIME);
        }else{
            /** 로그인 되지 않은 경우에 로그인 페이지로 이동한다 */
            Handler handler = new Handler();
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
}

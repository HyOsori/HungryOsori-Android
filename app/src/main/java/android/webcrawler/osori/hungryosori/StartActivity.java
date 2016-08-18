package android.webcrawler.osori.hungryosori;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

/**
 * Created by 고건주 on 2016-08-18.
 * 어플리케이션 시작 시 처음으로 보여지는 화면으로 DELAY_TIME 동안 실행됨
 */
public class StartActivity extends FragmentActivity {
    private final int DELAY_TIME = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        /** 로그인 되지 않은 경우에 로그인 페이지로 이동한다 */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, DELAY_TIME);
    }
}

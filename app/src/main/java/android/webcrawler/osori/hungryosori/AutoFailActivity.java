package android.webcrawler.osori.hungryosori;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Lib;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class AutoFailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button goLogin_btn;
    private Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_fail);

        /** Preference 값을 저장 */
        Pref.init(this);
        Constant.keepLogin      = Pref.getKeepLogin();
        Constant.userKey        = Pref.getUserKey();
        Constant.cookie         = Pref.getCookie();
        Constant.userID         = Pref.getUserID();
        Constant.userPassword   = Pref.getUserPassword();
        Constant.pushToken      = Pref.getPushToken();

        //객체설정
        goLogin_btn =  (Button)findViewById(R.id.go_login_button);
        login_btn   =  (Button)findViewById(R.id.login_button);

        login_btn.setOnClickListener(this);
        goLogin_btn.setOnClickListener(this);

        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        ((TextView)findViewById(R.id.login_button)).setTypeface(fontArial);
        ((TextView)findViewById(R.id.go_login_button)).setTypeface(fontArial);


    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login_button:
                tryLogin();
                break;
            case R.id.go_login_button:
                intent = new Intent(AutoFailActivity.this, LoginActivity.class);
                intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

        }
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

        new AutoFailActivity.TryLoginTask().execute(params);
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
                    Log.d("login success", ": autoFail");
                    Intent intent = new Intent(AutoFailActivity.this, CrawlerActivity.class);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }else{
                // 로그인 실패
                Toast.makeText(AutoFailActivity.this, "로그인 실패: ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

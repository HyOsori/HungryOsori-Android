package android.webcrawler.osori.hungryosori;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webcrawler.osori.hungryosori.common.Constant;
import android.webcrawler.osori.hungryosori.common.Http;
import android.webcrawler.osori.hungryosori.common.Lib;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 고건주&김규민 on 2016-08-18.
 * 로그인 페이지 액티비티
 */

public class LoginActivity extends FragmentActivity {
    private static String email;
    private static String password;
    private EditText editText_mail, editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /** 객체 설정 */
        editText_mail       = (EditText) findViewById(R.id.login_editText_email);
        editText_password   = (EditText) findViewById(R.id.login_editText_password);

        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        editText_mail.setTypeface(fontArial);
        editText_password.setTypeface(fontArial);
        ((TextView)findViewById(R.id.login_button_join)).setTypeface(fontArial);
        ((Button)findViewById(R.id.login_button_login)).setTypeface(fontArial);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login_button_login:
                email = editText_mail.getText().toString().trim();
                password = editText_password.getText().toString().trim();

                /** 아이디가 올바른 이메일 형식인지 검사 */
                if (!Lib.isValidEmail(email)) {
                    Toast.makeText(this, "잘못된 email 형식입니다.", Toast.LENGTH_SHORT).show();
                    editText_mail.requestFocus();
                    break;
                }

                /** 패스워드가 최소 6자리를 넘었는지를 검사 */
               if (password.length() < Constant.PASSWORD_LENGTH_MIN) {
                    Toast.makeText(this, "패스워드는 6자리 이상 입력해주세요.", Toast.LENGTH_LONG).show();
                    editText_password.requestFocus();
                    break;
                }
                tryLogin();
                break;

            case R.id.login_button_join:
                intent = new Intent(this,JoinActivity.class);
                startActivity(intent);
                break;
        }
    }

    // 로그인 시도
    private void tryLogin(){
        String url = Constant.SERVER_URL;
        String paramStr = "";

        Http.ParamModel params = new Http.ParamModel();
        params.setUrl(url);
        params.setParamStr(paramStr);

        new GetSubCategoryTask(this).execute(params);
    }

    // 로그인을 시도하는 AsyncTask
    private class GetSubCategoryTask extends AsyncTask<Http.ParamModel, Void, Boolean> {
        Context mContext;

        public GetSubCategoryTask(Context mContext){
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Http.ParamModel... params) {
            // TODO Auto-generated method stub
            Http http = new Http();

            String result = http.send(params[0]);

            if(result == null){
                return false;
            }else{

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 로그인 성공
                Intent intent = new Intent(mContext, CrawlerActivity.class);
                startActivity(intent);
            }else{
                // 로그인 실패
            }
        }
    }

}

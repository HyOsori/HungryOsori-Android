package android.webcrawler.osori.hungryosori;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webcrawler.osori.hungryosori.Method.GetMethod;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Lib;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;


/**
 * Created by 고건주&김규민 on 2016-08-18.
 * 로그인 페이지 액티비티
 */

public class LoginActivity extends FragmentActivity{

    private String email;
    private String password;
    private EditText editText_mail, editText_password;
    private CheckBox keepLogin_checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /** 객체 설정 */
        editText_mail       = (EditText) findViewById(R.id.login_editText_email);
        editText_password   = (EditText) findViewById(R.id.login_editText_password);
        keepLogin_checkBox  = (CheckBox) findViewById(R.id.keeplogin_checkBox);



        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("HI:", "Refreshed token: " + token);
        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        editText_mail.setTypeface(fontArial);
        editText_password.setTypeface(fontArial);
        ((TextView)findViewById(R.id.login_button_join)).setTypeface(fontArial);
        ((Button)findViewById(R.id.login_button_login)).setTypeface(fontArial);
        ((CheckBox)findViewById(R.id.keeplogin_checkBox)).setTypeface(fontArial);

    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login_button_find:
                intent = new Intent(LoginActivity.this,FindPwActivity.class);
                startActivity(intent);
                break;

            case R.id.login_button_login:
                email       = editText_mail.getText().toString().trim();
                password    = editText_password.getText().toString().trim();

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
        String url = Constant.SERVER_URL + "/signin/";
        String pushToken = Pref.getPushToken();

        ParamModel params = new ParamModel();
        params.setUrl(url);
        params.addParameter("email", email);
        params.addParameter("password", password);
        params.addParameter("push_token", pushToken);

        new TryLoginTask().execute(params);
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
                    if(keepLogin_checkBox.isChecked()) {
                        Pref.setUserKey(userKey);
                        Pref.setUserID(email);
                        Pref.setUserPassword(password);
                        Pref.setKeepLogin(true);
                    } else{
                        Pref.setUserKey(userKey);
                        Pref.setUserID(email);
                        Pref.setUserPassword(password);
                        Pref.setKeepLogin(false);
                    }

                    Intent intent = new Intent(LoginActivity.this, CrawlerActivity.class);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }else{
                // 로그인 실패
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

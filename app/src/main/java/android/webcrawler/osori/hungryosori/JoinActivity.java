package android.webcrawler.osori.hungryosori;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Lib;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

/**
 * Created by 고건주&김규민 on 2016-08-18.
 * 회원가입 페이지 액티비티
 */

public class JoinActivity extends FragmentActivity {

    private String email;
    private String password;
    private String rePassword;
    private String name;

    private EditText editText_mail, editText_password, editText_rePassword, editText_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        /** 객체 설정 */
        editText_mail = (EditText) findViewById(R.id.join_editText_email);
        editText_password = (EditText) findViewById(R.id.join_editText_password);
        editText_rePassword = (EditText) findViewById(R.id.join_editText_repassword);
        editText_name       = (EditText) findViewById(R.id.join_editText_name);

        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        editText_mail.setTypeface(fontArial);
        editText_password.setTypeface(fontArial);
        editText_rePassword.setTypeface(fontArial);
        ((Button)findViewById(R.id.join_button_submit)).setTypeface(fontArial);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.join_button_submit:
                email = editText_mail.getText().toString().trim();
                password = editText_password.getText().toString().trim();
                rePassword = editText_rePassword.getText().toString().trim();
                name = editText_name.getText().toString().trim();

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

                /** 비밀번호 확인 */
                if(!password.equals(rePassword)){
                    Toast.makeText(this,"비밀번호 확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                    break;
                }

                /** 이름 길이 확인 */
                if(name.length() < Constant.NAME_LENGTH_MIN){
                    Toast.makeText(this, "이름은 2자 이상 입력해주세요.", Toast.LENGTH_LONG).show();
                    break;
                }

                /** 서버 연동 */
                tryJoin();
                break;
        }
    }

    // 로그인 시도
    private void tryJoin(){
        String url = Constant.SERVER_URL + "/signup/";

        ParamModel params = new ParamModel();
        params.setUrl(url);
        params.addParameter("email", email);
        params.addParameter("password", password);
        params.addParameter("name", name);
        params.addParameter("sign_up_type", "email");

        new TryJoinTask().execute(params);
    }

    // 회원가입 시도하는 AsyncTask
    private class TryJoinTask extends AsyncTask<ParamModel, Void, Boolean> {
       public int error = -1;

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
                error = jsonObject.getInt("ErrorCode");
                if (error == 0) {
                    return true;
                } else if (error == -1) {
                    return false;
                } else if (error == -100) {
                    return false;
                } else if (error == -200) {
                    return false;
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 회원가입 성공
                Toast.makeText(JoinActivity.this,"가입 성공. 로그인창으로 이동합니다\n메일을 확인해주세요.",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                switch(error){
                    case -1:
                        Toast.makeText(JoinActivity.this,"가입 오류",Toast.LENGTH_SHORT).show();
                        break;
                    case -100:
                        Toast.makeText(JoinActivity.this,"가입 오류: 이미 존재하는 계정",Toast.LENGTH_SHORT).show();
                        break;
                    case -200:
                        Toast.makeText(JoinActivity.this,"가입 오류: osori@hanynag.ac.kr 형태로 입력하십시오.",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

}

package android.webcrawler.osori.hungryosori;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Http;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


/**
 * Created by 김규민 on 2016-09-01.
 * 비밀번호 변경 페이지 액티비티
 */

public class ChangePwActivity extends FragmentActivity {

    public static String email;
    private static String password;
    private static String passwordNew;
    private static String passwordNewChk;

    private EditText editText_mail, editText_password, editText_passwordNew, editText_passwordNewChk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        /** 객체 설정 */
//
        editText_password = (EditText) findViewById(R.id.cng_editText_password);
        editText_passwordNew = (EditText) findViewById(R.id.cng_editText_passwordnew);
        editText_passwordNewChk = (EditText) findViewById(R.id.cng_editText_passwordnewchk);
        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");

        editText_password.setTypeface(fontArial);
        editText_passwordNew.setTypeface(fontArial);
        editText_passwordNewChk.setTypeface(fontArial);

        ((Button)findViewById(R.id.cng_button_submit)).setTypeface(fontArial);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cng_button_submit:

                email = Pref.getUserID();
                password = editText_password.getText().toString().trim();
                passwordNew = editText_passwordNew.getText().toString().trim();
                passwordNewChk = editText_passwordNewChk.getText().toString().trim();

                /** 패스워드 체크 */
                if(password.equals(passwordNew) && password.equals(Pref.getUserPassword())){
                    Toast.makeText(this,"올바르지 않은 비밀번호 입니다", Toast.LENGTH_SHORT).show();
                    break;
                }

                /** 패스워드가 최소 6자리를 넘었는지를 검사 */
                if (passwordNew.length() < Constant.PASSWORD_LENGTH_MIN) {
                    Toast.makeText(this, "패스워드는 6자리 이상 입력해주세요.", Toast.LENGTH_LONG).show();
                    editText_password.requestFocus();
                    break;
                }
                /** 비밀번호 확인 */
                if(!passwordNew.equals(passwordNewChk)){
                    Toast.makeText(this,"새로운 비밀번호 확인 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                /** 서버 연동 */
                tryChange();
                break;
        }
    }

    // 로그인 시도
    private void tryChange(){
        String url = Constant.SERVER_URL + "/password_change/";
        ParamModel params = new ParamModel();
        params.setUrl(url);
        //조인 추가

        params.setParamStr("user_id", email);
        params.setParamStr("password", password);
        params.setParamStr("new_password",passwordNew);
        new TryChangeTask(this).execute(params);
    }

    // 회원가입 시도하는 AsyncTask
    private class TryChangeTask extends AsyncTask<ParamModel, Void, Boolean> {
        private Context mContext;
        private int error;
        public TryChangeTask(Context mContext){
            this.mContext = mContext;
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
                try{
                    JSONObject jsonObject = new JSONObject(result);

                    error = jsonObject.getInt("ErrorCode");
                    if(error == 0){
                        return true;
                    }
                    else if(error == -1){
                        return false;
                    }
                    else if(error == -100){
                        return false;
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
                // 회원가입 성공
                Toast.makeText(ChangePwActivity.this,"비밀번호 변경 완료",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, CrawlerActivity.class);
                startActivity(intent);
            }else{
                switch(error){
                    case -1:
                        Toast.makeText(ChangePwActivity.this, "변경 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case -100:
                        Toast.makeText(ChangePwActivity.this, "존재하지 않는 사용자", Toast.LENGTH_SHORT).show();
                        break;
                }

                // 회원가입 실패
            }
        }
    }


}

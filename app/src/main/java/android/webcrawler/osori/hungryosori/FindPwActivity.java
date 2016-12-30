//에러 뜨고 있음 :: 수정 해야 함.

package android.webcrawler.osori.hungryosori;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;


/**
 * Created by 김규민 on 2016-08-18.
 * 비밀번호 찾기 페이지 액티비티
 */

public class FindPwActivity extends FragmentActivity {

    public static String email;
    private EditText editText_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        editText_email = (EditText) findViewById(R.id.find_editText_email);
        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        editText_email.setTypeface(fontArial);
        ((Button) findViewById(R.id.find_button_submit)).setTypeface(fontArial);
     }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.find_button_submit:
                email = editText_email.getText().toString().trim();
                /** 서버 연동 */
                tryFind();
                break;
        }
    }

    // 로그인 시도
    private void tryFind() {
        String url = Constant.SERVER_URL + "/password/";

        ParamModel params = new ParamModel();
        params.setUrl(url);
        params.addParameter("user_id", email);

        new TryFindTask().execute(params);
    }

    // 회원가입 시도하는 AsyncTask
    private class TryFindTask extends AsyncTask<ParamModel, Void, Boolean> {

        private int error = -1;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
             String result = PostMethod.getInstance().send(params[0]);

            if (result == null) {
                return false;
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    error = jsonObject.getInt("ErrorCode");
                    if (error == 0) {
                        return true;
                    } else if (error == -1) {
                        return false;
                    }
                } catch (Exception e) {

                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if (success) {
                // 회원가입 성공
                Toast.makeText(FindPwActivity.this, "비밀번호 변경 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FindPwActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                switch (error) {
                    case -1:
                        Toast.makeText(FindPwActivity.this, "변경 실패", Toast.LENGTH_SHORT).show();
                        break;
                }

                // 회원가입 실패
            }
        }
    }


}

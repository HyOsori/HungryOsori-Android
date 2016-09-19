//에러 뜨고 있음 :: 수정 해야 함.

package android.webcrawler.osori.hungryosori;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Http;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;


/**
 * Created by 김규민 on 2016-08-18.
 * 비밀번호 찾기 페이지 액티비티
 */

public class FindPwActivity extends FragmentActivity {

    public static String email;
    private static String password;
    private static String passwordNew;

    private EditText editText_email;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        editText_email = (EditText) findViewById(R.id.find_editText_email);
        /** 폰트 설정 */
        Typeface fontArial = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");

        editText_email.setTypeface(fontArial);

        ((Button) findViewById(R.id.find_button_submit)).setTypeface(fontArial);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        String url = Constant.SERVER_URL + "/req_find_password";
        ParamModel params = new ParamModel();
        params.setUrl(url);
        params.setParamStr("user_id", email);
        new TryFindTask(this).execute(params);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "FindPw Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://android.webcrawler.osori.hungryosori/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "FindPw Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://android.webcrawler.osori.hungryosori/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    // 회원가입 시도하는 AsyncTask
    private class TryFindTask extends AsyncTask<ParamModel, Void, Boolean> {
        private Context mContext;
        private int error;

        public TryFindTask(Context mContext) {
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

            if (result == null) {
                return false;
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String message = jsonObject.getString(Constant.MESSAGE);
                    error = jsonObject.getInt("error");
                    passwordNew = jsonObject.getString("new_password");
                    if (message.equals(Constant.MESSAGE_SUCCESS)) {
                        Log.d("New password","New password:" + passwordNew);
                        return true;
                    } else if (error == -1) {
                        return false;
                    } else if (error == -100) {
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
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                switch (error) {
                    case -1:
                        Toast.makeText(FindPwActivity.this, "변경 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case -100:
                        Toast.makeText(FindPwActivity.this, "존재하지 않는 사용자", Toast.LENGTH_SHORT).show();
                        break;
                }

                // 회원가입 실패
            }
        }
    }


}

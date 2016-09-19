package android.webcrawler.osori.hungryosori;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webcrawler.osori.hungryosori.Common.Pref;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by kkm on 2016-08-29.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        Context context = getApplicationContext();
        String token = FirebaseInstanceId.getInstance().getToken();
        String prefToken = Pref.getPushtoken(context);
        Log.d(TAG, "Refreshed token: " + token);
                Pref.setPushToken(context, token);

    }
}

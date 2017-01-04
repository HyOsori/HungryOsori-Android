package android.webcrawler.osori.hungryosori;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webcrawler.osori.hungryosori.Common.Pref;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * OPENSOURCE: Created by FCM/Google on 2016-08-29. 김규민 modified
 *
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        Context context = getApplicationContext();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);
        Pref.setPushToken(token);
        // sendRegistrationToServer(token);
    }
}
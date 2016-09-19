package android.webcrawler.osori.hungryosori;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

/**
 * Created by kkm on 2016-08-29.
 */

//fcm

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived");
        remoteMessage.getData();
        sendNotification(remoteMessage.getData().get("data_message"));
    }

    private void sendNotification(String messageBody) {
        //클릭했을때 StartActivity로 가게된다.//
        Intent intent = new Intent(this, StartActivity.class);

//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("알림: 새로운 푸쉬가 들어왔습니다.")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
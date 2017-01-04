package android.webcrawler.osori.hungryosori;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

/**
 * OPENSOURCE: GOOGLE CDM : 김규민 modified
 */

//fcm

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    private static int msgId =0;
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (remoteMessage.getData().get("body") != null) {
                sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), remoteMessage.getData().get("clickurl"));
            }
            if (remoteMessage.getData().get("body") == null) {
                sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("clickurl"));
            }
        }

    }

    private void sendNotification(String messageTitle, String messageBody, String clickUrl) {
/*        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
*/
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri u = Uri.parse(clickUrl);
        i.setData(u);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500, 500, 500, 500, 500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Log.d(TAG, " " + msgId);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(msgId++  /* ID of notification */, notificationBuilder.build());
    }
    private void sendNotification(String messageTitle, String clickUrl) {
/*        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
*/
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri u = Uri.parse(clickUrl);
        i.setData(u);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500, 500, 500, 500, 500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_osori)
                .setContentTitle(messageTitle)
                .setContentText("클릭하여 확인해주세요")
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Log.d(TAG, " " + msgId);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(msgId++ /* ID of notification */, notificationBuilder.build());
    }
}
package b.android.untitled.com.notiftester;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final String CHANNEL_ID="NotifTester";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FIREBASE MESSAGING","Callback is successful Jai Mahishmathi");
        String s = remoteMessage.getData().get("body");
        Log.d("FIREBASE MESSAGING",s);

        NotificationManager manager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Updates",
                    "ATMOS Updates", NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),audioAttributes);
            notificationChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( this, CHANNEL_ID)
                .setSmallIcon(R.drawable.googleg_disabled_color_18)
                .setContentTitle("Firebase Message Title")
                .setContentText(s)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Log.d("FIREBASE MESSAGING","Notification Builder is built");
        manager.notify(1, mBuilder.build());
        Log.d("FIREBASE MESSAGING","Notification sent");
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}

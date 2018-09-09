package b.android.untitled.com.notiftester;

import android.content.Context;
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
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( this, CHANNEL_ID)
                .setSmallIcon(R.drawable.googleg_disabled_color_18)
                .setContentTitle("Firebase Message Title")
                .setContentText(s)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Log.d("FIREBASE MESSAGING","Notification Builder is built");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());
        Log.d("FIREBASE MESSAGING","Notification sent");
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}

package b.android.untitled.com.notiftester;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private FirebaseFunctions mFunctions;

    private AtomicInteger msgId = new AtomicInteger(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFunctions = FirebaseFunctions.getInstance();

        final SharedPreferences preferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        boolean flag = preferences.getBoolean("subscribed",false);
        if(!flag) {
            FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);

                            if (!task.isSuccessful()) msg = getString(R.string.msg_subscribe_failed);
                            else preferences.edit().putBoolean("subscribed",true).apply();

                            Log.d("MainActivity", msg);
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Updates",
                    "ATMOS Updates", NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),audioAttributes);
            notificationChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
            NotificationManager manager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        final TextInputEditText mTxt = findViewById(R.id.input_text);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("messages");

                HashMap<String,String> map = new HashMap<>();
                map.put("text",mTxt.getText().toString());

                myRef.child("3").setValue(map);
            }
        });
    }
}

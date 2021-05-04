package com.tram.saletech.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class GetDataService extends Service {
    Handler handler = new Handler();
    int i = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i <= 10 && i >= 0 ){
                    i++;
                    handler.postDelayed(this , 2000);
                }
            }
        },2000);


        return START_NOT_STICKY;
    }


}

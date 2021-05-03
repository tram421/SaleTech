package com.tram.saletech;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.Activities.NoInternetActivity;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BroadcastInternet extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                //do stuff
                Toast.makeText(context, "Đã có kết nối", Toast.LENGTH_SHORT).show();
                new CountDownTimer(10000, 1000) {
                    public void onFinish() {
                        if(MainActivity.active == false) {
                            Intent intent1 = new Intent(context, MainActivity.class);
                            context.startActivity(intent1);
                        }

                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                        Toast.makeText(context, "Đang chờ kết nối lại...", Toast.LENGTH_SHORT).show();
                    }
                }.start();





            } else {
                MainActivity.active = false;
                // wifi connection was lost
                Toast.makeText(context, "Mất kết nối", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(context, NoInternetActivity.class);
                context.startActivity(intent2);
            }
        }
    }
}

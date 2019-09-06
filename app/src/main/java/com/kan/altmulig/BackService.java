package com.kan.altmulig;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.net.wifi.WifiManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.TimeUnit;

public class BackService extends Service {

    // the countdown itself
    public CountDownTimer timer;

    // 3 hours in milliseconds
    int waitTimeInMilliSeconds = 3600000;

    // check if done
    boolean isDone = false;

    @Override
    public void onCreate () {
        super.onCreate ();
        startTimer ();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        return super.onStartCommand (intent, flags, startId);
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
    }

    @Override
    public IBinder onBind (Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException ("Not yet implemented");
    }

    public void startTimer (){
        //Countdown timer for button
        timer = new CountDownTimer (waitTimeInMilliSeconds, 1000) {
            @Override
            public void onTick (long millisUntilFinished) {

                // counts the seconds left, used for visual information in the app
                @SuppressLint("DefaultLocale") String secondsToText = (String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                System.out.println (secondsToText);
            }

            @Override
            public void onFinish () {
               isDone = true;

               if (isDone){
                    disableWifi ();
                    alertBox ();
               }
            }
        }.start ();
    }

    public void disableWifi(){
        WifiManager wifiManager = (WifiManager)getApplicationContext ().getSystemService (Context.WIFI_SERVICE);
        try {
            wifiManager.setWifiEnabled (false);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void alertBox(){
        // Notification when button is clicked by the other app
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "kim_vel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLights (0xff00ff00 , 300,100)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("The timer has ended.")
                .setContentText("WI-FI has been turned off, good night!");

        assert notificationManager != null;
        notificationManager.notify(1, notificationBuilder.build());
    }
}

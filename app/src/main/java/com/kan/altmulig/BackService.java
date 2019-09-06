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
import androidx.core.app.NotificationCompat;
import java.util.concurrent.TimeUnit;

public class BackService extends Service {

    // the countdown itself
    public CountDownTimer timer;

    // 1 hour in milliseconds
    int waitTimeInMilliSeconds = 3600000;
    
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
                    disableWifi ();
                    alertBox ();
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
}

package com.example.gesllprovet;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 *Denna klass skulle användas som en del för att få en ständig notification där man kan stoppa musiken osv
 * även när man har lämnat appen
 * @author Oliver Gallo, olga7031
 */


public class ApplicationClass extends Application {
    public static final String CHANNEL_ID_1 = "channel1";//gör channel1
    public static final String CHANNEL_ID_2 = "channel2";//gör channel2

    @Override
    public void onCreate() {
        super.onCreate();
        creatNotificationChannel();
    }

    private void creatNotificationChannel() {
        //kontrollerar api leveln
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID_1,
                    "Channel(1)", NotificationManager.IMPORTANCE_HIGH);//ny notis
            channel1.setDescription("Channel 1 description");
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID_2,
                    "Channel(2)", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Channel 2 description");//ny notis

            //gör notiser
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel1);//notis 1
            }
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel2);//notis 2
            }
        }
    }
}

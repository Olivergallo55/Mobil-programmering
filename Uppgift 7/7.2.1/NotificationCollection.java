package com.example.uppgift7;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 *  Denna klass förenklar att kunna få meddelandet till telefonen, genom att göra de två channels som man behöver i notis klassen
 *
 * @author Oliver Gallo, olga7031
 */


public class NotificationCollection extends Application {
    public static final String CHANNEL_1 = "channel1";
    public static final String CHANNEL_2 = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        createChannels();//gör de två channels
    }

    //metoden för att göra channels som notis klassen kommer att använda
    private void createChannels() {

        //kontrollerar användarens api level
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1, "channel1", NotificationManager.IMPORTANCE_HIGH
            );//nya channel1 med hög importence
            channel1.setDescription("this is channel1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2, "channel2", NotificationManager.IMPORTANCE_LOW
            );//nya channel2 med låg importence
                channel2.setDescription("this is channel2");

            NotificationManager manager = getSystemService(NotificationManager.class);//hämtar meddelande manager

                manager.createNotificationChannel(channel1);//gör en meddelande kanal
                manager.createNotificationChannel(channel2);//gör en meddelande kanal

        }
    }
}

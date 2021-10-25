package com.example.uppgift7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import static com.example.uppgift7.NotificationCollection.CHANNEL_1;
import static com.example.uppgift7.NotificationCollection.CHANNEL_2;


/**
 *  Denna klass skickar en notis och man kan öpnna meddelandet, vilket kommer visa den genom en toast
 *
 * @author Oliver Gallo, olga7031
 */


public class Notis extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notis);
        notificationManager = NotificationManagerCompat.from(this);
        //hitar edittexterna
        editTextTitle = findViewById(R.id.title);
        editTextMessage = findViewById(R.id.message);
    }
    //skickar notis som kommer att poppas upp
    public void channel1(View view){
        //hämtar de angivna texterna
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);// en ny intent
        PendingIntent content = PendingIntent.getActivity(this, 0, intent, 0);//vid klick tar tillbaka till huvudmenyn

         Intent broadCast = new Intent(this, NotificationReciver.class);// gör en broadcast intent
         broadCast.putExtra("Open message", message);//ger den extra data

        PendingIntent action = PendingIntent.getBroadcast(this, 0, broadCast, PendingIntent.FLAG_UPDATE_CURRENT);//gör en ny pending intent med broadcast

        //gör notifikationen till channel 1
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1)
                .setSmallIcon(R.drawable.pool)//sätter bilden
                .setContentTitle(title)//sätter den angivna titeln
                .setContentText(message)//sätter den angivna texten
                .setPriority(NotificationCompat.PRIORITY_HIGH)//gör så att den poppar upp
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)//sätter den till en instant message
                .setContentIntent(content)//sätter centent
                .setColor(Color.BLUE)//sätter färgen
                .setAutoCancel(true)//tas bort automatiskt efter att den är klickad
                .addAction(R.mipmap.ic_launcher, "Open message", action)//lägger till action vilket var en pending intent
                .build();//bygger meddelandet

        notificationManager.notify(1, notification);//gör meddelandet synbar
        Toast.makeText(this, "The message pops up", Toast.LENGTH_SHORT).show();//visar toast för användaren

    }
    //skickar diskret notis
    public void channel2(View view){

        String title = editTextTitle.getText().toString();//sätter den angivna titeln
        String message = editTextMessage.getText().toString();//sätter den angivna titeln

        //gör notifikationen till channel 2
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2)
                .setSmallIcon(R.drawable.hockey)//sätter bilden
                .setContentTitle(title)//sätter den angivna titeln
                .setContentText(message)//sätter den angivna texten
                .setPriority(NotificationCompat.PRIORITY_LOW)//gör en diskret meddelande
                .build();//bygger notifikationen

        notificationManager.notify(2, notification);//gör meddelandet synbar
        Toast.makeText(this, "The message didnt pop up, go look at it", Toast.LENGTH_SHORT).show();//visar toast för användaren
    }
}
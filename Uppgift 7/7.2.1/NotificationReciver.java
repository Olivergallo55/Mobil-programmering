package com.example.uppgift7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//
//Detta är en Broadcast reciver klass som sedan används i notis klassen
//
public class NotificationReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("Open message");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

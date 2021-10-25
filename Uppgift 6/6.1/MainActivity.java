package com.example.uppgift6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 *  Denna klass innehåller fragments för att kunna öpnna telefonen och kunna ringa
 *  skicka sms till en vald person samt att få en sms
 *
 * @author Oliver Gallo, olga7031
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);//hittar fragment menyn
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);//gör den intergrerbar

    }
    //gör en ny fragment many
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment frag = null;
                    switch (item.getItemId()){
                        case R.id.call:
                            frag = new Call_Fragment();//öppnar ring fragment
                            break;
                        case R.id.sms:
                            frag = new Send_Fragment();//öppnar sms fragment
                            break;
                        case R.id.recive:
                            frag = new Recive_Fragment();//öppnar få sms fragment
                            break;
                    }
                        assert frag != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout, frag).commit();
                    return true;
                }
            };

    //startar metoden för att kunna önna kontakter på mobilen
    public void call(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setType(CallLog.Calls.CONTENT_TYPE);//öppnar mobilens kontakter
        startActivity(intent);//startar aktiviteten
    }

    //startar metoden för att kunna skicka sms
    public void sendMessage(View view) {
        //hämtar de angivna textrena bland annat nummret och meddelandet
        EditText numberText = findViewById(R.id.toNumber);
        EditText messageText = findViewById(R.id
                .sendNumber);
        String number = numberText.getText().toString();//lägger nummret i en sträng
        String message = messageText.getText().toString();//lägger meddelandet i en sträng

        //kontroll
        if(number.length() == 0 || message.length() == 0){
            return;
        }
        //kontroll
        if(checkPermission()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);//skapar sms och skickar
            Toast.makeText(this, "message sent", Toast.LENGTH_SHORT).show();//visar att sms har skickats
            //rensar input fälterna för att göra det smidigt för nästa användning
            numberText.setText("");
            messageText.setText("");
        }
    }

    //kontrollerar om användaren har gett tillåtelse att kunna skicka sms inom appen
    private boolean checkPermission() {
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);//kontrollerar tillåtelsen
        return (check == PackageManager.PERMISSION_GRANTED);//tillåtelsen är tillåtet
    }
}
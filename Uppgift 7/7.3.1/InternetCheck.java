package com.example.uppgift7;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class InternetCheck extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_check);
        //hittar imageviewn och textviewn
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView2);

    }
    //kollar om nätverken är ansluten
    @SuppressLint("SetTextI18n")
    public void checkNetworkConnection(View view) {
            boolean wifiConnected, mobileConnected;//bool för att kolla hur man är uppkopplad

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//hämtar connectivity service
        NetworkInfo activeInfo = null;
        //kontroll
        if (connectivityManager != null) {
            activeInfo = connectivityManager.getActiveNetworkInfo();//tilldelar nätverkinformation
        }
        if(activeInfo != null && activeInfo.isConnected()){//ansluten med mobildata eller wifi
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI; //tilldelar wfi connection
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;  //tilldelar mobildata connection
            if(wifiConnected){//wifi ansluten
                imageView.setImageResource(R.drawable.wifi);//sätter wifi bild
                textView.setText("Ansluten till wifi");//sätter texten till att det är wifi ansluten
            }else if(mobileConnected){//mobildata ansluten
                imageView.setImageResource(R.drawable.mobilenet);//sätter mobildata bild
                textView.setText("Ansluten till mobilnät");//sätter texten till att det är mobildata ansluten
            }

        }else{ //ingen internet anslutning
            imageView.setImageResource(R.drawable.nowifi);//sätter ingen internetanslutnings bild
            textView.setText("Kunnde inte ansluta till nätverket");//sätter texten till att mobilen är inte ansluten till någon nätverk

        }
    }
}
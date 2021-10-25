package com.example.uppgift7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

/**
 *  Denna klass gör att man trycker på en knapp och telefonen börjar vibrera
 *
 * @author Oliver Gallo, olga7031
 */


public class Vibrate extends AppCompatActivity {
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);//hämtar vibratorn
    }

    public void vibrate(View view) {
        vibrator.vibrate(1000);//gör att telefonen vibrerar i 1000 milisekund
    }
}
package com.example.uppgift7;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


//************************************************
//
//Denna app funkar endast med telefoner som har temperatur mätare inbyggda, jag byggde den med android studios egen
//telefon och där funkade appen perfekt
//
//************************************************
public class Temperatur extends AppCompatActivity  implements SensorEventListener {
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private boolean isTempAvailable;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatur);
        textView = findViewById(R.id.textView4);//hittar textviewn

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);//hämtar sensor managern för att kunna mäta den omgivande temperaturen

        //kontroll om temperatur mätaren finns på mobilen
        if (sensorManager != null) {
            if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
                tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);//hämtar temperatur mätaren
                isTempAvailable = true;//temperatur mätare finns

            }else{
                textView.setText("Temperatur mätaren är inte tillgänglig");//text om temperatur mätaren inte finns
                Toast.makeText(this, "Temperatur mätaren är inte tillgänglig", Toast.LENGTH_SHORT).show();//text om temperatur mätaren inte finns
                isTempAvailable = false;//temperatur mätare finns inte
            }
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        textView.setText(event.values[0]+"celsius");//sätter temperaturs värde till text
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isTempAvailable){
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);//temperatur sensorens delay
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTempAvailable){
            sensorManager.unregisterListener(this);//oregistrerar sensormanagern
        }
    }
}
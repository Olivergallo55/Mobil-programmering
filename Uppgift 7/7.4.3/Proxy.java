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


//************************************************
//
//Denna app kan känna va närheten, om någonting kommer nära max avståndet vilket är 5cm avståndet så känner av mobilen
//det och ändrar textviewn till dens min avstånd, vilket är 0cm
//Däremot fungerar det felfritt, med bråkdel på android studios egen inbyggda mobil
//denna klass implementerar sensor eventlistner vilket kan känna av sensorens förändring
//
//************************************************
public class Proxy extends AppCompatActivity implements SensorEventListener {
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor proxySensor;
    private Boolean isProxyAvailable;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);

        //hittar textView
        textView = findViewById(R.id.textView5);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);//hämtar sensor service

        //kontroll ifall telefonen har proxy mätare
        if (sensorManager != null) {
            if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
                proxySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);//hämtar proxy sensorn
                isProxyAvailable = true;//proxy finns
            }else{
                textView.setText("Proxy is not avaliable");
                isProxyAvailable = false;//proxy finns inte
            }
        }

    }

    //obligatoriska implementationer från interfacet
    @Override
    public void onSensorChanged(SensorEvent event) {
        textView.setText(String.format("%scm", event.values[0]));//skriver ut värdet på närheten
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isProxyAvailable){
            sensorManager.registerListener(this, proxySensor, SensorManager.SENSOR_DELAY_NORMAL);//proxy sensorens delay
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(isProxyAvailable){
            sensorManager.unregisterListener(this);//oregistrerar sensormanagern
        }
    }
}
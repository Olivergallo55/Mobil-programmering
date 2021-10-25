package com.example.uppgift7;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 *  Denna klass beräknar accelerometern, den mäter mobilens rörelse, denna klass
 *  implementerar sensor eventlistner vilket kan känna av sensorens förändring
 *
 * @author Oliver Gallo, olga7031
 */

public class Acelerometer extends AppCompatActivity implements SensorEventListener {
    private TextView xView, yView, zView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acelerometer);
        //hittar textviewn för dem olika lederna
        xView = findViewById(R.id.x);
        yView = findViewById(R.id.y);
        zView = findViewById(R.id.z);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);//hämtar sensor managern för att kunna mäta telefonens rörelse

        //kontroll om accelerometer mätare finns på mobilen
        if (sensorManager != null) {
            if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){

                accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//hämtar accelerometern
                isAccelerometerSensorAvailable = true;

                //kontroll i fall telefonen inte stödjer accelerometern
            }else{
                xView.setText("Accelerometer är inte tillgänglig");
                isAccelerometerSensorAvailable = false;
            }
        }
    }

    //Denna metod sätter accelerometerns värde på de olika lederna så att de är synliga
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        xView.setText(event.values[0] +"m/s2");//värde för x led
        yView.setText(event.values[1] +"m/s2");//värde för y led
        zView.setText(event.values[2] +"m/s2");//värde för z led
    }

    //behövdes implamenteras pga interfacet
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isAccelerometerSensorAvailable){
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);//accelerometers sensorens delay
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelerometerSensorAvailable){
            sensorManager.unregisterListener(this);//oregistrerar sensormanagern
        }
    }
}
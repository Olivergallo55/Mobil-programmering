package com.example.uppgift3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.util.UUID;

/**
 *  Denna klass spelar in audio och kan spela upp det
 *
 * @author Oliver Gallo, olga7031
 */

public class Audio extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 1000;
    private Button recordButton, stopRecordingButton, playButton, stopButton;
    private String pathSave = "";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        //hittar knapparna
        recordButton = (Button) findViewById(R.id.recordButton);
        stopRecordingButton = (Button) findViewById(R.id.stopRecButton);
        playButton = (Button) findViewById(R.id.playButton);
        stopButton = (Button) findViewById(R.id.stopButton);
    }

    //förberedder recordern genom att välja audio source, format och encoder
    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);//sparar
    }

    //ber användaren om tillåtelse att använda mikrofonen och externa filer
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    //kontrollerar användarens tillåelse
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                toast("Permission Granted");//meddelar att använderan tillät användningen av mikrofonen och externa filer
            else
                toast("Permission Denied");//meddelar att använderan tillät inte användningen av mikrofonen och externa filer
        }

    }
    //kontrollerar om användaren tillät användningen av mikrofonen och externa filer
    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED; // retunerar att användaren tillät göra det
    }

    //förenklad toast metod
    public void toast(String s){
        Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
    }

    //spela in knappen som spelar audio
    public void record(View view) {
        if(checkPermissionFromDevice()){//kontrollerar åtkomst
        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString()
                + "audio_record.3gp";//sparar inspelningen till en sträng
        setupMediaRecorder();//förberedder för inspelning
        try{
            mediaRecorder.prepare();//förberedder inspelning
            mediaRecorder.start();//startar inspelning
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sätter knappar otillgängliga
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
        toast("Recording...");
        }else{
            requestPermission();//frågar om åtekomst
        }
    }
    //stoppar inspelningen
    public void stopRecord(View view) {
        if(checkPermissionFromDevice()){//kontrollerar åtkomst
        mediaRecorder.stop();//stoppar inspelning
            //hanterar knapparna
        stopRecordingButton.setEnabled(true);
        playButton.setEnabled(true);
        recordButton.setEnabled(true);
        stopButton.setEnabled(false);
        }else{
            requestPermission();//frågar om åtekomst
        }
    }

    public void play(View view) {
        if(checkPermissionFromDevice()){//kontrollerar åtkomst
            //hanterar knapparna
        stopButton.setEnabled(true);
        stopRecordingButton.setEnabled(false);
        recordButton.setEnabled(false);

        mediaPlayer = new MediaPlayer();//allokerar en ny mediaplayer
        try{
            mediaPlayer.setDataSource(pathSave);//hittar inspelade audion
            mediaPlayer.prepare();//förberedder audion

        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();//startar uppspelningen
        toast("Playing...");//meddelar användaren att uppspelning har börjat
     } else{
            requestPermission();//frågar om åtekomst
        }
    }

    public void stop(View view) {
        if(checkPermissionFromDevice()){//kontrollerar åtkomst
            //hanterar knapparna
        stopRecordingButton.setEnabled(true);
        recordButton.setEnabled(true);
        stopButton.setEnabled(false);
        playButton.setEnabled(true);

        //stoppar uppspelningen och förberdder för en ny inspelning
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            setupMediaRecorder();
        }
        } else{
            requestPermission();//frågar om åtekomst
        }
    }
}
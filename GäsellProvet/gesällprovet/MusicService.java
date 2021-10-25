package com.example.gesllprovet;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import java.util.ArrayList;
import static com.example.gesllprovet.PlayActivity.listSongs;

/**
 * Denna klass sköter mediaplayern
 * @author Oliver Gallo, olga7031
 */

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
   private IBinder myBinder =  new MyBinder();
   private MediaPlayer mediaPlayer;
   private ArrayList<MusicFiles> musicFiles = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }
    //binder
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    //Binder inre klass
    public class MyBinder extends Binder {
        //hämtar service
        MusicService getService() {
            return MusicService.this;
        }
    }

    //metod när den ska börja
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);//gör en ny position
        //kontroll
        if(myPosition != -1){
            playMedia(myPosition);//startar metoden
        }
        return START_STICKY;//startar speciellt bra för musik appar
    }

    //metoden som spelar musiken
    private void playMedia(int startPosition) {
        musicFiles = listSongs;//tilldelar låtarna
        //kontroll
        if(mediaPlayer != null){
            mediaPlayer.stop();//stoppar
            mediaPlayer.release();//släpper
            //kontroll
            if(musicFiles != null){
                createMediaPlayer(startPosition);//startar metoden
                mediaPlayer.start();//startar mediaplayer
            }
        }
        else{
            createMediaPlayer(startPosition);//startar metoden
            mediaPlayer.start();//startar mediaplayer
        }

    }

    //startar mediaplayern
    public void start(){
        mediaPlayer.start();
    }
    //stoppar mediaplayern
    public void stop(){
        mediaPlayer.stop();
    }
    //kontrollerar om mediaplayern splear något
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    //släpper mediaplayern
    public void release(){
        mediaPlayer.release();
    }
    //hämtar tiden mediaplayern
    public int getDuration(){
        return mediaPlayer.getDuration();
    }
    //söker till position
    public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }
    //gör en ny mediaplayer
    public void createMediaPlayer(int position){
        Uri uri = Uri.parse(musicFiles.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }
    //hämtar positionen
    int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }
    //pausar
    void pause(){
        mediaPlayer.pause();
    }
    //när den är klar
    void OnCompleted(){
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {}
}

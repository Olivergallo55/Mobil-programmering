package com.example.gesllprovet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Random;
import static com.example.gesllprovet.AlbumDetailAdapter.albumFiles;
import static com.example.gesllprovet.MainActivity.isRepeatOn;
import static com.example.gesllprovet.MainActivity.isShuffleOn;
import static com.example.gesllprovet.MainActivity.musicFiles;

/**
 * Denna klass sköter allt som tillhör till att spela upp musiken, såsom att skippa låten, att köra om den osv
 * @author Oliver Gallo, olga7031
 */

public class PlayActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        ActionPlaying, ServiceConnection {

    private TextView song_name, artist_name, duration_played, duration_total;
    private SeekBar seekbar;
    private FloatingActionButton playPauseBtn;
    private ImageView cover_art, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn;
    private int position = -1;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    private MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();//startar initilize metoden
        getIntentMethod();//startar metoden

        //kollar om man har ändrat nåt medd seekbaren
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(musicService != null && fromUser){
                    musicService.seekTo(progress * 1000);//letar efter den specifika tiden användaren satte musiken på
                }
            }
            //metod som följde med men inte behövs
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            //metod som följde med men inte behövs
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        //startar en thread för klassens aktivitet
        PlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //kontroll
                if(musicService != null){
                    int mCurrentPosition = musicService.getCurrentPosition() /1000;//hittar positionen
                    seekbar.setProgress(mCurrentPosition);//lägger seekbaren på rätt plats
                    duration_played.setText(formattedTime(mCurrentPosition));//sätter ut rätt tid av låtens längd
                }
                handler.postDelayed(this, 1000);//lägger daley
            }
        });
        playPauseBtn.setImageResource(R.drawable.pause);//sätter bilden till pausad om man pausar låten

        //hanetar shuffle knappen
        shuffleBtn.setOnClickListener(v -> {
            if(isShuffleOn){
                isShuffleOn = false;
                shuffleBtn.setImageResource(R.drawable.shuffle);//sätter bild
            }else{
                isShuffleOn = true;
                shuffleBtn.setImageResource(R.drawable.shuffle_on);//sätter bild
            }
        });
        //hanterar repeat knappen
        repeatBtn.setOnClickListener(v -> {
            if(isRepeatOn){
                isRepeatOn = false;
                repeatBtn.setImageResource(R.drawable.repeat);//sätter bild
            }else{
                isRepeatOn = true;
                repeatBtn.setImageResource(R.drawable.repeat_on);//sätter bild
            }
        });
    }

    //startar grejer
    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);//binder
        playThread();//startar spela tråden
        prevThread();//startar låten innan tråden
        nextThread();//startar låten efter tråden
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);//slutar binda
    }

    //metoden skapar ett nytt tråd för att kunna skippa låtar
    private void nextThread() {
        //skapar tråden
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(v -> nextBtnClicked());//startar metoden med lambda
            }
        };
        nextThread.start();//startar tråden
    }

    //metoden för när man vill skippa en låt
    public void nextBtnClicked() {
        //kontroll om musiken spelar
        if(musicService.isPlaying())
        {
            need();//startar metoden
            make();//startar metoden

            //gör en ny tråd
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService != null){
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000; //hämtar positionen
                        seekbar.setProgress(mCurrentPosition);//sätter seekbaren
                    }
                    handler.postDelayed(this, 1000);//lägger till en delay
                }
            });
            musicService.OnCompleted();//den är klar
            playPauseBtn.setBackgroundResource(R.drawable.pause);//sätter bild
            musicService.start();//startar
        }
        else{
             need();//startar metoden
             make();//startar metoden
             playingThread();//startar metoden
         }
    }

    //utbrytningsmetod där shuffle och repeat button är i focus
    private void need(){
        musicService.stop();//stoppar spelningen
        musicService.release();//släpper
        if(isShuffleOn && !isRepeatOn){//spelar random låt om shuffle knappen är på
            position = getRandom(listSongs.size() - 1);//tilldelar positionens värde
        }else if(!isShuffleOn && !isRepeatOn){//spelar nästa låt i listan
            position = ((position +1) % listSongs.size());//tilldelar positionen
        }
    }

    //startar tråden
    private void playingThread(){
        //Startar tråden
        PlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //kontroll
                if(musicService != null){
                    int mCurrentPosition = musicService.getCurrentPosition() /1000;//sätter positionen
                    seekbar.setProgress(mCurrentPosition);//sätter seekbaren
                }
                handler.postDelayed(this, 1000);//delay
            }
        });
        musicService.OnCompleted();//den är klar
        playPauseBtn.setBackgroundResource(R.drawable.play);//sätter bilden
    }

    //utbrytningsmetod hittar all info som den behöver
    private void make(){
        uri = Uri.parse(listSongs.get(position).getPath());//tilldelar uri ett värde
        musicService.createMediaPlayer(position);//gör en mediaplayer
        metaData(uri);//metadata
        song_name.setText(listSongs.get(position).getTitle());//sätter låt namn
        artist_name.setText(listSongs.get(position).getArtist());//sätter artist namn
        seekbar.setMax(musicService.getDuration() / 1000);//sätter seekbaren
    }

    //sättter random
    private int getRandom(int size) {
        Random random = new Random();//ny random
        return random.nextInt(size + 1);//gör en random
    }

    //metoden för den förrgående metoden
    private void prevThread() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                //gör en listner och startar metoden
                prevBtn.setOnClickListener(v -> prevBtnClicked());
            }
        };
        prevThread.start();//startar tråden
    }

    //previous knapp metoden
    public void prevBtnClicked() {
        if(musicService.isPlaying())
        {
           firstPart();//startar metoden
        }else{
            secondPart();//startar metoden
        }
    }

    //första delen av prevBtnClicked
    private void firstPart(){
        musicService.stop();//stoppar
        musicService.release();//släpper
        info();//info metoden
        running();//running metoden

        musicService.OnCompleted();//den är klar
        playPauseBtn.setBackgroundResource(R.drawable.pause);//sätter bilden
        musicService.start();//startar
    }

    //Andra delen av prevBtnClicked
    private void secondPart(){
        musicService.stop();//stoppar
        musicService.release();//släpper
        info();//startar metoden
        running();//startar metoden
        musicService.OnCompleted();//den är klar
        playPauseBtn.setBackgroundResource(R.drawable.play);//sätter bilden
    }

    //metoden som bestämmer positionen
    private void info(){
        if(isShuffleOn && !isRepeatOn){//spelar random låt om shuffle knappen är på
            position = getRandom(listSongs.size() - 1);//sätter positionen
        }else if(!isShuffleOn && !isRepeatOn){//spelar nästa låt i listan
            position = ((position - 1) < 0 ? (listSongs.size() -1) : (position - 1));//sätter positionen
        }
       make();//startar metoden
    }

    //startar tråden
    private void running(){
        //startar tråden
        PlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //kontroll
                if(musicService != null){
                    int mCurrentPosition = musicService.getCurrentPosition() /1000;//sätter positionen
                    seekbar.setProgress(mCurrentPosition);//sätter seekbaren
                }
                handler.postDelayed(this, 1000);//delay
            }
        });
    }

    //spela tråd metoden
    private void playThread() {
        playThread = new Thread(){//ny tråd
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(v -> playPauseBtnClicked());//startar metoden med lambda
            }
        };
        playThread.start();//startar tråden
    }

    //spela knapp metoden
    public void playPauseBtnClicked() {
        //kontroll
        if(musicService.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.play);//sätter bilden
            musicService.pause();//pausar
            //startar tråden
        }else{
            playPauseBtn.setImageResource(R.drawable.pause);//sätter bilden
            musicService.start();//startar
            //startar tråden
        }
        seekbar.setMax(musicService.getDuration() / 1000);//sätter seekbaren
        running();//startar tråd metoden
    }

    //metod för totaltiden
    private String formattedTime(int mCurrentPosition) {
        String totalOut, totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);//sekunder
        String minutes = String.valueOf(mCurrentPosition / 60);//minuter
        totalOut = minutes + ":" + seconds;//tilldelar totalvärde
        totalNew = minutes + ":" + "0" + seconds;//tilldelar nya värdet
        //kontroll
        if(seconds.length() == 1){
            return totalNew;//retunerar nya värdet
        }
        return totalOut;//retunerar totala värden
    }

    //tilldelningsmetod
    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);//position
        String sender = getIntent().getStringExtra("sender");//sender
        if(sender != null && sender.equals("albumDetails")){
            listSongs = albumFiles;//tilldelar låtar
        }else{
            listSongs = musicFiles;//tilldelar låtar
        }
        if(listSongs != null){
            playPauseBtn.setImageResource(R.drawable.pause);//sätter bild
            uri = Uri.parse(listSongs.get(position).getPath());//tilldelar uri
        }
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("servicePosition", position);//lägger extra data
        startService(intent);//startar
    }

    //intiliserar alla views
    private void initView() {
        //hittar alla textViews
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationPlayed);
        duration_total = findViewById(R.id.durationTotal);
        cover_art = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.next);
        prevBtn = findViewById(R.id.skip);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.shuffle);
        repeatBtn = findViewById(R.id.repeat);
        playPauseBtn = findViewById(R.id.play_pause);
        seekbar = findViewById(R.id.seekBar);
    }

    //metod för det mesta som händer på skärmen, tid, bild, låt osv.
    private void metaData(Uri uri){
    try{
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();//skapar metadata
        retriever.setDataSource(uri.toString());//sätter data källan
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;//hämtar totala låttiden
        duration_total.setText(formattedTime(durationTotal));//sätter totala låttiden
        byte[] art = retriever.getEmbeddedPicture();//byte array för bilderna
        Bitmap bitmap;//ny bitmap för bilderna
        if(art != null){
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);//decodar arrayens innehåll, alltså bilderna
            imageAnimation(this, cover_art, bitmap);//gör bild animation
            Palette.from(bitmap).generate(palette -> {//genererar bilder
                Palette.Swatch swatch = null;//hämtar bildens rgb färger
                if (palette != null) {
                    swatch = palette.getDominantSwatch();//hämtar de mest dominanta färgerna
                }
                //om bilden hittas
                if(swatch != null){
                    ImageView gredient = findViewById(R.id.imageViewGradient);//hittar imageview
                    RelativeLayout mContainer = findViewById(R.id.mContainer);//gör en konteiner
                    gredient.setBackgroundResource(R.drawable.gradient_bg);//sätter bakgrunden för imageviewn
                    mContainer.setBackgroundResource(R.drawable.main_bg);//sätter bakgunden för konteinern
                    //fixar färg utseendet så att de passar med bilden och ser smidigt ut
                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{swatch.getRgb(), 0x00000000});
                    gredient.setBackground(gradientDrawable);
                    GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{swatch.getRgb(), swatch.getRgb()});
                    mContainer.setBackground(gradientDrawableBg);//sätter bakgrundsfärgen
                    song_name.setTextColor(swatch.getTitleTextColor());//sätter färg på låt fält
                    artist_name.setTextColor(swatch.getBodyTextColor());//sätter färg på namn fält
                }
                else{
                    ImageView gredient = findViewById(R.id.imageViewGradient);//hittar imageview
                    RelativeLayout mContainer = findViewById(R.id.mContainer);//gör en konteiner
                    gredient.setBackgroundResource(R.drawable.gradient_bg);//sätter bakgrunden för imageviewn
                    mContainer.setBackgroundResource(R.drawable.main_bg);//sätter bakgunden för konteinern
                    //fixar färg utseendet så att de passar om bilden saknas och  fixar så att det ser smidigt ut
                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{0xff000000, 0x00000000});
                    gredient.setBackground(gradientDrawable);
                    GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{0xff000000, 0xff000000});
                    mContainer.setBackground(gradientDrawableBg);//sätter bakgrundsfärgen
                    song_name.setTextColor(Color.WHITE);//sätter låt titelns färgen till vit
                    artist_name.setTextColor(Color.DKGRAY);//sätter artistens färg till mörkt grå
                }
            });
        }else{
            Glide.with(this).asBitmap().load(R.drawable.music).into(cover_art);//hämtar basic bilden
            ImageView gredient = findViewById(R.id.imageViewGradient);//hittar imageviewn
            RelativeLayout mContainer = findViewById(R.id.mContainer);//hittar konteinern
            gredient.setBackgroundResource(R.drawable.gradient_bg);//sätter bakgrundsfärgen
            mContainer.setBackgroundResource(R.drawable.main_bg);//sätter konteiners bild
            song_name.setTextColor(Color.WHITE);//sätter titelns färg till vit
            artist_name.setTextColor(Color.DKGRAY);//sätter artistens namn till mörkt grå
            }
        } catch (IllegalArgumentException e) {
        e.printStackTrace();
        }
    }

    //metod för smidig bild annimation
    public void imageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap){
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);//hämta fade out animation
        final Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);//hämta fade in animation
        //sätter en lyssnare
        animOut.setAnimationListener(new Animation.AnimationListener() {
            //medföljande metod
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);//laddar bilderna
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    //medföljande metod
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    //medföljande metod
                    @Override
                    public void onAnimationEnd(Animation animation) {}
                    //medföljande metod
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                imageView.startAnimation(animIn);//startar vid slutet av låten animationen
            }
            //medföljande metod
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        imageView.startAnimation(animOut);//startar animationen vid början av ny låt
    }

    //metod för när den är klar
    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
        if(musicService != null){
            musicService.createMediaPlayer(position);
            musicService.start();
            musicService.OnCompleted();
        }
    }

    //tillbaka knapp
    public void backBtn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder)service;
        musicService = binder.getService();
        Toast.makeText(musicService, "Connected" + musicService, Toast.LENGTH_SHORT).show();
        seekbar.setMax(musicService.getDuration() / 1000);
        metaData(uri);
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        musicService.OnCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }
}
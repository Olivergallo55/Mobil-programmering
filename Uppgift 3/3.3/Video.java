package com.example.uppgift3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

/**
 *  Denna klass gör att man kan spela in en video och sedan titta på den
 *
 * @author Oliver Gallo, olga7031
 */

public class Video extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private  VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.videoView2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //kontrollerar
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "video tagen",Toast.LENGTH_SHORT).show();//skriver ut att videon har tagits
            Uri videoUri = intent.getData();
            videoView.setVideoURI(videoUri);//sätter upp videon i videoView
        }
    }

    public void capture(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//öppnar kameran

        if(videoIntent.resolveActivity(getPackageManager()) != null){//så länge det inte är null
            startActivityForResult(videoIntent, REQUEST_VIDEO_CAPTURE);//öppna kameran och börja spela in
        }
    }

    public void playVideo(View view) {
        videoView.start();//startar videon
    }
}
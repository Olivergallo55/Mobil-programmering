package com.example.uppgift4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *  Denna klass har tre knappar som kan öppna olika youtube videos
 *
 * @author Oliver Gallo, olga7031
 */

public class Youtube extends AppCompatActivity {
    Button b, sportBtn, musicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        //hittar knapparna
        b = findViewById(R.id.button);
        sportBtn = findViewById(R.id.button2);
        musicBtn = findViewById(R.id.button3);

        //sätter nöje knappen till en händelse
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntertainmentVid(v);
            }
        });
        //sätter sport knappen till en händelse
        sportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sportVid(v);
            }
        });
        //sätter musik knappen till en händelse
        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicVid(v);
            }
        });

    }
    //nöje mknapps metod
    public void EntertainmentVid(View view) {
        //gör en ny intent med en youtube video
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=fYWK65nzJxs"));
        try {
            startActivity(intent);//startar youtube videon
            //gör om det ifall något fel skulle ha uppkommit
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="));
            startActivity(intent);
        }
    }

    public void sportVid(View view) {
        //gör en ny intent med en youtube video
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=kE7D7qFayVg"));
        try {
            startActivity(intent);//startar youtube videon
            //gör om det ifall något fel skulle ha uppkommit
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="));
            startActivity(intent);
        }
    }

    public void musicVid(View view) {
        //gör en ny intent med en youtube video
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        try {
            startActivity(intent);//startar youtube videon
            //gör om det ifall något fel skulle ha uppkommit
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="));
            startActivity(intent);
        }
    }
}
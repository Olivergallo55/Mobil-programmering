package com.example.uppgift3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 *  Uppgiften berör 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.8
 *  Denna klass fungerar som en meny där man kan välja vilken aktivitet man vill starta
 *
 * @author Oliver Gallo, olga7031
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar= findViewById(R.id.toolbar); //costum toolbar
        setSupportActionBar(toolbar);//sätter menyn till toolbaren
    }

    //startar den valda aktiviteten
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.photo:
                Intent intent = new Intent(this, Photo.class);
                startActivity(intent);//startar foto
                break;
            case R.id.video:
                Intent intent2 = new Intent(this, Video.class);
                startActivity(intent2);//startar video
                break;
            case R.id.audio:
                Intent intent3 = new Intent(this, Audio.class);
                startActivity(intent3);//startar audio
                break;
            case R.id.SpeechToText:
                Intent intent5 = new Intent(this, SpeechToText.class);
                startActivity(intent5);//startar text till tal
                break;
            case R.id.TexToSpeech:
                Intent intent6 = new Intent(this, TextToSpeech.class);
                startActivity(intent6);//startar tal till text
                break;
            case R.id.rita2d:
                Intent intent7 = new Intent(this, draw2D.class);
                startActivity(intent7);//startar rita app
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //gör menyn
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}
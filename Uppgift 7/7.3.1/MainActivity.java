package com.example.uppgift7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 *  Uppgiften berör 7.1.1, 7.2.1, 7.2.2, 7.3.1, 7.4.1, 7.4.2, 7.4.3
 *  Denna klass fungerar som en meny där anbvändaren kan gå till önskade aktiviteten
 *
 * @author Oliver Gallo, olga7031
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);//costum toolbar
        setSupportActionBar(toolbar);//initialiserar toolbaren
    }

    //gör meny alternativen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.copypaste:
                Intent intent = new Intent(this, CopyPaste.class);
                startActivity(intent);//startar copy paste klassen
                break;
            case R.id.notis:
                Intent intent2 = new Intent(this, Notis.class);
                startActivity(intent2);//startar notis klassen
                break;
            case R.id.vibrate:
                Intent intent3 = new Intent(this, Vibrate.class);
                startActivity(intent3);//startar vibrate klassen
                break;
            case R.id.internetCheck:
                Intent intent4 = new Intent(this, InternetCheck.class);
                startActivity(intent4);//startar internet check klassen
                break;
            case R.id.acelerometer:
                Intent intent5 = new Intent(this, Acelerometer.class);
                startActivity(intent5);//startar accelerometer klassen
                break;
            case R.id.temperatur:
                Intent intent6 = new Intent(this, Temperatur.class);
                startActivity(intent6);//startar temperatur klassen
                break;
            case R.id.proxy:
                Intent intent7 = new Intent(this, Proxy.class);
                startActivity(intent7);//startar proxy klassen
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
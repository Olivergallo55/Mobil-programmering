package com.example.uppgift4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 *  Uppgiften berör 4.1, 4.2, 4.3
 *  Denna klass fungerar som en meny där man kan välja vilken aktivitet man vill välja
 *
 * @author Oliver Gallo, olga7031
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //Metod som öpnnar den valda aktivitetet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.öpnnawebbsida:
                Intent intent = new Intent(this, OpenWebbPage.class);
                startActivity(intent);//öppnar webbsida
                break;
            case R.id.youtube:
                Intent intent2 = new Intent(this, Youtube.class);
                startActivity(intent2);//öppnar youtube
                break;
            case R.id.email:
                Intent intent3 = new Intent(this, Email.class);
                startActivity(intent3);//öppnar email
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
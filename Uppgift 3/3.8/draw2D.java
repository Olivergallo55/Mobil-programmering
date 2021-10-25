package com.example.uppgift3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/**
 *  Denna klass gör att man kan rita på skärmen
 *
 * @author Oliver Gallo, olga7031
 */
public class draw2D extends AppCompatActivity {

    private PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw2_d);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        paintView = findViewById(R.id.paintView);//hittar paintview
        DisplayMetrics displayMetrics = new DisplayMetrics();//beskriver almän information om skärmen
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);//gör att man kan rita anpassat till användarens skärm
        paintView.init(displayMetrics);//initialiserar paintviewn anpassad till användarens skärm
    }

    //gör en meny
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.drawmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //switch-sats som gör att man kan välja mellan olika sorters pennor eller att suddarytan
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.normal:
                paintView.normal();//normal penna
                return true;
            case R.id.emboss:
                paintView.emboss();//emboss penna
                return true;
            case R.id.blur:
                paintView.blur();//suddig penna
                return true;
            case R.id.clear:
                paintView.clear();//suddar ut ytan
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
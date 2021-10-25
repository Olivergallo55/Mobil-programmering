package com.example.uppgift4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.os.Bundle;
import android.webkit.WebViewClient;

/**
 *  Denna klass öppnar aftonbladet i appen och kan användas på samma sätt som på en vanlig webbläsare
 *  man kan även välja andra att öppna andra webbplatser i menyn
 *
 * @author Oliver Gallo, olga7031
 */

public class OpenWebbPage extends AppCompatActivity {

   private WebView web;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_webb_page);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        web = new WebView(this);//gör en ny webbview
        web = findViewById(R.id.webbview);//hittar webviewn
        web.setWebViewClient(new WebViewClient());
        WebSettings webSettings = web.getSettings();//hämtar instälningar
        webSettings.setJavaScriptEnabled(true);//gör det möjligt att kunna använda javascript skrivna webbsidor smidigt
        webSettings.setBuiltInZoomControls(true);//gör att man kan zooma in inom appen
        webSettings.setSupportZoom(true);//gör att man kan zooma in inom appen
        web.loadUrl("https://www.aftonbladet.se/");//börjar med att ladda aftonbladet som första sida
    }

    //hanterar när man trycker tillbaka knappen, att man går tillbaka till webbsidans förrgående sida
    @Override
    public void onBackPressed() {
        if(web.canGoBack())
            web.goBack();//går tillbaka till förgående sida
        else
        super.onBackPressed();
    }
    //gör en meny
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.webbmenu, menu);
        return true;
    }

    //ger olika alternativ att kunna öppna olika sidor
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.aftonbladet:
                web.loadUrl("https://www.aftonbladet.se/");//öppnar aftonbladet
                break;
            case R.id.dn:
                web.loadUrl("https://www.dn.se/");//öppnar dn
                break;
            case R.id.expressen:
                web.loadUrl("https://www.expressen.se/");//öppnar expressen
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
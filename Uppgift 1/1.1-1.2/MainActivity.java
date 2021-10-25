package com.example.inlmning1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 *  Uppgiften berör 1.1, 1.2 och 1.3
 *  Denna klass hämtar texten från editfield och vid tryck av knappen startar en ny aktivitet
 *
 * @author Oliver Gallo, olga7031
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //hämtar den angivna texten och ger värdet till en string och sedan startar en ny aktivitet
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editView);
        String message = editText.getText().toString();
        intent.putExtra("hej", message);//ger meddelandet en nyckel så att andra aktiviteten ska kunna hämta meddelandet
        startActivity(intent);//startar aktiviteten
    }
}
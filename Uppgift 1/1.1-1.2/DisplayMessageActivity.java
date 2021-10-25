package com.example.inlmning1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
* Denna klass hämtar och visar medelandet som man har angett i mainactivity klassen
*
* @author Oliver Gallo, olga7031
*/

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // hämtar intent och omvandlar det till en string
        Intent intent = getIntent();
        String message = intent.getStringExtra("hej");

        // hämtar textviewn och sätter in textmeddelandet
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }
}
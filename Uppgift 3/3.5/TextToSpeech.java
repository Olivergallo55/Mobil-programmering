package com.example.uppgift3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 *  Klassen har fel klass namn då jag blandade ihop text till tal och tal till text!!
 *  Denna klass lyssnar för vad använderan har sagt och skriver ut det i en text
 *
 * @author Oliver Gallo, olga7031
 */
public class TextToSpeech extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);
        //hittar textviewn och knappen
        textView = findViewById(R.id.textTv);
        ImageButton voiceBtn = findViewById(R.id.voiceBtn);

        //om man klickar på knappen får man en ruta som lyssnar på vad användaren har att säge
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen();//metoden som lyssnar
            }
        });

    }
    //metod som omvandlar röst dialog till text
    public void listen(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//känner igen språket
        //lägger extra data till intenten som språkmodell, förklarande text osv...
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try{
            //startar aktiviteten
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

        }catch(Exception e){
            //felmeddelande ifall något har gått snett
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SPEECH_INPUT){
            //hämtar text array från röst intent
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //sätter inspelade rösten på skärmen
            textView.setText(result.get(0));
        }
    }
}
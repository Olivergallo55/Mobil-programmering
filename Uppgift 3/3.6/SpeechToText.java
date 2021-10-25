package com.example.uppgift3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
import java.util.Locale;



/**
 *  Klassen har fel klass namn då jag blandade ihop text till tal och tal till text!!
 *  Denna klass kan läsa in en text och sedan kunna säga det, med några modifikationer såsom
 *  pitch och speed
 *
 * @author Oliver Gallo, olga7031
 */

public class SpeechToText extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private EditText editText;
    private SeekBar pitch;
    private SeekBar speed;
    private ImageButton speakerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        speakerBtn = findViewById(R.id.speakerButton);//hittar knappen

        //initialiserar text till tal funktionen
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){//kontrollerar om man har fått rätt data

                    int result = textToSpeech.setLanguage(Locale.getDefault());//sätter språket

                    //kontrollerar om språket stöds av programmet
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(SpeechToText.this, "Error language not supported", Toast.LENGTH_SHORT).show();//skriver ett felmeddelande om språket inte stöds av programmet
                    }else{

                        speakerBtn.setEnabled(true);//gör att man kan trycka på knappen
                    }

                }else{
                    //om programmet inte har fått rätt data skriver den ut en felmeddelande
                    Toast.makeText(SpeechToText.this, "Error initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //hämtar alla seekbar och textField
        editText = findViewById(R.id.editField);
        pitch = findViewById(R.id.seekBar2);
        speed = findViewById(R.id.seekBar3);

        //speaker button hanterare
        speakerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }
    //metoden som sköter att programmet kan säga den angivna texten
    public void speak(){
        String text = editText.getText().toString();//hämtar texten
        float pitcher = (float) pitch.getProgress()/50;//tillsätter pitch värde
        if(pitcher <=0.1 ) pitcher = 0.1f;//kontrollerar så att det inte kan bli minus

        float speeder = (float) speed.getProgress()/50;//tillsätter speed värde
        if(speeder <=0.1 ) speeder = 0.1f;//kontrollerar så att det inte kan bli minus

        textToSpeech.setPitch(pitcher);//sättter pitch
        textToSpeech.setSpeechRate(speeder);//sätter speed
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);//säger den angivna strängen
    }

    //metoden kontrollerar om systemet har sagt hela texten
    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();//slutar att säga texten
            textToSpeech.shutdown();//stoppar den helt clean
        }
        super.onDestroy();
    }
}
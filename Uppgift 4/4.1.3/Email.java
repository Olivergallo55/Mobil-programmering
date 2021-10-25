package com.example.uppgift4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 *  Denna klass skickar epost till den angivna personen med ett titel och meddelande del
 *  utöver det kan man även skicka bild filer
 *
 * @author Oliver Gallo, olga7031
 */


public class Email extends AppCompatActivity {
    private static final int PICK_ID = 10;
    private Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
    }

    //metoden för att kunna skicka emailen
    public void sendEmail(View view) {
        //hittar alla edittext
        EditText email = findViewById(R.id.email);
        EditText subject = findViewById(R.id.subject);
        EditText message = findViewById(R.id.message);

        String mimeType = "text/plain";

        //hjälper att dela data mellan aktiviteter
        ShareCompat.IntentBuilder
                .from(this)//från denna aktivitet
                .setType(mimeType)//sätter data typen till en sträng
                .setChooserTitle("What mail app do you want to use")//sätter titeln
                .addEmailTo(email.getText().toString())//lägger till mottagare
                .setSubject(subject.getText().toString())//lägger till ämne
                .setText(message.getText().toString())//lägger till meddelande
                .setStream(file)//sätter ett stream
                .startChooser();//startar aktiviteten där man får välja hur man vill skicka datan
        //rensar textfälterna för att göra det lättare för användaren
        email.getText().clear();
        subject.getText().clear();
        message.getText().clear();
    }

    //öppnar galleriet
    public void openFolder(View view){
        Intent intent = new Intent();//gör en ny intent
        intent.setType("image/*");//sätter typen
        intent.setAction(Intent.ACTION_GET_CONTENT);//sätter en ny händelse
        intent.putExtra("return-data", true);//lägger till extra data till denna intent
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_ID);//startar denna aktivitet
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //kontroll
        if(requestCode == PICK_ID && resultCode == RESULT_OK){
            if (data != null) {
                file = data.getData();//hämtar datan
            }
        }
    }
}
 package com.example.uppgift7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


 /**
  *  Denna klass ska man skriva in en text och sedan trycka på copy,
  *  efterår trycker man på paste så kommer den inskrivna texten att skriva upp
  *
  * @author Oliver Gallo, olga7031
  */


 public class CopyPaste extends AppCompatActivity {

    private EditText editText;
    private Button pasteBtn;
    private TextView textView;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_paste);
        //hittar knapparna, textviewn och edittexten
        editText = findViewById(R.id.inputfield);
        Button copyBtn = findViewById(R.id.copy);
        pasteBtn = findViewById(R.id.paste);
        textView = findViewById(R.id.textView);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);//hämtar clipboard service

        //kontroll
        if(clipboardManager != null && clipboardManager.hasPrimaryClip()){
            pasteBtn.setEnabled(false);
        }
        //initialisera copy knappen
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();//lägger den angivna texten i en sträng

                //kontroll
                if(!text.equals("")){
                    ClipData clipData = ClipData.newPlainText("text", text);//gör en ny klipp data
                    clipboardManager.setPrimaryClip(clipData);//kopierar texten

                    Toast.makeText(CopyPaste.this, "Copied", Toast.LENGTH_SHORT).show();//visar användaren att texten har kopierats
                    pasteBtn.setEnabled(true);//gör paste knappen tillgänglig
                }
            }
        });

        //initialisera paste knappen
        pasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clipData = clipboardManager.getPrimaryClip();//hämtar clip datan
                ClipData.Item item = null;//gör en item
                //kontroll
                if (clipData != null) {
                    item = clipData.getItemAt(0);//tilldelar item den kopierade texten
                }
                //kontroll
                if (item != null) {
                    textView.setText(item.getText().toString());//klistrar in den kopierade texten till textviewn
                }
                Toast.makeText(CopyPaste.this, "Pasted", Toast.LENGTH_SHORT).show();//meddelar användaren att texten har klistrats in
            }
        });
    }
}
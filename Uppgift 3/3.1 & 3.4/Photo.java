package com.example.uppgift3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 *  Denna klass gör att man kan ta bild eller välja från galleriet och se det på skärmen
 *
 * @author Oliver Gallo, olga7031
 */


public class Photo extends AppCompatActivity {
    private static final int PICK_ID = 123;
    private static final int PERMISSION_CODE = 1000;
    private ImageView im;
    public Audio a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        //hittar imageView och knapparna
        im =  findViewById(R.id.imageView);
        Button galleryBtn = (Button) findViewById(R.id.button2);

        //galleri knappen
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Kontrolerar api leveln
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){//kontrolerar om man har gett tillåtelse till telefonens filer

                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};//fråga om tillåtelse

                        requestPermissions(permission, PERMISSION_CODE);//pop up fråga om tillåtelse

                    }else{
                        pickImageFromGallery();//metod för att välja från galleriet

                    }
                }else{
                    pickImageFromGallery();//metod för att välja från galleriet
                }
            }
        });
    }

    //hämtar vald bild från galleriet
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_ID);
    }

    //frågar om tillåtelse
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                a.toast("Permission Granted");
                pickImageFromGallery();//hämtar från galleriet

            }else{
                a.toast("Permission Denied");
            }
        }
    }
//metod för att ta foto
    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_ID);
    }

    //metoden som lägger bilden som bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_ID){//kontrollerar bildens id
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");//hämtar bitmap
            Toast.makeText(this, "bild tagen",Toast.LENGTH_SHORT).show();//visar toast vid tagen bild
            im.setImageBitmap(bitmap);//lägger in bilden på skärmen
            im.setImageURI(data.getData());//lägger in bilden från galleriet på skärmen
        }
    }
}
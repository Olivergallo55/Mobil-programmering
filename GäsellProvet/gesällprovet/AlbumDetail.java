package com.example.gesllprovet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import static com.example.gesllprovet.MainActivity.musicFiles;

/**
 * Denna klass är för uteseendet av album layouten. ALbumdetaljerna
 * @author Oliver Gallo, olga7031
 */

public class AlbumDetail extends AppCompatActivity {

   private RecyclerView recyclerView;
   private ImageView albumPhoto;
   private String albumName;
   private ArrayList<MusicFiles> albumSongs = new ArrayList<>();
   private AlbumDetailAdapter albumDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        //hämtar saker
        recyclerView = findViewById(R.id.recyklerView1);
        albumPhoto = findViewById(R.id.album_photo);
        albumName = getIntent().getStringExtra("albumName");
        //lägger till låtarna i albumen
        int j = 0;
        for(int i = 0; i < musicFiles.size(); i++) {
            if (albumName.equals(musicFiles.get(i).getAlbum())) {
                albumSongs.add(j, musicFiles.get(i));
                j++;
            }
        }
        byte[] image = getAlbumArt(albumSongs.get(0).getPath());//hämtar bilder
        if(image != null){
            Glide.with(this)
                    .load(image)
                    .into(albumPhoto);//sätter bilder
        }else{
            Glide.with(this)
                    .load(R.drawable.music)
                    .into(albumPhoto);//sätter basic bilden
        }
    }

    //lägger ut album komponenterna
    @Override
    protected void onResume() {
        super.onResume();
        if(!(albumSongs.size() < 1)){
            albumDetailAdapter = new AlbumDetailAdapter(this, albumSongs);
            recyclerView.setAdapter(albumDetailAdapter);//sätter adaptern
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));//sätter ut de vertikalt

        }
    }
    //metod för att hämta album bilden
    private byte[] getAlbumArt(String uri){
        byte[] art = new byte[0];
        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();
            retriever.release();
        }catch(IllegalArgumentException e){
            e.getStackTrace();
        }
        return art;
    }
}
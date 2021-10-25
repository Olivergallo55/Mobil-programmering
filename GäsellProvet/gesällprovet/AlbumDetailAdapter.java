package com.example.gesllprovet;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * Denna klass behövs för tat kunna använda det i AlbumDetail klassen och för att kunna använda det i
 * recycleviewn
 * @author Oliver Gallo, olga7031
 */

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.MyHolder>{
    private Context mContext;
    static ArrayList<MusicFiles> albumFiles;

    //konstruktorn
    public AlbumDetailAdapter(Context mContext, ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        AlbumDetailAdapter.albumFiles = albumFiles;
    }

    //gör en hiriarchy av låtar
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyHolder(view);
    }
    //sköter bindningen av komponenter såsom bilder, texter, menyer osv
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_name.setText(albumFiles.get(position).getTitle());
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());
        if(image != null){
            Glide.with(mContext).asBitmap().load(image).
                    into(holder.album_image);
        }else{
            Glide.with(mContext).
                    load(R.drawable.music).
                    into(holder.album_image);
        }
        //klickar man på en album så kommer playactivity upp
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PlayActivity.class);
            intent.putExtra("sender","albumDetails");
            intent.putExtra("position", position);
            mContext.startActivity(intent);
        });
    }

    //arrayens storlek
    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        ImageView album_image;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.music_img);
            album_name = itemView.findViewById(R.id.music_filename);
        }
    }
    //costum recyklerview innre klass
    private byte[] getAlbumArt(String uri){
        byte[] art = new byte[0];
        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();//hämtar bild
            retriever.release();
        }catch(IllegalArgumentException e){
            e.getStackTrace();
        }
        return art;
    }
}

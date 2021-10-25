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
 * Denna klass är för albums
 * @author Oliver Gallo, olga7031
 */

public class Album_adapter extends RecyclerView.Adapter<Album_adapter.MyHolder>{
    private Context mContext;
    private ArrayList<MusicFiles> albumFiles;

    //konstruktor
    public Album_adapter(Context mContext, ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    //gör en hiriarchy av album
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new MyHolder(view);
    }

    //sköter bilder osv
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_name.setText(albumFiles.get(position).getAlbum());//sätter album namn
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());//hämtar album bild
        if(image != null){
            Glide.with(mContext).asBitmap().load(image).
                    into(holder.album_image);//sätter albumbild
        }else{
            //om album bild inte finns
            Glide.with(mContext).
                    load(R.drawable.music).
                    into(holder.album_image);//sätter det basic bilden
        }
        //en setOn click listner
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AlbumDetail.class);//ny intent
            intent.putExtra("albumName", albumFiles.get(position).getAlbum());//lägger extra data som namn och position
            mContext.startActivity(intent);//startar aktiviteten
        });
    }

    //album arrayens storlek
    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    //anonymt innre klass för recyclerViewn
    public static class MyHolder extends RecyclerView.ViewHolder{
        ImageView album_image;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //hämtar itemViews
            album_image = itemView.findViewById(R.id.album_image);
            album_name = itemView.findViewById(R.id.album_name);
        }
    }

    //hämtar album bilder
    private byte[] getAlbumArt(String uri){
        byte[] art = new byte[0];
        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);//sätter datan
            art = retriever.getEmbeddedPicture();//hämtar bild
            retriever.release();//släpper
        }catch(IllegalArgumentException e){
            e.getStackTrace();
        }
        return art;//retunerar bilden
    }
}

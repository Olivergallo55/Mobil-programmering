package com.example.gesllprovet;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.util.ArrayList;

/**
 * Denna klass är för låtar
 * @author Oliver Gallo, olga7031
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyVieHolder> {

    private Context mContext;
    static ArrayList<MusicFiles> mFiles;

    public MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles) {
        this.mContext = mContext;
        MusicAdapter.mFiles = mFiles;
    }

    // gör en hiriarchy av låtar
    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyVieHolder(view);
    }

    //sköter bindningen av komponenter såsom bilder, texter, menyer osv
    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, final int position) {
        holder.file_name.setText(mFiles.get(position).getTitle());//sätter titel namn
        byte[] image = getAlbumArt(mFiles.get(position).getPath());//hämtar låt bild
        if(image != null){
            Glide.with(mContext).asBitmap().load(image).
                    into(holder.album_art);//laddar låtens bild
        }else{
            Glide.with(mContext).
                    load(R.drawable.music).
                    into(holder.album_art);//laddar basic bilden
        }
        //klickar man på en låt så kommer playactivity upp
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PlayActivity.class);
            intent.putExtra("position",position);//extra data
            mContext.startActivity(intent);//startar play activity
        });
        //den visar menyn
        holder.menu_more.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(mContext, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());//gör en popup meny
            popupMenu.show();//visar menyn
            //gör en listner på den
            popupMenu.setOnMenuItemClickListener((item) ->{
                if (item.getItemId() == R.id.delete) {
                    deleteFile(position, v);//man kan ta bort låtar
                }
                return true;
            });
        });
    }

    //detta är för att ta bort filer
    private void deleteFile(int position, View v) {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFiles.get(position).getId()));//hämtar id osv
        File file = new File(mFiles.get(position).getPath());//hämtar positionen
        boolean deleted = file.delete();//tar bort filen
        //tar bort filen
        if(deleted){
            mContext.getContentResolver().delete(contentUri, null, null);
            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFiles.size());
            Snackbar.make(v,"File deleted", Snackbar.LENGTH_LONG)
                    .show();//visar att filen har tagits bort
        }else{
            Snackbar.make(v,"File cant be deleted", Snackbar.LENGTH_LONG)
                    .show();//ifall filen kan inte bli raderad
        }
    }

    //arrayens storlek
    @Override
    public int getItemCount() {
        return mFiles.size();
    }


    //costum recyklerview
    public static class MyVieHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        ImageView album_art, menu_more;

        //konstruktorn
        public MyVieHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_filename);
            album_art = itemView.findViewById(R.id.music_img);
            menu_more = itemView.findViewById(R.id.more_menu);
        }
    }

    //hmtar album bilden
    private byte[] getAlbumArt(String uri){
        byte[] art = new byte[0];
        try{
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        art = retriever.getEmbeddedPicture();
        retriever.release();
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        return art;
    }

    //uppdaterar listan
    public void updateLista(ArrayList<MusicFiles> musicFiles){
        mFiles = new ArrayList<>();
        mFiles.addAll(musicFiles);
        notifyDataSetChanged();//meddelar om datan har ändrats
    }

}

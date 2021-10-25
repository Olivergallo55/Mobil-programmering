package com.example.gesllprovet;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.gesllprovet.MainActivity.musicFiles;

/**
 * Denna klass är för Låt fragmenten
 * @author Oliver Gallo, olga7031
 */

public class SongFragment extends Fragment {

    private RecyclerView recyclerView;
    public static MusicAdapter musicAdapter;

    //konstruktor
    public SongFragment() {}

    //metoden lägger ut låt komponenterna
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if(!(musicFiles.size() < 1)){
            musicAdapter = new MusicAdapter(getContext(), musicFiles);
            recyclerView.setAdapter(musicAdapter);//sätter adapter
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), recyclerView.VERTICAL, false));//lägger ut låtar efter varandra verticalt
        }
        return view;
    }
}
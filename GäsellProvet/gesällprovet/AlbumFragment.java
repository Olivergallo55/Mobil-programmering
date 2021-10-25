package com.example.gesllprovet;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.gesllprovet.MainActivity.albums;

/**
 * Denna klass är för Album fragmenten
 * @author Oliver Gallo, olga7031
 */

public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    Album_adapter album_adapter;
    //kontruktor
    public AlbumFragment() {

    }


    //lägger ut komponenterna i ordning genom en recykle view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if(!(albums.size() < 1)){
            album_adapter = new Album_adapter(getContext(), albums);
            recyclerView.setAdapter(album_adapter);//sätter adaptern
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));//lägger ut layouten
        }
        return view;
    }
}
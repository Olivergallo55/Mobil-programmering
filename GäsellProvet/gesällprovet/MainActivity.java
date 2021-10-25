package com.example.gesllprovet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * Denna klass är Gäsellprovens huvudklass, När man öppnar mobilen är det här man börjar all aktivitet
 * här laddas upp alla låtar som finns nerladdat på mobilen, och om man swipear åt vänster så kommer man
 * till albums
 * @author Oliver Gallo, olga7031
 */


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final int REQUEST_CODE = 1001;
    static ArrayList<MusicFiles> musicFiles;
    static boolean isShuffleOn = false, isRepeatOn = false;
    static ArrayList<MusicFiles> albums = new ArrayList<>();
    private String MY_SORTER = "Sorted";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();//kontrollerar tillåtelse
    }

    //denna metod kontrollerar om man har gett tillåtelse för att använda externa filer på mobilen
    private void permission() {
        //om man inte har gett det än
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);//frågar om det
        }
        //annars
        else{
            musicFiles = getAllAudio(this);//tilldelar arrayen all musik som finns på datorn
            initViewPage();//och startar layoutens metod
        }
    }

    //metod för att fråga om tillåtelse
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //kontroll om användaren klickade in ok
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                musicFiles = getAllAudio(this);//tilldelar musiken
                initViewPage();//börjar layouten
            }
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE );//frågar om tillåtelse
        }
    }

    //layoutens metod
    private void initViewPage() {
        //hittar wiewPager och tabLayout
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());//kallar på innreklassen och hämtar så att man kan intergera med fragmenten på skärmen
        viewPagerAdapter.addFragment(new SongFragment(), "Songs");//adderar en fragment av låtar
        viewPagerAdapter.addFragment(new AlbumFragment(), "Albums");//adderar en fragment av album
        viewPager.setAdapter(viewPagerAdapter);//förser med views som det behövs
        tabLayout.setupWithViewPager(viewPager);//lägger ut alla data vertikalt

    }

    //annonymt innreklass
    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;


        //konstruktorn
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }
        //adderar fragments till arraylistan
        void addFragment(Fragment fragment, String title){
            fragments.add(fragment);//adderar fragment
            titles.add(title);//adderar titel
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);//hämtar artikelns position
        }

        @Override
        public int getCount() {
            return fragments.size();//hämtar arrayens storlek
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    //metod som hämtar musik från mobilen
    public ArrayList<MusicFiles> getAllAudio(Context context){
        SharedPreferences preferences = getSharedPreferences(MY_SORTER, MODE_PRIVATE);//behövs för att kunna sortera
        String sortOrder = preferences.getString("sorting", "sortByName");//lägger namn och värde som nyckel och värde
        ArrayList<String> duplicate = new ArrayList<>();//ny arraylist
        albums.clear();//för att slippa duplicera albums
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();//ny arraylist
        String order = null;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;//gör en ny uri
        //sorterings ordern
        switch (sortOrder){
            case"sortByName":
                order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";//sortera genom namn
                break;
            case"sortByDate":
                order = MediaStore.MediaColumns.DATE_ADDED + " ASC";//sortera genom datum
                break;
            case"sortBySize":
                order = MediaStore.MediaColumns.SIZE + " DESC";//sortera genom storlek
                break;
        }
        //En string array av låtar och deras attributer
        String[] projection;
            projection = new String[]{
                    MediaStore.Audio.Media.ALBUM,//lägger albumen på låten
                    MediaStore.Audio.Media.TITLE,//lägger titeln på låten
                    MediaStore.Audio.Media.DURATION,//lägger längden på låten
                    MediaStore.Audio.Media.DATA,//lägger datan på låten
                    MediaStore.Audio.Media.ARTIST,//lägger artisten på låten
                    MediaStore.Audio.Media._ID,//lägger idn på låten

            };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, order);//den kommer att peka på en rad av information, i denna fall på en låt
        //kontroll
        if(cursor != null){
            //loop för att kunna lägga ut och ge acces till alla låtar
            while(cursor.moveToNext()){
                String album = cursor.getString(0);//lägger ut album
                String title = cursor.getString(1);//lägger ut titeln
                String duration = cursor.getString(2);//lägger ut längden på låten
                String path = cursor.getString(3);//lägger ut pathen
                String artist = cursor.getString(4);//lägger ut artisten
                String id = cursor.getString(5);//lägger ut idn på låten
                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration, id);//gör en ny musikfil med dessa attributer

                tempAudioList.add(musicFiles);//lägger till i en arraylist
                //kontrollerar om det inte finns dupletter
                if(!duplicate.contains(album)){
                    albums.add(musicFiles);//lägger till musikfiler
                    duplicate.add(album);//lägger till albums
                }
            }
            cursor.close();//stänger cursorn
        }
        return tempAudioList;//retunerar arrayen med musikfiler
    }

    //gör en meny
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();//en sökfäkt
        searchView.setOnQueryTextListener(this);//söker medan man skriver
        return super.onCreateOptionsMenu(menu);
    }

    //det var obligatoriskt att ha med denna metod pga interfacet som jag implementaerade
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //ständigt söker efter resultat
    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();//gör bokstäverna till små bokstäver
        ArrayList<MusicFiles> myFiles = new ArrayList<>();//gör en arraylist
        //går igenbom arraylistan med alla låtar
        for(MusicFiles ms : musicFiles){
            if(ms.getTitle().toLowerCase().contains(userInput)){
                myFiles.add(ms);//lägger till de hittade resultaten i en arraylist
            }
        }
        SongFragment.musicAdapter.updateLista(myFiles);//uppdaterar listan och visar hittade resultaten
        return true;
    }

    //gör att man kan välja i meny alternativen
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_SORTER, MODE_PRIVATE).edit();
        switch(item.getItemId()){
            //sorterar av namn
            case R.id.sort_name:
                editor.putString("sorting","sortByName");
                editor.apply();
                this.recreate();
                break;
                //sorterar av datum
            case R.id.sort_date:
                editor.putString("sorting","sortByDate");
                editor.apply();
                this.recreate();
                break;
                //sorterar av storlek
            case R.id.sort_size:
                editor.putString("sorting","sortBySize");
                editor.apply();
                this.recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

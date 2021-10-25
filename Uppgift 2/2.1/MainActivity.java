package com.example.uppgift2;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


/*
 * Uppgiften berör 2.1, 2.2
 * Denna klass räknar ut primtal och sedan visar nr man trycker primtalknappen
 * Sedan sparar den till fil och öpnnar upp vid den sista primtalet
 * Sedan kan man spara den till databasen
 *
 * @author Oliver Gallo, olga7031
 */
public class MainActivity extends AppCompatActivity {

    private int number = 500;
    private int j = 1;
    private Button d;
    private TextView textView;
    private RoomDataBase roomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button1);//hittar knappen
        d = (Button) findViewById(R.id.button2);//hittar knappen
        d.setEnabled(false);//gör knappen oklickbar
        textView = (TextView) findViewById(R.id.numberText);//hittar textviewn
        roomDatabase = Room.databaseBuilder(getApplicationContext(), RoomDataBase.class, "myDB")
                .allowMainThreadQueries().build();//hittar databasen
        readFromFile();//börjar med att läsa in den senaste primtalet

        //börjar primknappshändelsen
        b.setOnClickListener(new View.OnClickListener(){

            //hittar primtal och skriver ut den nya
            @Override
            public void onClick(View v) {
                for(int i = j; i <= number; i++){
                    if(isPrime(i)){//kontrolerar om det är ett primtal
                        j += 2;//adderar
                        saveToFile(String.valueOf(i));//sparar primtalet
                        textView.setText(String.valueOf(j));//skriver ut primtalet
                        break;
                    }
                }
                d.setEnabled(true);//gör knappen klickbar
            }
        });
    }

    //sparar primtalen till fil
    public void saveToFile(String file){
        try{
            BufferedWriter buffer = new BufferedWriter(new FileWriter(new File(getFilesDir()
                    + File.separator + "uppgift2.txt")));//skriver i en fil
            buffer.write(file);
            buffer.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //läser in från filen
    public String readFromFile(){
        StringBuilder build = new StringBuilder();

        try{
            BufferedReader buffer = new BufferedReader(new FileReader(new File(getFilesDir()
                    + File.separator + "uppgift2.txt")));//läser in från filen
            String read;
            while((read = buffer.readLine()) != null){
                textView.setText(build.append(read));//tills det finns värde så skriver den ut det
            }
            j = Integer.parseInt(build.toString());//ger j den senaste värdet
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return build.toString();
    }
    // hittar primtal
    private boolean isPrime(int num) {
        for(int i = 2; i<=num / 2; i++)
            if(num % i == 0) return false;
        return true;
    }

    //sparar till databas
    public void saveToDataBase(View view) {

        int prime = Integer.parseInt(textView.getText().toString());//hämtar primtal

        Date date = Calendar.getInstance().getTime();//hämtar tiden

        Base base = new Base();
        base.setPrimeNumber(prime);//sätter primtalet
        base.setDate(String.valueOf(date));//sätter datumet

        roomDatabase.baseDAO().addPrime(base);//lägger primtalet till det i databasen

        Base prim = roomDatabase.baseDAO().getPrime(String.valueOf(date));

        int y = prim.getPrimeNumber();//hämtar primtalet
        String datum = prim.getDate();//hämtar datumet
        Toast.makeText(this, "Saved to database " + y + " date: " + datum, Toast.LENGTH_SHORT).show();//skriver ut det som en toast
        d.setEnabled(false);//gör knappen oklickbar
        }
}
package com.example.uppgift2;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

//databas interface
@Dao
public interface BaseDAO {
    @Insert
    void addPrime(Base base);


    @Query("SELECT * FROM base WHERE Date LIKE :date")
         Base getPrime(String date);

}

package com.example.uppgift2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

//databas klassen
@Entity
public class Base {

    @PrimaryKey
    public int primeNumber;

    @ColumnInfo(name="Date")
    public String date;

    //getters och setters
    public int getPrimeNumber() {
        return primeNumber;
    }

    public void setPrimeNumber(int primeNumber) {
        this.primeNumber = primeNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

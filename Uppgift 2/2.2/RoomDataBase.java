package com.example.uppgift2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//databas abstract klass
@Database(entities = {Base.class}, version = 2)
public abstract class RoomDataBase extends RoomDatabase {
    public abstract BaseDAO baseDAO();

}

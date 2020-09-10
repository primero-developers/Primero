package com.example.primero.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.primero.Models.FavModel;
import com.example.primero.Models.TurfModel;
import com.example.primero.Models.UserModel;
import com.example.primero.Utils.ArrayToRoom;

@Database(entities = {UserModel.class, TurfModel.class, FavModel.class} , version = 1, exportSchema = false)
@TypeConverters({ArrayToRoom.class})
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase INSTANCE;
    public abstract UserDao userDao();
    public abstract TurfDao turfDao();
    public abstract FavTurfDao favTurfDao();


    public static AppDatabase initDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            AppDatabase.class, "PRIMERO_DATABASE")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .enableMultiInstanceInvalidation()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static AppDatabase getDatabase(){
        return INSTANCE;
    }

}

package com.example.primero.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.primero.Models.FavModel;

import java.util.List;

@Dao
public interface FavTurfDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertFavList(List<FavModel> favModels);

    @Query("SELECT * FROM favTurf")
    LiveData<List<FavModel>> getFavTurfs();

}

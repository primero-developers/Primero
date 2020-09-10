package com.example.primero.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.primero.Models.TurfModel;

import java.util.List;

@Dao
public interface TurfDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTurfList(List<TurfModel> turfList);

    @Query("SELECT * FROM turf")
    LiveData<List<TurfModel>> getAllTurf();

    @Query("SELECT * FROM turf WHERE turfId = :turfId")
    TurfModel getTurf(String turfId);

    @Update
    void updateTurf(TurfModel turf);

    @Query("DELETE FROM turf")
    void clearRoom();


    @Query("SELECT imgUrl FROM turf")
    List<String> getImageUrl();
}

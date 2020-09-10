package com.example.primero.Room;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.primero.Models.UserModel;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insUser(UserModel user);

    @Update
    public void upUser(UserModel user);

    @Delete
    public void delUser(UserModel user);

    @Nullable
    @Query("SELECT * FROM user")
    public LiveData<UserModel> getUser();

}

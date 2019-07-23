package com.example.study.movieapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DAO
{
    @Query("Select * from favorite")
    LiveData<List<Favorite>> loadAll();

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("Select id from favorite where id=:ids")
    public int compareId(int ids);
}

package com.example.study.movieapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Favorite.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase
{
    private static String Database="fav_list";
    private static AppDatabase instance;
    private static Object LOCK=new Object();

    public static AppDatabase getInstance(Context context)
    {
        if(instance==null)
        {
            synchronized (LOCK)
            {
                instance= Room
                        .databaseBuilder(context,AppDatabase.class,AppDatabase.Database)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }
    public abstract DAO getDAO();
}

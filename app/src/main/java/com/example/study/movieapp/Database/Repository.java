package com.example.study.movieapp.Database;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class Repository {
    Context ct;
    private DAO dao;
    private LiveData<List<Favorite>> liveData;

    public Repository(Context ct) {
        //this.ct = ct;
        AppDatabase adb=AppDatabase.getInstance(ct);
        dao=adb.getDAO();
        liveData=dao.loadAll();
    }

    public void insertTasks(Favorite favorite)
    {
        new InsertTask().execute(favorite);

    }
    public void deleteTasks(Favorite favorite)
    {
        new DeleteTask().execute(favorite);
    }
    LiveData<List<Favorite>> getAlldata()
    {
        return liveData;
    }
    public int c(int a)
    {
        return dao.compareId(a);
    }


    public class InsertTask extends AsyncTask<Favorite,Void,Void>
    {

        @Override
        protected Void doInBackground(Favorite... favorites) {
            dao.insert(favorites[0]);
            return null;
        }
    }
    public class DeleteTask extends AsyncTask<Favorite,Void,Void>
    {

        @Override
        protected Void doInBackground(Favorite... favorites) {
            dao.delete(favorites[0]);
            return null;
        }
    }
}

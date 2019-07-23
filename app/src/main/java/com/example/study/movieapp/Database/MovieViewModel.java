package com.example.study.movieapp.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Favorite>> listLiveData;
    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository=new Repository(application);
        listLiveData=repository.getAlldata();
    }
    public void i(Favorite favorite)
    {
        repository.insertTasks(favorite);
    }
    public void d(Favorite favorite)
    {
        repository.deleteTasks(favorite);
    }
    public LiveData<List<Favorite>> getMovielist()
    {
        return listLiveData;
    }
    public int cc(int s)
    {
        return repository.c(s);
    }
}

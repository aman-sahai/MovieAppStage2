package com.example.study.movieapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.study.movieapp.Database.Favorite;
import com.example.study.movieapp.Database.MovieViewModel;
import com.example.study.movieapp.Model.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;

    List<Model> modelList;
    FavAdapter favAdapter;

    private static final String popular_url = "https://api.themoviedb.org/3/movie/popular?api_key=ad6753c8ec1896613c5a137cf8748290";
    private static final String top_rated_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=ad6753c8ec1896613c5a137cf8748290";

    public static String TAG = "com.example.study.movieapp";
    public static String KEY = "KEY";
    public String jsonData;
    public boolean is_fav = false;
    public static MovieViewModel mvm;

    public static final String res="results";
    public static final String ii="id";
    public static final String va="vote_average";
    public static final String p_p="poster_path";
    public static final String  o_t="original_title";
    public static final String over="overview";
    public static final String rd="release_date";
    public static final String ps="plot_synopsis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        mvm= ViewModelProviders.of(this).get(MovieViewModel.class);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(savedInstanceState!= null){
            if(savedInstanceState.containsKey(getResources().getString(R.string.jsons)))
            {
                jsonData = savedInstanceState.getString(getResources().getString(R.string.jsons));
                parseMyJson(jsonData);
            }else if(savedInstanceState.containsKey(getResources().getString(R.string.status))){
                datafetch_favorite();
            }

        } else if (cm.getActiveNetworkInfo() != null && networkInfo.isConnected()) {
            SharedPreferences sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
            String key_value = sharedPreferences.getString(KEY, null);
            if (key_value != null) {
                if (key_value.equals(getResources().getString(R.string.topr))) {
                    datafetch_toprated();

                } else if (key_value.equals(getResources().getString(R.string.pop))){
                    datafetch_popular();
                } else if (key_value.equals(getResources().getString(R.string.fav))) {
                    datafetch_favorite();
                }
            } else {
                datafetch_popular();
            }
        } else {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }
    }


    public void datafetch_popular() {
        jsonData = null;
        is_fav = false;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, popular_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonData = response;
                parseMyJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_cause) + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void parseMyJson(String response) {

        modelList = new ArrayList<>();
        Model model = new Model();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(res);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int id = jsonObject1.optInt(ii,1);
                Double vote_average = jsonObject1.optDouble(va,0.0);
                String poster_path = jsonObject1.optString(p_p,getResources().getString(R.string.nopos));
                String original_title = jsonObject1.optString(o_t,getResources().getString(R.string.notit));
                String overview = jsonObject1.optString(over,getResources().getString(R.string.noover));
                String release_date = jsonObject1.optString(rd,getResources().getString(R.string.nord));
                model.setOriginal_title(original_title);
                model.setVote_average(vote_average);
                model.setPoster_path(poster_path);
                model.setOverview(overview);
                model.setRelease_date(release_date);
                Model m = new Model(id, vote_average, poster_path, original_title, overview, release_date);
                modelList.add(m);
                movieAdapter = new MovieAdapter(MainActivity.this, modelList);
                recyclerView.setAdapter(movieAdapter);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                }

            }
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void datafetch_toprated() {
        jsonData = null;
        is_fav = false;
        StringRequest request = new StringRequest(Request.Method.GET, top_rated_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonData = response;
                parseMyJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_cause) + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void datafetch_favorite() {
        jsonData = null;
        is_fav = true;
        mvm.getMovielist().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable List<Favorite> favorites) {

                if (favorites.isEmpty()) {
                    AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this)
                            .setMessage(getResources().getString(R.string.nofavmovie))
                            .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog=ab.create();
                    alertDialog.show();
                } else {
                    favAdapter = new FavAdapter(MainActivity.this, favorites);
                    recyclerView.setAdapter(favAdapter);
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.top_rated:
                datafetch_toprated();
                break;
            case R.id.popular:
                datafetch_popular();
                break;
            case R.id.favorite:
                datafetch_favorite();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(jsonData!=null){
            outState.putString(getResources().getString(R.string.jsons),jsonData);
        }
        if(is_fav){
            outState.putBoolean(getResources().getString(R.string.status),true);
        }
    }
}

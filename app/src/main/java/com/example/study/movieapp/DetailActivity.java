package com.example.study.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.study.movieapp.Database.Favorite;
import com.example.study.movieapp.Model.ReviewModel;
import com.example.study.movieapp.Model.TrailerModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    List<TrailerModel> list;
    TrailerAdapter trailerAdapter;

    List<ReviewModel> reviewList;
    ReviewAdapter reviewAdapter;

    private ImageButton fav_fab;
    private ImageView poster_image;
    private TextView movie_title, userrating, synopsis, releasedate, reviewtext;

    RecyclerView trailerrecyclerView;
    RecyclerView reviewrecyclerview;

    private boolean image_state;

    int id;

    public static final String k="key";
    public static final String n="name";
    public static final String a="author";
    public static final String c="content";



    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        poster_image = findViewById(R.id.poster_image);
        movie_title = findViewById(R.id.movie_title);
        userrating = findViewById(R.id.userrating);
        synopsis = findViewById(R.id.synopsis);
        releasedate = findViewById(R.id.releasedate);
        reviewtext = findViewById(R.id.reviewstext);
        fav_fab = findViewById(R.id.fav_fab);
        trailerrecyclerView = findViewById(R.id.recycler_view_trailer);
        list = new ArrayList<>();
        reviewrecyclerview = findViewById(R.id.recycler_view_review);
        reviewList = new ArrayList<>();
        setValue();
        setuptrailer();
        setupReview();
        id = getIntent().getIntExtra(MainActivity.ii, 0);
        int list=MainActivity.mvm.cc(id);
            if (id == list) {
                fav_fab.setImageResource(R.drawable.fav_clicked);
                image_state = true;
            }
        fav_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_state) {
                    Intent intent = getIntent();
                    String title = intent.getStringExtra(MainActivity.o_t);
                    String poster = intent.getStringExtra(MainActivity.p_p);
                    String plotsynopsis = intent.getStringExtra(MainActivity.ps);
                    Double vote = intent.getDoubleExtra(MainActivity.va, 0.0);
                    String release_date = intent.getStringExtra(MainActivity.rd);
                    Favorite favorite = new Favorite(id, title, poster, vote, plotsynopsis, release_date);
                    MainActivity.mvm.d(favorite);
                    fav_fab.setImageResource(R.drawable.fav);
                    image_state = false;
                } else {
                    Intent intent = getIntent();
                    String title = intent.getStringExtra(MainActivity.o_t);
                    String poster = intent.getStringExtra(MainActivity.p_p);
                    String plotsynopsis = intent.getStringExtra(MainActivity.ps);
                    Double vote = intent.getDoubleExtra(MainActivity.va, 0.0);
                    String release_date = intent.getStringExtra(MainActivity.rd);
                    Favorite favorite = new Favorite(id, title, poster, vote, plotsynopsis, release_date);
                    MainActivity.mvm.i(favorite);
                    fav_fab.setImageResource(R.drawable.fav_clicked);
                    image_state = true;
                }
            }
        });

    }


    private void setValue() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.o_t);
        String poster = intent.getStringExtra(MainActivity.p_p);
        String release = intent.getStringExtra(MainActivity.rd);
        String plotsynopsis = intent.getStringExtra(MainActivity.ps);
        Double vote = intent.getDoubleExtra(MainActivity.va, 0.0);
        Picasso.with(this)
                .load(poster)
                .placeholder(R.drawable.loading)
                .into(poster_image);
        movie_title.setText(title);
        synopsis.setText(plotsynopsis);
        releasedate.setText(release);
        userrating.setText(String.valueOf(vote));
    }

    private void setuptrailer() {
        int id = getIntent().getIntExtra(MainActivity.ii, 0);
        String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=ad6753c8ec1896613c5a137cf8748290";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray(MainActivity.res);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject jsonObject1 = results.getJSONObject(i);
                        String key = jsonObject1.optString(k,getResources().getString(R.string.nokey));
                        String name = jsonObject1.optString(n,getResources().getString(R.string.noname));
                        TrailerModel trailerModel = new TrailerModel(key, name);
                        list.add(trailerModel);
                        trailerAdapter = new TrailerAdapter(DetailActivity.this, list);
                        trailerrecyclerView.setAdapter(trailerAdapter);
                        trailerrecyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
                    }
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(getResources().getString(R.string.error), error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void setupReview() {
        int id = getIntent().getIntExtra(MainActivity.ii, 0);
        String url = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=ad6753c8ec1896613c5a137cf8748290";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray(MainActivity.res);
                    if (results.isNull(0)) {
                        reviewtext.setText(getResources().getString(R.string.nodata));
                    } else {
                        for (int i = 0; i < results.length(); i++) {

                            JSONObject jsonObject1 = results.getJSONObject(i);
                            String author = jsonObject1.optString(a,getResources().getString(R.string.noauthor));
                            String content = jsonObject1.optString(c,getResources().getString(R.string.nocontent));
                            ReviewModel reviewModel = new ReviewModel(author, content);
                            reviewList.add(reviewModel);
                            reviewtext.setVisibility(View.GONE);
                            reviewAdapter = new ReviewAdapter(reviewList, DetailActivity.this);
                            reviewrecyclerview.setAdapter(reviewAdapter);
                            reviewrecyclerview.setLayoutManager(new LinearLayoutManager(DetailActivity.this));


                        }
                    }
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(getResources().getString(R.string.errorreview), error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}

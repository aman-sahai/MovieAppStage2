package com.example.study.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study.movieapp.Database.Favorite;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder>
{

    Context context;
    List<Favorite> favoriteList;

    public FavAdapter(Context context, List<Favorite> favoriteList)
    {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(context).inflate(R.layout.fav_list,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.MyViewHolder myViewHolder, int i)
    {
        myViewHolder.fav_title.setText(favoriteList.get(i).getOriginal_title());
        String rating=favoriteList.get(i).getUser_rating().toString();
        myViewHolder.fav_user_rating.setText(rating);
        Picasso.with(context)
                .load(favoriteList.get(i).getPoster_path())
                .into(myViewHolder.fav_movie_poster_image);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView fav_movie_poster_image;
        TextView fav_title;
        TextView fav_user_rating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fav_movie_poster_image=itemView.findViewById(R.id.fav_movie_poster_image);
            fav_title=itemView.findViewById(R.id.fav_title);
            fav_user_rating=itemView.findViewById(R.id.fav_user_rating);
            fav_movie_poster_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(MainActivity.ii,favoriteList.get(position).getId());
            intent.putExtra(MainActivity.o_t, favoriteList.get(position).getOriginal_title());
            intent.putExtra(MainActivity.va, favoriteList.get(position).getUser_rating());
            intent.putExtra(MainActivity.p_p, favoriteList.get(position).getPoster_path());
            intent.putExtra(MainActivity.rd, favoriteList.get(position).getRelease_date());
            intent.putExtra(MainActivity.ps, favoriteList.get(position).getPlot_synopsis());
            context.startActivity(intent);
        }
    }
}

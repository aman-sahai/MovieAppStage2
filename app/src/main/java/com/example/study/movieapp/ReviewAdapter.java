package com.example.study.movieapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.study.movieapp.Model.ReviewModel;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    Context context;
    List<ReviewModel> reviewModelList;

    public ReviewAdapter(List<ReviewModel> reviewModelList, Context context) {
        this.reviewModelList = reviewModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.review_list, viewGroup, false);
        ReviewHolder reviewHolder = new ReviewHolder(v);
        return reviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewHolder reviewHolder, int i) {
      reviewHolder.content_text.setText(reviewModelList.get(i).getContent());
      reviewHolder.author_text.setText(reviewModelList.get(i).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        TextView author_text;
        TextView content_text;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            author_text = itemView.findViewById(R.id.author_text);
            content_text = itemView.findViewById(R.id.content_text);
        }
    }
}

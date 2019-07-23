package com.example.study.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study.movieapp.Model.TrailerModel;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>
{
    Context context;
    List<TrailerModel> trailerModelList;

    public TrailerAdapter(Context context, List<TrailerModel> trailerModelList) {
        this.context = context;
        this.trailerModelList = trailerModelList;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(context).inflate(R.layout.trailer,viewGroup,false);
        TrailerViewHolder tvh=new TrailerViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder trailerViewHolder, int i)
    {
       trailerViewHolder.trailer_text.setText(trailerModelList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return trailerModelList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder
    {
        ImageView youtube_image;
        TextView trailer_text;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            youtube_image=itemView.findViewById(R.id.youtube_image);
            trailer_text=itemView.findViewById(R.id.trailer_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    String video=trailerModelList.get(position).getKey();
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+video));
                    if(intent.resolveActivity(context.getPackageManager())!=null)
                    {
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}

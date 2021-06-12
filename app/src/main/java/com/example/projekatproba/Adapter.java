



package com.example.projekatproba;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<String> titles;
    List<String> images;
    LayoutInflater inflater;

    public Adapter(Context ctx, List<String> titles, List<String> images){
        this.titles=titles;
        this.images=images;
        this.inflater= LayoutInflater.from(ctx);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.custom_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(titles.get(position));

        Picasso.get().load(images.get(position)).into(holder.gridIcon);
        //holder.gridIcon.setImageResource(images.get(position));


    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView gridIcon;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            title= itemView.findViewById(R.id.textView2);
            gridIcon= itemView.findViewById(R.id.imageView2);
        }
    }
}
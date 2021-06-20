



package com.example.projekatproba;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<String> titles;
    List<String> images;
    LayoutInflater inflater;
    List<String> nizSelektovanih;
    TextView textView;
    String s="";

    public Adapter(Context ctx, List<String> titles, List<String> images ){
        this.titles=titles;
        this.images=images;
        this.inflater= LayoutInflater.from(ctx);
        this.nizSelektovanih=new ArrayList<>();

    }
    public void copyConstructor(Adapter adapter){
        this.titles=adapter.titles;
        this.images=adapter.images;
        this.inflater= adapter.inflater;
        this.nizSelektovanih=adapter.nizSelektovanih;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView gridIcon;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            title= itemView.findViewById(R.id.textView2);
            gridIcon= itemView.findViewById(R.id.imageView2);

//            for(String pom : nizSelektovanih){
//                s+=pom;
//            }

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    Pattern p=Pattern.compile(title.getText().toString());

                    Matcher m=p.matcher(s);
                    if(!m.find()) {
                        nizSelektovanih.add(title.getText().toString());
                        view.findViewById(R.id.constraintLayout2).setBackgroundColor(Color.parseColor("#008000"));
                        Log.d("TITLE SASTOJKA", title.getText().toString());
                        Log.d("NIZ SELEKTOVANIH", nizSelektovanih.toString());
                        s+=title.getText().toString();
                        Log.d("TAG", "STRING ALEK ALEK "+s);

                    }
                    else{
                        for(int i=0;i<nizSelektovanih.size();i++ ){
                            if( nizSelektovanih.get(i).equals(title.getText().toString())){
                                nizSelektovanih.remove(i);
                                view.findViewById(R.id.constraintLayout2).setBackgroundColor(Color.parseColor("#2196F3"));
                                break;
                            }
                        }
                        Log.d("NIZ SELEKTOVANIH", nizSelektovanih.toString());
                    }

                }
            });
        }
    }

}
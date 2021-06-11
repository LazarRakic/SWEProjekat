package com.example.projekatproba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dataList= findViewById(R.id.dataList);

        titles= new ArrayList<>();
        images= new ArrayList<>();

        titles.add("First Item");
        titles.add("Second Item");
        titles.add("Third Item");
        titles.add("Fourth Item");
        titles.add("First2 Item");
        titles.add("Second 2Item");
        titles.add("Third 2Item");
        titles.add("Fourth 2Item");

        images.add(R.drawable.add_icon);
        images.add(R.drawable.ic_icon);
        images.add(R.drawable.reply_icon);
        images.add(R.drawable.search_icon);
        images.add(R.drawable.add_icon);
        images.add(R.drawable.ic_icon);
        images.add(R.drawable.reply_icon);
        images.add(R.drawable.search_icon);

        adapter= new Adapter(this, titles, images);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);
    }
}
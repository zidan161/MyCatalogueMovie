package com.zapp.zidan.favouriteapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static DatabaseFavourite.DatabaseContract.CONTENT_URI;
import static android.provider.BaseColumns._ID;

public class FavActivity extends AppCompatActivity {

    @BindView(R.id.rv_fav) RecyclerView rvFav;
    private FavAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        ButterKnife.bind(this);
        adapter = new FavAdapter(this);
        rvFav.setLayoutManager(new LinearLayoutManager(this));
        rvFav.setAdapter(adapter);

        try {
            Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, _ID+" ASC");
            adapter.setData(cursor);
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

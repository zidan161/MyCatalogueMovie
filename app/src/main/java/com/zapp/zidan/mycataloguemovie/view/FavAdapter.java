package com.zapp.zidan.mycataloguemovie.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zapp.zidan.mycataloguemovie.DetailActivity;
import com.zapp.zidan.mycataloguemovie.moviedata.MovieItems;
import com.zapp.zidan.mycataloguemovie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MovieHolder> {

    private Context context;
    private Cursor cursor;

    public FavAdapter (Context context){
        this.context = context;
    }

    public void setData(Cursor cursor){
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

        final MovieItems items = getItem(position);
        holder.tvMovieName.setText(items.getMovie());
        holder.tvDetail.setText(items.getOverview());
        holder.tvReleaseDate.setText(items.getDate());
        String Image = items.getImg();

        if (Image != null) {
            Glide.with(context).load("http://image.tmdb.org/t/p/w342/" + Image).into(holder.imgPoster);
        }

        holder.btnDetail.setOnClickListener(new CustomOnClickListener(position, new CustomOnClickListener.OnItemClickCallBack() {
            @Override
            public void onItemClicked(View view, int position) {
                toDetail(items);
            }
        }));
    }

    private void toDetail(MovieItems items) {

        Uri uri = Uri.parse(CONTENT_URI+"/"+items.getId());

        Intent detailIntent = new Intent(context, DetailActivity.class);
        detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, items);
        detailIntent.setData(uri);
        context.startActivity(detailIntent);
    }

    @Override
    public int getItemCount() {
        if(cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    public MovieItems getItem(int position){
        if(!cursor.moveToPosition(position)){
            throw new IllegalStateException("Position Invalid");
        }
        return new MovieItems(cursor);
    }

    class MovieHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_movie) ImageView imgPoster;
        @BindView(R.id.tv_name) TextView tvMovieName;
        @BindView(R.id.tv_date) TextView tvReleaseDate;
        @BindView(R.id.tv_detail) TextView tvDetail;
        @BindView(R.id.btn_detail) Button btnDetail;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

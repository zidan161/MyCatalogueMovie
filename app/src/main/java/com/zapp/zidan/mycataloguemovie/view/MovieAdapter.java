package com.zapp.zidan.mycataloguemovie.view;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieView> {

    private ArrayList<MovieItems> movieData = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context){
        this.context = context;
    }

    public void setDataMovie(ArrayList<MovieItems> movieData){
        this.movieData = movieData;
    }

    @Override
    public int getItemCount() {
        if (movieData == null) {
            return 0;
        } else {
            return movieData.size();
        }
    }

    @NonNull
    @Override
    public MovieView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
        return new MovieView(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieView holder, int position) {

        final MovieItems items = movieData.get(position);
        holder.tvMovieName.setText(items.getMovie());
        holder.tvReleaseDate.setText(items.getDate());
        holder.tvDetail.setText(items.getOverview());
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

    class MovieView extends RecyclerView.ViewHolder {
        @BindView(R.id.img_movie) ImageView imgPoster;
        @BindView(R.id.tv_name) TextView tvMovieName;
        @BindView(R.id.tv_date) TextView tvReleaseDate;
        @BindView(R.id.tv_detail) TextView tvDetail;
        @BindView(R.id.btn_detail) Button btnDetail;

        MovieView (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
package com.zapp.zidan.mycataloguemovie;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.zapp.zidan.mycataloguemovie.database.MoviePreference;
import com.zapp.zidan.mycataloguemovie.moviedata.MovieItems;
import com.zapp.zidan.mycataloguemovie.view.FavAdapter;

import static android.provider.BaseColumns._ID;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.DATE;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.IMAGE;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.NAME;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.OVERVIEW;

public class DetailActivity extends AppCompatActivity{

    @BindView(R.id.img_poster) ImageView imgPoster;
    @BindView(R.id.tv_detail_name) TextView tvName;
    @BindView(R.id.tv_detail_date) TextView tvDate;
    @BindView(R.id.tv_detail_over) TextView tvOver;
    @BindView(R.id.button_fav) Button btnFav;

    public static String EXTRA_MOVIE = "extra_movie";
    private MoviePreference preference;
    private boolean isFavorite = false;
    private FavAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        adapter = new FavAdapter(this);

        final MovieItems item = getIntent().getParcelableExtra(EXTRA_MOVIE);

        final int getId = item.getId();
        final String id = String.valueOf(getId);
        final String image = item.getImg();
        final String name = item.getMovie();
        final String date = item.getDate();
        final String overview = item.getOverview();

        if (image != null) {
            Glide.with(this).load("http://image.tmdb.org/t/p/w342/" + image).into(imgPoster);
        } else {
            imgPoster.setImageResource(R.drawable.ic_image_black_24dp);
        }

        tvName.setText(name);
        tvDate.setText(date);
        tvOver.setText(overview);

        preference = new MoviePreference(this);
        isFavorite = preference.isFavorite(id);
        final Uri uri = getIntent().getData();

        if (isFavorite){
            btnFav.setBackgroundResource(R.drawable.ic_star_black_24dp);
        }

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = preference.isFavorite(id);
                if (isFavorite && uri != null) {
                    getContentResolver().delete(uri, _ID+" = '"+id+"'", null);
                    preference.setFavorite(id, false);
                    btnFav.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                } else {
                    ContentValues values = new ContentValues();
                    values.put(_ID, getId);
                    values.put(NAME, name);
                    values.put(OVERVIEW, overview);
                    values.put(DATE, date);
                    values.put(IMAGE, image);
                    getContentResolver().insert(CONTENT_URI, values);
                    preference.setFavorite(id, true);
                    btnFav.setBackgroundResource(R.drawable.ic_star_black_24dp);
                }
            }
        });
    }
}

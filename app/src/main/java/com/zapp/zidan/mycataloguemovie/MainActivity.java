package com.zapp.zidan.mycataloguemovie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.zapp.zidan.mycataloguemovie.database.MoviePreference;
import com.zapp.zidan.mycataloguemovie.moviedata.MovieAsyncTaskLoader;
import com.zapp.zidan.mycataloguemovie.moviedata.MovieItems;
import com.zapp.zidan.mycataloguemovie.notification.AlarmReceiver;
import com.zapp.zidan.mycataloguemovie.view.FavAdapter;
import com.zapp.zidan.mycataloguemovie.view.MovieAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static com.zapp.zidan.mycataloguemovie.SettingActivity.KEY_DAILY;
import static com.zapp.zidan.mycataloguemovie.SettingActivity.KEY_RELEASE;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{

    MovieAdapter movieAdapter;
    FavAdapter favAdapter;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.pg_bar) ProgressBar pgBar;
    @BindView(R.id.tv_result) TextView tvResult;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    static final String EXTRA_MOVIE = "extra_movie";
    static final String KEY_DATA = "key_data";
    static final String KEY_BOOL = "key_bool";
    private static final int ID_POP_MOVIE = 10;
    private static final int ID_SEARCH_MOVIE = 20;
    private static final int ID_NOW_MOVIE = 30;
    private static final int ID_UPCOMING_MOVIE = 40;
    private ArrayList<MovieItems> data = new ArrayList<>();
    private boolean isFavPage = false;
    private MoviePreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        preference = new MoviePreference(this);

        if (preference.isFirsTime()) {
            AlarmReceiver.alarmDailyReminder(this);
            AlarmReceiver.alarmReleaseReminder(this);
            preference.setOn(KEY_DAILY, true);
            preference.setOn(KEY_RELEASE, true);
            preference.setFirstTime(false);
        }

        movieAdapter = new MovieAdapter(this);
        favAdapter = new FavAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Visibility();

        if (savedInstanceState != null){
            getState(savedInstanceState);
        } else {
            recyclerView.setAdapter(movieAdapter);
            getSupportLoaderManager().initLoader(ID_POP_MOVIE, null, MainActivity.this);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {

        String searchMovie = "";

        if (args != null) {
            searchMovie = args.getString(EXTRA_MOVIE);
        }

        return new MovieAsyncTaskLoader(this, searchMovie, id);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        if (data.size() > 0) {
            this.data = data;
            this.isFavPage = false;
            movieAdapter.setDataMovie(data);
            movieAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            tvResult.setVisibility(View.VISIBLE);
        }
        pgBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        movieAdapter.setDataMovie(null);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setQueryHint(getResources().getString(R.string.cari));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String search) {

                if (TextUtils.isEmpty(search)) {
                    return false;
                }

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_MOVIE, search);
                Visibility();
                getSupportLoaderManager().restartLoader(ID_SEARCH_MOVIE, bundle, MainActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_popular) {
            Visibility();
            recyclerView.setAdapter(movieAdapter);
            getSupportLoaderManager().restartLoader(ID_POP_MOVIE, null, MainActivity.this);
        } else if (id == R.id.nav_now_play) {
            Visibility();
            recyclerView.setAdapter(movieAdapter);
            getSupportLoaderManager().restartLoader(ID_NOW_MOVIE, null, MainActivity.this);
        } else if (id == R.id.nav_upcoming) {
            Visibility();
            recyclerView.setAdapter(movieAdapter);
            getSupportLoaderManager().restartLoader(ID_UPCOMING_MOVIE, null, MainActivity.this);
        } else if (id == R.id.nav_favourite) {
            Visibility();
            isFavPage = true;
            recyclerView.setAdapter(favAdapter);
            Cursor cursor = getContentResolver().query(CONTENT_URI
                    , null
                    , null
                    , null
                    , _ID+" DESC");
            if(cursor != null) {
                favAdapter.setData(cursor);
                recyclerView.setVisibility(View.VISIBLE);
                pgBar.setVisibility(View.INVISIBLE);
            }
        } else if (id == R.id.nav_language) {
            Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(languageIntent);
        } else if (id == R.id.nav_notif) {
        Intent notifIntent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(notifIntent);
    }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList(KEY_DATA, data);
        outState.putBoolean(KEY_BOOL, isFavPage);
        super.onSaveInstanceState(outState);
    }

    private void getState(Bundle bundle){

        ArrayList<MovieItems> data = bundle.getParcelableArrayList(KEY_DATA);
        boolean isFavPage = bundle.getBoolean(KEY_BOOL);

        if (!isFavPage) {
            this.data = data;
            recyclerView.setAdapter(movieAdapter);
            movieAdapter.setDataMovie(data);
            movieAdapter.notifyDataSetChanged();
        } else {
            this.isFavPage = true;
            recyclerView.setAdapter(favAdapter);
            Cursor cursor = getContentResolver().query(CONTENT_URI
                    , null
                    , null
                    , null
                    , _ID+" DESC");
            favAdapter.setData(cursor);
        }
        recyclerView.setVisibility(View.VISIBLE);
        pgBar.setVisibility(View.INVISIBLE);
    }

    public void Visibility(){
        recyclerView.setVisibility(View.INVISIBLE);
        tvResult.setVisibility(View.INVISIBLE);
        pgBar.setVisibility(View.VISIBLE);
    }
}

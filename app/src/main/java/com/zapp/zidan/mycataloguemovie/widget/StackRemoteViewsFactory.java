package com.zapp.zidan.mycataloguemovie.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.zapp.zidan.mycataloguemovie.moviedata.MovieItems;
import com.zapp.zidan.mycataloguemovie.R;


import java.util.concurrent.ExecutionException;

import static android.provider.BaseColumns._ID;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor items;
    private Context context;

    public StackRemoteViewsFactory(Context context, Intent intent){
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        final long identityToken = Binder.clearCallingIdentity();

        items = context.getContentResolver().query(CONTENT_URI, null, null, null, _ID+" DESC");

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return items.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        String img = getItem(position).getImg();
        String url = "http://image.tmdb.org/t/p/w342/";
        Bitmap bitmap = null;

        try {
            bitmap = Glide.with(context)
                    .load(url + img)
                    .asBitmap()
                    .error(new ColorDrawable(context.getResources().getColor(R.color.colorPrimary)))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("Widget Load Error", "error");
        }

        rv.setImageViewBitmap(R.id.img_view, bitmap);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private MovieItems getItem(int position){
        if (!items.moveToPosition(position)){
            throw new IllegalStateException("Position Invalid");
        }
        return new MovieItems(items);
    }
}

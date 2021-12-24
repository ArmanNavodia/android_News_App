package com.example.newsapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String mUrl;

    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        //if no url found return the empty list
        if (mUrl == null) return null;
        //call the FetchNewsData from the QueryUtils class to get the data from the internet
        List<News> news = QueryUtils.FetchNewsData(mUrl);
        //return the list of news
        return news;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

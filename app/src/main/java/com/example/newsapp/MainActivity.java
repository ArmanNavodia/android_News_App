package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private TextView mEmptyStateTextView;
    private NewsArrayAdapter mAdapter;
    private static final int NEWS_LOADER_ID = 1;

    //API url to fetch data from
    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?format=json&from-date=2010-01-01&show-fields=headline&api-key=test&show-tags=contributor,publication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        // Create a new {@link ArrayAdapter} of earthquakes
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsArrayAdapter(this, new ArrayList<News>());

        //set the adapter to display content
        earthquakeListView.setAdapter(mAdapter);

        /*
         *set {@Link onItemClickListener} to know when user clicks on the view
         */
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getting the current news from the adapter
                News currentNews = mAdapter.getItem(position);
                //creating uri from the string
                Uri NewsUri = Uri.parse(currentNews.getUrl());
                //throwing intent to browser to open the website
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);
                startActivity(websiteIntent);
            }
        });

        //get the connectivity manager
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //get the current network status
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //check if Internet is on
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            //initialize loader manager to fetch the data from the background
            LoaderManager.getInstance(this).initLoader(NEWS_LOADER_ID, null, this);
        } else {
            //hiding the progress indicator
            ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            //displaying error message when internet is off
            mEmptyStateTextView.setText("No internet connection");
        }
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        //hiding the loading indicator as the data is fetched and ready to display
        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText("No News Found");
        //clearing all the views
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            //adding the fetched list of data to adapter to display
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        //clearing the resources
        mAdapter.clear();
    }
}

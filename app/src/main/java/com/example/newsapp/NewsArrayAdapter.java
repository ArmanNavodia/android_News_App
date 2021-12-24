package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsArrayAdapter extends ArrayAdapter<News> {

    //To separate the date and time from the fetched data
    private static final String DATE_SEPARATOR = "T";

    public NewsArrayAdapter(@NonNull Context context, @NonNull ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_items, parent, false);
        }
        News currentnews = getItem(position);

        //Find the text view to display the author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        //checking if the author field is present or not
        if (currentnews.hasAuthor()) {
            //display the author
            authorTextView.setText(currentnews.getAuthor());
        } else {
            //remove the text view for author field as there is no author mentioned
            authorTextView.setVisibility(View.GONE);
        }

        //separating date and time field using separator defined.
        String dateAndTimePublished = currentnews.getDateAndTimePublishedPublished();
        String[] PublishedDate = dateAndTimePublished.split(DATE_SEPARATOR);
        String date = PublishedDate[0];
        String time = PublishedDate[1];
        Date Dateobj = new Date();
        Date Timeobj = new Date();

        //getting the textview for date and setting the date after formatting the date.
        TextView dateTextView = (TextView) listItemView.findViewById((R.id.date_text_view));
        dateTextView.setText(DateFormat(Dateobj));

        //getting the textview from time and setting the time after formatting
        TextView timeTextView = (TextView) listItemView.findViewById((R.id.time_text_view));
        timeTextView.setText(TimeFormat(Timeobj));

        //getting the textview for headline and setting the headline from the fetched data.
        TextView headlineTextView = (TextView) listItemView.findViewById((R.id.headline_text_view));
        headlineTextView.setText(currentnews.getHeadline());

        //getting the textview for section name and setting the section name from the fetched data.
        TextView sectionTextView = (TextView) listItemView.findViewById((R.id.section_text_view));
        sectionTextView.setText(currentnews.getSectionName());

        return listItemView;
    }

    //To format the date in the required format
    private String DateFormat(Date dateobj) {
        ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateobj);

    }

    //TO format the time in the required format
    private String TimeFormat(Date dateobj) {
        ;
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        return timeFormat.format(dateobj);

    }
}

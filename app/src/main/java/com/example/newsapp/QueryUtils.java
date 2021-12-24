package com.example.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }

    /**
     * @param stringUrl the url from where the data need sto be fetched
     * @return created url
     */
    private static URL CreateURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "createURL: ", e);
        }
        return url;
    }

    /**
     * @param NewsJSON String of JSON file fetched from the API
     * @return list of the News type
     */
    private static List<News> ExtractFromJSON(String NewsJSON) {
        //create new ArrayList to store News type data
        ArrayList<News> news = new ArrayList<>();

        //check if the input is empty
        if (TextUtils.isEmpty(NewsJSON)) {
            return null;
        }
        try {
            //creating a new JSONObject
            JSONObject root = new JSONObject(NewsJSON);

            //extracting the response JSONObject from the root
            JSONObject response = root.getJSONObject("response");

            //extracting the response JSONArray from the response object
            JSONArray resultsArray = response.getJSONArray("results");

            //iterating through the JSONArray
            for (int i = 0; i < resultsArray.length(); i++) {

                //extracting the JSONObject from the the JSOnArray
                JSONObject newsDetail = resultsArray.getJSONObject(i);

                //extracting SectionName from the JSONObject
                String sectionName = newsDetail.getString("sectionName");

                //extracting date and time from the JSONObject
                String publishedDateandTime = newsDetail.getString("webPublicationDate");

                //extracting web url from the JSONObject
                String url = newsDetail.getString("webUrl");

                //extracting JSONObject field
                JSONObject field = newsDetail.getJSONObject("fields");

                //extracting headline from the JSONObject
                String newsHeadline = field.getString("headline");

                //extracting JSONArray from the JSONObject
                JSONArray tag = newsDetail.getJSONArray("tags");

                //extracting JSONObject from the JSONArray
                JSONObject contributor = tag.getJSONObject(0);

                //extracting firstName from the JSONObject
                String firstName = contributor.getString("firstName");

                //extracting lastName from the JSONObject
                String lastName = contributor.getString("lastName");
                String name = null;

                //concating the first name and the last name
                name = firstName + " " + lastName;
                News n1;
                //checking if name is not null
                if (name == null) {
                    n1 = new News(sectionName, publishedDateandTime, newsHeadline, url);
                } else {
                    n1 = new News(sectionName, name, publishedDateandTime, newsHeadline, url);
                }
                //adding the data in arraylist
                news.add(n1);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "ExtractFromJSON: ", e);
        }
        //return the arraylist
        return news;
    }

    private static String MakeHTTPRequest(URL url) throws IOException {
        String JSonResponse = null;
        if (url == null) return null;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            //openning the connection to fetch the data
            urlConnection = (HttpURLConnection) url.openConnection();
            //setting the request method
            urlConnection.setRequestMethod("GET");
            //specifying time for the connection time out and read time out
            urlConnection.setReadTimeout(1500);
            urlConnection.setConnectTimeout(2000);

            //connecting with the server to get the Jsonresponse
            urlConnection.connect();

            //checking if connection is made or not
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSonResponse = readFromStream(inputStream);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "MakeHTTPRequest: ", e);
        } finally {
            //closing all the connection and input stream.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return JSonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        //creating object of string builder
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            //creating object of inputstreamreader and passing it to BufferReader object
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }


    public static List<News> FetchNewsData(String requestUrl) {
        URL url = CreateURL(requestUrl);
        String JsonResponse = null;
        try {
            JsonResponse = MakeHTTPRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "FetchNewsData: ", e);
        }
        List<News> news = ExtractFromJSON(JsonResponse);
        return news;
    }

}

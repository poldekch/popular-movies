package com.example.leopo.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final public static String ORDER_POPULAR = "popular";
    final public static String ORDER_TOP_RATED = "top_rated";

    final static String PARAM_KEY = "api_key";
    final static String API_KEY = "";

    final static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";

    final static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    final static String IMAGE_SIZE = "w185";

    public static URL buildUrl(String order) {
        Uri buildUri = Uri.parse(MOVIEDB_BASE_URL + order).buildUpon()
                .appendQueryParameter(PARAM_KEY, API_KEY).build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getApiResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL buildImageUrl(String poster) {
        Uri buildUri = Uri.parse(IMAGE_BASE_URL + IMAGE_SIZE + "/" + poster).buildUpon()
                .appendQueryParameter(PARAM_KEY, API_KEY).build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}

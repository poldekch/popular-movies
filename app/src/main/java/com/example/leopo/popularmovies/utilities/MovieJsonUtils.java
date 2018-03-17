package com.example.leopo.popularmovies.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieJsonUtils {

    public static String[] getSimpleMovieImagesFromJson(Context context, String moviesJsonString)
        throws JSONException {

        final String PAGE = "page";
        final String TOTAL_RESULTS = "total_results";
        final String TOTAL_PAGES = "total_pages";

        final String RESULTS = "results";

        final String STATUS_CODE = "status_code";
        final String STATUS_MESSAGE = "status_message";
        final String SUCCESS = "success";

        final String VOTE_COUNT = "vote_count";
        final String ID = "id";
        final String VIDEO = "video";
        final String VOTE_AVERAGE = "vote_average";
        final String TITLE = "title";
        final String POPULARITY = "popularity";
        final String POSTER_PATH = "poster_path";
        final String ORIGINAL_LANGUAGE = "original_language";
        final String ORIGINAL_TITLE = "original_title";
        final String GENRE_IDS = "genre_ids";
        final String BACKDROP_PATH = "backdrop_path";
        final String ADULT = "adult";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";

        String[] parsedMoviesData = null;

        JSONObject movies = new JSONObject(moviesJsonString);

        // Error check
        if (movies.has(SUCCESS)) {
            boolean success = movies.optBoolean(SUCCESS);

            if (!success) {
                return null;
            }
        }

        JSONArray results = movies.optJSONArray(RESULTS);

        parsedMoviesData = new String[results.length()];

        for (int i=0; i<results.length(); i++) {
            // TODO complete parsing
            JSONObject movie = (JSONObject) results.get(i);
            parsedMoviesData[i] = movie.optString(POSTER_PATH);
        }

        return parsedMoviesData;
    }



}

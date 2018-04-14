package com.example.leopo.popularmovies.utilities;

import android.content.Context;

import com.example.leopo.popularmovies.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrailerJsonUtils {

    public static ArrayList<Trailer> getTrailersFromJson(Context context, String trailersJsonString)
            throws JSONException {

        // https://api.themoviedb.org/3/movie/551/videos?api_key=
//        {
//            id: 551,
//                    results: [
//            {
//                id: "533ec654c3a36854480003f1",
//                        iso_639_1: "en",
//                    iso_3166_1: "US",
//                    key: "dd03qev59Jo",
//                    name: "The Poseidon Adventure (1972) Trailer",
//                    site: "YouTube",
//                    size: 360,
//                    type: "Trailer"
//            },
//            {
//                id: "533ec654c3a36854480003f2",
//                        iso_639_1: "en",
//                    iso_3166_1: "US",
//                    key: "_jrfK5v-v9s",
//                    name: "The Poseidon Adventure - Radio Spot 1972",
//                    site: "YouTube",
//                    size: 480,
//                    type: "Featurette"
//            }
//]
//        }

//        final String PAGE = "page";
//        final String TOTAL_RESULTS = "total_results";
//        final String TOTAL_PAGES = "total_pages";
//
        final String RESULTS = "results";
//
//        final String STATUS_CODE = "status_code";
//        final String STATUS_MESSAGE = "status_message";
//        final String SUCCESS = "success";
//
//        final String VOTE_COUNT = "vote_count";
        final String ID = "id";
        final String KEY = "key";
        final String NAME = "name";
//        final String VIDEO = "video";
//        final String VOTE_AVERAGE = "vote_average";
//        final String TITLE = "title";
//        final String POPULARITY = "popularity";
//        final String POSTER_PATH = "poster_path";
//        final String ORIGINAL_LANGUAGE = "original_language";
//        final String ORIGINAL_TITLE = "original_title";
//        final String GENRE_IDS = "genre_ids";
//        final String BACKDROP_PATH = "backdrop_path";
//        final String ADULT = "adult";
//        final String OVERVIEW = "overview";
//        final String RELEASE_DATE = "release_date";
//
        JSONObject trailers = new JSONObject(trailersJsonString);
//
//        // Error check
//        if (movies.has(SUCCESS)) {
//            boolean success = movies.optBoolean(SUCCESS);
//
//            if (!success) {
//                return null;
//            }
//        }
//
        JSONArray results = trailers.optJSONArray(RESULTS);

        ArrayList<Trailer> parsedTrailersData = new ArrayList<>();

        for (int i=0; i<results.length(); i++) {
            JSONObject jsonTrailer = (JSONObject) results.get(i);
//            // TODO complete parsing
            Trailer trailer = new Trailer();
            trailer.setId(jsonTrailer.optString(ID));
            trailer.setKey(jsonTrailer.optString(KEY));
            trailer.setName(jsonTrailer.optString(NAME));

//            movie.setTitle(jsonMovie.optString(TITLE));
//            movie.setReleaseDate(jsonMovie.optString(RELEASE_DATE));
//            movie.setMoviePosterUrl(jsonMovie.optString(POSTER_PATH));
//            movie.setVoteAverage(jsonMovie.optString(VOTE_AVERAGE));
//            movie.setPlotSynopsis(jsonMovie.optString(OVERVIEW));
//            parsedMoviesData.add(movie);
            parsedTrailersData.add(trailer);
        }

        return parsedTrailersData;
    }
}

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

        final String RESULTS = "results";
        final String ID = "id";
        final String KEY = "key";
        final String NAME = "name";
        JSONObject trailers = new JSONObject(trailersJsonString);
        JSONArray results = trailers.optJSONArray(RESULTS);

        ArrayList<Trailer> parsedTrailersData = new ArrayList<>();

        for (int i=0; i<results.length(); i++) {
            JSONObject jsonTrailer = (JSONObject) results.get(i);
            Trailer trailer = new Trailer();
            trailer.setId(jsonTrailer.optString(ID));
            trailer.setKey(jsonTrailer.optString(KEY));
            trailer.setName(jsonTrailer.optString(NAME));

            parsedTrailersData.add(trailer);
        }

        return parsedTrailersData;
    }
}

package com.example.leopo.popularmovies.utilities;

import android.content.Context;

import com.example.leopo.popularmovies.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewJsonUtils {

    public static ArrayList<Review> getReviewsFromJson(Context context, String reviewsJsonString)
        throws JSONException {

        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        JSONObject reviews = new JSONObject(reviewsJsonString);

        JSONArray results = reviews.optJSONArray(RESULTS);

        ArrayList<Review> parsedReviewsData = new ArrayList<>();

        for (int i=0; i<results.length(); i++) {
            JSONObject jsonReview = (JSONObject) results.get(i);

            Review review = new Review();
            review.setAuthor(jsonReview.optString(AUTHOR));
            review.setContent(jsonReview.optString(CONTENT));

            parsedReviewsData.add(review);
        }

        return parsedReviewsData;
    }
}

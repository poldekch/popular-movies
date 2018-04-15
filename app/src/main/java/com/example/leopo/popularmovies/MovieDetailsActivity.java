package com.example.leopo.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.leopo.popularmovies.utilities.NetworkUtils;
import com.example.leopo.popularmovies.utilities.ReviewJsonUtils;
import com.example.leopo.popularmovies.utilities.TrailerJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import com.example.leopo.popularmovies.TrailerAdapter.TrailerAdapterOnClickHandler;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapterOnClickHandler{

    private Movie mMovie;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent startingIntent = getIntent();

        mMovie = (Movie) startingIntent.getSerializableExtra("Movie");

        mTrailerRecyclerView = findViewById(R.id.rv_trailers);
        mReviewRecyclerView = findViewById(R.id.rv_reviews);
        mErrorMessageDisplay = findViewById(R.id.tv_details_error_message_display);

        LinearLayoutManager trailerLayoutManager
                = new LinearLayoutManager(this);

        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);

        LinearLayoutManager reviewLayoutManager
                = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        mLoadingIndicator = findViewById(R.id.pb_details_loading_indicator) ;

        loadTrailerData();
        loadReviewData();
        populateView();
    }

    private void populateView() {
        TextView title = findViewById(R.id.tv_movie_title);
        title.setText(mMovie.getTitle());

        TextView releaseDate = findViewById(R.id.tv_release_date);
        releaseDate.setText("Released: " + mMovie.getReleaseDate());

        ImageView poster = findViewById(R.id.iv_poster);
        URL url = NetworkUtils.buildImageUrl(mMovie.getMoviePosterUrl());
        Picasso.with(this).load(url.toString()).into(poster);

        TextView voteAverage = findViewById(R.id.tv_vote_average);
        voteAverage.setText("Rating: " + mMovie.getVoteAverage() + "/10");

        TextView plotSynopsis = findViewById(R.id.tv_plot_synopsis);
        plotSynopsis.setText(mMovie.getPlotSynopsis());
    }

    private void loadTrailerData() {
        new MovieDetailsActivity.FetchMovieTrailers().execute(mMovie.getId().toString());
    }

    private void loadReviewData() {
        new MovieDetailsActivity.FetchMovieReviews().execute(mMovie.getId().toString());
    }

    @Override
    public void onClick(int clickedMovieId) {
        String key = mTrailerAdapter.getTrailer(clickedMovieId).getKey();

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
        mReviewRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mTrailerRecyclerView.setVisibility(View.INVISIBLE);
        mReviewRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTrailers extends AsyncTask<String, Void, ArrayList<Trailer>> {
        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String movieId = params[0];
            URL trailersUrl = NetworkUtils.buildVideosUrl(movieId);

            try {
                String trailersResponse = NetworkUtils.getApiResponse(trailersUrl);
                return TrailerJsonUtils.getTrailersFromJson(MovieDetailsActivity.this, trailersResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Process loaded trailer data
         *
         * @param trailerData
         */
        @Override
        protected void onPostExecute(ArrayList<Trailer> trailerData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (trailerData != null) {
                // TODO common flag
                showMovieDataView();
                mTrailerAdapter.setTrailersData(trailerData);
            } else {
                showErrorMessage();
            }
        }
    }

    public class FetchMovieReviews extends AsyncTask<String, Void, ArrayList<Review>> {
        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String movieId = params[0];
            URL reviewsUrl = NetworkUtils.buildReviewsUrl(movieId);

            try {
                String reviewsResponse = NetworkUtils.getApiResponse(reviewsUrl);
                return ReviewJsonUtils.getReviewsFromJson(MovieDetailsActivity.this, reviewsResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviewsData) {
            // TODO common flag
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (reviewsData != null) {
                // TODO common flag
                showMovieDataView();
                mReviewAdapter.setReviewsData(reviewsData);
            } else {
                showErrorMessage();
            }
        }
    }
}

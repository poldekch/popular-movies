package com.example.leopo.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.leopo.popularmovies.adapters.ReviewAdapter;
import com.example.leopo.popularmovies.adapters.TrailerAdapter;
import com.example.leopo.popularmovies.data.MovieContract;
import com.example.leopo.popularmovies.utilities.NetworkUtils;
import com.example.leopo.popularmovies.utilities.ReviewJsonUtils;
import com.example.leopo.popularmovies.utilities.TrailerJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import com.example.leopo.popularmovies.adapters.TrailerAdapter.TrailerAdapterOnClickHandler;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapterOnClickHandler{

    private Movie mMovie;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    private LinearLayoutManager mTrailerLayoutManager;
    private LinearLayoutManager mReviewLayoutManager;

    public static final String TRAILERS_STATE_KEY = "trailers";
    public static final String REVIEWS_STATE_KEY = "reviews";

    private Parcelable mTrailersState;
    private Parcelable mReviewsState;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private boolean mFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent startingIntent = getIntent();

        mMovie = (Movie) startingIntent.getParcelableExtra("Movie");

        mTrailerRecyclerView = findViewById(R.id.rv_trailers);
        mReviewRecyclerView = findViewById(R.id.rv_reviews);
        mErrorMessageDisplay = findViewById(R.id.tv_details_error_message_display);

        mTrailerLayoutManager = new LinearLayoutManager(this);

        mTrailerRecyclerView.setLayoutManager(mTrailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);

        mReviewLayoutManager = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(mReviewLayoutManager);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mTrailersState = mTrailerLayoutManager.onSaveInstanceState();
        mReviewsState = mReviewLayoutManager.onSaveInstanceState();

        outState.putParcelable(TRAILERS_STATE_KEY, mTrailersState);
        outState.putParcelable(REVIEWS_STATE_KEY, mReviewsState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mTrailersState = savedInstanceState.getParcelable(TRAILERS_STATE_KEY);
            mReviewsState = savedInstanceState.getParcelable(REVIEWS_STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mTrailersState != null) {
            mTrailerLayoutManager.onRestoreInstanceState(mTrailersState);
        }
        if (mReviewsState != null) {
            mReviewLayoutManager.onRestoreInstanceState(mReviewsState);
        }
    }

    private void displayFavourite() {
        ImageView favourite = findViewById(R.id.ib_favourite);
        if (true == mFavourite) {
            favourite.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    private void populateView() {
        TextView title = findViewById(R.id.tv_movie_title);
        title.setText(mMovie.getTitle());

        TextView releaseDate = findViewById(R.id.tv_release_date);
        releaseDate.setText("Released:\n" + mMovie.getReleaseDate());

        ImageView poster = findViewById(R.id.iv_poster);
        URL url = NetworkUtils.buildImageUrl(mMovie.getMoviePosterUrl());
        Picasso.with(this).load(url.toString()).into(poster);

        TextView voteAverage = findViewById(R.id.tv_vote_average);
        voteAverage.setText("Rating:\n" + mMovie.getVoteAverage() + "/10");

        TextView plotSynopsis = findViewById(R.id.tv_plot_synopsis);
        plotSynopsis.setText(mMovie.getPlotSynopsis());

        new CheckFavouriteTask().execute();
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
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (reviewsData != null) {
                showMovieDataView();
                mReviewAdapter.setReviewsData(reviewsData);
            } else {
                showErrorMessage();
            }
        }
    }

    public class CheckFavouriteTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            int movieId = mMovie.getId();
            Cursor movie = getContentResolver().query(
                    MovieContract.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                    new String[] {String.valueOf(movieId)},
                    null
            );
            if (0 == movie.getCount()) {
                mFavourite = false;
            } else {
                mFavourite = true;
            }
            movie.close();
            return null;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            displayFavourite();
        }
    }

    public class SwitchFavouriteTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            int movieId = mMovie.getId();
            Cursor movie = getContentResolver().query(
                    MovieContract.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                    new String[] {String.valueOf(movieId)},
                    null
            );
            if (0 == movie.getCount()) {
                // making favourite
                mFavourite = true;
                saveFavourite();
            } else {
                mFavourite = false;
                removeFavourite();
            }
            movie.close();
            return null;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            displayFavourite();
        }
    }

    private void saveFavourite() {
        // Called from within asynctask
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, mMovie.getMoviePosterUrl());
        cv.put(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS, mMovie.getPlotSynopsis());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());

        getContentResolver().insert(MovieContract.CONTENT_URI, cv);
    }

    private void removeFavourite() {
        // Called from within asynctask
        int movieId = mMovie.getId();
        getContentResolver().delete(
                MovieContract.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[] { String.valueOf(movieId)});
    }

    public void favouriteClick(View view) {
        new SwitchFavouriteTask().execute();
    }
}

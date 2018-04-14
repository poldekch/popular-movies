package com.example.leopo.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.leopo.popularmovies.utilities.NetworkUtils;
import com.example.leopo.popularmovies.utilities.TrailerJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import com.example.leopo.popularmovies.TrailerAdapter.TrailerAdapterOnClickHandler;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapterOnClickHandler{

    private Movie mMovie;

    private RecyclerView mRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent startingIntent = getIntent();

        mMovie = (Movie) startingIntent.getSerializableExtra("Movie");

        mRecyclerView = findViewById(R.id.rv_trailers);
        mErrorMessageDisplay = findViewById(R.id.tv_details_error_message_display);

        // TODO
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 1);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerView.setAdapter(mTrailerAdapter);

        mLoadingIndicator = findViewById(R.id.pb_details_loading_indicator) ;

        loadTrailerData();
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
//        showMovieDataView(); // TODO
        new MovieDetailsActivity.FetchMovieTrailers().execute(mMovie.getId().toString());
    }

    @Override
    public void onClick(int clickedMovieId) {
        // TODO
//        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
//
//        Movie movie = mMovieAdapter.getMovie(clickedMovieId);
//        intent.putExtra("Movie", movie);
//        startActivity(intent);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
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
         * Process loaded movie data
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
}

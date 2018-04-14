package com.example.leopo.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leopo.popularmovies.utilities.NetworkUtils;
import com.example.leopo.popularmovies.utilities.TrailerJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent startingIntent = getIntent();

        mMovie = (Movie) startingIntent.getSerializableExtra("Movie");

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
    }
}

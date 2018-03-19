package com.example.leopo.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leopo.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.Date;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent startingIntent = getIntent();

        mMovie = (Movie) startingIntent.getSerializableExtra("Movie");

        populateView();
    }

    private void populateView() {

        TextView title = (TextView)findViewById(R.id.tv_movie_title);
        title.setText((CharSequence) mMovie.getTitle());

        TextView releaseDate = (TextView)findViewById(R.id.tv_release_date);
        releaseDate.setText(mMovie.getReleaseDate());

        ImageView poster = (ImageView)findViewById(R.id.iv_poster);
        URL url = NetworkUtils.buildImageUrl(mMovie.getMoviePosterUrl());
        Picasso.with(this).load(url.toString()).into(poster);

        TextView voteAverage = (TextView)findViewById(R.id.tv_vote_average);
        voteAverage.setText(String.valueOf(mMovie.getVoteAverage()) + "/10");

        TextView plotSynopsis = (TextView)findViewById(R.id.tv_plot_synopsis);
        plotSynopsis.setText(mMovie.getPlotSynopsis());
    }
}

package com.example.leopo.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
        TextView title = (TextView)findViewById(R.id.movie_title);
        title.setText((CharSequence) mMovie.getTitle());

//        TextView releaseDate = (TextView)findViewById(R.id)
//        private Date mReleaseDate;
//        private String mMoviePoster;
//        private float mVoteAverage;
//        private String mPlotSynopsis;

    }
}

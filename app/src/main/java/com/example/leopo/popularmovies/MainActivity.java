package com.example.leopo.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.leopo.popularmovies.utilities.MovieJsonUtils;
import com.example.leopo.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mMovieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieView = (TextView) findViewById(R.id.tv_title);

        loadMovieData();
    }

    String type = "popular";

    private void loadMovieData() {
        new FetchMoviesTask().execute(type);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            // TODO implement
            if (params.length == 0) {
                return null;
            }

            String type = params[0];

            // TODO should be order line below
            URL moviesUrl = NetworkUtils.buildUrl(type);

            try {
                String movieResponse = NetworkUtils.getApiResponse(moviesUrl);

                String[] simpleJsonMovieData = MovieJsonUtils.getSimpleMovieImagesFromJson(MainActivity.this, movieResponse);

                return simpleJsonMovieData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            if (movieData != null) {
                for (String movieString: movieData) {
                    mMovieView.append((movieString) + "\n\n\n");
                }
            }
        }
    }
}

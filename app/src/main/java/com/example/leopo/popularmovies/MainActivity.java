package com.example.leopo.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.leopo.popularmovies.utilities.MovieJsonUtils;
import com.example.leopo.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mMovieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMovieData();
    }

    String type = "popular";

    private void loadMovieData() {
        new FetchMoviesTask().execute(type);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
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

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(layoutManager);

            ArrayList<Movie> movies = new ArrayList<Movie>();
            Movie movie;


            for (int i=0; i<movieData.length; i++) {
                movie = new Movie();
                movie.setMovie_poster_url(movieData[i]);
                movies.add(movie);
            }

            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), movies);
            recyclerView.setAdapter(adapter);
        }
    }
}

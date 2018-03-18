package com.example.leopo.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.leopo.popularmovies.utilities.MovieJsonUtils;
import com.example.leopo.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mMovieView;

    String mOrder = NetworkUtils.ORDER_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMovieData(mOrder);
    }

    private void loadMovieData(String type) {
        new FetchMoviesTask().execute(type);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        /**
         * Background load
         *
         * @param params
         * @return
         */
        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String order = params[0];

            URL moviesUrl = NetworkUtils.buildUrl(order);

            try {
                String movieResponse = NetworkUtils.getApiResponse(moviesUrl);

                String[] simpleJsonMovieData = MovieJsonUtils.getSimpleMovieImagesFromJson(MainActivity.this, movieResponse);

                return simpleJsonMovieData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Process loaded movie data
         *
         * @param movieData
         */
        @Override
        protected void onPostExecute(String[] movieData) {

            if (movieData == null) {
                return;
            }
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

    /**
     * Create menu with options
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Reload data when changed sorting type
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popularity:
                mOrder = NetworkUtils.ORDER_POPULAR;
                loadMovieData(mOrder);
                return true;
            case R.id.sort_rating:
                mOrder = NetworkUtils.ORDER_TOP_RATED;
                loadMovieData(mOrder);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Turning on currently selected menu option
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mOrder == NetworkUtils.ORDER_POPULAR) {
            menu.findItem(R.id.sort_popularity).setChecked(true);
        } else if (mOrder == NetworkUtils.ORDER_TOP_RATED) {
            menu.findItem(R.id.sort_rating).setChecked(true);
        }

        return true;
    }


}

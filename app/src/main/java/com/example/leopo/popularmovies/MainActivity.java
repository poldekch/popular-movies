package com.example.leopo.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leopo.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;
import com.example.leopo.popularmovies.utilities.MovieJsonUtils;
import com.example.leopo.popularmovies.MovieDetailsActivity;
import com.example.leopo.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    String mOrder = NetworkUtils.ORDER_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mErrorMessageDisplay = (TextView)findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar)findViewById(R.id.pb_loading_indicator) ;

        loadMovieData();
    }

    private void loadMovieData() {
        showMovieDataView();
        new FetchMoviesTask().execute(mOrder);
    }

    @Override
    public void onClick(int clickedMovieId) {
        Context context = this;
        Toast.makeText(context, String.valueOf(clickedMovieId), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        startActivity(intent);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

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
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movieData != null) {
                showMovieDataView();
                ArrayList<Movie> movies = new ArrayList<Movie>();
                Movie movie;
                for (int i=0; i<movieData.length; i++) {
                    movie = new Movie();
                    movie.setMovie_poster_url(movieData[i]);
                    movies.add(movie);
                }

                mMovieAdapter.setMovieData(movies);
            } else {
                showErrorMessage();
            }

//            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), movies, this);
//            recyclerView.setAdapter(adapter);
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
                loadMovieData();
                return true;
            case R.id.sort_rating:
                mOrder = NetworkUtils.ORDER_TOP_RATED;
                loadMovieData();
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

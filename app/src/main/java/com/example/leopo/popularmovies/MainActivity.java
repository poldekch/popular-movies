package com.example.leopo.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.leopo.popularmovies.adapters.MovieAdapter;
import com.example.leopo.popularmovies.adapters.MovieAdapter.MovieAdapterOnClickHandler;
import com.example.leopo.popularmovies.data.MovieContract;
import com.example.leopo.popularmovies.data.MovieDbHelper;
import com.example.leopo.popularmovies.utilities.MovieJsonUtils;
import com.example.leopo.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String mOrder = NetworkUtils.ORDER_POPULAR;

    private SQLiteDatabase mDb;

    private static MenuItem mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator) ;

        MovieDbHelper dbHelper = new MovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

//        insertFakeData(mDb);

        if (null == mMenuItem) {
            loadMovieData();
        } else {
            onOptionsItemSelected(mMenuItem);
        }
    }

    private void loadMovieData() {
        showMovieDataView();
        new FetchMoviesTask().execute(mOrder);
    }

    @Override
    public void onClick(int clickedMovieId) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);

        Movie movie = mMovieAdapter.getMovie(clickedMovieId);
        intent.putExtra("Movie", movie);
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


    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        /**
         * Background load
         *
         * @param params Parameters passed to bacground task. First param needs to be sorting type
         * @return ArrayList of movies
         */
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String order = params[0];
            URL moviesUrl = NetworkUtils.buildUrl(order);

            try {
                String movieResponse = NetworkUtils.getApiResponse(moviesUrl);
                return MovieJsonUtils.getSimpleMovieFromJson(MainActivity.this, movieResponse);
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
        protected void onPostExecute(ArrayList<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }

    public class FetchFavouriteMoviesTask extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            ContentResolver resolver = getContentResolver();

            Cursor cursor = mDb.query(
                    MovieContract.MovieEntry.CONTENT_URI,
//                    MovieContract.MovieEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            ArrayList<Movie> movies = new ArrayList<>();
            Movie movie;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setMoviePosterUrl(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL)));
                movie.setPlotSynopsis(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));

                movies.add(movie);
            }

            cursor.close();

            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movies != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movies);
            } else {
                showErrorMessage();
            }
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
        mMenuItem = item;
        switch (item.getItemId()) {
            case R.id.sort_popularity:
                mOrder = NetworkUtils.ORDER_POPULAR;
                loadMovieData();
                return true;
            case R.id.sort_rating:
                mOrder = NetworkUtils.ORDER_TOP_RATED;
                loadMovieData();
                return true;
            case R.id.sort_favourite:
                mOrder = NetworkUtils.ORDER_FAVOURITE;
//                ArrayList<Movie> favouriteMovies = getFavouriteMovies();
//                mMovieAdapter.setMovieData(favouriteMovies);
                new FetchFavouriteMoviesTask().execute();
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
        } else if (mOrder == NetworkUtils.ORDER_FAVOURITE) {
            menu.findItem(R.id.sort_favourite).setChecked(true);
        }

        return true;
    }

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        List<ContentValues> list = new ArrayList<>();

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 311);
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, "/x733R4ISI0RbKeHhVkXdTMFmTFr.jpg");
        cv.put(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS, "A former Prohibition-era Jewish gangster returns to the Lower East Side of Manhattan over thirty years later, where he once again must confront the ghosts and regrets of his old life.");
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "1984-05-23");
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, "Once Upon a Time in America");
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, "8.3");
        list.add(cv);

        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (MovieContract.MovieEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(MovieContract.MovieEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }
    }
}

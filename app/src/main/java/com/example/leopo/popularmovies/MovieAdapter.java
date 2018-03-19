package com.example.leopo.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.leopo.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movie> mMovies;
    private Context mContext;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(int clickedMovieId);
    }

    public Movie getMovie(int i) {
        return mMovies.get(i);
    }

    /**
     * Constructor
     *
     * @param clickHandler
     */
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final ImageView mPoster;

        public MovieAdapterViewHolder(View view) {
            super(view);

            mPoster = (ImageView) view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(clickedPosition);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        mContext = parent.getContext();

        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        URL url = NetworkUtils.buildImageUrl(mMovies.get(position).getMoviePosterUrl());
        Picasso.with(mContext).load(url.toString()).into(holder.mPoster);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();

    }

    public void setMovieData(ArrayList<Movie> movieData) {
        mMovies = movieData;
        notifyDataSetChanged();
    }
}

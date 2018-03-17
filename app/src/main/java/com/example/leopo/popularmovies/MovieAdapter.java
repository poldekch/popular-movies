package com.example.leopo.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leopo.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context); //parent.getContext() TODO

        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        URL url = NetworkUtils.buildImageUrl(movies.get(position).getMovie_poster_url());
        Picasso.with(context).load(url.toString()).into(holder.iv_poster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_poster;

        public MovieAdapterViewHolder(View view) {
            super(view);

            iv_poster = (ImageView) view.findViewById(R.id.iv_poster);
//            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

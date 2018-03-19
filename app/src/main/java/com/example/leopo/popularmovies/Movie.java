package com.example.leopo.popularmovies;

import java.io.Serializable;

public class Movie implements Serializable{

    private String title;
    private String movie_poster_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMovie_poster_url() {
        return movie_poster_url;
    }

    public void setMovie_poster_url(String movie_poster_url) {
        this.movie_poster_url = movie_poster_url;
    }
}

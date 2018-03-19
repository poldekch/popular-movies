package com.example.leopo.popularmovies;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable{

    private String mTitle;
    private String mReleaseDate;
    private String mMoviePosterUrl;
    private String mVoteAverage;
    private String mPlotSynopsis;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getMoviePosterUrl() {
        return mMoviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        mMoviePosterUrl = moviePosterUrl;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setPlotSynopsis(String mPlotSynopsis) {
        this.mPlotSynopsis = mPlotSynopsis;
    }
}

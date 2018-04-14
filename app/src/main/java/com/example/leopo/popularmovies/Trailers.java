package com.example.leopo.popularmovies;

import java.io.Serializable;
import java.util.ArrayList;

public class Trailers implements Serializable {

    private Integer mId;
    private ArrayList<Trailer> mTrailers;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public ArrayList<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(ArrayList<Trailer> mTrailers) {
        this.mTrailers = mTrailers;
    }
}

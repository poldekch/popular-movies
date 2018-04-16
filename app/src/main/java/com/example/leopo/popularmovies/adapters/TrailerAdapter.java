package com.example.leopo.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leopo.popularmovies.R;
import com.example.leopo.popularmovies.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> mTrailers;
    private Context mContext;

    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(int id);
    }

    public Trailer getTrailer(int i) {
        return mTrailers.get(i);
    }

    /**
     * Constructor
     *
     * @param clickHandler
     */
    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        mContext = parent.getContext();

        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(mTrailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (null == mTrailers) return 0;
        return mTrailers.size();
    }

    public void setTrailersData(ArrayList<Trailer> trailersData) {
        mTrailers = trailersData;
        notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        public final LinearLayout mTrailer;
        TextView trailerLabelView;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            trailerLabelView = (TextView) itemView.findViewById(R.id.tv_trailer_label);
            mTrailer = itemView.findViewById(R.id.ll_trailer);
            itemView.setOnClickListener(this);
        }

        void bind(String trailerName) {
            trailerLabelView.setText(trailerName);
        }

        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(clickedPosition);
        }
    }
}

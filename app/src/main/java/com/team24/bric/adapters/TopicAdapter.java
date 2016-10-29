package com.team24_jpm.bric.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team24_jpm.bric.R;
import com.team24_jpm.bric.views.SquareImageView;

/**
 * Created by harrisonmelton on 10/28/16.
 * Adapter file for GridView on main page.
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    // Index 0 is categories, index 1 is locations, index 2 is event series
    private final String[][] CATEGORIES = {{"Comedy", "Family Friendly", "Theater", "Films",
                                            "Dance", "Free", "Music", "Arts"},
                                            {"B Scene", "B-Side", "BRIC FamJam", "BRIC FLIX",
                                            "BRIC JazzFest", "BRIClab Residencies",
                                            "Dance at BRIC House", "Media Talks",
                                            "Fireworks Residency", "In Concert"},
                                            {"BRIC Arts | Media House", "BRIC House",
                                            "Brooklyn Bridge Park",
                                            "Brooklyn Public Library: Brooklyn Heights",
                                            "Prospect Park Bandshell",
                                            "Weeksville Heritage Center"}};
    private final int[] IMAGES = {R.drawable.comedy, R.drawable.children, R.drawable.theater,
                                    R.drawable.old_camera, R.drawable.dancing, R.drawable.free,
                                    R.drawable.music, R.drawable.art};
    private int categorySection;

    public TopicAdapter(int category) {
        categorySection = category;
    }

    @Override
    public TopicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_view_layout,
                parent, false);

        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TopicAdapter.ViewHolder holder, int position) {
        holder.title.setText(CATEGORIES[categorySection][position]);
        holder.imageView.setImageResource(IMAGES[position]);
    }

    @Override
    public int getItemCount() {
        return CATEGORIES[categorySection].length;
    }

    /**
     * ViewHolder class for each cell in RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View parentView;
        public SquareImageView imageView;
        public TextView title;

        public ViewHolder(View v) {
            super(v);
            parentView = v;
            imageView = (SquareImageView) v.findViewById(R.id.topic_image);
            title = (TextView) v.findViewById(R.id.title);
        }
    }
}

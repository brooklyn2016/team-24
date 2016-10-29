package com.team24_jpm.bric.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by harrisonmelton on 10/28/16.
 * Adapter file for GridView on main page.
 */
public class TagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Index 0 is categories, index 1 is locations, index 2 is event series
    private final String[][] CATEGORIES = {{"Comedy", "Family Friendly", "Theater", "Films",
                                            "Dance", "Free", "Music", "Arts"},
                                            {"BRIC Arts | Media House", "BRIC House",
                                            "Brooklyn Bridge Park",
                                                    "Brooklyn Public Library: Brooklyn Heights",
                                                    "Prospect Park Bandshell",
                                            "Weeksville Heritage Center"},
                                            {"B Scene", "B-Side", "BRIC FamJam", "BRIC FLIX",
                                            "BRIC JazzFest", "BRIClab Residencies",
                                            "Dance at BRIC House", "Media Talks",
                                            "Fireworks Residency", "In Concert"}};
    private int categorySection;
    private Context context;

    public TagAdapter(Context context) {
        this.context = context;
        categorySection = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return CATEGORIES[categorySection].length;
    }
}

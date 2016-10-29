package com.team24_jpm.bric.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team24_jpm.bric.R;
import com.team24_jpm.bric.adapters.TopicAdapter;
import com.team24_jpm.bric.helpers.SpacesItemDecoration;
import com.team24_jpm.bric.helpers.Topic;

/**
 * Created by harrisonmelton on 10/29/16.
 * Fragment for topics on main page.
 */
public class TopicFragment extends Fragment {

    private Topic category;
    private RecyclerView recycler;

    public TopicFragment(){}

    public static TopicFragment newInstance(Topic category) {

        Bundle args = new Bundle();

        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        fragment.category = category;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_topics, container, false);

        // Set RecyclerView to use 2 columns
        recycler = (RecyclerView) mainView.findViewById(R.id.fragment_recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        int categoryIndex;
        // Pass array index to adapter in order to pull correct array of titles
        if (category == Topic.CATEGORIES) {
            categoryIndex = 0;
        } else if (category == Topic.LOCATIONS) {
            categoryIndex = 1;
        } else {
            categoryIndex = 2;
        }

        // Set up RecyclerView Adapter
        recycler.setAdapter(new TopicAdapter(categoryIndex));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recycler.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        return mainView;
    }
}

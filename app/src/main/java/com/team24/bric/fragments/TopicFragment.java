package com.team24_jpm.bric.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team24_jpm.bric.R;
import com.team24_jpm.bric.helpers.Topic;

/**
 * Created by harrisonmelton on 10/29/16.
 * Fragment for topics on main page.
 */
public class TopicFragment extends Fragment {

    private Topic category;

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
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }
}

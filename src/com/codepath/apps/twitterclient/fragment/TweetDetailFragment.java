package com.codepath.apps.twitterclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.R;

public class TweetDetailFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
	View view = inflater.inflate(R.layout.fragment_tweet_list, parent, false);
	//lvTimeline = (ListView) view.findViewById(R.id.lvTimeline);
	return view;
    }

}

package com.codepath.apps.twitterclient.activity;

import android.support.v4.app.FragmentActivity;

import com.codepath.apps.twitterclient.fragment.TweetListFragment;
import com.codepath.apps.twitterclient.helper.IConstants;

public class BaseFragmentActivity extends FragmentActivity implements IConstants, TweetListFragment.OnItemSelectedListener
{
    // Should be called manually when an async task has started
    public void showProgressBar()
    {
	setProgressBarIndeterminateVisibility(true);
    }

    // Should be called when an async task has finished
    public void hideProgressBar()
    {
	setProgressBarIndeterminateVisibility(false);
    }

    
    @Override
    public void onItemSelected(String link)
    {
	// TODO
	// getTweetDetailFragment() = getSupportFragmentManager().findFragmentById(R.id.);
	// transaction.replace(R.id.flHolder, getTweetDetailFragment());

	// if (getTweetDetailFragment() != null && getTweetDetailFragment().isInLayout())
	// {
	// // getTweetDetailFragment().setText(link);
	// }
    }
}

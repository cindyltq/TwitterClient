package com.codepath.apps.twitterclient.fragment;

import java.util.List;

import android.os.Bundle;

import com.codepath.apps.twitterclient.activity.BaseFragmentActivity;
import com.codepath.apps.twitterclient.dao.TweetDAO;
import com.codepath.apps.twitterclient.helper.CommonUtil;
import com.codepath.apps.twitterclient.helper.EndlessScrollListener;
import com.codepath.apps.twitterclient.helper.TwitterRestClient;
import com.codepath.apps.twitterclient.model.Tweet;

public class UserTimelineFragment extends TweetListFragment
{
    String screenName;
    
   @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
    }  
   
   public String getEndpoint()
   {
       return TwitterRestClient.USER_TIMELINE_ENDPOINT;
   }
   
   @Override
   public void populateTimeline()
   {
	if (CommonUtil.isNetworkConnected(getActivity()))
	{
	    screenName = getArguments().getString(SCREEN_NAME_KEY);	
	    REST_CLIENT.getUserTimeLine(maxId, screenName, getEndpoint(), getResponseHandler());
	}
	else
	    itemAdapter.addAll(getSavedItems());
   }
   
   @Override
   protected EndlessScrollListener getOnScrollListener()
   {
	return new EndlessScrollListener()
	{
	    @Override
	    public void onLoadMore(int page, int totalItemsCount)
	    {
		((BaseFragmentActivity)getActivity()).showProgressBar();
		// Triggered only when new data needs to be appended to the list
		 REST_CLIENT.getUserTimeLine(maxId, screenName, getEndpoint(), getResponseHandler());
	    }
	};
   }
   
   @Override
   public List<Tweet> getSavedItems()
   {
	return TweetDAO.getRecentUserItems();
   }
   
 public static UserTimelineFragment newInstance(String screenName)
 {
	UserTimelineFragment fragment = new UserTimelineFragment();
	Bundle args = new Bundle();
	args.putString(SCREEN_NAME_KEY, screenName);	
	fragment.setArguments(args);

	return fragment;
 }
 
}

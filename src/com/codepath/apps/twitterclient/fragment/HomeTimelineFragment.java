package com.codepath.apps.twitterclient.fragment;

import java.util.List;

import android.os.Bundle;

import com.codepath.apps.twitterclient.dao.TweetDAO;
import com.codepath.apps.twitterclient.helper.TwitterRestClient;
import com.codepath.apps.twitterclient.model.Tweet;

public class HomeTimelineFragment extends TweetListFragment
{
   @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);	
    }  
   

   @Override
   public List<Tweet> getSavedItems()
   {
	return TweetDAO.getRecentHomeItems();
   }
   
   public String getEndpoint()
   {
       return TwitterRestClient.HOME_TIMELINE_ENDPOINT;
   }
   
//   public static HomeTimelineFragment newInstance(boolean isNetworkAvailable)
//   {
//	HomeTimelineFragment fragment = new HomeTimelineFragment();	
//	fragment.setArguments(getBundle(isNetworkAvailable));
//
//	return fragment;
//   }
  
 
}

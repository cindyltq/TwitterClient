package com.codepath.apps.twitterclient.fragment;

import java.util.List;

import android.os.Bundle;

import com.codepath.apps.twitterclient.dao.TweetDAO;
import com.codepath.apps.twitterclient.helper.TwitterRestClient;
import com.codepath.apps.twitterclient.model.Tweet;

public class MentionsTimelineFragment extends TweetListFragment
{
   @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);	
    }  
   
   @Override
   public List<Tweet> getSavedItems()
   {
	return TweetDAO.getRecentMentionsItems();
   }
   
   public String getEndpoint()
   {
       return TwitterRestClient.MENTIONS_TIMELINE_ENDPOINT;
   }
   
//   
//   public static MentionsTimelineFragment newInstance(boolean isNetworkAvailable)
//   {
//       MentionsTimelineFragment fragment = new MentionsTimelineFragment();	
//	fragment.setArguments(getBundle(isNetworkAvailable));
//
//	return fragment;
//   }
}

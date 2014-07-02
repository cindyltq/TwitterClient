package com.codepath.apps.twitterclient.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activity.BaseFragmentActivity;
import com.codepath.apps.twitterclient.adapter.TweetArrayAdapter;
import com.codepath.apps.twitterclient.dao.TweetDAO;
import com.codepath.apps.twitterclient.helper.CommonUtil;
import com.codepath.apps.twitterclient.helper.EndlessScrollListener;
import com.codepath.apps.twitterclient.helper.IConstants;
import com.codepath.apps.twitterclient.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class TweetListFragment extends Fragment implements IConstants
{
    protected long maxId = 0;
    protected long sinceId = 0;
    protected ListView lvTimeline;
    protected ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
    protected ArrayAdapter<Tweet> itemAdapter;
    protected OnItemSelectedListener listener;
    protected String endpoint;

    public interface OnItemSelectedListener
    {
	public void onItemSelected(String link);
    }

    @Override
    public void onAttach(Activity activity)
    {
	super.onAttach(activity);
	
	if(activity instanceof OnItemSelectedListener)
	{
	    listener = (OnItemSelectedListener) activity;
	}
	else
	{
	    throw new ClassCastException(activity.toString() + " must implement TweetListFragment.OnItemSelectedListener.");
	}
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	itemAdapter = new TweetArrayAdapter(getActivity(), tweetList);

	//boolean isNetworkAvaialble = getArguments().getBoolean(NETWORK_ON_FLAG);
	populateTimeline();
    }
    
    public void populateTimeline()
    {
	if (CommonUtil.isNetworkConnected(getActivity()))
	    REST_CLIENT.getTimeLine(maxId, getEndpoint(), getResponseHandler());
	else
	    itemAdapter.addAll(getSavedItems());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
	View view = inflater.inflate(R.layout.fragment_tweet_list, parent, false);
	lvTimeline = (ListView) view.findViewById(R.id.lvTimeline);
	lvTimeline.setAdapter(itemAdapter);
	lvTimeline.setOnScrollListener(getOnScrollListener());
	return view;
    }


    protected JsonHttpResponseHandler getResponseHandler()
    {
	return new JsonHttpResponseHandler()
	{
	    @Override
	    public void onSuccess(JSONArray jsonArray)
	    {
		((BaseFragmentActivity)getActivity()).hideProgressBar();
		
		if (jsonArray.length() <= 0)
		    return;

		List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
		itemAdapter.addAll(tweets);
		
		Log.d("DEBUG", "adapter size = " + itemAdapter.getCount());

		setCursors(jsonArray);

		// Persist all tweets in DB
		TweetDAO.saveAllItems(tweets);
	    }

	    @Override
	    public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
	    {
		((BaseFragmentActivity)getActivity()).hideProgressBar();
		
		String msg = CommonUtil.getJsonErrorMsg(errorResponse);
		Toast.makeText(getActivity(), "Remote server call failed. " + msg, Toast.LENGTH_SHORT).show();		
		
		Log.d("ERROR", "getTimeline REST call failure : " + e.getMessage() + "JSON error message: " + msg);
	    }
	};
    }

    private void setCursors(JSONArray jsonArray)
    {
	try
	{
	    JSONObject oldestObject = (JSONObject) jsonArray.get(jsonArray.length() - 1);
	    JSONObject newestObject = (JSONObject) jsonArray.get(0);

	    Tweet oldestTweet = Tweet.fromJson(oldestObject);
	    Tweet newestTweet = Tweet.fromJson(newestObject);

	    setMaxId( oldestTweet.getTweetId() - 1);
	    // sinceId = newestTweet.getTweetId();

	    Log.d("DEBUG", "this.maxId = " + getMaxId() + ", parent.maxId = " + maxId);
	}
	catch (JSONException e)
	{
	    Log.d("ERROR", "Error in JsonHttpResponseHandler : " + e.getMessage());
	    e.printStackTrace();
	}
    }

    // Respond to image grid scrolling
    protected EndlessScrollListener getOnScrollListener()
    {
	return new EndlessScrollListener()
	{
	    @Override
	    public void onLoadMore(int page, int totalItemsCount)
	    {
		// Triggered only when new data needs to be appended to the list
		REST_CLIENT.getTimeLine(getMaxId(), getEndpoint(), getResponseHandler());
	    }
	};
    }

    public void displayNewTweet(Tweet newTweet, boolean isFromServer)
    {
	if (isFromServer)
	    tweetList.set(0, newTweet);
	else
	    tweetList.add(0, newTweet);

	lvTimeline.setSelection(0);
	itemAdapter.notifyDataSetChanged();
    }

    protected static Bundle getBundle(boolean isNetworkAvailable)
    {
	Bundle args = new Bundle();
	args.putBoolean(NETWORK_ON_FLAG, isNetworkAvailable);
	
	return args;
    }


    //TODO
    public void onProfileImageClick(View v)
    {
	listener.onItemSelected("some link");
    }

    protected long getMaxId()
    {
        return this.maxId;
    }

    protected void setMaxId(long maxId)
    {
        this.maxId = maxId;
    }    

    public String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    protected  abstract List<Tweet> getSavedItems();
    
}

package com.codepath.apps.twitterclient.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapter.TweetArrayAdapter;
import com.codepath.apps.twitterclient.helper.EndlessScrollListener;
import com.codepath.apps.twitterclient.helper.TwitterApplication;
import com.codepath.apps.twitterclient.helper.TwitterRestClient;
import com.codepath.apps.twitterclient.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity
{ 
    TwitterRestClient restClient = TwitterApplication.getRestClient();
    private ListView lvTimeline;
    private ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
    private ArrayAdapter<Tweet> itemAdapter;
    public static int REQUEST_CODE = 40;
    
    long maxId = 0;
    long sinceId = 0;
    public static int COUNT = 5;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_timeline);
	
	populateTimeline();
	
	lvTimeline = (ListView) findViewById(R.id.lvTimeline);		
	itemAdapter = new TweetArrayAdapter(this, tweetList);
	lvTimeline.setAdapter(itemAdapter);	
	
	lvTimeline.setOnScrollListener(getOnScrollListener());
    }
    
    private void populateTimeline()
    {
	restClient.getHomeTimeLine(maxId, sinceId, getResponseHandler());
    }
    
    JsonHttpResponseHandler getResponseHandler()
    {
	return new JsonHttpResponseHandler()
	{
	    @Override
	    public void onSuccess(JSONArray jsonArray)
	    {
		 if(jsonArray.length() <= 0)
			return;
		 
		Log.d("DEBUG", jsonArray.toString());
		
		itemAdapter.addAll(Tweet.fromJsonArray(jsonArray));
		
		Log.d("DEBUG", "adapter size = " + itemAdapter.getCount());
		
		setCursors(jsonArray);
	    }
	    
	    @Override
	    public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
	    {
		Toast.makeText(TimelineActivity.this, "Remote server call failed.", Toast.LENGTH_SHORT).show();
		
		Log.d("ERROR", "REST call failure : " + e.getMessage());
		e.printStackTrace();
	    }	    
	};
    }
    
    private void setCursors(JSONArray jsonArray)
    {
	try
	{
	    JSONObject oldestObject = (JSONObject) jsonArray.get(jsonArray.length()-1);
	    JSONObject newestObject = (JSONObject) jsonArray.get(0);
	    
	    Tweet oldestTweet = Tweet.fromJson(oldestObject);
	    Tweet newestTweet = Tweet.fromJson(newestObject);
	    
	    maxId = oldestTweet.getTweetId() -1;
	  //  sinceId = newestTweet.getTweetId();
		
	    Log.d("DEBUG", "maxId = " + maxId + ", sinceId = " + sinceId);
	}
	catch (JSONException e)
	{
	    Log.d("ERROR", "Error in JsonHttpResponseHandler : " + e.getMessage());
	    e.printStackTrace();
	}	
    }
    
    
    // Respond to image grid scrolling
    private EndlessScrollListener getOnScrollListener()
    {
	return new EndlessScrollListener()
	{
	    @Override
	    public void onLoadMore(int page, int totalItemsCount)
	    {
		// Triggered only when new data needs to be appended to the list
		restClient.getHomeTimeLine(maxId, sinceId, getResponseHandler());

	    }
	};
    }
    
    // Inflate ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.menu_compose, menu);	
	return true;
    }
    
    // Respond to ActionBar icon click
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	case R.id.miCompose:
	    composeTweet();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}

    }
    
    // Respond to Compose icon click on ActionBar
    private void composeTweet()
    {
	Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
//	Intent intent = new Intent(this, SettingActivity.class);
//	startActivityForResult(intent, REQUEST_CODE);
    }
    
}

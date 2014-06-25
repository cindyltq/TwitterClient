package com.codepath.apps.twitterclient.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.ComposeTweetActivity;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapter.TweetArrayAdapter;
import com.codepath.apps.twitterclient.dao.TweetDAO;
import com.codepath.apps.twitterclient.helper.CommonUtil;
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
    public static int COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_timeline);

	lvTimeline = (ListView) findViewById(R.id.lvTimeline);
	itemAdapter = new TweetArrayAdapter(this, tweetList);
	lvTimeline.setAdapter(itemAdapter);
	
	populateTimeline();
	
	lvTimeline.setOnScrollListener(getOnScrollListener());
    }

    private void populateTimeline()
    {
	boolean isNetworkAvailable = getIntent().getBooleanExtra("isNetworkAvailable", false);
	
	if(isNetworkAvailable)
	    restClient.getHomeTimeLine(maxId, sinceId, getResponseHandler());
	else
	    itemAdapter.addAll(TweetDAO.getRecentItems());
    }

    JsonHttpResponseHandler getResponseHandler()
    {
	return new JsonHttpResponseHandler()
	{
	    @Override
	    public void onSuccess(JSONArray jsonArray)
	    {
		if (jsonArray.length() <= 0)
		    return;

		//Log.d("DEBUG", jsonArray.toString());

		List<Tweet> tweetList = Tweet.fromJsonArray(jsonArray);
		itemAdapter.addAll(tweetList);

		Log.d("DEBUG", "adapter size = " + itemAdapter.getCount());

		setCursors(jsonArray);
		
		//Persist all tweets in DB
		TweetDAO.saveAllItems(tweetList);
	    }

	    @Override
	    public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
	    {
		String msg =  CommonUtil.getJsonErrorMsg(errorResponse);
		
		Toast.makeText(TimelineActivity.this, "Remote server call failed. " + msg, Toast.LENGTH_SHORT).show();

		Log.d("ERROR", "REST call failure : " + e.getMessage() + "JSON error message: " + msg);
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

	    maxId = oldestTweet.getTweetId() - 1;
	    // sinceId = newestTweet.getTweetId();

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
	Intent intent = new Intent(this, ComposeTweetActivity.class);
	startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE)
	{
	    Tweet newTweet = (Tweet) data.getExtras().getSerializable("tweet");
	    if (!StringUtils.isEmpty(newTweet.getBody()))
	    {
		// Post Tweet to server
		restClient.postStatusUpdate(newTweet.getBody(), getResponseHandlerForPost());

		// Insert the new tweet into current item list for display
		displayNewTweet(newTweet, false);
	    }

	}
    }

    JsonHttpResponseHandler getResponseHandlerForPost()
    {
	return new JsonHttpResponseHandler()
	{
	    @Override
	    public void onSuccess(JSONObject jsonObject)
	    {

		Log.d("DEBUG", jsonObject.toString());

		Tweet newTweet = Tweet.fromJson(jsonObject);

		displayNewTweet(newTweet, true);
	    }

	    @Override
	    public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
	    {
		String msg =  CommonUtil.getJsonErrorMsg(errorResponse);		
		Toast.makeText(TimelineActivity.this, "Remote server call failed. " + msg, Toast.LENGTH_SHORT).show();

		Log.d("ERROR", "REST call failure : " + e.getMessage() + "JSON error message: " + msg);
		e.printStackTrace();
	    }
	};
    }

    private void displayNewTweet(Tweet newTweet, boolean isFromServer)
    {	
	if (isFromServer)
	    tweetList.set(0,newTweet);
	else
	    tweetList.add(0,newTweet);
	
	lvTimeline.setSelection(0);
	itemAdapter.notifyDataSetChanged();
    }

}

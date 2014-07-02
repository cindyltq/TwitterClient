package com.codepath.apps.twitterclient.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.fragment.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragment.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.fragment.TweetDetailFragment;
import com.codepath.apps.twitterclient.helper.CommonUtil;
import com.codepath.apps.twitterclient.helper.FragmentTabListener;
import com.codepath.apps.twitterclient.model.Tweet;
import com.codepath.apps.twitterclient.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends BaseFragmentActivity
{
    private final String HOME_TAB_TAG = "HOME";
    private final String MENTIONS_TAB_TAG = "MENTIONS";

    HomeTimelineFragment homeTimelineFragment;
    MentionsTimelineFragment mentionsFragment;
    TweetDetailFragment tweetDetailFragment;

    public static int REQUEST_CODE = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
	setContentView(R.layout.activity_timeline);

	showProgressBar();
	setupTabs();
    }

    private void setupTabs()
    {
	ActionBar actionBar = getActionBar();
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	actionBar.setDisplayShowTitleEnabled(true);

	Tab tab1 = actionBar
		.newTab()
		.setText("Home")
		.setIcon(R.drawable.ic_home)
		.setTag(HOME_TAB_TAG)
		.setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, HOME_TAB_TAG,
			HomeTimelineFragment.class));

	actionBar.addTab(tab1);
	actionBar.selectTab(tab1);

	Tab tab2 = actionBar
		.newTab()
		.setText("Mentions")
		.setIcon(R.drawable.ic_mentions)
		.setTag(MENTIONS_TAB_TAG)
		.setTabListener(new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, MENTIONS_TAB_TAG,
			MentionsTimelineFragment.class));

	actionBar.addTab(tab2);
    }

    // Inflate ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.menu_compose, menu);
	getMenuInflater().inflate(R.menu.menu_profile, menu);
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
	case R.id.miProfile:
	    loadProfileInfo();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    private void loadProfileInfo()
    {
	showProgressBar();
	
	REST_CLIENT.getMyInfo(new JsonHttpResponseHandler()
	{
	    @Override
	    public void onSuccess(JSONObject json)
	    {
		User user = User.fromJson(json);
		displayProfile(user);
	    }

	    @Override
	    public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
	    {
		String msg = CommonUtil.getJsonErrorMsg(errorResponse);

		Toast.makeText(TimelineActivity.this, "loadProfileInfo Remote server call failed. " + msg, Toast.LENGTH_SHORT).show();

		Log.d("ERROR", "loadProfileInfo REST call failure : " + e.getMessage() + "JSON error message: " + msg);
	    }
	});
    }

    public void displayProfile(User user)
    {
	Intent intent = new Intent(this, ProfileActivity.class);
	intent.putExtra(USER_OBJ_KEY, user);
	startActivity(intent);
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
		showProgressBar();
		// Post Tweet to server
		REST_CLIENT.postStatusUpdate(newTweet.getBody(), getResponseHandlerForPost());

		// Insert the new tweet into current item list for display
		homeTimelineFragment.displayNewTweet(newTweet, false);
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

		homeTimelineFragment.displayNewTweet(newTweet, true);
		
		TimelineActivity.this.hideProgressBar();
	    }

	    @Override
	    public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
	    {
		TimelineActivity.this.hideProgressBar();
		String msg = CommonUtil.getJsonErrorMsg(errorResponse);
		Toast.makeText(TimelineActivity.this, "Remote server call failed. " + msg, Toast.LENGTH_SHORT).show();

		Log.d("ERROR", "UpdateStatus REST call failure : " + e.getMessage() + "JSON error message: " + msg);
		e.printStackTrace();
	    }
	};
    }
    
    // // TODO
    // private void displayHomeTimelineFragment()
    // {
    // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    //
    // // if the fragment is already in container
    // if (getHomeTimelineFragment().isAdded())
    // transaction.show(getHomeTimelineFragment());
    // else
    // transaction.add(R.id.flContainer, getHomeTimelineFragment(), "Home Timeline");
    //
    // if (mentionsFragment.isAdded())
    // transaction.hide(mentionsFragment);
    //
    // transaction.commit();
    // }
    //
    // // Respond to Compose icon click on ActionBar
    // private void displayMentionsFragment()
    // {
    // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    //
    // // if the fragment is already in container
    // if (getMentionsTimelineFragment().isAdded())
    // transaction.show(getMentionsTimelineFragment());
    // else
    // transaction.add(R.id.flContainer, getMentionsTimelineFragment(), "Mentions");
    //
    // if (homeTimelineFragment.isAdded())
    // transaction.hide(homeTimelineFragment);
    //
    // transaction.commit();
    //
    // }
    // private HomeTimelineFragment getHomeTimelineFragment()
    // {
    // if (homeTimelineFragment == null)
    // {
    // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    //
    // boolean networkFlag = getIntent().getBooleanExtra(NETWORK_ON_FLAG, false);
    // homeTimelineFragment = HomeTimelineFragment.newInstance(networkFlag);
    // transaction.replace(R.id.flContainer, homeTimelineFragment);
    //
    // transaction.commit();
    // }
    //
    // return homeTimelineFragment;
    // }
    //
    // private MentionsTimelineFragment getMentionsTimelineFragment()
    // {
    // if (mentionsFragment == null)
    // {
    // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    //
    // boolean networkFlag = getIntent().getBooleanExtra(NETWORK_ON_FLAG, false);
    // mentionsFragment = MentionsTimelineFragment.newInstance(networkFlag);
    // transaction.replace(R.id.flContainer, homeTimelineFragment);
    //
    // transaction.commit();
    // }
    //
    // return mentionsFragment;
    // }

    private TweetDetailFragment getTweetDetailFragment()
    {
	if (tweetDetailFragment == null)
	    tweetDetailFragment = new TweetDetailFragment();

	return tweetDetailFragment;
    }

    @Override
    public void onItemSelected(String link)
    {
	// TODO
	// getTweetDetailFragment() = getSupportFragmentManager().findFragmentById(R.id.);
	// transaction.replace(R.id.flHolder, getTweetDetailFragment());

	if (getTweetDetailFragment() != null && getTweetDetailFragment().isInLayout())
	{
	    // getTweetDetailFragment().setText(link);
	}
    }
}

package com.codepath.apps.twitterclient.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.fragment.UserTimelineFragment;
import com.codepath.apps.twitterclient.helper.CommonUtil;
import com.codepath.apps.twitterclient.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends BaseFragmentActivity 
{
    UserTimelineFragment userTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
	setContentView(R.layout.activity_profile);
	
	User user = (User) getIntent().getSerializableExtra(USER_OBJ_KEY);
	//populateProfileHeader(user);	
	//Somehow, above  passed User object does not contain "follower", "following", "tweet count" info. I have to call server API to fetch again
	loadProfileInfo(user.getScreenName());

	displayUserTimelineFragment(user.getScreenName());
    }

    private void displayUserTimelineFragment(String screenName)
    {
	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	userTimelineFragment = UserTimelineFragment.newInstance(screenName);
	transaction.replace(R.id.flUserTimeline, userTimelineFragment);

	transaction.commit();
    }

    private void loadProfileInfo(String screenName)
    {
	showProgressBar();
	
	REST_CLIENT.getUser(screenName, new JsonHttpResponseHandler()
	{
	    @Override
	    public void onSuccess(JSONObject json)
	    {
		User user = User.fromJson(json);
		populateProfileHeader(user);
	    }

	    @Override
	    public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
	    {
		ProfileActivity.this.hideProgressBar();
		
		String msg = CommonUtil.getJsonErrorMsg(errorResponse);
		Toast.makeText(ProfileActivity.this, "Remote server call failed. " + msg, Toast.LENGTH_SHORT).show();

		Log.d("ERROR", "loadProfileInfo REST call failure : " + e.getMessage() + "JSON error message: " + msg);
	    }
	});
    }

    public void populateProfileHeader(User user)
    {
	TextView tvName = (TextView) findViewById(R.id.tvName);
	TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
	TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
	TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
	TextView tvTweets = (TextView) findViewById(R.id.tvTweets);
	ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

	tvName.setText(user.getName());
	tvTagline.setText(user.getTagLine());
	tvTweets.setText(user.getTweetsCount() + " TWEETS");
	tvFollowers.setText(user.getFollowersCount() + " FOLLOWERS");
	tvFollowing.setText(user.getFollowingCount() + " FOLLOWING");
	ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);

	getActionBar().setTitle("@" + user.getScreenName());
    }

    @Override
    public void onItemSelected(String link)
    {
	// TODO Auto-generated method stub

    }

}

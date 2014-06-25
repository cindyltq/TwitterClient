package com.codepath.apps.twitterclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helper.CommonUtil;
import com.codepath.apps.twitterclient.helper.TwitterRestClient;
import com.codepath.oauth.OAuthLoginActivity;

public class LoginActivity extends OAuthLoginActivity<TwitterRestClient>
{
    TextView tvAppDesc;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_login);
	
	tvAppDesc = (TextView) findViewById(R.id.tvAppDesc);
	tvAppDesc.setText(Html.fromHtml(getString(R.string.question_desc)));
    }


    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    @Override
    public void onLoginSuccess()
    {
	Intent i = new Intent(this, TimelineActivity.class);
	if (CommonUtil.isNetworkConnected(this))
	    i.putExtra("isNetworkAvailable", false);
	else	
	  i.putExtra("isNetworkAvailable", false);
	
	startActivity(i);
    }

    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e)
    {
	Toast.makeText(this, "Sign in failed.", Toast.LENGTH_SHORT).show();
	
	Log.e("ERROR", e.toString());
	e.printStackTrace();
    }

    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view)
    {
	if (!CommonUtil.isNetworkConnected(this))
	{
	    Toast.makeText(this, "Internet connection is unavailable.", Toast.LENGTH_SHORT).show();
	    
	    Intent i = new Intent(this, TimelineActivity.class);
	    i.putExtra("isNetworkAvailable", false);
	    startActivity(i);
	}
	else
	{	
	    getClient().connect();
	}
    }

}

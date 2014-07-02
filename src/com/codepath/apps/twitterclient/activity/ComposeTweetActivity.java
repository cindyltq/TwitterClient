package com.codepath.apps.twitterclient.activity;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helper.CommonUtil;
import com.codepath.apps.twitterclient.model.Tweet;
import com.codepath.apps.twitterclient.model.User;

public class ComposeTweetActivity extends Activity
{
    EditText etStatus ;
    TextView tvDisplay;
    int counter = 140;
 
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_compose_tweet);
	
	etStatus = (EditText) findViewById(R.id.etStatus);
	etStatus.addTextChangedListener(getTextChangedListener());
	
	tvDisplay =  (TextView) findViewById(R.id.tvDisplay);
    }
    
    private TextWatcher getTextChangedListener()
    {
	return new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// Fires right as the text is being changed (even supplies the range of text)
		    counter = counter - count;
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// Fires right before text is changing
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// Fires right after the text has changed
		       
			tvDisplay.setText("" + counter);
		}
	};
    }
    
    public void onSendAction(View view)
    {
	Intent returnResultIntent = new Intent();
	
	Tweet tweet = new Tweet();
	tweet.setBody(etStatus.getText().toString());
	tweet.setCreatedTime(CommonUtil.getStringFromDate(new Date()));
	
	User me = new User();
	me.setName("Cindy T. Li");
	me.setScreenName("cindyltq");
	tweet.setUser(me);
	
	returnResultIntent.putExtra("tweet", tweet );	
	
	setResult(RESULT_OK, returnResultIntent); 
	finish(); // closes the activity, pass data to parent
    }
    
    public void onCancelAction(View view)
    {
	etStatus.setText("");
	finish();
    }
}

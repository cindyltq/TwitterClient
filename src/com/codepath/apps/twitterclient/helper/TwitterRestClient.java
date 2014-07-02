package com.codepath.apps.twitterclient.helper;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterRestClient extends OAuthBaseClient implements IConstants{
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "xStctYyrNHtcxyqftFBmQifAh";       // Change this
	public static final String REST_CONSUMER_SECRET = "bfmZu2aR6i2Wr0aE5FCsY88IlQQzGz5F9HvWZZcgjDmjhiYPkE"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)
	public static final String HOME_TIMELINE_ENDPOINT = "statuses/home_timeline.json";
	public static final String USER_TIMELINE_ENDPOINT = "statuses/user_timeline.json";
	public static final String MENTIONS_TIMELINE_ENDPOINT = "statuses/mentions_timeline.json";
	public static final String GET_MY_INFO_ENDPOINT = "account/verify_credentials.json";
	public static final String GET_USER_ENDPOINT = "users/show.json";
	
	
		
	public TwitterRestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
//	public void getHomeTimeLine(long maxId, AsyncHttpResponseHandler handler)
//	{
//	   getTimeLine(maxId, 0, HOME_TIMELINE_ENDPOINT, handler );
//	}
//	
	
	public void getUserTimeLine(long maxId, String screenName, String endPoint, AsyncHttpResponseHandler handler)
	{
	   getTimeLine(maxId, 0, screenName, endPoint, handler );
	}
//	
//	public void getMentions(long maxId, AsyncHttpResponseHandler handler)
//	{
//	   getTimeLine(maxId, 0, MENTIONS_TIMELINE_ENDPOINT, handler );
//	}
	
	public void getTimeLine(long maxId, String endPoint, AsyncHttpResponseHandler handler)
	{
	    getTimeLine(maxId, 0, null, endPoint, handler );
	}
	
	public void getTimeLine(long maxId, long sinceId, String screenName, String endPoint, AsyncHttpResponseHandler handler)
	{
	    String apiUrl = getApiUrl (endPoint);
	    RequestParams params = new RequestParams();   
	    
	    if(maxId != 0)
		params.put("max_id", String.valueOf(maxId));
	    
	    if(sinceId != 0)
		 params.put("since_id", String.valueOf(sinceId));
	    
	    if(screenName != null)
		 params.put(SCREEN_NAME_KEY, screenName);
	    
	    params.put("count", String.valueOf(IConstants.TWEET_COUNT));
	    client.get(apiUrl, params, handler);
	}
	
	public void postStatusUpdate(String statusText, AsyncHttpResponseHandler handler)
	{
	    String apiUrl = getApiUrl ("statuses/update.json");
	    RequestParams params = new RequestParams();	    
	    params.put("status", statusText);
	    client.post(apiUrl, params, handler);
	}
	
	public void getMyInfo (AsyncHttpResponseHandler handler)
	{
	    String apiUrl = getApiUrl (GET_MY_INFO_ENDPOINT);
	    client.get(apiUrl, null, handler);	    
	}
	
	public void getUser (String screenName, AsyncHttpResponseHandler handler)
	{
	    String apiUrl = getApiUrl (GET_USER_ENDPOINT);
	    RequestParams params = new RequestParams();   
	    params.put(SCREEN_NAME_KEY, screenName);
	    client.get(apiUrl, params, handler);	    
	}
}
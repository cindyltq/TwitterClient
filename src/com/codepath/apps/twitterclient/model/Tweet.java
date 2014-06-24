package com.codepath.apps.twitterclient.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.codepath.apps.twitterclient.helper.CommonUtil;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "tweet")
public class Tweet extends Model implements Serializable
{
    // Define table fields
    @Column(name = "body")
    private String body;

    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long tweetId;

    @Column(name = "createdTime", index = true)
    private Date createdTime;

    @Column(name = "User")
    private User user;

    public Tweet()
    {
	super();
    }

    // Parse model from JSON
//    public Tweet(JSONObject jsonObject)
//    {
//	super();
//
//	try
//	{
//	    this.tweetId = jsonObject.getLong("id");
//	    this.body = jsonObject.getString("text");
//	    this.createdTime = jsonObject.getString("created_at");
//	    this.user = User.fromJson(jsonObject.getJSONObject("user"));
//	}
//	catch (JSONException e)
//	{
//	    e.printStackTrace();
//	}
//    }


    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }


    public long getTweetId()
    {
        return tweetId;
    }

    public void setTweetId(long tweetId)
    {
        this.tweetId = tweetId;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime(String createdTime)
    {
	this.createdTime = CommonUtil.getDateFromString(createdTime);
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public static Tweet fromJson(JSONObject jsonObject)
    {
	Tweet tweet = new Tweet();
	try
	{
	    tweet.tweetId = jsonObject.getLong("id");
	    tweet.body = jsonObject.getString("text");
	    tweet.createdTime = CommonUtil.getDateFromString(jsonObject.getString("created_at"));
	    tweet.user =  User.fromJson(jsonObject.getJSONObject("user"));
	}
	catch (JSONException e)
	{
	    e.printStackTrace();
	    return null;
	}
	return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray)
    {
	ArrayList<Tweet> tweetList = new ArrayList<Tweet>(jsonArray.length());
	
	// Process each result in json array, decode and convert to Tweet object
	for (int i = 0; i < jsonArray.length(); i++)
	{
	    JSONObject tweetJson = null;
	    try
	    {
		tweetJson = jsonArray.getJSONObject(i);
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
		continue;
	    }

	    Tweet tweet = Tweet.fromJson(tweetJson);
	    if (tweet != null)
	    	tweetList.add(tweet);
	}

	return tweetList;
    }

    @Override
    public String toString()
    {
	return getBody()+ " -" + getUser().getScreenName();
	
    }   
}

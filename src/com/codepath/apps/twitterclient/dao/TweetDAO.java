package com.codepath.apps.twitterclient.dao;

import java.util.Collection;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.model.Tweet;
import com.codepath.apps.twitterclient.model.User;

public class TweetDAO
{

    // Record Finders
    public static Tweet getItemById(long id)
    {
	return new Select().from(Tweet.class).where("id = ?", id).executeSingle();
    }

  
    public static List<Tweet> getAllItemsByUser(User user)
    {
	// This is how you execute a query
	return new Select()
		.from(Tweet.class)
		.where("User = ?", user.getId())
		.orderBy("Name ASC")
		.execute();
    }

    public static void saveAllItems(List<Tweet> tweetList)
    {
	ActiveAndroid.beginTransaction();
	try
	{
	    for (Tweet tweet : tweetList)
	    {
		createItem(tweet);
	    }
	    ActiveAndroid.setTransactionSuccessful();
	}
	finally
	{
	    ActiveAndroid.endTransaction();
	}

    }

    public static void createItem(Tweet tweet)
    {
	User user = tweet.getUser();
	User newUser = new User();
	newUser.setUid(user.getUid());
	newUser.setName(user.getName());
	newUser.setScreenName(user.getScreenName());
	newUser.setProfileImageUrl(user.getProfileImageUrl());
	newUser.save();
	
	tweet.setUser(newUser);
	tweet.save();
    }

    public static void deleteItemById(long tweetId)
    {
	Tweet item = Tweet.load(Tweet.class, tweetId);
	item.delete();
	// or with
	new Delete().from(Tweet.class).where("remote_id = ?", tweetId).execute();

    }

    public static void deleteAllItems()
    {
	new Delete().from(Tweet.class).execute(); // all records
    }

    public static List<Tweet> getRecentHomeItems()
    {
	List<Tweet> tweetList = new Select().from(Tweet.class).orderBy("id DESC").limit("300").execute();
	
	return tweetList;
    }

    public static List<Tweet> getRecentMentionsItems()
    {
	// TODO Auto-generated method stub
	return null;
    }
    
    public static List<Tweet> getRecentUserItems()
    {
	// TODO Auto-generated method stub
	return null;
    }
}

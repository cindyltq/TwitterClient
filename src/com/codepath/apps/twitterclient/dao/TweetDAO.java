package com.codepath.apps.twitterclient.dao;

import java.util.List;

import android.content.ClipData.Item;

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

    public static List<Tweet> getRecentItems()
    {
	return new Select().from(Tweet.class).orderBy("id DESC").limit("300").execute();
    }
    
    public static List<Tweet> getAllItemsByUser(User user) {
        // This is how you execute a query
        return new Select()
          .from(Tweet.class)
          .where("User = ?", user.getId())
          .orderBy("Name ASC")
          .execute();
    }

    public static void createItem()
    {
	// Create a category
	User user = new User();
	//user.setUid(uid);
	user.setName("Jim");
	user.save();

	// Create an item 
	Tweet item = new Tweet();
	item.setTweetId(1);;
	item.setBody(""); 
	//item.setCreatedTime(createdTime); 
	item.setUser(user);
	item.save();

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
}

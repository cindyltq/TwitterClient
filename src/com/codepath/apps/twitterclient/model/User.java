package com.codepath.apps.twitterclient.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "user")
public class User extends Model implements Serializable
{
    // Define table fields
    @Column(name = "name")
    private String name;

    @Column(name = "uid")
    private long uid;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "profileImageUrl")
    private String profileImageUrl;
    
    @Column(name = "tagLine")
    private String tagLine;

    @Column(name = "followersCount")
    private String followersCount;
    
    @Column(name = "followingCount")
    private String followingCount;
    
    @Column(name = "tweetsCount")
    private String tweetsCount;
    
    public User()
    {
	super();
    }

    // Parse model from JSON
//    public User(JSONObject jsonObject)
//    {
//	super();
//
//	try
//	{
//	    this.uid = jsonObject.getLong("id");
//	    this.name = jsonObject.getString("name");
//	    this.screenName = jsonObject.getString("screen_name");
//	    this.profileImageUrl = jsonObject.getString("profile_image_url");
//	}
//	catch (JSONException e)
//	{
//	    e.printStackTrace();
//	}
//    }

    // Getters
    public String getName()
    {
	return name;
    }

    public long getUid()
    {
	return uid;
    }

    public void setUid(long uid)
    {
	this.uid = uid;
    }

    public String getScreenName()
    {
	return screenName;
    }

    public void setScreenName(String screenName)
    {
	this.screenName = screenName;
    }

    public String getProfileImageUrl()
    {
	return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl)
    {
	this.profileImageUrl = profileImageUrl;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    
    public String getTagLine()
    {
        return tagLine;
    }

    public void setTagLine(String tagLine)
    {
        this.tagLine = tagLine;
    }
    
    

    public String getFollowersCount()
    {
        return followersCount;
    }

    public void setFollowersCount(String followersCount)
    {
        this.followersCount = followersCount;
    }

    public String getFollowingCount()
    {
        return followingCount;
    }

    
    public String getTweetsCount()
    {
        return tweetsCount;
    }

    public void setTweetsCount(String tweetsCount)
    {
        this.tweetsCount = tweetsCount;
    }

    public void setFollowingCount(String followingCount)
    {
        this.followingCount = followingCount;
    }

    public static User fromJson(JSONObject jsonObject)
    {
	User user = new User();
	try
	{
	    user.uid = jsonObject.getLong("id");
	    user.name = jsonObject.getString("name");
	    user.screenName = jsonObject.getString("screen_name");
	    user.profileImageUrl = jsonObject.getString("profile_image_url");
	    user.tagLine = jsonObject.getString("description");
	    user.followersCount = jsonObject.getString("followers_count");
	    user.followingCount = jsonObject.getString("friends_count");
	    user.tweetsCount = jsonObject.getString("statuses_count");
	}
	catch (JSONException e)
	{
	    e.printStackTrace();
	    return null;
	}
	// Return new object
	return user;
    }

    public static ArrayList<User> fromJsonArray(JSONArray jsonArray)
    {
	ArrayList<User> userList = new ArrayList<User>(jsonArray.length());
	
	// Process each result in json array, decode and convert to userModel object
	for (int i = 0; i < jsonArray.length(); i++)
	{
	    JSONObject userJson = null;
	    try
	    {
		userJson = jsonArray.getJSONObject(i);
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
		continue;
	    }

	    User user = User.fromJson(userJson);
	    if (user != null)
	    	userList.add(user);
	}

	return userList;
    }

    // Used to return items from another table based on the foreign key
    public List<Tweet> tweets() {
        return getMany(Tweet.class, "User");
    }

    // Record Finders
    public static User byId(long id)
    {
	return new Select().from(User.class).where("id = ?", id).executeSingle();
    }


}

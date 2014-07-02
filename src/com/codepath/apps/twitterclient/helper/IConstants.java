package com.codepath.apps.twitterclient.helper;

public interface IConstants
{
    public static final String NETWORK_ON_FLAG = "isNetworkAvailable";
    
    public static final String SCREEN_NAME_KEY = "screen_name";
    
    public static final String USER_OBJ_KEY = "user";
    
    public static int TWEET_COUNT = 10;
    
    public static int MAX_CHAR = 140;
    
    
    public static TwitterRestClient REST_CLIENT = TwitterApplication.getRestClient(); 

}

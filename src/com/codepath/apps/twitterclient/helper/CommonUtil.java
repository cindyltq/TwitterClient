package com.codepath.apps.twitterclient.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.format.DateUtils;

public class CommonUtil
{
   public static SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
    
    public static Date getDateFromString(String dateString)
    {	
	sf.setLenient(true);
	Date date = null;
	try
	{
	    date = sf.parse(dateString);
	}
	catch (ParseException e)
	{
	    e.printStackTrace();
	}

	return date;
    }
    
    public static String getStringFromDate(Date date)
    {
	return sf.format(date);
    }

    public static String getRelativeTimeAgo(String rawJsonDate)
    {
	String relativeDate = "";
	long dateMillis = getDateFromString(rawJsonDate).getTime();
	relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

	relativeDate = StringUtils.replace(relativeDate, "hours", "h");
	relativeDate = StringUtils.replace(relativeDate, "hour", "h");
	relativeDate = StringUtils.replace(relativeDate, "minutes", "m");
	relativeDate = StringUtils.replace(relativeDate, "minute", "m");
	relativeDate = StringUtils.replace(relativeDate, "days", "d");
	relativeDate = StringUtils.replace(relativeDate, "day", "d");
	
	return relativeDate;
    }

    public static boolean isNetworkConnected(Context context)
    {
	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	return (cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}

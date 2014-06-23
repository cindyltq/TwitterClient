package com.codepath.apps.twitterclient.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helper.CommonUtil;
import com.codepath.apps.twitterclient.model.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet>
{
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvTimestamp;
    private TextView tvBody;
    private ImageView ivProfileImage;

    public TweetArrayAdapter(Context context, List<Tweet> tweets)
    {
	super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	View view;

	Tweet tweet = (Tweet) getItem(position);

	if (convertView == null)
	{
	    LayoutInflater inflater = LayoutInflater.from(getContext());
	    view = inflater.inflate(R.layout.tweet_item, parent, false);
	}
	else
	{
	    view = convertView;
	}

	tvUserName = (TextView) view.findViewById(R.id.tvUserName);
	tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
	tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);	
	tvBody = (TextView) view.findViewById(R.id.tvBody);
	ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);

	ivProfileImage.setImageResource(android.R.color.transparent);

	ImageLoader loader = ImageLoader.getInstance();
	loader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);

	tvUserName.setText(tweet.getUser().getName());
	tvScreenName.setText(" @" + tweet.getUser().getScreenName());	
	tvTimestamp.setText(CommonUtil.getRelativeTimeAgo(CommonUtil.getStringFromDate(tweet.getCreatedTime())));
	tvBody.setText(tweet.getBody());

	return view;

    }

}

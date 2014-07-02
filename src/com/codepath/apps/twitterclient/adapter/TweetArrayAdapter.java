package com.codepath.apps.twitterclient.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activity.ProfileActivity;
import com.codepath.apps.twitterclient.helper.CommonUtil;
import com.codepath.apps.twitterclient.helper.IConstants;
import com.codepath.apps.twitterclient.model.Tweet;
import com.codepath.apps.twitterclient.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> implements IConstants
{
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvTimestamp;
    private TextView tvBody;
    private ImageView ivProfileImage;
    Context context;

    public TweetArrayAdapter(Context context, List<Tweet> tweets)
    {
	super(context, 0, tweets);
	this.context = context;
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
	ivProfileImage.setTag(tweet.getUser());  //save User object into tag
	ImageLoader loader = ImageLoader.getInstance();
	loader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);

	tvUserName.setText(tweet.getUser().getName());
	tvScreenName.setText(" @" + tweet.getUser().getScreenName());
	tvTimestamp.setText(CommonUtil.getRelativeTimeAgo(CommonUtil.getStringFromDate(tweet.getCreatedTime())));
	tvBody.setText(tweet.getBody());

	ivProfileImage.setOnClickListener(getOnClickListener());

	return view;

    }

    private OnClickListener getOnClickListener()
    {
	return new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		Intent intent = new Intent(context, ProfileActivity.class);
		intent.putExtra(USER_OBJ_KEY, (User) v.getTag());  //retrieve User object from tag to pass to ProfileActivity to build UserTimeline
		context.startActivity(intent);
	    }
	};
    }

}

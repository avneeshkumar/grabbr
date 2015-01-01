package com.grabbr.grabbrapp.activities;


import java.util.ArrayList;
import java.util.List;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.MyProfileNewsfeedAdapter;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.customviews.RoundedImageView;
import com.grabbr.grabbrapp.daos.FollowersData;
import com.grabbr.grabbrapp.daos.NewsfeedData;
import com.grabbr.grabbrapp.services.FetchFollow;
import com.grabbr.grabbrapp.services.FetchMyPosts;
import com.grabbr.grabbrapp.services.FetchProfileInfo;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FloatingProfileActivity extends Activity {

	private MyTextView person_city;
	private MyTextView person_descrip;
	private MyTextView follower_count;
	private MyTextView following_count;
	private RelativeLayout find_friend_layout;
	private RoundedImageView profile_pic_view;
	private MyTextView person_name_tv;
	
	ListView lv ;
	FrameLayout searchbar;
	 
	public  MyProfileNewsfeedAdapter newsfeed_adapter;
	
	private int currentList;
	
	private List<NewsfeedData> rowItems;
	private ArrayList<FollowersData> followerList;
	private ArrayList<FollowersData> followingList;
	public String UserID;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_profile);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		
		UserID = getIntent().getStringExtra("userid");
		
		 if (android.os.Build.VERSION.SDK_INT > 9) {
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
		 }
		 
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		params.height = height - dpToPx(112);
		
		person_city = (MyTextView) findViewById(R.id.person_city);
		person_descrip = (MyTextView) findViewById(R.id.person_descrip);
		follower_count = (MyTextView) findViewById(R.id.followers_count);
		following_count = (MyTextView) findViewById(R.id.following_count);
		find_friend_layout = (RelativeLayout) findViewById(R.id.find_friends);
		lv = (ListView) findViewById(R.id.list);
		profile_pic_view = (RoundedImageView) findViewById(R.id.profile_pic);
		person_name_tv = (MyTextView) findViewById(R.id.person_name);
		
		newsfeed_adapter = new MyProfileNewsfeedAdapter(FloatingProfileActivity.this);
		searchbar = (FrameLayout)findViewById(R.id.search_bar);
		searchbar.setVisibility(View.GONE);
		

		
		InitilaiseView();
		
	}

	@SuppressLint("NewApi")
	private void InitilaiseView() {
		// TODO Auto-generated method stub
		
		Picasso.with(this)
        .load(new MyConstants().profile_url+UserID+"/")
        .resize(100,100)
        .centerCrop()
        .into(profile_pic_view);
		
		rowItems = new ArrayList<NewsfeedData>();
		currentList = 0;
		
		FetchMyPosts fp = new FetchMyPosts(FloatingProfileActivity.this,lv,rowItems,newsfeed_adapter);       
		fp.execute(new MyConstants().mypostview_url);
		
		FetchProfileInfo fetchProfileInfo =  new FetchProfileInfo(FloatingProfileActivity.this,follower_count,
							following_count,person_city,person_descrip);
		fetchProfileInfo.setPerson_name_tv(person_name_tv);
		fetchProfileInfo.execute(new MyConstants().publicprofile_info_url+UserID+"/");
		
		LinearLayout following = (LinearLayout) findViewById(R.id.following);
		following.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchbar.setVisibility(View.VISIBLE);
				if(currentList!=1){
					lv.setAdapter(null);
					followingList = new ArrayList<FollowersData>();
					new FetchFollow(FloatingProfileActivity.this,lv,followingList,"following").
									execute(new MyConstants().publicfollowings_url+UserID+"/");
					currentList = 1;
				}
			}
		});
		
		LinearLayout followers = (LinearLayout) findViewById(R.id.followers);
		followers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchbar.setVisibility(View.VISIBLE);
				if(currentList!=2){
					lv.setAdapter(null);
					followerList = new ArrayList<FollowersData>();
					new FetchFollow(FloatingProfileActivity.this,lv,followerList,"follower").
									execute(new MyConstants().publicfollowers_url+UserID+"/");
					currentList = 2;
				}
			}
		});
		
		LinearLayout favorites = (LinearLayout) findViewById(R.id.favorites);
		favorites.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchbar.setVisibility(View.GONE);
				if(currentList!=3){
					lv.setAdapter(null);
					rowItems = new ArrayList<NewsfeedData>();
					FetchMyPosts fp = new FetchMyPosts(FloatingProfileActivity.this,lv,rowItems,newsfeed_adapter);				
					fp.execute(new MyConstants().publicfavpost_url+UserID+"/");
					currentList = 3;
				}
			}
		});
		
		LinearLayout selling = (LinearLayout) findViewById(R.id.selling);
		selling.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchbar.setVisibility(View.GONE);
				if(currentList!=0){
					lv.setAdapter(null);
					rowItems = new ArrayList<NewsfeedData>();
					FetchMyPosts fp = new FetchMyPosts(FloatingProfileActivity.this,lv,rowItems,newsfeed_adapter);			
					fp.execute(new MyConstants().publicpost_url+UserID+"/");
					currentList = 0;
				}
			}
		});
	}

	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
}

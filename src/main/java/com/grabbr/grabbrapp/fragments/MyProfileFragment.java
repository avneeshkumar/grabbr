package com.grabbr.grabbrapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.MyProfileActivity;
import com.grabbr.grabbrapp.customviews.FollowerListAdapter;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.customviews.RoundedImageView;
import com.grabbr.grabbrapp.customviews.MyProfileNewsfeedAdapter;
import com.grabbr.grabbrapp.daos.FollowersData;
import com.grabbr.grabbrapp.daos.NewsfeedData;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.services.FetchFollow;
import com.grabbr.grabbrapp.services.FetchMyPosts;
import com.grabbr.grabbrapp.services.FetchProfileInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class MyProfileFragment extends Fragment {
	
	Fragment f = this;
	SharedPreferences sharedpreference;
	RoundedImageView profile_pic_view;
	private int currentList;
	
	 public  MyProfileNewsfeedAdapter newsfeed_adapter;
	 private List<NewsfeedData> rowItems;
	 private ArrayList<FollowersData> followerList;
	 private ArrayList<FollowersData> followingList;
	 ImageView edit_profile;
	 ImageView edit_settings;
	 ListView lv ;
	 MyProfileActivity myProfile;
	 RelativeLayout find_friend_layout;
	 private MyTextView person_city;
	 private MyTextView person_descrip;
	 private MyTextView follower_count;
	 private MyTextView following_count;
	 FrameLayout searchbar;
	 FetchMyPosts sellingTask;
	 FetchFollow followingTask;
	 FetchFollow followerTask;
	 FetchMyPosts favoriteTask;
	 FetchProfileInfo fetchProfileInfo;
	 EditText search_edit_text;
	 int follow_follower = -1;
	 
	@Override
	   public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.fragment_profile, container, false);
		MyTextView person_name_tv = (MyTextView) v.findViewById(R.id.person_name);
		MyConstants c = new MyConstants();
		sharedpreference = getActivity().getSharedPreferences(c.MyPREFERENCES, Context.MODE_PRIVATE);
		find_friend_layout = (RelativeLayout) v.findViewById(R.id.find_friends);
		//find_friend_layout.setVisibility(View.GONE);
		profile_pic_view = (RoundedImageView) v.findViewById(R.id.profile_pic);
		person_name_tv.setText(sharedpreference.getString(c.name, ""));
		Picasso.with(getActivity())
        .load(c.profile_url+sharedpreference.getString(c.id, "")+"/")
        .resize(100,100)
        .centerCrop()
        .into(profile_pic_view);
		//sharedpreference = 
		person_city = (MyTextView) v.findViewById(R.id.person_city);
		person_descrip = (MyTextView) v.findViewById(R.id.person_descrip);
		follower_count = (MyTextView) v.findViewById(R.id.followers_count);
		following_count = (MyTextView) v.findViewById(R.id.following_count);
		lv = (ListView) v.findViewById(R.id.list);
		edit_profile = (ImageView) v.findViewById(R.id.editprofile_icon);
		edit_settings = (ImageView) v.findViewById(R.id.edit_settings_icon);
		search_edit_text = (EditText) v.findViewById(R.id.search_edit_text);
		searchbar = (FrameLayout) v.findViewById(R.id.search_bar);
		
		searchbar.setVisibility(View.GONE);
		
		newsfeed_adapter = new MyProfileNewsfeedAdapter(getActivity());
		
		rowItems = new ArrayList<NewsfeedData>();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
		}
		currentList = 0;
		sellingTask = new FetchMyPosts(getActivity(),lv,rowItems,newsfeed_adapter); 
		sellingTask.execute(new MyConstants().mypostview_url);
			
		LinearLayout following = (LinearLayout) v.findViewById(R.id.following);
		following.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//find_friend_layout.setVisibility(View.VISIBLE);
				searchbar.setVisibility(View.VISIBLE);
				follow_follower = 0;
				if(currentList!=1){
					stopAsyncTasks();
					lv.setAdapter(null);
					followingList = new ArrayList<FollowersData>();
					followingTask = new FetchFollow(getActivity(),lv,followingList,"following");
					followingTask.execute(new MyConstants().myfollowings_url);		
					currentList = 1;
				}
			}
		});
		
		LinearLayout followers = (LinearLayout) v.findViewById(R.id.followers);
		followers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//find_friend_layout.setVisibility(View.VISIBLE);
				searchbar.setVisibility(View.VISIBLE);
				follow_follower = 1;
				if(currentList!=2){
					stopAsyncTasks();
					lv.setAdapter(null);
					followerList = new ArrayList<FollowersData>();
					followerTask = new FetchFollow(getActivity(),lv,followerList,"follower");
					followerTask.execute(new MyConstants().myfollowers_url);
				//	Toast.makeText(getActivity(),"Followers",Toast.LENGTH_LONG).show();
					currentList = 2;
				}
			}
		});
		
		LinearLayout favorites = (LinearLayout) v.findViewById(R.id.favorites);
		favorites.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//find_friend_layout.setVisibility(View.GONE);
				follow_follower = -1;
				searchbar.setVisibility(View.GONE);
				if(currentList!=3){
					stopAsyncTasks();
					lv.setAdapter(null);
					rowItems = new ArrayList<NewsfeedData>();
					favoriteTask = new FetchMyPosts(getActivity(),lv,rowItems,newsfeed_adapter);
					favoriteTask.execute(new MyConstants().favpost_url);
					currentList = 3;
				}
			}
		});
		
		LinearLayout selling = (LinearLayout) v.findViewById(R.id.selling);
		selling.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//find_friend_layout.setVisibility(View.GONE);
				searchbar.setVisibility(View.GONE);
				follow_follower = -1;
				if(currentList!=0){
					stopAsyncTasks();
					lv.setAdapter(null);
					rowItems = new ArrayList<NewsfeedData>();
					sellingTask = new FetchMyPosts(getActivity(),lv,rowItems,newsfeed_adapter);
					sellingTask.execute(new MyConstants().mypostview_url);
					currentList = 0;
					Toast.makeText(getActivity(),"Selling",Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
		
		edit_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				myProfile = (MyProfileActivity) getActivity();
				
				ProfileEditFragment ls_fragment = new ProfileEditFragment();
				fragmentTransaction.replace(myProfile.ll, ls_fragment);
				fragmentTransaction.commit();
			}
		});
		
		edit_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				myProfile = (MyProfileActivity) getActivity();
				
				SettingsEditFragment ls_fragment = new SettingsEditFragment();
				fragmentTransaction.replace(myProfile.ll, ls_fragment);
				fragmentTransaction.commit();
				
			}
		});
		
		search_edit_text.addTextChangedListener(new TextWatcher() {
			
			@SuppressLint("DefaultLocale")
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				ArrayList<FollowersData> tempfollowerList=new ArrayList<FollowersData>();
				ArrayList<FollowersData> tempfollowingList=new ArrayList<FollowersData>();
				
				if(follow_follower==0){
					
					for(int i=0;i<followingTask.followList.size();i++){
						String name = followingTask.followList.get(i).getName().toLowerCase();
						if(name.contains(s.toString().toLowerCase())){
							
							tempfollowingList.add(followingTask.followList.get(i));
						}
						
					}
					//Log.d("MyProfileFragment", s.toString());
					lv.setAdapter(new FollowerListAdapter(getActivity(), tempfollowingList,"following"));
					
				}
				else if(follow_follower==1){
					for(int i=0;i<followerTask.followList.size();i++){
						String name = followerTask.followList.get(i).getName().toLowerCase();
						if(name.contains(s.toString().toLowerCase())){
							
							tempfollowerList.add(followerTask.followList.get(i));
						}
						
					}
					//Log.d("MyProfileFragment", s.toString());
					lv.setAdapter(new FollowerListAdapter(getActivity(), tempfollowerList,"following"));
				}
				
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		fetchProfileInfo= new FetchProfileInfo(getActivity(),follower_count,following_count,person_city,person_descrip);
		fetchProfileInfo.execute(new MyConstants().profileview_url);
		
		return v;

	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("my_profile_fragment", "coming in onActivityResult");
		newsfeed_adapter.onActivityResult(requestCode, resultCode, data);
	}
	
	public void stopAsyncTasks(){
		if(sellingTask!=null)
			sellingTask.cancel(true);
		if(favoriteTask!=null)
			favoriteTask.cancel(true);
		if(followerTask!=null)
			followerTask.cancel(true);
		if(followingTask!=null)
			followingTask.cancel(true);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(MyProfileFragment.class.getName(),"Myprofile fragment destroyed");
		stopAsyncTasks();
		if(fetchProfileInfo!=null){
			fetchProfileInfo.cancel(true);
		}
	}
	
}

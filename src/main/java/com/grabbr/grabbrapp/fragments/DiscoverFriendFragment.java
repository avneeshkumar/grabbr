package com.grabbr.grabbrapp.fragments;


import java.util.ArrayList;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.MyProfileActivity;
import com.grabbr.grabbrapp.activities.NewsfeedActivity;
import com.grabbr.grabbrapp.daos.FollowersData;
import com.grabbr.grabbrapp.services.FetchFollow;
import com.grabbr.grabbrapp.services.SearchFriends;
import com.grabbr.grabbrapp.utils.MyConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

@SuppressLint("NewApi")
public class DiscoverFriendFragment extends Fragment {
	ImageButton backbutton;
	private ImageButton home_button;
	private ImageButton profile_button;
	private ImageButton cross_button;
	private ImageButton search_button;
	private EditText search_text;
	FetchFollow Suggest;
	SearchFriends Search;
	
	private ArrayList<FollowersData> followerList;
	ListView lv;
	Fragment f = this;
	@Override
	   public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.discover_friends, container, false);
		
		backbutton = (ImageButton) v.findViewById(R.id.back_icon);
		home_button = (ImageButton) v.findViewById(R.id.home_icon);
		profile_button = (ImageButton)v.findViewById(R.id.profile_icon);
		search_button = (ImageButton) v.findViewById(R.id.search_button);
		cross_button = (ImageButton) v.findViewById(R.id.cross_button);
		search_text = (EditText) v.findViewById(R.id.search_text);
		
		
		lv = (ListView) v.findViewById(R.id.list);
		
		
//		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
//		      Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(search_text.getWindowToken(), 0);
		
		backbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
				
			}
		});
		
		home_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), NewsfeedActivity.class);
        		startActivity(i);
        		getActivity().finish();
				
			}
		});
		profile_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), MyProfileActivity.class);
        		startActivity(i);
        		getActivity().finish();
				
			}
		});
		
		search_text.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.d("Search Text","onTextChanged");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				Log.d("Search Text","beforeTextChanged");
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.d("Search Text","afterTextChanged");			
			}
		});
		
		search_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = search_text.getText().toString();
				ArrayList<FollowersData> friendList = new ArrayList<FollowersData>();
				if(Suggest!=null){
					Suggest.cancel(true);
				}
				Search = new SearchFriends(getActivity(),lv,friendList);
				Search.execute(new MyConstants().search_friend_url,text);
			}
			
		});
	
		Suggest =  new FetchFollow(getActivity(),lv,followerList,"following");
		Suggest.execute(new MyConstants().suggestfollower_url);
		return v;
		
	} 
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(Suggest!=null){
			Suggest.cancel(true);
		}
		if(Search!=null){
			Search.cancel(true);
		}
	}

}

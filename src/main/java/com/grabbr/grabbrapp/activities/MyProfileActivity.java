package com.grabbr.grabbrapp.activities;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.fragments.ChatListFragment;
import com.grabbr.grabbrapp.fragments.MyProfileFragment;
import com.grabbr.grabbrapp.fragments.new_post_fragment_form_only;
import com.grabbr.grabbrapp.fragments.NotificationFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

@SuppressLint("ResourceAsColor")
public class MyProfileActivity extends ActionBarActivity {

	ImageButton home_button;
	ImageButton discover_button;
	int current_fragment=0;
	MyTextView profile_button;
	MyTextView sell_now_button;
	MyTextView notification_button;
	MyTextView inbox_button;
	MyProfileFragment myprofile_f;
	public int ll;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile);
		if(getActionBar()!=null){
			getActionBar().hide();
		}
		if (getSupportActionBar() != null) {
	        getSupportActionBar().hide();
	    }
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		}
		profile_button = (MyTextView) findViewById(R.id.profile);
		sell_now_button = (MyTextView) findViewById(R.id.sell_now);
		notification_button = (MyTextView) findViewById(R.id.notifications);
		inbox_button = (MyTextView) findViewById(R.id.inbox);
		
		FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
         
        myprofile_f = new MyProfileFragment();
        ll = R.id.layoutToReplace;
        ft.add(R.id.layoutToReplace, myprofile_f);
        ft.commit();
        current_fragment=0;
        
 
		profile_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(current_fragment!=0){
					FragmentManager fm = getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			         
			        myprofile_f = new MyProfileFragment();
			        ft.replace(R.id.layoutToReplace, myprofile_f);
			        ft.commit();
			        current_fragment=0;
			        profile_button.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
			        profile_button.setTextColor(getResources().getColor(R.color.appblue));
			        sell_now_button.setTextColor(getResources().getColor(android.R.color.white));
			        sell_now_button.setBackgroundResource(R.color.appblue);
			        notification_button.setTextColor(getResources().getColor(android.R.color.white));
			        notification_button.setBackgroundResource(R.color.appblue);
			        inbox_button.setTextColor(getResources().getColor(android.R.color.white));
			        inbox_button.setBackgroundResource(R.color.appblue);
			        
			        
				}
				
				
			}
		});
		sell_now_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(current_fragment!=1){
					FragmentManager fm = getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			         
			        new_post_fragment_form_only llf = new new_post_fragment_form_only();
			        ft.replace(R.id.layoutToReplace, llf);
			        ft.commit();
			        current_fragment=1;
			        sell_now_button.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
			        sell_now_button.setTextColor(getResources().getColor(R.color.appblue));
			        profile_button.setTextColor(getResources().getColor(android.R.color.white));
			        profile_button.setBackgroundResource(R.color.appblue);
			        notification_button.setTextColor(getResources().getColor(android.R.color.white));
			        notification_button.setBackgroundResource(R.color.appblue);
			        inbox_button.setTextColor(getResources().getColor(android.R.color.white));
			        inbox_button.setBackgroundResource(R.color.appblue);
				}
				
				
			}
		});
		
		notification_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(current_fragment!=2){
					FragmentManager fm = getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			         
			        NotificationFragment llf = new NotificationFragment();
			        ft.replace(R.id.layoutToReplace, llf);
			        ft.commit();
			        current_fragment=2;
			        notification_button.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
			        notification_button.setTextColor(getResources().getColor(R.color.appblue));
			        profile_button.setTextColor(getResources().getColor(android.R.color.white));
			        profile_button.setBackgroundResource(R.color.appblue);
			        sell_now_button.setTextColor(getResources().getColor(android.R.color.white));
			        sell_now_button.setBackgroundResource(R.color.appblue);
			        inbox_button.setTextColor(getResources().getColor(android.R.color.white));
			        inbox_button.setBackgroundResource(R.color.appblue);
				}
				
				
			}
		});
		inbox_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(current_fragment!=3){
					FragmentManager fm = getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			         
			        ChatListFragment llf = new ChatListFragment();
			        ft.replace(R.id.layoutToReplace, llf);
			        ft.commit();
			        current_fragment=3;
			        inbox_button.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
			        inbox_button.setTextColor(getResources().getColor(R.color.appblue));
			        profile_button.setTextColor(getResources().getColor(android.R.color.white));
			        profile_button.setBackgroundResource(R.color.appblue);
			        sell_now_button.setTextColor(getResources().getColor(android.R.color.white));
			        sell_now_button.setBackgroundResource(R.color.appblue);
			        notification_button.setTextColor(getResources().getColor(android.R.color.white));
			        notification_button.setBackgroundResource(R.color.appblue);
				}
				
				
			}
		});
		
		
		
		home_button = (ImageButton) findViewById(R.id.home_icon);
		discover_button = (ImageButton) findViewById(R.id.discover_icon);
		home_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MyProfileActivity.this, NewsfeedActivity.class);
        		startActivity(i);
        		finish();
				
			}
		});
		
		discover_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MyProfileActivity.this, DiscoverActivity.class);
        		startActivity(i);
        		finish();
				
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("my_profile_fragment", "coming in onActivityResult");
		myprofile_f.onActivityResult(requestCode, resultCode, data);
	}

	
}

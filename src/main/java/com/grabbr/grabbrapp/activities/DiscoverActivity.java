package com.grabbr.grabbrapp.activities;

import java.util.ArrayList;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.customviews.DiscoverGridviewAdapter;
import com.grabbr.grabbrapp.daos.DiscoverData;
import com.grabbr.grabbrapp.fragments.DiscoverFriendFragment;
import com.grabbr.grabbrapp.fragments.NewPostDiscoverfragment;
import com.grabbr.grabbrapp.services.FetchDiscover;
import com.grabbr.grabbrapp.utils.MyConstants;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class DiscoverActivity extends ActionBarActivity {
	
	
	private ImageButton home_button;
	private ImageButton profile_button;
	ImageButton createnew_post;
	private Uri imageuri;
	public NewPostDiscoverfragment ls_fragment;
	int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=100;
	MyTextView forhim_tv;
	MyTextView forher_tv;
	MyTextView books_tv;
	MyTextView electronics_tv;
	MyTextView household_tv;
	MyTextView miscellaneous_tv;
	ImageButton discover_friends_button;
	int currenttab =2;
	GridView gridView;
	ArrayList<DiscoverData> discoverlist;
	DiscoverGridviewAdapter adapter;
	FetchDiscover fetchDiscover;
	ImageButton searchbutton;
	private EditText search_text;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover);
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
		home_button = (ImageButton) findViewById(R.id.home_icon);
		profile_button = (ImageButton)findViewById(R.id.profile_icon);
		createnew_post = (ImageButton) findViewById(R.id.create_post);
		gridView=(GridView)findViewById(R.id.sale_grids);
		discover_friends_button = (ImageButton) findViewById(R.id.people_icon);
		adapter = new DiscoverGridviewAdapter(DiscoverActivity.this);
		discoverlist = new ArrayList<DiscoverData>();
		searchbutton = (ImageButton) findViewById(R.id.search_button);
		fetchDiscover = new FetchDiscover(this, gridView, discoverlist,adapter);
		
		fetchDiscover.execute(new MyConstants().discover_url,"for her","");
		
		search_text = (EditText) findViewById(R.id.search_text);
		
		home_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DiscoverActivity.this, NewsfeedActivity.class);
        		startActivity(i);
        		finish();
				
			}
		});
		profile_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DiscoverActivity.this, MyProfileActivity.class);
        		startActivity(i);
        		finish();
				
			}
		});
		createnew_post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				long unixTime = System.currentTimeMillis();
				String fileName = ""+unixTime;
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, fileName);
				values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
				imageuri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				
			}
		});
		
		discover_friends_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				DiscoverFriendFragment ls_fragment = new DiscoverFriendFragment();
				fragmentTransaction.add(android.R.id.content, ls_fragment);
				fragmentTransaction.commit();
			}
		});
		
		
		searchbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = search_text.getText().toString();
				if(!text.equals("")){
					String searchType = "";
					if(currenttab==1){
						searchType = "for him";
					}
					if(currenttab==2){
						searchType = "for her";
					}
					if(currenttab==3){
						searchType = "electronics";
					}
					if(currenttab==4){
						searchType = "books";
					}
					if(currenttab==5){
						searchType = "household";
					}
					if(currenttab==6){
						searchType = "miscellaneous";
					}
					gridView.setAdapter(null);
					discoverlist = new ArrayList<DiscoverData>();
					
					if(fetchDiscover!=null){
						fetchDiscover.cancel(true);
					}
					fetchDiscover = new FetchDiscover(DiscoverActivity.this, gridView, discoverlist,adapter);
					
					fetchDiscover.execute(new MyConstants().discover_url,searchType,text);
				}
			}
		});
		
		/*************************TABS onlcikc listener***************************************************/
		forher_tv = (MyTextView) findViewById(R.id.forher);
		forhim_tv = (MyTextView) findViewById(R.id.forhim);
		books_tv = (MyTextView) findViewById(R.id.books);
		electronics_tv = (MyTextView) findViewById(R.id.electronics);
		household_tv = (MyTextView) findViewById(R.id.households);
		miscellaneous_tv = (MyTextView) findViewById(R.id.miscellaneous);
		
		forhim_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(currenttab!=1){
					
					forhim_tv.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
					forhim_tv.setTextColor(getResources().getColor(R.color.appblue));
					forher_tv.setBackgroundResource(R.color.appblue);
					forher_tv.setTextColor(getResources().getColor(android.R.color.white));
					books_tv.setBackgroundResource(R.color.appblue);
					books_tv.setTextColor(getResources().getColor(android.R.color.white));
					electronics_tv.setBackgroundResource(R.color.appblue);
					electronics_tv.setTextColor(getResources().getColor(android.R.color.white));
					household_tv.setBackgroundResource(R.color.appblue);
					household_tv.setTextColor(getResources().getColor(android.R.color.white));
					miscellaneous_tv.setBackgroundResource(R.color.appblue);
					miscellaneous_tv.setTextColor(getResources().getColor(android.R.color.white));
					forher_tv.setPadding(32, 0, 32, 0);
					forhim_tv.setPadding(32, 0, 32, 0);
					books_tv.setPadding(32, 0, 32, 0);
					household_tv.setPadding(32, 0, 32, 0);
					electronics_tv.setPadding(32, 0, 32, 0);
					miscellaneous_tv.setPadding(32, 0, 32, 0);
					
					adapter.clear();
					gridView.setAdapter(null);
					discoverlist = new ArrayList<DiscoverData>();
					
					if(fetchDiscover!=null){
						fetchDiscover.cancel(true);
					}
					
					fetchDiscover = new FetchDiscover(DiscoverActivity.this, gridView, discoverlist,adapter);
					
					fetchDiscover.execute(new MyConstants().discover_url,"for him","");
					
					
					
					
					
				}
				currenttab = 1;
				
			}
		});
		forher_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(currenttab!=2){
					forher_tv.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
					forher_tv.setTextColor(getResources().getColor(R.color.appblue));
					forhim_tv.setBackgroundResource(R.color.appblue);
					forhim_tv.setTextColor(getResources().getColor(android.R.color.white));
					books_tv.setBackgroundResource(R.color.appblue);
					books_tv.setTextColor(getResources().getColor(android.R.color.white));
					electronics_tv.setBackgroundResource(R.color.appblue);
					electronics_tv.setTextColor(getResources().getColor(android.R.color.white));
					household_tv.setBackgroundResource(R.color.appblue);
					household_tv.setTextColor(getResources().getColor(android.R.color.white));
					miscellaneous_tv.setBackgroundResource(R.color.appblue);
					miscellaneous_tv.setTextColor(getResources().getColor(android.R.color.white));
					forher_tv.setPadding(32, 0, 32, 0);
					forhim_tv.setPadding(32, 0, 32, 0);
					books_tv.setPadding(32, 0, 32, 0);
					household_tv.setPadding(32, 0, 32, 0);
					electronics_tv.setPadding(32, 0, 32, 0);
					miscellaneous_tv.setPadding(32, 0, 32, 0);
					
					adapter.clear();
					
					if(fetchDiscover!=null){
						fetchDiscover.cancel(true);
					}
					
					discoverlist = new ArrayList<DiscoverData>();
					fetchDiscover = new FetchDiscover(DiscoverActivity.this, gridView, discoverlist,adapter);
					
					fetchDiscover.execute(new MyConstants().discover_url,"for her","");
				}
				currenttab = 2;
				
			}
		});
		
		electronics_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(currenttab!=3){
					electronics_tv.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
					electronics_tv.setTextColor(getResources().getColor(R.color.appblue));
					forher_tv.setBackgroundResource(R.color.appblue);
					forher_tv.setTextColor(getResources().getColor(android.R.color.white));
					books_tv.setBackgroundResource(R.color.appblue);
					books_tv.setTextColor(getResources().getColor(android.R.color.white));
					forhim_tv.setBackgroundResource(R.color.appblue);
					forhim_tv.setTextColor(getResources().getColor(android.R.color.white));
					household_tv.setBackgroundResource(R.color.appblue);
					household_tv.setTextColor(getResources().getColor(android.R.color.white));
					miscellaneous_tv.setBackgroundResource(R.color.appblue);
					miscellaneous_tv.setTextColor(getResources().getColor(android.R.color.white));
					forher_tv.setPadding(32, 0, 32, 0);
					forhim_tv.setPadding(32, 0, 32, 0);
					books_tv.setPadding(32, 0, 32, 0);
					household_tv.setPadding(32, 0, 32, 0);
					electronics_tv.setPadding(32, 0, 32, 0);
					miscellaneous_tv.setPadding(32, 0, 32, 0);
					
					if(fetchDiscover!=null){
						fetchDiscover.cancel(true);
					}
					
					adapter.clear();
					discoverlist = new ArrayList<DiscoverData>();
					fetchDiscover = new FetchDiscover(DiscoverActivity.this, gridView, discoverlist,adapter);
					
					fetchDiscover.execute(new MyConstants().discover_url,"electronics","");
				}
				currenttab = 3;
				
			}
		});
		
		books_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(currenttab!=4){
					books_tv.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
					books_tv.setTextColor(getResources().getColor(R.color.appblue));
					forher_tv.setBackgroundResource(R.color.appblue);
					forher_tv.setTextColor(getResources().getColor(android.R.color.white));
					forhim_tv.setBackgroundResource(R.color.appblue);
					forhim_tv.setTextColor(getResources().getColor(android.R.color.white));
					electronics_tv.setBackgroundResource(R.color.appblue);
					electronics_tv.setTextColor(getResources().getColor(android.R.color.white));
					household_tv.setBackgroundResource(R.color.appblue);
					household_tv.setTextColor(getResources().getColor(android.R.color.white));
					miscellaneous_tv.setBackgroundResource(R.color.appblue);
					miscellaneous_tv.setTextColor(getResources().getColor(android.R.color.white));
					
					
					forher_tv.setPadding(32, 0, 32, 0);
					forhim_tv.setPadding(32, 0, 32, 0);
					books_tv.setPadding(32, 0, 32, 0);
					household_tv.setPadding(32, 0, 32, 0);
					electronics_tv.setPadding(32, 0, 32, 0);
					miscellaneous_tv.setPadding(32, 0, 32, 0);
					adapter.clear();
					discoverlist = new ArrayList<DiscoverData>();
					
					if(fetchDiscover!=null){
						fetchDiscover.cancel(true);
					}
					
					fetchDiscover = new FetchDiscover(DiscoverActivity.this, gridView, discoverlist,adapter);
					
					fetchDiscover.execute(new MyConstants().discover_url,"books","");

				}
				currenttab=4;
				
			}
		});
		household_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				household_tv.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
				household_tv.setTextColor(getResources().getColor(R.color.appblue));
				forher_tv.setBackgroundResource(R.color.appblue);
				forher_tv.setTextColor(getResources().getColor(android.R.color.white));
				forhim_tv.setBackgroundResource(R.color.appblue);
				forhim_tv.setTextColor(getResources().getColor(android.R.color.white));
				electronics_tv.setBackgroundResource(R.color.appblue);
				electronics_tv.setTextColor(getResources().getColor(android.R.color.white));
				books_tv.setBackgroundResource(R.color.appblue);
				books_tv.setTextColor(getResources().getColor(android.R.color.white));
				miscellaneous_tv.setBackgroundResource(R.color.appblue);
				miscellaneous_tv.setTextColor(getResources().getColor(android.R.color.white));
				
				
				forher_tv.setPadding(32, 0, 32, 0);
				forhim_tv.setPadding(32, 0, 32, 0);
				books_tv.setPadding(32, 0, 32, 0);
				household_tv.setPadding(32, 0, 32, 0);
				electronics_tv.setPadding(32, 0, 32, 0);
				miscellaneous_tv.setPadding(32, 0, 32, 0);
				adapter.clear();
				
				if(fetchDiscover!=null){
					fetchDiscover.cancel(true);
				}
				
				discoverlist = new ArrayList<DiscoverData>();
				fetchDiscover = new FetchDiscover(DiscoverActivity.this, gridView, discoverlist,adapter);
				
				fetchDiscover.execute(new MyConstants().discover_url,"household","");
				
				currenttab=5;
			}
			
		});
		
		miscellaneous_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				miscellaneous_tv.setBackground(getResources().getDrawable(R.drawable.blue_tab_topdown));
				miscellaneous_tv.setTextColor(getResources().getColor(R.color.appblue));
				forher_tv.setBackgroundResource(R.color.appblue);
				forher_tv.setTextColor(getResources().getColor(android.R.color.white));
				forhim_tv.setBackgroundResource(R.color.appblue);
				forhim_tv.setTextColor(getResources().getColor(android.R.color.white));
				electronics_tv.setBackgroundResource(R.color.appblue);
				electronics_tv.setTextColor(getResources().getColor(android.R.color.white));
				household_tv.setBackgroundResource(R.color.appblue);
				household_tv.setTextColor(getResources().getColor(android.R.color.white));
				forhim_tv.setBackgroundResource(R.color.appblue);
				forhim_tv.setTextColor(getResources().getColor(android.R.color.white));
				forher_tv.setPadding(32, 0, 32, 0);
				forhim_tv.setPadding(32, 0, 32, 0);
				books_tv.setPadding(32, 0, 32, 0);
				household_tv.setPadding(32, 0, 32, 0);
				electronics_tv.setPadding(32, 0, 32, 0);
				miscellaneous_tv.setPadding(32, 0, 32, 0);
				adapter.clear();
				
				if(fetchDiscover!=null){
					fetchDiscover.cancel(true);
				}
				
				discoverlist = new ArrayList<DiscoverData>();
				fetchDiscover = new FetchDiscover(DiscoverActivity.this, gridView, discoverlist,adapter);
				
				fetchDiscover.execute(new MyConstants().discover_url,"miscellaneous","");
				
				currenttab=6;
			}
		});
		
		
		
		/*************************************************************************************************/
		
		
		
		
	}
	@SuppressLint({ "ShowToast", "NewApi" })
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
		    if (resultCode == Activity.RESULT_OK) {
		    	FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				ls_fragment = new NewPostDiscoverfragment();
				Bundle b = new Bundle();
				b.putString("uri", imageuri.toString());
				ls_fragment.setArguments(b);
				fragmentTransaction.add(android.R.id.content, ls_fragment);
				//fragmentTransaction.addToBackStack("newpost_fragment");
				//Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
				fragmentTransaction.commit();
		 
		    } else if (resultCode == Activity.RESULT_CANCELED) {
		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
		    } else {
		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
		    }
		}
		}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(fetchDiscover!=null){
			fetchDiscover.cancel(true);
		}
		super.onDestroy();
	}

	
}

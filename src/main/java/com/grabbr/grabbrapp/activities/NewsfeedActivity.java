package com.grabbr.grabbrapp.activities;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.fragments.NewsfeedFragment;
import com.grabbr.grabbrapp.fragments.NewPostFragment;
import com.grabbr.grabbrapp.fragments.NoInternetFragment;
import com.grabbr.grabbrapp.services.AppLocationService;
import com.grabbr.grabbrapp.services.MyAlarmReceiver;

import com.grabbr.grabbrapp.utils.CheckInternet;
import com.grabbr.grabbrapp.utils.Globalchatconnection;
import com.grabbr.grabbrapp.utils.MyConstants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


public class NewsfeedActivity extends ActionBarActivity {

	
	
	
	
	AppLocationService appLocationService;
	SharedPreferences sharedpreference;
	private ImageButton new_post_button;
	private Uri imageuri;
	int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=100;
	public NewPostFragment ls_fragment;
	ImageButton myprofile_button;
	ImageButton discover_button;
	NewsfeedFragment newsfeedFragment;
	
	
	
	public NoInternetFragment no_i_frag;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.newsfeed_main);
		
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
		

		
		
		 new_post_button = (ImageButton)findViewById(R.id.new_post);
		 myprofile_button = (ImageButton) findViewById(R.id.profile_icon);
		 discover_button = (ImageButton) findViewById(R.id.discover_icon);
		 sharedpreference = getSharedPreferences(new MyConstants().MyPREFERENCES, Context.MODE_PRIVATE);
		 //new Globalchatconnection(sharedpreference.getString(new MyConstants().id,""),sharedpreference.getString(new MyConstants().chatpassword,""), getApplicationContext()).execute();
		 //scheduleAlarm();
		CheckInternet ci = new CheckInternet();
		if(!ci.isConnectingToInternet(getApplicationContext())){
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			no_i_frag = new NoInternetFragment();
			fragmentTransaction.replace(android.R.id.content, no_i_frag);
			Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
			fragmentTransaction.commit();
		}
		else{
			if(AppLocationService.locationsent==0){
				AppLocationService appLocationService = new AppLocationService(getApplicationContext(),this,sharedpreference);
				appLocationService.getLocation();
			}
			
		}
		
		
		new_post_button.setOnClickListener(new View.OnClickListener() {
			
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
		
		myprofile_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NewsfeedActivity.this, MyProfileActivity.class);
        		startActivity(i);
        		finish();
				
			}
		});
		discover_button.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NewsfeedActivity.this, DiscoverActivity.class);
        		startActivity(i);
        		finish();
				
			}
		});
		
		
		
		FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
         
        newsfeedFragment = new NewsfeedFragment();
        ft.add(R.id.layoutToReplace, newsfeedFragment);
        ft.commit();
		
		
		
		
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    //getActionBar().hide();
        
		
	}
	
	protected void onResume(){
		super.onResume();
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		        //All location services are disabled
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Enable Location Services");  // GPS not found
	        builder.setMessage("Please enable the location service as this application is purely location based."); // Want to enable?
	        builder.setPositiveButton("Go To settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialogInterface, int i) {
	                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	            }
	        });
	        builder.setNegativeButton("No", null);
	        builder.create().show();
		}
		else{
			if(AppLocationService.locationsent==0){
				AppLocationService appLocationService = new AppLocationService(getApplicationContext(),this,sharedpreference);
				appLocationService.getLocation();
			}
		}
		
	}
	
		
	protected void onDestroy(){
		super.onDestroy();
		
	}
	
	public void scheduleAlarm() {
	    // Construct an intent that will execute the AlarmReceiver
	    Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
	    // Create a PendingIntent to be triggered when the alarm goes off
	    final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
	        intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    // Setup periodic alarm every 5 seconds
	    long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
	    int intervalMillis = 5000*60; // 5 seconds
	    AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
	  }
	
	@SuppressLint({ "ShowToast", "NewApi" })
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
		    if (resultCode == Activity.RESULT_OK) {
		        //use imageUri here to access the image
		    	//iv1.setImageURI(imageuri);
		    	FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				ls_fragment = new NewPostFragment();
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
	 
}

package com.grabbr.grabbrapp.activities;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.fragments.FacebookLoginFragment;
import com.grabbr.grabbrapp.fragments.NoInternetFragment;
import com.grabbr.grabbrapp.services.AppLocationService;
import com.grabbr.grabbrapp.services.SignIn;
import com.grabbr.grabbrapp.utils.CheckInternet;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	public EditText mUsername;
	public EditText mPassword;
	protected MyTextView mSignUpButton;
	public NoInternetFragment ls_fragment;
	private static final String TAG = "MainActivity";
	private FacebookLoginFragment mainFragment;
	MyConstants c = new MyConstants();
	protected MyTextView signin_button;
	LoginButton  facebook_signin_button;
	SharedPreferences sharedpreferences;
	
	private UiLifecycleHelper uiHelper;
	Session session;
	MyUtils utility = new MyUtils();
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		// hiding Keyboard method done
		//ActionBar actionbar = getActionBar();
		//actionbar.hide();
		 uiHelper = new UiLifecycleHelper(this, callback);
		 uiHelper.onCreate(savedInstanceState);
		sharedpreferences = getSharedPreferences(c.MyPREFERENCES, Context.MODE_PRIVATE);
		mUsername = (EditText) findViewById(R.id.username_edit);
		mPassword = (EditText) findViewById(R.id.password_edit);
		signin_button = (MyTextView)findViewById(R.id.login_btn);
		facebook_signin_button = (LoginButton)findViewById(R.id.facebook_btn);
		facebook_signin_button.setReadPermissions(Arrays.asList("public_profile", "email"));
		
		mSignUpButton = (MyTextView) findViewById(R.id.create_account_btn);
		if ((sharedpreferences.contains(c.username)&&sharedpreferences.contains(c.id)&&sharedpreferences.contains(c.password))||(sharedpreferences.contains(c.username)&&sharedpreferences.contains(c.fb_id)&&sharedpreferences.contains(c.fb_token)&&sharedpreferences.contains(c.isfb)&&sharedpreferences.contains(c.id)))
	      {
			//System.out.println(sharedpreferences.getString(c.isverified,""));
			if(sharedpreferences.getString(c.isverified,"").equals("0")){
				Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
			else{
				Intent intent = new Intent(getApplicationContext(), NewsfeedActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
				
			}

	      }
		
		
		
		mSignUpButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
				startActivity(intent);
				
			}
		});
		
		signin_button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				//System.out.print(c.login_url);
				new SignIn(getApplicationContext(),sharedpreferences,MainActivity.this).execute(c.login_url,username,password);
				
			}
		});
		
//		facebook_signin_button.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				
//				
//			}
//		});
		
		
		

		
	}
	@Override
	protected void onResume() {
	  super.onResume();

	  // Logs 'install' and 'app activate' App Events.
	  AppEventsLogger.activateApp(this);
	  uiHelper.onResume();
	  
	  session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    
	    utility.getUserData(session, session.getState(),getApplicationContext(),sharedpreferences,this);
	    
	    uiHelper.onResume();
	    CheckInternet ci = new CheckInternet();
		if(!ci.isConnectingToInternet(getApplicationContext())){
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			ls_fragment = new NoInternetFragment();
			fragmentTransaction.replace(android.R.id.content, ls_fragment);
			Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
			fragmentTransaction.commit();
		}
		
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
	}
	
	@Override
	protected void onPause() {
	  super.onPause();

	  // Logs 'app deactivate' App Event.
	  AppEventsLogger.deactivateApp(this);
	  uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
/******************facebook functions******************************************************/
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	    	
	    	mSignUpButton.setVisibility(View.GONE);
	        Log.i(TAG, "Logged in...");
	        
	    } else if (state.isClosed()) {
	    	mSignUpButton.setVisibility(View.VISIBLE);
	        Log.i(TAG, "Logged out...");
	    }
	}
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	   
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	   
	}
	
	 public static void dumpIntent(Intent i){

		    Bundle bundle = i.getExtras();
		    if (bundle != null) {
		        Set<String> keys = bundle.keySet();
		        Iterator<String> it = keys.iterator();
		        Log.d("Facebook intent","Dumping Intent start");
		        while (it.hasNext()) {
		            String key = it.next();
		            Log.e("Facebook intent","[" + key + "=" + bundle.get(key)+"]");
		        }
		        Log.d("Facebook intent","Dumping Intent end");
		    }
		}
	 
	 public static void callFacebookLogout(Context context) {
		    Session session = Session.getActiveSession();
		    if (session != null) {

		        if (!session.isClosed()) {
		            session.closeAndClearTokenInformation();
		            //clear your preferences if saved
		        }
		    } else {

		        session = new Session(context);
		        Session.setActiveSession(session);

		        session.closeAndClearTokenInformation();
		            //clear your preferences if saved

		    }

		}
	 
	/**********************************************************************************************/
	
	
	
	
}

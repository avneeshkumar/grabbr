package com.grabbr.grabbrapp.activities;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.RoundedImageView;
import com.grabbr.grabbrapp.services.GetContacts;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class ConfirmationDoneActivity extends Activity {
	
	RoundedImageView iv1;
	ImageView tickview;
	TextView tv1;
	SharedPreferences sharedpreferences;
	MyConstants c = new MyConstants();

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation_done);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		
		tv1 = (TextView) findViewById(R.id.person_name);
		MyConstants c = new MyConstants();
		iv1 = (RoundedImageView) findViewById(R.id.profile_pic);
		tickview = (ImageView) findViewById(R.id.imageview2);
		System.out.println(iv1.getWidth());
		sharedpreferences = getSharedPreferences(c.MyPREFERENCES, Context.MODE_PRIVATE);
		tv1.setText(sharedpreferences.getString(c.name,""));
		Picasso.with(this)
        .load(c.profile_url+sharedpreferences.getString(c.id, "")+"/")
        .resize(100,100)
        .centerCrop()
        .into(iv1);
		
		if(sharedpreferences.getString(c.isfb,"").equals("0")){
			new GetContacts(ConfirmationDoneActivity.this).execute(c.send_contacts,"0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
		}
		else{
			new GetContacts(ConfirmationDoneActivity.this).execute(c.send_contacts,"1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
		}
		
		tickview.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), NewsfeedActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
				
			}
		});
			
			
	}
			

		// hiding the action bar
		//ActionBar actionbar = getActionBar();
		//actionbar.hide();
		// hiding the action bar done
	
	
	

	
}

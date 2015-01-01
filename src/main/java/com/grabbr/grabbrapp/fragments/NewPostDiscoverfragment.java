package com.grabbr.grabbrapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.entity.mime.content.FileBody;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.DiscoverActivity;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;
import com.grabbr.grabbrapp.services.SendPostDiscover;

@SuppressLint("NewApi")
public class NewPostDiscoverfragment extends Fragment {
	
	Fragment f = this;
	ImageView iv1;
	ImageView iv2;
	ImageView iv3;
	int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE1=100;
	int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE2=100;
	int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE3=100;
	Uri imageuri1;
	Uri imageuri2=null;
	Uri imageuri3=null;
	SharedPreferences sharedpreferences;
	MyTextView submit_button;
	EditText heading_et;
	EditText description_et;
	EditText rupees_et;
	Spinner category_spinner;
	MyTextView pub_button;
	MyTextView fol_button;
	String privacy=  "1";
	LinearLayout lo1;
	LinearLayout lo2;
	
	LinearLayout lo3;
	MyTextView back_button;
	
	MyUtils util=new MyUtils();
	MyConstants c = new MyConstants();
	@Override
	   public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.layout_5b, container, false);
		iv1 = (ImageView) v.findViewById(R.id.add_image1);
		
		iv2 = (ImageView) v.findViewById(R.id.add_image2);
		
		iv3 = (ImageView) v.findViewById(R.id.add_image3);
		
		lo1 = (LinearLayout) v.findViewById(R.id.linearlayout1);
		lo2 = (LinearLayout) v.findViewById(R.id.linearlayout2);
		lo3 = (LinearLayout) v.findViewById(R.id.linearlayout3);
		
		submit_button = (MyTextView) v.findViewById(R.id.submit_button);
		sharedpreferences = getActivity().getSharedPreferences(c.MyPREFERENCES, Context.MODE_PRIVATE);
		heading_et = (EditText) v.findViewById(R.id.headline);
		description_et = (EditText) v.findViewById(R.id.description);
		rupees_et = (EditText) v.findViewById(R.id.price);
		category_spinner = (Spinner) v.findViewById(R.id.spinner1);
		fol_button = (MyTextView) v.findViewById(R.id.follow_textview);
		pub_button = (MyTextView) v.findViewById(R.id.public_textview);
		back_button = (MyTextView) v.findViewById(R.id.back_tv);
		
		back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("new_post_fragment", "getting clicked");
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.remove(NewPostDiscoverfragment.this);
				fragmentTransaction.commit();
				
				
			}
		});
		pub_button.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ResourceAsColor")
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pub_button.setTextColor(getResources().getColor(android.R.color.white));
				pub_button.setBackgroundResource(R.color.appblue);
				fol_button.setTextColor(getResources().getColor(R.color.appblue));
				fol_button.setBackgroundResource(android.R.color.white);
				privacy = "1";
			}
		});

		fol_button.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ResourceAsColor")
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				fol_button.setTextColor(getResources().getColor(android.R.color.white));
				fol_button.setBackgroundResource(R.color.appblue);
				pub_button.setTextColor(getResources().getColor(R.color.appblue));
				pub_button.setBackgroundResource(android.R.color.white);
				privacy = "2";
				
			}
		});
		
		
		
		
		
	
		submit_button.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String heading = heading_et.getText().toString();
				Log.d("new_post_fragment",heading);
				String descrition = description_et.getText().toString();
				Log.d("new_post_fragment",descrition);
				String rupees = rupees_et.getText().toString();
				Log.d("new_post_fragment",rupees);
				String category = category_spinner.getSelectedItem().toString();
				
				if(category.equals("For her"))category ="1";
				else if(category.equals("For him"))category ="2";
				else if(category.equals("Books"))category ="3";
				else if(category.equals("Households"))category ="4";
				else if(category.equals("Electronics"))category ="5";
				else if(category.equals("Miscellaneous"))category ="6";
				Log.d("new_post_fragment",category);
				
				
				Log.d("new_post_fragment",privacy);
				//Log.d("new_post_fragment", heading+"  "+descrition+"	"+rupees+"	"+category+"	"+privacy);
				
				Bitmap bitmap1;
				Bitmap bitmap2;
				Bitmap bitmap3;
			
					
					if(imageuri2==null&&imageuri3==null){
						//bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageuri1);
						String realpath1 = util.getPath(getActivity(), imageuri1);
						bitmap1=util.get_Picture_bitmap(realpath1);
						FileBody bin=util.createfilebodyfrombmp(getActivity(), bitmap1);
						FileBody fb = util.createfilebodyfrombmp(getActivity(), bitmap1);
						
						if(sharedpreferences.getString(c.isfb,"").equals("0")){
							new SendPostDiscover((Fragment)NewPostDiscoverfragment.this,(DiscoverActivity)getActivity(), heading, descrition, rupees, category, privacy, fb).execute(c.sendpost_url,"0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
						}
						else{
							new SendPostDiscover((Fragment)NewPostDiscoverfragment.this,(DiscoverActivity)getActivity(), heading, descrition, rupees, category, privacy, fb).execute(c.sendpost_url,"1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
						}
					}
					else if(imageuri2==null&&imageuri3!=null){
						String realpath1 = util.getPath(getActivity(), imageuri1);
						bitmap1=util.get_Picture_bitmap(realpath1);
						FileBody fb1 = util.createfilebodyfrombmp(getActivity(), bitmap1);
						
						String realpath2 = util.getPath(getActivity(), imageuri3);
						bitmap2=util.get_Picture_bitmap(realpath2);
						
						FileBody fb2 = util.createfilebodyfrombmp(getActivity(), bitmap2);
						if(sharedpreferences.getString(c.isfb,"").equals("0")){
							new SendPostDiscover((Fragment)NewPostDiscoverfragment.this,(DiscoverActivity)getActivity(), heading, descrition, rupees, category, privacy, fb1,fb2).execute(c.sendpost_url,"0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
						}
						else{
							new SendPostDiscover((Fragment)NewPostDiscoverfragment.this,(DiscoverActivity)getActivity(), heading, descrition, rupees, category, privacy, fb1,fb2).execute(c.sendpost_url,"1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
						}
					}
					else if(imageuri2!=null&&imageuri3==null){
						String realpath1 = util.getPath(getActivity(), imageuri1);
						bitmap1=util.get_Picture_bitmap(realpath1);
						FileBody fb1 = util.createfilebodyfrombmp(getActivity(), bitmap1);
						
						String realpath2 = util.getPath(getActivity(), imageuri2);
						bitmap2=util.get_Picture_bitmap(realpath2);
						
						FileBody fb2 = util.createfilebodyfrombmp(getActivity(), bitmap2);
						if(sharedpreferences.getString(c.isfb,"").equals("0")){
							new SendPostDiscover((Fragment)NewPostDiscoverfragment.this,(DiscoverActivity)getActivity(), heading, descrition, rupees, category, privacy, fb1,fb2).execute(c.sendpost_url,"0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
						}
						else{
							new SendPostDiscover((Fragment)NewPostDiscoverfragment.this,(DiscoverActivity)getActivity(), heading, descrition, rupees, category, privacy, fb1,fb2).execute(c.sendpost_url,"1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
						}
					}
					
					else{
						String realpath1 = util.getPath(getActivity(), imageuri1);
						bitmap1=util.get_Picture_bitmap(realpath1);
						FileBody fb1 = util.createfilebodyfrombmp(getActivity(), bitmap1);
						
						String realpath2 = util.getPath(getActivity(), imageuri2);
						bitmap2=util.get_Picture_bitmap(realpath2);
						
						FileBody fb2 = util.createfilebodyfrombmp(getActivity(), bitmap2);

						String realpath3 = util.getPath(getActivity(), imageuri3);
						bitmap3=util.get_Picture_bitmap(realpath3);
						
						FileBody fb3 = util.createfilebodyfrombmp(getActivity(), bitmap3);
						if(sharedpreferences.getString(c.isfb,"").equals("0")){
							new SendPostDiscover((Fragment)NewPostDiscoverfragment.this,(DiscoverActivity)getActivity(), heading, descrition, rupees, category, privacy, fb1,fb2,fb3).execute(c.sendpost_url,"0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
						}
						else{
							new SendPostDiscover((Fragment)NewPostDiscoverfragment.this,(DiscoverActivity)getActivity(), heading, descrition, rupees, category, privacy, fb1,fb2,fb3).execute(c.sendpost_url,"1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
						}
								
					}
					//new sendpost((NewsfeedActivity)getActivity(), heading, descrition, rupees, category, privacy, fb).execute(params);
					
			}
		});
		
iv1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				long unixTime = System.currentTimeMillis();
				String fileName = ""+unixTime;
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, fileName);
				values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
				imageuri1 = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri1);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE1);
				Log.d("Image URI1",imageuri1.toString());
			}
		});	
		
iv2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				long unixTime = System.currentTimeMillis();
//				String fileName = ""+unixTime;
//				ContentValues values = new ContentValues();
//				values.put(MediaStore.Images.Media.TITLE, fileName);
//				values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
//				imageuri2 = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri2);
//				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE2);
//				Log.d("Image URI2",imageuri2.toString());
				Toast.makeText(getActivity(), "Coming in future Soon..", Toast.LENGTH_SHORT).show();
			}
		});
		
		iv3.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				long unixTime = System.currentTimeMillis();
//				String fileName = ""+unixTime;
//				ContentValues values = new ContentValues();
//				values.put(MediaStore.Images.Media.TITLE, fileName);
//				values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
//				imageuri3 = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri3);
//				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE3);
//				Log.d("Image URI3",imageuri3.toString());
				Toast.makeText(getActivity(), "Coming in future Soon..", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		
		Bundle bundle = this.getArguments();
		if(bundle != null){
		    String temp= bundle.getString("uri");
		    imageuri1 = Uri.parse(temp);
		    iv1.setImageURI(imageuri1);
			lo1.setVisibility(View.GONE);
		}
		
		return v;

	}
	
	
	
	@SuppressLint("ShowToast")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE1) {
		    if (resultCode == Activity.RESULT_OK) {
		        //use imageUri here to access the image
		    	iv1.setImageURI(imageuri1);
		    	lo1.setVisibility(View.GONE);
		    	CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE1=100;
		    	
		 
		    } else if (resultCode == Activity.RESULT_CANCELED) {
		        Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT);
		    } else {
		        Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT);
		    }
		}
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE2) {
		    if (resultCode == Activity.RESULT_OK) {
		        //use imageUri here to access the image
		    	iv2.setImageURI(imageuri2);
		    	lo2.setVisibility(View.GONE);
		    	CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE2=100;

		    	
		 
		    } else if (resultCode == Activity.RESULT_CANCELED) {
		        Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT);
		    } else {
		        Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT);
		    }
		}
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE3){
			 if (resultCode == Activity.RESULT_OK) {
			        //use imageUri here to access the image
			    	iv3.setImageURI(imageuri3);
			    	lo3.setVisibility(View.GONE);
			    	CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE3=100;
			 
			    } else if (resultCode == Activity.RESULT_CANCELED) {
			        Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT);
			    } else {
			        Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT);
			    }
		}
		
		}
	
	
	
}

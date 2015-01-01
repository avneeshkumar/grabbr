package com.grabbr.grabbrapp.fragments;


import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.MyProfileActivity;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.services.SendNewProfile;
import com.grabbr.grabbrapp.utils.MyConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ProfileEditFragment extends Fragment {
	
	Fragment f = this;
	ImageButton back_button;
	EditText full_name;
	EditText city;
	EditText aboutme;
	MyTextView done;
	SharedPreferences sharedPreferences;
	@Override
	   public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.profile_edit_view, container, false);
		back_button = (ImageButton) v.findViewById(R.id.back_icon);
		full_name = (EditText) v.findViewById(R.id.full_name);
		city = (EditText) v.findViewById(R.id.city);
		aboutme = (EditText) v.findViewById(R.id.aboutme);
		done = (MyTextView) v.findViewById(R.id.done);
		sharedPreferences = getActivity().getSharedPreferences(new MyConstants().MyPREFERENCES, Context.MODE_PRIVATE);
		full_name.setText(sharedPreferences.getString(new MyConstants().name, ""));
		back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				MyProfileActivity myProfile = (MyProfileActivity) getActivity();
				
				MyProfileFragment ls_fragment = new MyProfileFragment();
				fragmentTransaction.replace(myProfile.ll, ls_fragment);
				fragmentTransaction.commit();
			}
		});
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String name = full_name.getText().toString();
				String city_text = city.getText().toString();
				String desc = aboutme.getText().toString();
				MyConstants c = new MyConstants();
				if(sharedPreferences.getString(c.isfb,"").equals("0")){
		        	Log.d("NewsfeedActivity", "Sending location");
					new SendNewProfile(name,city_text,desc).execute(c.profile_update_url,"0",sharedPreferences.getString(c.username,""),sharedPreferences.getString(c.password,""));
					
				}
				else{
					Log.d("NewsfeedActivity", "Sending location");
					new SendNewProfile(name,city_text,desc).execute(c.profile_update_url,"1",sharedPreferences.getString(c.fb_id,""),sharedPreferences.getString(c.fb_token,""),sharedPreferences.getString(c.username,""));
				}
				
				Toast.makeText(getActivity(), "Information Updated", Toast.LENGTH_LONG).show();
				Editor editor = sharedPreferences.edit();
				editor.putString(c.name, name);
				editor.commit();
				done.setVisibility(View.INVISIBLE);
			}
			
		});
		return v;
		
	} 

}

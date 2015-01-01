package com.grabbr.grabbrapp.fragments;


import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.MainActivity;
import com.grabbr.grabbrapp.activities.MyProfileActivity;
import com.grabbr.grabbrapp.utils.MyConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInstaller.Session;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class SettingsEditFragment extends Fragment {
	
	Fragment f = this;
	ImageButton back_button;
	TextView notification_settings;
	int notification_toggle=0;
	TextView change_password_settings;
	int password_toggle=0;
	TextView deactivate_accounts_settings;
	int deactivate_account_toggle;
	TextView report_problems;
	int report_toggle;
	TextView signout;
	
	
	@Override
	   public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.setting_fragment, container, false);
		back_button = (ImageButton) v.findViewById(R.id.back_icon);
		notification_settings= (TextView) v.findViewById(R.id.notification_setting);
		change_password_settings = (TextView) v.findViewById(R.id.changepasswd);
//		deactivate_accounts_settings = (TextView) v.findViewById(R.id.deactivateaccount);
		report_problems = (TextView) v.findViewById(R.id.reportproblem);
		signout = (TextView) v.findViewById(R.id.signout);
		
		final LinearLayout notification_ll = (LinearLayout) v.findViewById(R.id.notification_ll);
		notification_ll.setVisibility(View.GONE);
		final LinearLayout changepassword_ll = (LinearLayout) v.findViewById(R.id.changepassword_ll);
		changepassword_ll.setVisibility(View.GONE);
		final LinearLayout reportproblem_ll = (LinearLayout) v.findViewById(R.id.reportproblem_ll);
		reportproblem_ll.setVisibility(View.GONE);
		
		/***********************Back Button********************************************************/
		
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
		/*******************************Notification******************************************************/
		
		notification_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				if(notification_toggle==0){
					notification_ll.setVisibility(View.VISIBLE);
//					LinearLayout parentll = (LinearLayout) v.findViewById(R.id.notification_parent_ll);
//					parentll.setBackgroundResource(R.color.appblue);
//					notification_settings.setTextColor(getResources().getColor(android.R.color.white));
					notification_toggle=1;
				}
				else{
					notification_ll.setVisibility(View.GONE);
//					LinearLayout parentll = (LinearLayout) v.findViewById(R.id.notification_parent_ll);
//					parentll.setBackgroundResource(R.color.textgrey);
//					notification_settings.setTextColor(getResources().getColor(R.color.appblue));
					notification_toggle=0;
				}
				
			}
		});
		
		change_password_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(password_toggle==0){
					changepassword_ll.setVisibility(View.VISIBLE);
//					LinearLayout parentll = (LinearLayout) v.findViewById(R.id.notification_parent_ll);
//					parentll.setBackgroundResource(R.color.appblue);
//					notification_settings.setTextColor(getResources().getColor(android.R.color.white));
					password_toggle=1;
				}
				else{
					changepassword_ll.setVisibility(View.GONE);
//					LinearLayout parentll = (LinearLayout) v.findViewById(R.id.notification_parent_ll);
//					parentll.setBackgroundResource(R.color.textgrey);
//					notification_settings.setTextColor(getResources().getColor(R.color.appblue));
					password_toggle=0;
				}
				
			}
				
			
		});
		
		report_problems.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(report_toggle==0){
					reportproblem_ll.setVisibility(View.VISIBLE);
//					LinearLayout parentll = (LinearLayout) v.findViewById(R.id.notification_parent_ll);
//					parentll.setBackgroundResource(R.color.appblue);
//					notification_settings.setTextColor(getResources().getColor(android.R.color.white));
					report_toggle=1;
				}
				else{
					reportproblem_ll.setVisibility(View.GONE);
//					LinearLayout parentll = (LinearLayout) v.findViewById(R.id.notification_parent_ll);
//					parentll.setBackgroundResource(R.color.textgrey);
//					notification_settings.setTextColor(getResources().getColor(R.color.appblue));
					report_toggle=0;
				}
				
			}
				
			
		});
		
		signout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sharedPreferences = getActivity().getSharedPreferences(new MyConstants().MyPREFERENCES, Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.clear();
				editor.commit();
				MainActivity.callFacebookLogout(getActivity());
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
				
				
			}
		});
		
		
		
		
		return v;
		
	} 

}

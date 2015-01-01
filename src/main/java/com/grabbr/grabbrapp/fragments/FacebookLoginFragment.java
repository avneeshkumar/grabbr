package com.grabbr.grabbrapp.fragments;

import com.grabbr.grabbrapp.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



@SuppressLint("NewApi")
public class FacebookLoginFragment extends Fragment {
	
	Fragment f = this;
	@Override
	   public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.no_internet, container, false);
		
		
		
		
		return v;
		
	} 

}

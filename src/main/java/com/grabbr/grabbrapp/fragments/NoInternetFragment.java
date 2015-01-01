package com.grabbr.grabbrapp.fragments;


import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.utils.CheckInternet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("NewApi")
public class NoInternetFragment extends Fragment {
	
	Fragment f = this;
	@Override
	   public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.no_internet, container, false);
		Button button = (Button) v.findViewById(R.id.button1);
			   button.setOnClickListener(new OnClickListener()
			   {
			 
			             public void onClick(View v)
			             {
			                // do something
			            	CheckInternet ci = new CheckInternet();
			         		if(ci.isConnectingToInternet(getActivity().getApplicationContext())){
			         			getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
			         			//Toast.makeText(getActivity(), "yes internet", Toast.LENGTH_SHORT).show();
			         		}
			         		
			             } 
			   }); 
		
		
		
		return v;
		
	} 

}

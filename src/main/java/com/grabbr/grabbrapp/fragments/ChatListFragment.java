package com.grabbr.grabbrapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.ExpandableHeightListView;
import com.grabbr.grabbrapp.customviews.ChatListAdapter;
import com.grabbr.grabbrapp.daos.ChatListData;
import com.grabbr.grabbrapp.services.FetchAllChats;
import com.grabbr.grabbrapp.utils.MyConstants;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class ChatListFragment extends Fragment {
	public  ChatListAdapter adapter;
	ArrayList<ChatListData> rowItems;
	
	 
	
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   	
       return inflater.inflate(R.layout.notification_list, null, false);
       
   }

   @SuppressLint("NewApi")
	@Override
   public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
       ExpandableHeightListView lv = (ExpandableHeightListView) getView().findViewById(R.id.list);
       adapter = new ChatListAdapter(getActivity());
       rowItems = new ArrayList<ChatListData>();
       new FetchAllChats(getActivity(),lv,rowItems).execute(new MyConstants().notification_url);
       
       if (android.os.Build.VERSION.SDK_INT > 9) {
           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
           StrictMode.setThreadPolicy(policy);
       }
      
   }
   
   


   
   
   
	
	
	
	
	
	
}

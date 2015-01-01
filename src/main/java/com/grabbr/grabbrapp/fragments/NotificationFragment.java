package com.grabbr.grabbrapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.ExpandableHeightListView;
import com.grabbr.grabbrapp.customviews.NotificationListAdapter;
import com.grabbr.grabbrapp.daos.NotificationData;
import com.grabbr.grabbrapp.services.FetchNotifications;
import com.grabbr.grabbrapp.utils.MyConstants;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class NotificationFragment extends Fragment {
	public  NotificationListAdapter adapter;
	ArrayList<NotificationData> rowItems;
	FetchNotifications fp;
	
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   	
       return inflater.inflate(R.layout.notification_list, null, false);
       
   }

   @SuppressLint("NewApi")
	@Override
   public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
       ExpandableHeightListView lv = (ExpandableHeightListView) getView().findViewById(R.id.list);
       adapter = new NotificationListAdapter(getActivity());
       rowItems = new ArrayList<NotificationData>();
       fp = new FetchNotifications(getActivity(),lv,rowItems);
       fp.execute(new MyConstants().notification_url);
       
       if (android.os.Build.VERSION.SDK_INT > 9) {
           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
           StrictMode.setThreadPolicy(policy);
       }
      
   }
   
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(NewsfeedFragment.class.getName(),"NewsFeed fragment destroyed");
		if(fp!=null){
			fp.cancel(true);
		}
	}
	
}

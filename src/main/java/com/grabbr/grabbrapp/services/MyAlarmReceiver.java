package com.grabbr.grabbrapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {
	public static final int REQUEST_CODE = 12345;
	  //public static final String ACTION = "com.codepath.example.servicesdemo.alarm";
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context, FetchOfflineMessage.class);
		
	   // i.putExtra("foo", "bar");
		
		System.out.println("coming in Myalarmreceiver");
	    context.startService(i);
		
	}

}

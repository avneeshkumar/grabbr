package com.grabbr.grabbrapp.activities;

import com.grabbr.grabbrapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FloatingIsSoldActivity extends Activity {
	Intent returnIntent;
	LinearLayout yes;
	LinearLayout no;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell_popup);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		params.y = (height*2)/10;
		returnIntent = new Intent();
		int position =getIntent().getIntExtra("position",0);
		returnIntent.putExtra("position", position);
		
		yes = (LinearLayout) findViewById(R.id.yes);
		no = (LinearLayout) findViewById(R.id.no);
		yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				returnIntent.putExtra("keypressed", "yes");
				Toast.makeText(getApplicationContext(), "Item Sold", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK,returnIntent);
				finish();
				
			}
		});
		no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				returnIntent.putExtra("keypressed", "no");
				
				setResult(RESULT_OK,returnIntent);
				finish();
				
			}
		});
		
		
	}

	
}

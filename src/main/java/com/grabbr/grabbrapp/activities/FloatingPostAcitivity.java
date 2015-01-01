package com.grabbr.grabbrapp.activities;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.services.FetchOnePost;
import com.grabbr.grabbrapp.utils.MyConstants;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatingPostAcitivity extends Activity {

	private View rootView;
	ImageView cross_button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_5a_postelement);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		params.height = height - dpToPx(112);
		cross_button = (ImageView) findViewById(R.id.cross_button);
		cross_button.setVisibility(View.VISIBLE);
		cross_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rootView = findViewById(android.R.id.content).getRootView();
		
		FetchOnePost fp = new FetchOnePost(FloatingPostAcitivity.this, rootView,getIntent().getStringExtra("position"));
		fp.execute(new MyConstants().fetchOnePost_url);
		
	}

	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}


	
}

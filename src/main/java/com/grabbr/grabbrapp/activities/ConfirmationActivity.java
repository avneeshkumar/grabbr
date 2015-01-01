package com.grabbr.grabbrapp.activities;


import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.services.Confimation;
import com.grabbr.grabbrapp.utils.MyConstants;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class ConfirmationActivity extends Activity {

	public ImageView resendImage;
	private EditText conformation_edittext;
	public ImageView doneImage;
	SharedPreferences sharedpreferences;
	MyConstants c = new MyConstants();

	// ////////////////////////////////////////////////////////////

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation);

		// hiding the action bar
		//ActionBar actionbar = getActionBar();
		//actionbar.hide();
		// hiding the action bar done

		// hiding Keyboard method
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		setupUI(findViewById(R.id.confirmationparent));
		conformation_edittext = (EditText) findViewById(R.id.conformation_edittext);
		sharedpreferences = getSharedPreferences(MyConstants.MyPREFERENCES, Context.MODE_PRIVATE);
		// hiding Keyboard method done

		resendButton();
		doneButton();

	}

	// ///////////////////////////////////////////////////////////

	private void doneButton() {
		doneImage = (ImageView) findViewById(R.id.confirmation_done_circle);
		doneImage.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("ShowToast")
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(),
				// R.string.signup_done_button_toast, Toast.LENGTH_SHORT)
				// .show();
				//System.out.println("hello");
				//Toast.makeText(getApplicationContext(), "Hello "+sharedpreferences.getString(c.username,""), Toast.LENGTH_SHORT).show();
				String code = conformation_edittext.getText().toString();
				new Confimation(getApplicationContext(), sharedpreferences, ConfirmationActivity.this).execute(c.verify_url,sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""),code);
				

			}
		});

	}

	private void resendButton() {
		resendImage = (ImageView) findViewById(R.id.confirmation_resend_circle);
		resendImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						R.string.confirmation_soon_you_will_get_message,
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	@SuppressLint("ClickableViewAccessibility")
	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(ConfirmationActivity.this);
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	
}

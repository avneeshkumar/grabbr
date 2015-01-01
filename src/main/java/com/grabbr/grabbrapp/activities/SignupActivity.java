package com.grabbr.grabbrapp.activities;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.entity.mime.content.FileBody;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.services.Register;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignupActivity extends Activity {

	protected ImageView mCancelButton;
	protected MyTextView mDoneButton;
	public EditText username_edit;
	public EditText email_edit;
	public EditText password_edit;
	public RelativeLayout AddImage;
	ImageView iv1;
	TextView tv1;
	Bitmap bm1;
	private static final int SELECT_PHOTO = 100;
	MyConstants c = new MyConstants();
	public Uri selectedImage;
	SharedPreferences sharedpreferences;
	Bitmap bm=null;
	MyUtils util;
	String imei;
	String realpath;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		// hiding the action bar
		//ActionBar actionbar = getActionBar();
		//actionbar.hide();
		// hiding the action bar done

		// hiding Keyboard method
		util = new MyUtils();
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		username_edit = (EditText)findViewById(R.id.signup_username_edittext);
		email_edit = (EditText)findViewById(R.id.signup_email_edittext);
		password_edit = (EditText)findViewById(R.id.signup_password_edittext);
		AddImage = (RelativeLayout)findViewById(R.id.add_image1);
		sharedpreferences = getSharedPreferences(c.MyPREFERENCES, Context.MODE_PRIVATE);
		iv1 = (ImageView) findViewById(R.id.plus_img);
		tv1 = (TextView) findViewById(R.id.addimg_tv);
		
		// hiding Keyboard method done

		cancelButton();
		DoneButton();
		addImageButton();

	}
	
	private void addImageButton(){
		AddImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//System.out.println("aaya");
				Toast.makeText(getApplicationContext(), "Hello toast!", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, SELECT_PHOTO);
			}
			
		});
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    //super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

	    switch(requestCode) { 
	    case SELECT_PHOTO:
	        if(resultCode == RESULT_OK){  
	            selectedImage = imageReturnedIntent.getData();
	            if(selectedImage!=null){
		            //realpath=util.getRealPathFromURI(selectedImage,getApplicationContext());
		            
		            InputStream imageStream;
					try {
						imageStream = getContentResolver().openInputStream(selectedImage);
						realpath = util.getPath(getApplicationContext(), selectedImage);
						
						bm = BitmapFactory.decodeStream(imageStream);
						bm1=util.getResizedBitmap(bm,AddImage.getHeight(),AddImage.getWidth());
						
						Bitmap circleBitmap = Bitmap.createBitmap(AddImage.getWidth(), AddImage.getHeight(), Bitmap.Config.ARGB_8888);
						BitmapShader shader = new BitmapShader (bm1,  TileMode.CLAMP, TileMode.CLAMP);
						Paint paint = new Paint();
						paint.setShader(shader);
	
						Canvas c = new Canvas(circleBitmap);
						c.drawCircle(AddImage.getWidth()/2, AddImage.getHeight()/2, AddImage.getWidth()/2, paint);
						Drawable d = new BitmapDrawable(getResources(),circleBitmap);
						AddImage.setBackground(d);
						iv1.setVisibility(View.GONE);
						tv1.setVisibility(View.GONE);
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            
	        	
	        }
	    }
	}
	
	
	
	
	private void DoneButton() {
		mDoneButton = (MyTextView) findViewById(R.id.done_btn);
		mDoneButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				String name = username_edit.getText().toString();
		        String password = password_edit.getText().toString();
		        String email = email_edit.getText().toString();
		        
				if(bm==null){
					Toast.makeText(SignupActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
				}
				
				else if(email.equals("")){
					Toast.makeText(SignupActivity.this, "Please select email", Toast.LENGTH_SHORT).show();
				}
				else if(password.equals("")){
					Toast.makeText(SignupActivity.this, "Please select password", Toast.LENGTH_SHORT).show();
					
				}
				else if(name.equals("")){
					Toast.makeText(SignupActivity.this, "Please select name", Toast.LENGTH_SHORT).show();
				}
				else{
				
					
					
					Bitmap bm2=util.get_Picture_bitmap(realpath.toString());
					FileBody bin=util.createfilebodyfrombmp(getApplicationContext(), bm2);
					//System.out.println(util.getPath(getApplicationContext(), selectedImage));
				
					//FileBody bin=util.createfilebodyfrombmp(getApplicationContext(), bm);
			        
			        String imei="999999";
			        new Register(getApplicationContext(),sharedpreferences,SignupActivity.this,bin).execute(c.register_url,email,name,password,imei);
				}
			}
		});
		

	}

	private void cancelButton() {
		mCancelButton = (ImageView) findViewById(R.id.previous);
		mCancelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();

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
					hideSoftKeyboard(SignupActivity.this);
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

package com.grabbr.grabbrapp.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.grabbr.grabbrapp.activities.ConfirmationDoneActivity;
import com.grabbr.grabbrapp.activities.MainActivity;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class RegisterFacebook extends AsyncTask<String, Void, HttpResponse >  {
	
	private ProgressDialog progress;
	private Context context;
	SharedPreferences sharedpreferences;
	public MainActivity activity;
	MyUtils myutils = new MyUtils();
	 HttpClient httpclient = new DefaultHttpClient();
	
	//FileBody bitmap;
	String isfb = "1";
	String email = "";
    String fb_id = "";
    String fb_token = "";
    String name = "";
    String device_id= "99999";
    private SecureRandom random = new SecureRandom();
    String password = new BigInteger(130, random).toString(32);
    MyConstants c = new MyConstants();
	
	public RegisterFacebook(Context con,SharedPreferences sp,MainActivity ac,String f_id,String f_token,String ifb,String mail){
		context=con;
		sharedpreferences = sp;
		activity = ac;
		//bitmap = bp;
		email = mail;
		fb_id = f_id;
		fb_token = f_token;
		
		progress = new ProgressDialog(ac);
	}
	
	
	@Override
    protected void onPreExecute() {
		progress.setMessage("Registering...Please Wait...");
		progress.setIndeterminate(true);
		progress.show();
    }
	
	@Override
	protected HttpResponse doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		
        
        HttpResponse response = null;
        try {
			@SuppressWarnings("deprecation")
			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("fb_token", new StringBody(fb_token)).addPart("fb_id", new StringBody(fb_id)).addPart("name",new StringBody(name)).addPart("isfb",new StringBody("1")).addPart("password",new StringBody(password)).addPart("device_id",new StringBody(device_id)).build();
			response = myutils.sendPost(arg0[0], reqEntity,httpclient);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
    protected void onPostExecute(HttpResponse result) {
		
		StatusLine statusLine = result.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		Log.d("register_facebook", ""+statusCode);
		InputStream in = null;
		try {
			in = result.getEntity().getContent();
		} catch (IllegalStateException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//progress.setProgress(100);
		if(statusCode==201){
			
	            // A Simple JSON Response Read
			JSONObject reader;
			
			try {
				
				BufferedReader reader1 = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
				String response = org.apache.commons.io.IOUtils.toString(reader1);
				
				//Log.d("register.java",response);
				reader = new JSONObject(response);
				String id;
				try {
					id = reader.getString("id");
					String name  = reader.getString("name");
					String chatpassword = reader.getString("chat_password");
					Editor editor = sharedpreferences.edit();
					editor.putString(c.username, email);
					editor.putString(c.isfb, "1");
					editor.putString(c.name, name);
					editor.putString(c.chatpassword, chatpassword);
					editor.putString(c.fb_token, fb_token);
					editor.putString(c.fb_id, fb_id);
					editor.putString(c.id, id);
					editor.putString(c.isverified, "1");
					editor.commit();
					Intent intent = new Intent(context, ConfirmationDoneActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					activity.finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	            
					
					
					
		            
				} 
	            // now you have the string representation of the HTML request
	            
	            

	        
			
		
		else if(statusCode==203){
			//progress.setProgress(100);
			Toast.makeText(context, "Invalid username password combination", Toast.LENGTH_SHORT).show();
		}
		else if(statusCode==200){
			//progress.setProgress(100);
			Log.d("register_facebook","Error code 200");
			Toast.makeText(context, "Email already exits", Toast.LENGTH_SHORT).show();
		}
		else{
			//progress.setProgress(100);
			Log.d("register_facebook","error code not found");
			Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
		}
		
		
		try {
			in.close();
			if (result.getEntity() != null) {
			    try {
			    	result.getEntity().consumeContent();
			    } catch (IOException e) {
			        Log.e("register.java", "Not able to consume", e);
			    }
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		progress.cancel();

    }
	
	
	
	

}

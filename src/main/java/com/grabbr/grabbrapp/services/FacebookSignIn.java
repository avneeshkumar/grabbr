package com.grabbr.grabbrapp.services;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.grabbr.grabbrapp.activities.MainActivity;
import com.grabbr.grabbrapp.activities.NewsfeedActivity;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class FacebookSignIn extends AsyncTask<String, Void, HttpResponse >  {
	
	private ProgressDialog progress;
	private Context context;
	SharedPreferences sharedpreferences;
	public MainActivity activity;
	MyUtils myutils = new MyUtils();
	String fb_id;
	String fb_token;
	String isfb;
	String email;
    MyConstants c = new MyConstants();
    HttpClient httpclient = new DefaultHttpClient();
	
	public FacebookSignIn(Context con,SharedPreferences sp,MainActivity ac){
		context=con;
		sharedpreferences = sp;
		activity = ac;
		progress = new ProgressDialog(ac);
	}
	
	
	@Override
    protected void onPreExecute() {
		progress.setMessage("Signing In...Please Wait...");
		progress.setIndeterminate(true);
		progress.show();
    }
	
	@Override
	protected HttpResponse doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		fb_id = arg0[1];
		fb_token = arg0[2];
		isfb = arg0[3];
		email = arg0[4] ;
        HttpResponse response = null;
        try {
			@SuppressWarnings("deprecation")
			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("isfb", new StringBody("1")).addPart("fb_token", new StringBody(fb_token)).addPart("fb_id", new StringBody(fb_id)).build();
			response = myutils.sendPost(arg0[0], reqEntity,httpclient);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	@SuppressLint("NewApi")
	@Override
    protected void onPostExecute(HttpResponse result) {
		//System.out.println(result.toString());
		//System.out.println(result.getStatusLine().toString());
		//progress.setProgress(100);
		if(result.getStatusLine().getStatusCode()==200){
			
			
			HttpEntity resEntity = result.getEntity();
			if (resEntity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = null;
				try {
					instream = resEntity.getContent();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            String json = MyUtils.convertStreamToString(instream);
	            try {
					JSONObject reader = new JSONObject(json);
					String id  = reader.getString("id");
					String name  = reader.getString("name");
					String chatpassword = reader.getString("chat_password");
					Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
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
					//yahan per news feed add kerna hai
					Intent intent = new Intent(context, NewsfeedActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					activity.finish();
		            
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            // now you have the string representation of the HTML request
	            
	            try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        }
			
			
		}
		else if(result.getStatusLine().getStatusCode()==203){
			
			
			new RegisterFacebook(context, sharedpreferences, activity, fb_id, fb_token, isfb, email).execute(c.register_url_fb);
			
		}
		else{
			Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
		}
		
		progress.cancel();
		try {
			
			result.getEntity().consumeContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();

    }
	
	
	
	

}

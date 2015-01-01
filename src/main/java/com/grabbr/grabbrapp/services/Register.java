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
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.grabbr.grabbrapp.activities.ConfirmationActivity;
import com.grabbr.grabbrapp.activities.SignupActivity;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Register extends AsyncTask<String, Void, HttpResponse >  {
	
	private ProgressDialog progress;
	private Context context;
	SharedPreferences sharedpreferences;
	public SignupActivity activity;
	 HttpClient httpclient = new DefaultHttpClient();
	MyUtils myutils = new MyUtils();
	FileBody bitmap;
	String name = "";
	String email = "";
    String password = "";
    
    MyConstants c = new MyConstants();
	
	public Register(Context con,SharedPreferences sp,SignupActivity ac,FileBody bp){
		context=con;
		sharedpreferences = sp;
		activity = ac;
		bitmap = bp;
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
		email = arg0[1];
		name = arg0[2];
		password = arg0[3];
		String imei = arg0[4];
        
        
        
        HttpResponse response = null;
        try {
			@SuppressWarnings("deprecation")
			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("fb_token", new StringBody("0")).addPart("fb_id", new StringBody("0")).addPart("picurl", bitmap).addPart("name",new StringBody(name)).addPart("isfb",new StringBody("0")).addPart("password",new StringBody(password)).addPart("device_id",new StringBody(imei)).build();
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
		if(statusCode==201){
			
	            // A Simple JSON Response Read
			JSONObject reader;
			
			
				
				
				

		            // A Simple JSON Response Read
				String json = null;
				try {
					json = EntityUtils.toString(result.getEntity());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
				

					//String json = utils.convertStreamToString(instream);
					try {
						
						reader = new JSONObject(json);
						String id = reader.getString("id");
						String name = reader.getString("name");
						String chatpassword = reader.getString("chat_password");
						Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
						Editor editor = sharedpreferences.edit();
						editor.putString(c.username, email);
						editor.putString(c.name, name);
						editor.putString(c.chatpassword, chatpassword);
						editor.putString(c.password, password);
						editor.putString(c.isfb, "0");
						editor.putString(c.id, id);
						editor.putString(c.isverified, "0");
						editor.commit();
						Intent intent = new Intent(context, ConfirmationActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
						activity.finish();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			
					
				
					
		            
				} 
	            // now you have the string representation of the HTML request
	            
	            

	        
			
		
		else if(statusCode==203){
			//progress.setProgress(100);
			Toast.makeText(context, "Invalid username password combination", Toast.LENGTH_SHORT).show();
		}
		else if(statusCode==200){
			//progress.setProgress(100);
			Toast.makeText(context, "Email already exits", Toast.LENGTH_SHORT).show();
		}
		else{
			//progress.setProgress(100);
			Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
		}
		
		if (result.getEntity() != null) {
		    try {
		    	result.getEntity().consumeContent();
		    } catch (IOException e) {
		        Log.e("register.java", "Not able to consume", e);
		    }
		}
		httpclient.getConnectionManager().shutdown();
		progress.cancel();
		

    }
	
	
	
	

}

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

public class SignIn extends AsyncTask<String, Void, HttpResponse >  {
	
	private ProgressDialog progress;
	private Context context;
	SharedPreferences sharedpreferences;
	public MainActivity activity;
	MyUtils myutils = new MyUtils();
	String username = "";
    String password = "";
    MyConstants c = new MyConstants();
    HttpClient httpclient = new DefaultHttpClient();
	public SignIn(Context con,SharedPreferences sp,MainActivity ac){
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
		
		username = arg0[1];
        password = arg0[2];
        HttpResponse response = null;
        try {
			@SuppressWarnings("deprecation")
			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(username)).addPart("isfb", new StringBody("0")).addPart("password", new StringBody(password)).build();
			response = myutils.sendPost(arg0[0], reqEntity,httpclient);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
    protected void onPostExecute(HttpResponse result) {
//		System.out.println(result.toString());
//		System.out.println(result.getStatusLine().toString());
		
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
					editor.putString(c.username, username);
					editor.putString(c.password, password);
					editor.putString(c.name, name);
					editor.putString(c.id, id);
					editor.putString(c.isfb, "0");
					editor.putString(c.chatpassword, chatpassword);
					editor.putString(c.isverified, "1");
					editor.commit();
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
			Toast.makeText(context, "Invalid username password combination", Toast.LENGTH_SHORT).show();
		}
		else{
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

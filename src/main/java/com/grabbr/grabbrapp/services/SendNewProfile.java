package com.grabbr.grabbrapp.services;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SendNewProfile extends AsyncTask<String, Void, HttpResponse >  {
	
	
	String name;
	String city;
	String aboutme;
	HttpClient httpclient = new DefaultHttpClient();
	
	public SendNewProfile(String n,String c,String a) {
		// TODO Auto-generated constructor stub
		
		name=n;
		city=c;
		aboutme=a;
	}
	
	
	@Override
    protected void onPreExecute() {
		
    }
	
	@Override
	protected HttpResponse doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		Log.d("sendlocation.java", "Coming hreee");
		HttpResponse response = null;
		MyUtils myutils = new MyUtils();
		if(arg0[1]=="0"){
			String username = arg0[2];
	        String password = arg0[3];
	        try {
	        	
				HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(username)).addPart("isfb", new StringBody("0")).addPart("password", new StringBody(password)).addPart("name", new StringBody(name)).addPart("city", new StringBody(city)).addPart("about_me", new StringBody(aboutme)).build();
				response = myutils.sendPost(arg0[0], reqEntity,httpclient);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			String fb_id = arg0[2];
			String fb_token = arg0[3];
			String isfb = arg0[1];
			String email = arg0[4] ;
			HttpEntity reqEntity;
			try {
				reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("isfb", new StringBody(isfb)).addPart("fb_token", new StringBody(fb_token)).addPart("fb_id", new StringBody(fb_id)).addPart("name", new StringBody(name)).addPart("city", new StringBody(city)).addPart("about_me", new StringBody(aboutme)).build();
				response = myutils.sendPost(arg0[0], reqEntity,httpclient);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		return response;

		
		
	}
	
	@Override
    protected void onPostExecute(HttpResponse result) {
		
		if (result.getEntity() != null) {
		    try {
		    	result.getEntity().consumeContent();
		    } catch (IOException e) {
		        Log.e("register.java", "Not able to consume", e);
		    }
		}
		httpclient.getConnectionManager().shutdown();
		

    }
	
	
	
	

}

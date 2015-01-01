package com.grabbr.grabbrapp.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class FetchProfileInfo extends AsyncTask<String, Void, HttpResponse > {

	private Context context;
	private MyTextView followers;
	private MyTextView followings;
	private MyTextView city;
	private MyTextView desc;
	private HttpClient httpclient;
	private SharedPreferences sharedpreferences;
	private MyConstants constant;
	private String cityname;
	private String descrisption;
	private String followercount;
	private String followingscount;
	private String username;
	private MyTextView person_name_tv;
	
	public FetchProfileInfo(Context context,MyTextView follower,MyTextView followings,MyTextView city,MyTextView desc) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.city = city;
		this.followers = follower;
		this.followings = followings;
		this.desc = desc;
		constant = new MyConstants();
		sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
		httpclient = new DefaultHttpClient();
	}
	

	public void setPerson_name_tv(MyTextView person_name_tv) {
		this.person_name_tv = person_name_tv;
	}


	@Override
	protected HttpResponse doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		HttpResponse response = null;
		if(isCancelled()){
			return response;
		}else{
	        MyUtils myutils = new MyUtils();
	
	        if (sharedpreferences.getString(constant.isfb, "").equals("0")) {
	            try {
	                HttpEntity reqEntity = MultipartEntityBuilder.create().
	                        addPart("email", new StringBody(sharedpreferences.getString(constant.username, ""))).
	                        addPart("password", new StringBody(sharedpreferences.getString(constant.password, ""))).
	                        addPart("isfb", new StringBody(sharedpreferences.getString(constant.isfb, ""))).
	                        addPart("id", new StringBody(sharedpreferences.getString(constant.id, ""))).
	                        build();
	                response = myutils.sendPost(arg0[0], reqEntity, httpclient);
	            } catch (UnsupportedEncodingException e) {
	                Log.d("post_element.java", "UnsupportedEncodingException");
	            }
	        } else {
	            try {
	                HttpEntity reqEntity = MultipartEntityBuilder.create().
	                        addPart("email", new StringBody(sharedpreferences.getString(constant.username, ""))).
	                        addPart("fb_token", new StringBody(sharedpreferences.getString(constant.fb_token, ""))).
	                        addPart("fb_id", new StringBody(sharedpreferences.getString(constant.fb_id, ""))).
	                        addPart("isfb", new StringBody(sharedpreferences.getString(constant.isfb, ""))).
	                        addPart("id", new StringBody(sharedpreferences.getString(constant.id, ""))).
	                        build();
	                response = myutils.sendPost(arg0[0], reqEntity, httpclient);
	            } catch (UnsupportedEncodingException e) {
	                Log.d("post_element.java", "UnsupportedEncodingException");
	            }
	        }
	        return response;
		}
	}
	
	@Override
	protected void onPostExecute(HttpResponse httpResponse) {
		// TODO Auto-generated method stub
		super.onPostExecute(httpResponse);
		 if (httpResponse != null) {

	            StatusLine statusLine = httpResponse.getStatusLine();
	            int statusCode = statusLine.getStatusCode();
	            InputStream in = null;
	
	            try {
	                in = httpResponse.getEntity().getContent();
	            } catch (IllegalStateException e2) {
	                // TODO Auto-generated catch block
	                Log.d("post_element.java", "IllegalStateException in OnPostExecute");
	            } catch (IOException e2) {
	                // TODO Auto-generated catch block
	                Log.d("post_element.java", "IOException in OnPostExecute");
	            }
	            if(!isCancelled()){        	
		            if (statusCode == 200) {
		                	JSONObject reader;
		                    BufferedReader reader1;
							try {
								reader1 = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
								String response = org.apache.commons.io.IOUtils.toString(reader1);
			                    reader = new JSONObject(response);
			                    System.out.println(reader.toString());
		                		descrisption = reader.getString("about_me");
		                		cityname = reader.getString("city");
		                		followercount = reader.getString("followers");
		                		followingscount = reader.getString("following"); 
		                		username = reader.getString("User"); 
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							if(!cityname.equals("")){
								city.setText(cityname);
							}else{
								city.setText("Add Location");
							}
							followers.setText(followercount);
							followings.setText(followingscount);
							if(!descrisption.equals("0")){
								desc.setText(descrisption);  
							}
							if(person_name_tv!=null){
								person_name_tv.setText(username);
							}
		            } else {
		                Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
		            }
	            }
	            
	            try {
	                in.close();
	                httpResponse.getEntity().consumeContent();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            
	        } else {
	            Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
	        }
	        httpclient.getConnectionManager().shutdown();
	}

}

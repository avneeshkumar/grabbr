package com.grabbr.grabbrapp.services;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.grabbr.grabbrapp.activities.DiscoverActivity;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class SendPostDiscover extends AsyncTask<String, Void, HttpResponse >  {
	
	DiscoverActivity discoverActivity;
	String heading;
	String descrition;
	String rupees ;
	String category;
	String privacy;
	Fragment f;
	int mycase=-1;
	FileBody file1=null;
	FileBody file2=null;
	FileBody file3 = null;
	private ProgressDialog progress;
	 HttpClient httpclient = new DefaultHttpClient();
	
	public SendPostDiscover(Fragment myf,DiscoverActivity cf,String head,String des,String rup,String cat,String pri,FileBody f1,FileBody f2,FileBody f3) {
		// TODO Auto-generated constructor stub
		discoverActivity = cf;
		heading=head;
		descrition= des;
		rupees=rup;
		category = cat;
		privacy= pri;
		mycase=3;
		file1 = f1;
		file2 = f2;
		file3 = f3;
		progress = new ProgressDialog(cf);
		f=myf;
	}
	public SendPostDiscover(Fragment myf,DiscoverActivity cf,String head,String des,String rup,String cat,String pri,FileBody f1,FileBody f2) {
		// TODO Auto-generated constructor stub
		discoverActivity = cf;
		f=myf;
		heading=head;
		descrition= des;
		rupees=rup;
		category = cat;
		privacy= pri;
		mycase=2;
		file1 = f1;
		file2 = f2;
		progress = new ProgressDialog(cf);
		
	}
	public SendPostDiscover(Fragment myf,DiscoverActivity cf,String head,String des,String rup,String cat,String pri,FileBody f1) {
		// TODO Auto-generated constructor stub
		f=myf;
		discoverActivity = cf;
		heading=head;
		descrition= des;
		rupees=rup;
		category = cat;
		privacy= pri;
		mycase=1;
		file1 = f1;
		progress = new ProgressDialog(cf);
		
	}
	
	
	@Override
    protected void onPreExecute() {
		progress.setMessage("Posting...");
		progress.setIndeterminate(true);
		progress.show();
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
	        switch(mycase){
	        	case 1:
	        		
				try {
					
					
					HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(username)).addPart("isfb", new StringBody("0")).addPart("password", new StringBody(password))
	        		.addPart("heading", new StringBody(heading)).addPart("description", new StringBody(descrition)).addPart("price",new StringBody(rupees)).addPart("category", new StringBody(category))
	        		.addPart("privacy", new StringBody(privacy)).addPart("picurl1", file1).build();
					Log.d("sendost","Only one image available");
					response = myutils.sendPost(arg0[0], reqEntity,httpclient);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        		break;
	        	case 2:
				try {
					HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(username)).addPart("isfb", new StringBody("0")).addPart("password", new StringBody(password))
	        		.addPart("heading", new StringBody(heading)).addPart("description", new StringBody(descrition)).addPart("price",new StringBody(rupees)).addPart("category", new StringBody(category))
	        		.addPart("privacy", new StringBody(privacy)).addPart("picurl1", file1).addPart("picurl2", file2).build();
					response = myutils.sendPost(arg0[0], reqEntity,httpclient);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        		break;
	        	case 3:
				try {
					HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(username)).addPart("isfb", new StringBody("0")).addPart("password", new StringBody(password))
	        		.addPart("heading", new StringBody(heading)).addPart("description", new StringBody(descrition)).addPart("price",new StringBody(rupees)).addPart("category", new StringBody(category))
	        		.addPart("privacy", new StringBody(privacy)).addPart("picurl1", file1).addPart("picurl2", file2).addPart("picurl3", file3).build();
					response = myutils.sendPost(arg0[0], reqEntity,httpclient);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        		break;
	        	default:
	        		
	        		break;
	        		
	        	
	        
	        
	        }
	        
				//HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(username)).addPart("isfb", new StringBody("0")).addPart("password", new StringBody(password)).addPart("latitude", new StringBody(latitude)).addPart("longitude", new StringBody(longitude)).build();
				//response = myutils.sendPost(arg0[0], reqEntity);
			
		}
		else{
			String fb_id = arg0[2];
			String fb_token = arg0[3];
			String isfb = arg0[1];
			String email = arg0[4] ;
			HttpEntity reqEntity;
			switch(mycase){
        	case 1:
			try {
				 reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("isfb", new StringBody(isfb)).addPart("fb_token", new StringBody(fb_token)).addPart("fb_id", new StringBody(fb_id))
        		.addPart("heading", new StringBody(heading)).addPart("description", new StringBody(descrition)).addPart("price",new StringBody(rupees)).addPart("category", new StringBody(category))
        		.addPart("privacy", new StringBody(privacy)).addPart("picurl1", file1).build();
				 response = myutils.sendPost(arg0[0], reqEntity,httpclient);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	case 2:
			try {
				 reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("isfb", new StringBody(isfb)).addPart("fb_token", new StringBody(fb_token)).addPart("fb_id", new StringBody(fb_id))
        		.addPart("heading", new StringBody(heading)).addPart("description", new StringBody(descrition)).addPart("price",new StringBody(rupees)).addPart("category", new StringBody(category))
        		.addPart("privacy", new StringBody(privacy)).addPart("picurl1", file1).addPart("picurl2", file2).build();
				 response = myutils.sendPost(arg0[0], reqEntity,httpclient);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	case 3:
			try {
				reqEntity =MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("isfb", new StringBody(isfb)).addPart("fb_token", new StringBody(fb_token)).addPart("fb_id", new StringBody(fb_id))
        		.addPart("heading", new StringBody(heading)).addPart("description", new StringBody(descrition)).addPart("price",new StringBody(rupees)).addPart("category", new StringBody(category))
        		.addPart("privacy", new StringBody(privacy)).addPart("picurl1", file1).addPart("picurl2", file2).addPart("picurl3", file3).build();
				response = myutils.sendPost(arg0[0], reqEntity,httpclient);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	default:
        		
        		break;
        		
        	
        
        
        }
			
				//reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("isfb", new StringBody(isfb)).addPart("fb_token", new StringBody(fb_token)).addPart("fb_id", new StringBody(fb_id)).addPart("latitude", new StringBody(latitude)).addPart("longitude", new StringBody(longitude)).build();
				//response = myutils.sendPost(arg0[0], reqEntity);
			
			
			
		}
		
		return response;

		
		
	}
	
	@SuppressLint("NewApi")
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
					//String name  = reader.getString("name");
					Toast.makeText(discoverActivity.getApplicationContext(), "Sucessfully posted.", Toast.LENGTH_SHORT).show();
					discoverActivity.getSupportFragmentManager().beginTransaction().remove(f).commit();

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
			Toast.makeText(discoverActivity.getApplicationContext(), "Invalid username password combination", Toast.LENGTH_SHORT).show();
		}
		else if(statusCode==200){
			//progress.setProgress(100);
			Log.d("register_facebook","Error code 200");
			Toast.makeText(discoverActivity.getApplicationContext(), "Email already exits", Toast.LENGTH_SHORT).show();
		}
		else{
			//progress.setProgress(100);
			Log.d("register_facebook","error code not found");
			BufferedReader reader1;
			try {
				reader1 = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
				try {
					String response = org.apache.commons.io.IOUtils.toString(reader1);
					System.out.println(response);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			Toast.makeText(discoverActivity.getApplicationContext(), "Service unavailable", Toast.LENGTH_SHORT).show();
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

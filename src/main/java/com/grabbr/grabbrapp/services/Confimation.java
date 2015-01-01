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

import com.grabbr.grabbrapp.activities.ConfirmationActivity;
import com.grabbr.grabbrapp.activities.ConfirmationDoneActivity;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Confimation extends AsyncTask<String, Void, HttpResponse >  {
	
	private ProgressDialog progress;
	private Context context;
	SharedPreferences sharedpreferences;
	public ConfirmationActivity activity;
	MyUtils myutils = new MyUtils();
	String username = "";
    String password = "";
    MyConstants c = new MyConstants();
    HttpClient httpclient = new DefaultHttpClient();
	
	public Confimation(Context con,SharedPreferences sp,ConfirmationActivity ac){
		context=con;
		sharedpreferences = sp;
		activity = ac;
		progress = new ProgressDialog(ac);
	}
	
	
	@Override
    protected void onPreExecute() {
		progress.setMessage("Verifying...Please Wait...");
		progress.setIndeterminate(true);
		progress.show();
    }
	
	@Override
	protected HttpResponse doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		
		username = arg0[1];
        password = arg0[2];
        String code = arg0[3];
        
        
        HttpResponse response = null;
        try {
			@SuppressWarnings("deprecation")
			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(username)).addPart("password", new StringBody(password)).addPart("code", new StringBody(code)).build();
			response = myutils.sendPost(arg0[0], reqEntity,httpclient);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
    protected void onPostExecute(HttpResponse result) {
		
		//progress.setProgress(100);
		int code = result.getStatusLine().getStatusCode();
		if(code==202){
			   
					Editor editor = sharedpreferences.edit();
					editor.putString(c.isverified, "1");
					
					editor.commit();
					Intent intent = new Intent(context, ConfirmationDoneActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					activity.finish();
		       
	            // now you have the string representation of the HTML request
	            
	           

	        }
			
		else if(code==200){
			Toast.makeText(context, "Time expired..Click resend again", Toast.LENGTH_SHORT).show();
		}
		else if(code==205){
			Toast.makeText(context, "Wrong Code", Toast.LENGTH_SHORT).show();
		}
		else if(code==204){
			Toast.makeText(context, "Already verified", Toast.LENGTH_SHORT).show();
		}
		else if(code==203){
			Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show();
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
		
//		if(result!=null){
//			try {
//				
//					
//					//result.getEntity().consumeContent();
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				Log.d("confirmation.java","result null");
//				e.printStackTrace();
//			}
//		}

    }
	
	
	
	

}

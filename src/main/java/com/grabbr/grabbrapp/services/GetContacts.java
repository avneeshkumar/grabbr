package com.grabbr.grabbrapp.services;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.grabbr.grabbrapp.activities.ConfirmationDoneActivity;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GetContacts extends AsyncTask<String, Void, HttpResponse >  {
	
	ConfirmationDoneActivity confirmationdoneactivity;
	 HttpClient httpclient = new DefaultHttpClient();
	public GetContacts(ConfirmationDoneActivity cf) {
		// TODO Auto-generated constructor stub
		confirmationdoneactivity = cf;
	}
	
	
	@Override
    protected void onPreExecute() {
		
    }
	
	@Override
	protected HttpResponse doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		ContentResolver resolver = confirmationdoneactivity.getContentResolver();
		Cursor c = resolver.query(Data.CONTENT_URI,null,Data.HAS_PHONE_NUMBER + "!=0 AND (" + Data.MIMETYPE + "=? OR " + Data.MIMETYPE + "=?)", 
		        new String[]{Email.CONTENT_ITEM_TYPE, Phone.CONTENT_ITEM_TYPE},Data.CONTACT_ID);
		String s="";
		while (c.moveToNext()) {
		    long id = c.getLong(c.getColumnIndex(Data.CONTACT_ID));
		    String name = c.getString(c.getColumnIndex(Data.DISPLAY_NAME));
		    String data1 = c.getString(c.getColumnIndex(Data.DATA1));
		
		   s=s+(id+","+name + "," + data1+"\n");
		}
		
		System.out.println(s);
		HttpResponse response = null;
		MyUtils myutils = new MyUtils();
		if(arg0[1]=="0"){
			String username = arg0[2];
	        String password = arg0[3];
	        try {
	        	
				HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(username)).addPart("isfb", new StringBody("0")).addPart("password", new StringBody(password)).addPart("contacts", new StringBody(s)).build();
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
				reqEntity = MultipartEntityBuilder.create().addPart("email", new StringBody(email)).addPart("isfb", new StringBody(isfb)).addPart("fb_token", new StringBody(fb_token)).addPart("contacts", new StringBody(s)).addPart("fb_id", new StringBody(fb_id)).build();
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

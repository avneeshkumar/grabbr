package com.grabbr.grabbrapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.entity.mime.content.FileBody;

import com.grabbr.grabbrapp.activities.MainActivity;
import com.grabbr.grabbrapp.utils.MyConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class ProfileDownloadImage extends AsyncTask<URL, Integer, Bitmap > {
	
	String fb_id;
	String fb_token;
	String isfb;
	String email;
	SharedPreferences sharedpreferences;
	URL url;
	MainActivity activity;
	Context context;
	
	public ProfileDownloadImage(Context con,SharedPreferences sp,MainActivity ac,URL u,String f_id,String f_token,String ifb,String mail) {
		// TODO Auto-generated constructor stub
		url=u;
		fb_id = f_id;
		sharedpreferences = sp;
		isfb = ifb;
		fb_token = f_token;
		email = mail;
		
	}
	
	@Override
    protected void onPreExecute() {
		//progress.setMessage("Signing In...Please Wait...");
		//progress.setIndeterminate(true);
		//progress.show();
    }
	
	@Override
	protected Bitmap doInBackground(URL... arg0) {
		// TODO Auto-generated method stub
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp;
	}
	
	@Override
    protected void onPostExecute(Bitmap result) {
		
		if(result!=null){
			
			
			//Log.d("download_image", ""+result.getHeight());
			File file = new File("test.jpg");
	        try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	        //Convert bitmap to byte array
	        
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        result.compress(CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
	        byte[] bitmapdata = bos.toByteArray();

	        //write the bytes in file
	        FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				fos.write(bitmapdata);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				fos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	        
	        //File file = new File(realpath);
	        FileBody bin = new FileBody(file);
	        MyConstants c = new MyConstants();
	        
		}
		else{
			
			Log.e("download_image", "Bitmap coming null");
		}
		
	}

}

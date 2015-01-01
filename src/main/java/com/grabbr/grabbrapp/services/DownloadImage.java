package com.grabbr.grabbrapp.services;



import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by arun on 12/12/14.
 */
public class DownloadImage {

    private MyConstants constant;
    private HttpClient httpclient;
    private SharedPreferences sharedpreferences;
    private String URL = "";
    private Context context;
    private String post_id;
    int ImageNumber;

    public DownloadImage(String url,String post_id,Context context){
        URL = url;
        this.context = context;
        constant = new MyConstants();
        this.post_id = post_id;
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        httpclient = new DefaultHttpClient();
    }
    
    public DownloadImage(String url,String post_id,int ImageNumber,Context context){
        URL = url;
        this.context = context;
        constant = new MyConstants();
        this.post_id = post_id;
        this.ImageNumber = ImageNumber;
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        httpclient = new DefaultHttpClient();
    }

    public Bitmap getBitmap(){
        HttpResponse response = null;
        MyUtils myutils = new MyUtils();
        if(sharedpreferences.getString(constant.isfb,"").equals("0")){
            try {
                HttpEntity reqEntity = MultipartEntityBuilder.create().
                        addPart("email", new StringBody(sharedpreferences.getString(constant.username, ""))).
                        addPart("password",new StringBody(sharedpreferences.getString(constant.password,""))).
                        addPart("isfb",new StringBody(sharedpreferences.getString(constant.isfb,""))).
                        addPart("id",new StringBody(sharedpreferences.getString(constant.id,""))).
                        addPart("postid",new StringBody(post_id)).
                        build();
                response = myutils.sendPost(URL, reqEntity,httpclient);
            }catch (UnsupportedEncodingException e) {
                Log.d("post_element.java", "UnsupportedEncodingException");
            }
        }else{
            try {
                HttpEntity reqEntity = MultipartEntityBuilder.create().
                        addPart("email", new StringBody(sharedpreferences.getString(constant.username, ""))).
                        addPart("fb_token",new StringBody(sharedpreferences.getString(constant.fb_token,""))).
                        addPart("fb_id",new StringBody(sharedpreferences.getString(constant.fb_id,""))).
                        addPart("isfb", new StringBody(sharedpreferences.getString(constant.isfb, ""))).
                        addPart("id",new StringBody(sharedpreferences.getString(constant.id,""))).
                        addPart("postid",new StringBody(post_id)).
                        build();
                response = myutils.sendPost(URL, reqEntity,httpclient);
            }catch (UnsupportedEncodingException e) {
                Log.d("post_element.java","UnsupportedEncodingException");
            }
        }
        ByteArrayOutputStream baos;
        Bitmap bmp = null;
        if(response!=null) {
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try {
                        InputStream instream = entity.getContent();
                        baos = new ByteArrayOutputStream();
                        int bufferSize = 1024;
                        byte[] buffer = new byte[bufferSize];
                        int len = 0;
                        try {
                            while ((len = instream.read(buffer)) != -1) {
                                baos.write(buffer, 0, len);
                            }
                            baos.flush();
                            byte[] b = baos.toByteArray();
                            bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                            baos.close();
                        }catch (IOException e) {
                            Log.d("cusotomAdapter.java", "IOException\n");
                        }
                    }catch (IOException e1){
                        Log.d("cusotomAdapter.java","IOException\n");
                    }
                }
            }else{
               // Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
            }
        }else{
            //Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
        }
        httpclient.getConnectionManager().shutdown();
        String filename = post_id + "_" + ImageNumber + ".jpg";
        storeImage(bmp,filename);
        return bmp;
    }

    private boolean storeImage(Bitmap imageData, String filename) {
    	//get path to external storage (SD card)
    	String iconsStoragePath = Environment.getExternalStorageDirectory() + "/grabbrApp/myImages/";
    	File sdIconStorageDir = new File(iconsStoragePath);
    	//create storage directories, if they don't exist
    	sdIconStorageDir.mkdirs();
    	try {
    		String filePath = sdIconStorageDir.toString()+"/" + filename;
    		FileOutputStream fileOutputStream = new FileOutputStream(filePath);

    		BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
    		//choose another format if PNG doesn't suit you
    		imageData.compress(CompressFormat.JPEG,100, bos);
    		bos.flush();
    		bos.close();

    	} catch (FileNotFoundException e) {
    		Log.w("TAG", "Error saving image file: " + e.getMessage());
    		return false;
    	} catch (IOException e) {
    		Log.w("TAG", "Error saving image file: " + e.getMessage());
    		return false;
    	}

    	return true;
    }
}

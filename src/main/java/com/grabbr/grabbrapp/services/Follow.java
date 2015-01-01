package com.grabbr.grabbrapp.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by arun on 15/12/14.
 */
public class Follow extends AsyncTask<String, Void, HttpResponse > {

    private ProgressDialog progress;
    private Context context;
    private SharedPreferences sharedpreferences;
    private HttpClient httpclient;
    private MyUtils myutils;
    private MyConstants constant;
    public int statusCode = -1;
    private ImageView imageView;
    private MyTextView myTextView;
    
    public Follow(Context context,ImageView imageView,MyTextView myTextView){
        this.context = context;
        this.imageView = imageView;
        this.myTextView = myTextView;
        httpclient = new DefaultHttpClient();
        myutils = new MyUtils();
       
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected HttpResponse doInBackground(String... params) {

        HttpResponse response = null;
        if(sharedpreferences.getString(constant.isfb,"").equals("0")){
            try {
                HttpEntity reqEntity = MultipartEntityBuilder.create().
                        addPart("email", new StringBody(sharedpreferences.getString(constant.username, ""))).
                        addPart("password",new StringBody(sharedpreferences.getString(constant.password,""))).
                        addPart("isfb",new StringBody(sharedpreferences.getString(constant.isfb,""))).
                        addPart("id",new StringBody(sharedpreferences.getString(constant.id,""))).
                        addPart("id2",new StringBody(params[1])).
                        build();
                response = myutils.sendPost(params[0], reqEntity,httpclient);
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
                        addPart("id2", new StringBody(params[1])).
                        build();
                response = myutils.sendPost(params[0], reqEntity,httpclient);
            }catch (UnsupportedEncodingException e) {
                Log.d("post_element.java","UnsupportedEncodingException");
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(HttpResponse httpResponse) {
        super.onPostExecute(httpResponse);
        if(httpResponse!=null) {
            StatusLine statusLine = httpResponse.getStatusLine();
            statusCode = statusLine.getStatusCode();
            if(statusCode==201){
                myTextView.setText("Following");
            }
            else
            if(statusCode==200){
            	myTextView.setText("Follow");
            }
        }else{
            Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
        }
        httpclient.getConnectionManager().shutdown();
    }
}

package com.grabbr.grabbrapp.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.grabbr.grabbrapp.customviews.MyProfileNewsfeedAdapter;
import com.grabbr.grabbrapp.daos.NewsfeedData;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class FetchMoreMyPosts extends AsyncTask<String, Void, HttpResponse >{

	private Context context;
    private SharedPreferences sharedpreferences;
    private MyConstants constant;
    private HttpClient httpclient;
    private ListView Lv;
    private List<NewsfeedData> rowItems;
    private MyProfileNewsfeedAdapter adapter;
    
    public FetchMoreMyPosts(Context context,ListView Lv,List<NewsfeedData> rowItems,MyProfileNewsfeedAdapter adapter){
        this.context = context;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        httpclient = new DefaultHttpClient();
        this.Lv = Lv;
        this.rowItems = rowItems;
        this.adapter = adapter;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
	@Override
	protected HttpResponse doInBackground(String... params) {
		// TODO Auto-generated method stub
		HttpResponse response = null;
        MyUtils myutils = new MyUtils();

        if(sharedpreferences.getString(constant.isfb,"").equals("0")){
            try {
                HttpEntity reqEntity = MultipartEntityBuilder.create().
                        addPart("email", new StringBody(sharedpreferences.getString(constant.username, ""))).
                        addPart("password",new StringBody(sharedpreferences.getString(constant.password,""))).
                        addPart("isfb",new StringBody(sharedpreferences.getString(constant.isfb,""))).
                        addPart("id",new StringBody(sharedpreferences.getString(constant.id,""))).
                        build();
                response = myutils.sendPost(params[0], reqEntity,httpclient);
            }catch (UnsupportedEncodingException e) {
                Log.d("post_element.java","UnsupportedEncodingException");
            }
        }else{
            try {
                HttpEntity reqEntity = MultipartEntityBuilder.create().
                        addPart("email", new StringBody(sharedpreferences.getString(constant.username, ""))).
                        addPart("fb_token",new StringBody(sharedpreferences.getString(constant.fb_token,""))).
                        addPart("fb_id",new StringBody(sharedpreferences.getString(constant.fb_id,""))).
                        addPart("isfb", new StringBody(sharedpreferences.getString(constant.isfb, ""))).
                        addPart("id",new StringBody(sharedpreferences.getString(constant.id,""))).
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
		// TODO Auto-generated method stub
		super.onPostExecute(httpResponse);
		if(httpResponse!=null) {

            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            InputStream in = null;
            try {
				in = httpResponse.getEntity().getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (statusCode == 200) {
            	JSONArray reader;
            	BufferedReader reader1;
				try {
					reader1 = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
					String response = org.apache.commons.io.IOUtils.toString(reader1);
					reader = new JSONArray(response);
					for (int i = 0; i < reader.length(); i++) {
                        JSONObject obj = reader.getJSONObject(i);
                        JSONObject fields = obj.getJSONObject("fields");
                        NewsfeedData ri = new NewsfeedData();
                        ri.setCatgeory(fields.getString("category"));
                        ri.setDescription(fields.getString("description"));
                        ri.setPrice((float) fields.getDouble("price"));
                        Duration duration = new Duration((int)(Float.parseFloat(fields.getString("timestamp"))*1000)); // in milliseconds
                        PeriodFormatter formatter = new PeriodFormatterBuilder()
                             .appendDays()
                             .appendSuffix("d")
                             .appendHours()
                             .appendSuffix("h")
                             .appendMinutes()
                             .appendSuffix("m")
                             .toFormatter();
                        String formatted = formatter.print(duration.toPeriod());
                        ri.setTimeStamp(formatted);
                        ri.setHeading(fields.getString("heading"));
                        ri.setPostId(obj.getString("pk"));
                        ri.setName(fields.getString("name"));
                        ri.setPrivacy(fields.getString("privacy"));
                        ri.setUserId(fields.getString("userid"));
                        ri.setPicurl1(fields.getString("picurl1"));
                        ri.setPicurl2(fields.getString("picurl2"));
                        ri.setPicurl3(fields.getString("picurl3"));
                        ri.setLike(fields.getString("like"));
                        ri.setLiked(fields.getString("liked"));
                        ri.setCity(fields.getString("city"));
                        ri.setIsSold(fields.getString("sold"));
                        Log.d("Post Elements", fields.getString("heading"));
                        rowItems.add(ri);
                    }
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
				adapter.notifyDataSetChanged();
                try {
                	in.close();
					httpResponse.getEntity().consumeContent();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }else{
                Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
            }
            httpclient.getConnectionManager().shutdown();
		}
	}

}

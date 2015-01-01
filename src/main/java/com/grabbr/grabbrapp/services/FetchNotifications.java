package com.grabbr.grabbrapp.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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

import com.grabbr.grabbrapp.customviews.ExpandableHeightListView;
import com.grabbr.grabbrapp.customviews.NotificationListAdapter;
import com.grabbr.grabbrapp.daos.NotificationData;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class FetchNotifications extends AsyncTask<String, Void, HttpResponse > {

    private Context context;
    private SharedPreferences sharedpreferences;
    public NotificationListAdapter adapter;
    private MyConstants constant;
    private HttpClient httpclient;
    private ExpandableHeightListView lv;
    ArrayList<NotificationData> notificationList;
    private ProgressDialog progress;

    public FetchNotifications(Context context,ExpandableHeightListView lv,ArrayList<NotificationData> notificationList) {
        this.context = context;
        this.lv = lv;
        this.notificationList = notificationList;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        httpclient = new DefaultHttpClient();
        adapter = new NotificationListAdapter(context);
        progress = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress.setMessage("Please Wait...");
		progress.setIndeterminate(true);
		progress.show();
    }

    @Override
    protected HttpResponse doInBackground(String... params) {

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
	                response = myutils.sendPost(params[0], reqEntity, httpclient);
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
	                response = myutils.sendPost(params[0], reqEntity, httpclient);
	            } catch (UnsupportedEncodingException e) {
	                Log.d("post_element.java", "UnsupportedEncodingException");
	            }
	        }
	        return response;
		}
    }

    @Override
    protected void onPostExecute(HttpResponse httpResponse) {

        if (httpResponse != null) {

            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            InputStream in = null;
            notificationList = new ArrayList<NotificationData>();

            try {
                in = httpResponse.getEntity().getContent();
            } catch (IllegalStateException e2) {
                // TODO Auto-generated catch block
                Log.d("post_element.java", "IllegalStateException in OnPostExecute");
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                Log.d("post_element.java", "IOException in OnPostExecute");
            }

            if (statusCode == 200) {
                JSONArray reader;
                try {
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
                    String response = org.apache.commons.io.IOUtils.toString(reader1);
                    reader = new JSONArray(response);
                    try {
                        for (int i = 0; i < reader.length(); i++) {
                            JSONObject obj = reader.getJSONObject(i);
                            JSONObject fields = obj.getJSONObject("fields");
                            NotificationData ri = new NotificationData();
                            ri.setText(fields.getString("notification_text"));
                            //ri.setId(obj.getString("pk"));
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
                            ri.setTimestamp(formatted);
                            ri.setUserid(fields.getString("userid"));
                            ri.setUserid2(fields.getString("userid2"));
                            notificationList.add(ri);
                            if(isCancelled())
                            	break;
                        }
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        Log.d("post_element.java", "IOException1 in OnPostExecute Json Exceprtion");
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    Log.d("post_element.java", "IOException2 in OnPostExecute Buffer Reading");
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    Log.d("post_element.java", "IOException3 in OnPostExecute Json Exceprtion");
                }
                if(!isCancelled()){
	                adapter.setRowItems(notificationList);
	                lv.setAdapter(adapter);
	                lv.setExpanded(true);
                }
            } else {
                Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show();
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
        progress.cancel();
    }

}


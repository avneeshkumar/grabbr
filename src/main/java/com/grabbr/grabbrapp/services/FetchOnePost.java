package com.grabbr.grabbrapp.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.FloatingChatActivity;
import com.grabbr.grabbrapp.customviews.RoundedImageView;
import com.grabbr.grabbrapp.daos.NewsfeedData;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.MyUtils;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FetchOnePost extends AsyncTask<String, Void, HttpResponse >{

	private Context context;
    private SharedPreferences sharedpreferences;
    private MyConstants constant;
    private HttpClient httpclient;
    private View rootView;
    private String postID;
    RoundedImageView profile_pic;
    ImageView post_image;
    ImageView like_icon;
    ImageView share_icon;
    ImageView more_icon;
    TextView username;
    TextView time;
    TextView location_stamp;
    TextView item_title;
    TextView description;
    TextView price_id;
    TextView like_count;
    TextView share_count;
    TextView chattobuy;
    
    public FetchOnePost(Context context,View rootView,String postID) {
		// TODO Auto-generated constructor stub
    	
    	this.context = context;
    	this.rootView = rootView;
    	this.postID = postID;
    	
    	constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        httpclient = new DefaultHttpClient();
        
        profile_pic = (RoundedImageView)rootView.findViewById(R.id.profile_pic);
        post_image = (ImageView)rootView.findViewById(R.id.post_image);
        like_icon = (ImageView)rootView.findViewById(R.id.like_icon);
        share_icon = (ImageView)rootView.findViewById(R.id.share_icon);
        more_icon = (ImageView)rootView.findViewById(R.id.more_icon);
        username = (TextView)rootView.findViewById(R.id.username);
        time = (TextView)rootView.findViewById(R.id.time_stamp);
        location_stamp = (TextView)rootView.findViewById(R.id.location_stamp);
        item_title = (TextView)rootView.findViewById(R.id.item_title);
        description = (TextView)rootView.findViewById(R.id.description);
        price_id = (TextView)rootView.findViewById(R.id.price_id);
        like_count = (TextView)rootView.findViewById(R.id.like_count);
        share_count = (TextView)rootView.findViewById(R.id.share_count);
        chattobuy = (TextView)rootView.findViewById(R.id.chattobuy);
        
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
                response = myutils.sendPost(params[0]+postID+"/", reqEntity,httpclient);
            }catch (UnsupportedEncodingException e) {
                Log.d(FetchOnePost.class.getName(),"UnsupportedEncodingException");
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
                response = myutils.sendPost(params[0]+postID+"/", reqEntity,httpclient);
            }catch (UnsupportedEncodingException e) {
                Log.d(FetchOnePost.class.getName(),"UnsupportedEncodingException");
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
            	JSONObject reader;
            	BufferedReader reader1;
				try {
					reader1 = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
					String response = org.apache.commons.io.IOUtils.toString(reader1);
					reader = new JSONObject(response);
					JSONObject fields = reader.getJSONObject("fields");
                    final NewsfeedData ri = new NewsfeedData();
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
                    ri.setPostId(reader.getString("pk"));
                    ri.setName(fields.getString("name"));
                    ri.setPrivacy(fields.getString("privacy"));
                    ri.setUserId(fields.getString("userid"));
                    ri.setPicurl1(fields.getString("picurl1"));
                    ri.setPicurl2(fields.getString("picurl2"));
                    ri.setPicurl3(fields.getString("picurl3"));
                    ri.setLike(fields.getString("like"));
                    ri.setLiked(fields.getString("liked"));
                    ri.setCity(fields.getString("city"));
                    Log.d("Post Elements", fields.getString("heading"));
                    
                    username.setText(ri.getName());
                    time.setText(ri.getTimeStamp());
                    item_title.setText(ri.getHeading());
                    description.setText(ri.getDescription());
                    price_id.setText(""+ri.getPrice());
                    like_count.setText(ri.getLike());
                    share_count.setText("1");
                    
                    final MyConstants c = new MyConstants();

                    like_icon.setOnClickListener(new OnClickListener() {
            			
            			@Override
            			public void onClick(View arg0) {
            				// TODO Auto-generated method stub
            				if(ri.getLiked().equals("0")){
            					if(sharedpreferences.getString(c.isfb,"").equals("0")){
            						
            						new SendLike().execute(c.like_url+ri.getPostId()+"/","0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
            					}
            					else{
            						new SendLike().execute(c.like_url+ri.getPostId()+"/","1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
            					}
            					like_count.setText(""+(Integer.parseInt(ri.getLike())+1));
            				}
            				else{
            					if(sharedpreferences.getString(c.isfb,"").equals("0")){
            						
            						new SendLike().execute(c.like_url+ri.getPostId()+"/","0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
            					}
            					else{
            						new SendLike().execute(c.like_url+ri.getPostId()+"/","1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
            					}
            					like_count.setText(""+(Integer.parseInt(ri.getLike())-1));
            				}
            			}
            		});
                    chattobuy.setOnClickListener(new OnClickListener() {
            			
            			@Override
            			public void onClick(View v) {
            				// TODO Auto-generated method stub
            				Intent i = new Intent(context, FloatingChatActivity.class);
            				i.putExtra("username", ri.getName());
            				i.putExtra("userid", ri.getUserId());
            				((Activity)context).startActivity(i);
            				
            			}
            		});
                    
                    //imageLoader.DisplayImage(constant.profile_url+ri.getUserId()+"/",imageView1);
                    Picasso.with(context)
                            .load(constant.profile_url+ri.getUserId()+"/")
                            .resize(100,100)
                            .centerCrop()
                            .into(profile_pic);

//                    AnimationDrawable animation = new AnimationDrawable();
//                    if(ri.getCurrImg()==1){
//                    	new setAnimation(row_pos, post_image,1).execute();
//                    }
                    
                    new setPostBitmap(post_image,1).execute();
                    
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
	
	public class setPostBitmap  extends AsyncTask<String,Void,Bitmap>{

    	private ImageView imageAnim;
    	private int imgNum ;
    	
    	public setPostBitmap(ImageView imageAnim,int imgNum){
    		this.imageAnim = imageAnim;
    		this.imgNum = imgNum;
    	}
    	
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			String iconsStoragePath = Environment.getExternalStorageDirectory() + "/grabbrApp/myImages/";
	    	File sdIconStorageDir = new File(iconsStoragePath);
	    	String filename = postID + "_" + imgNum + ".jpg";
			String filePath = sdIconStorageDir.toString()+"/" + filename;
			File file = new File(filePath);
			Bitmap bitmap = null;
			if(!file.exists()){
				bitmap = new DownloadImage(constant.postimage_url+postID+"/"+1+"/",postID,1, context).getBitmap();
			}else{
				bitmap = getBitamp(1,postID);
			}			
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null){
				System.out.println(result.toString());
				Drawable frame = new BitmapDrawable(context.getResources(),result);
				imageAnim.setImageDrawable(frame);
				//imageAnim.setImageBitmap(bitmap);
			}	
		}
    	
    }
    
  
    private Bitmap getBitamp(int number,String post_id) {
    	
    	File sdCard = Environment.getExternalStorageDirectory();

    	File directory = new File (sdCard.getAbsolutePath() + "/grabbrApp/myImages/");

    	File file = new File(directory,post_id+"_"+number+".jpg"); //or any other format supported

    	FileInputStream streamIn;
    	
		try {
			streamIn = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(streamIn); //This gets the image
			try {
				streamIn.close();
				return bitmap;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}

package com.grabbr.grabbrapp.customviews;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.FloatingPostAcitivity;
import com.grabbr.grabbrapp.daos.DiscoverData;
import com.grabbr.grabbrapp.services.DownloadImage;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

	public class DiscoverGridviewAdapter extends ArrayAdapter
	{
	         Context context;
	         ArrayList<DiscoverData> discover_list;
	       
	    

	     public ArrayList<DiscoverData> getDiscover_list() {
				return discover_list;
			}

			public void setDiscover_list(ArrayList<DiscoverData> discover_list) {
				this.discover_list = discover_list;
			}
			

		public DiscoverGridviewAdapter(Context context) 
	     {
	             super(context, 0);
	             this.context=context;
	             
	             
	     }
	    
	     public int getCount() 
	        {
	                     return discover_list.size();
	        }
	     
	     public static class ViewHolder{
	         ImageView picture;
	         MyTextView price;
	         MyTextView headline;
	         RoundedImageView profilepic;
	         MyTextView name;
	         MyTextView time_stamp;

	     }

	     @Override
	     public View getView(int position, View convertView, ViewGroup parent) 
	     {
	             View row = convertView;
	             ViewHolder holder;
	             
	             if (row == null) 
	             {
	                     LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	                     row = inflater.inflate(R.layout.single_itemview, parent, false);
	                     holder = new ViewHolder();
	                     holder.profilepic = (RoundedImageView)row.findViewById(R.id.profile_pic);
	                     holder.time_stamp = (MyTextView) row.findViewById(R.id.time_stamp);
	                     holder.headline = (MyTextView) row.findViewById(R.id.headline);
	                     holder.price = (MyTextView) row.findViewById(R.id.price);
	                     holder.picture = (ImageView) row.findViewById(R.id.postpic);
	                     holder.name = (MyTextView) row.findViewById(R.id.username);
	                     row.setTag( holder );
	                     
	             } 
	             else{
	            	 holder=(ViewHolder)row.getTag();
	             }
	             
	             
	             final DiscoverData row_pos = discover_list.get(position);
	             Picasso.with(context)
	                .load(new MyConstants().profile_url+row_pos.getUserid()+"/")
	                .resize(100,100)
	                .centerCrop()
	                .into(holder.profilepic);
	             holder.headline.setText(row_pos.getHeading());
	             holder.time_stamp.setText(row_pos.getTimestamp());
	             holder.price.setText(row_pos.getPrice());
	             holder.name.setText(row_pos.getName());
	             new setBitmap(row_pos,holder.picture,1,position).execute();
	             holder.picture.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(context, FloatingPostAcitivity.class);
						i.putExtra("position",row_pos.getPostid());
						((Activity)context).startActivity(i);
					}
				});
	             
	             
	             return row;

	     }
	     
	     private class setBitmap  extends AsyncTask<String,Void,Bitmap>{

	     	private ImageView imageAnim;
	     	private int imgNum ;
	     	private int position;
	     	private DiscoverData item;
	     	
	     	public setBitmap(DiscoverData item,ImageView imageAnim,int imgNum,int position){
	     		this.imageAnim = imageAnim;
	     		this.imgNum = imgNum;
	     		this.position = position;
	     		this.item = item;
	     	}
	     	
	 		@Override
	 		protected Bitmap doInBackground(String... params) {
	 			// TODO Auto-generated method stub
	 			// TODO Auto-generated method stub
	 			String iconsStoragePath = Environment.getExternalStorageDirectory() + "/grabbrApp/myImages/";
	 	    	File sdIconStorageDir = new File(iconsStoragePath);
	 	    	String filename = item.getPostid() + "_" + imgNum + ".jpg";
	 			String filePath = sdIconStorageDir.toString()+"/" + filename;
	 			File file = new File(filePath);
	 			Bitmap bitmap = null;
	 			if(!file.exists()){
	 				bitmap = new DownloadImage(new MyConstants().postimage_url+item.getPostid()+"/"+1+"/",item.getPostid(),1, context).getBitmap();
	 			}else{
	 				bitmap = getBitamp(1,item.getPostid());
	 			}
	 			return bitmap;
	 		}
	 		
	 		@Override
	 		protected void onPostExecute(Bitmap result) {
	 			// TODO Auto-generated method stub
	 			super.onPostExecute(result);
	 			if(result!=null){
	 				//System.out.println(result.toString());
	 				imageAnim.setImageBitmap(result);
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


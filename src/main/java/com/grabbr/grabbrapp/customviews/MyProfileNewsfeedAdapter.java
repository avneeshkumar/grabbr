package com.grabbr.grabbrapp.customviews;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.FloatingIsSoldActivity;
import com.grabbr.grabbrapp.daos.NewsfeedData;
import com.grabbr.grabbrapp.services.DownloadImage;
import com.grabbr.grabbrapp.services.SendLike;
import com.grabbr.grabbrapp.services.Sendsold;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileNewsfeedAdapter extends BaseAdapter {
	private Context context;
    List<NewsfeedData> rowItems;
    private SharedPreferences sharedpreferences;
    private MyConstants constant;
    private static LayoutInflater inflater=null;
    int count1 = 0;
    int result_sold=100;
    

    public MyProfileNewsfeedAdapter(Context context){
        this.context = context;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

 
    public void setRowItems(List<NewsfeedData> rowItems) {
		this.rowItems = rowItems;
		count1 = rowItems.size();
		//Log.d("aa",""+count1);
	}

    public void setCount1(int count1){
    	this.count1 = count1;
    }

	public int getCount1() {
        return count1;
    }

	public int getCount() {
        return rowItems.size();
    }

 
    public Object getItem(int position) {
        return rowItems.get(position);
    }


    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    public static class ViewHolder{
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
        MyTextView itemsold_tv;
        TextView sold;
        

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View view =convertView;
        final ViewHolder holder;
        
        final MyConstants c = new MyConstants();
        
        final SharedPreferences sharedpreferences;
        sharedpreferences = context.getSharedPreferences(c.MyPREFERENCES, Context.MODE_PRIVATE);

        if (view == null) {
            view = inflater.inflate(R.layout.my_profile_post_element, null);
            holder = new ViewHolder();
            holder.profile_pic = (RoundedImageView)view.findViewById(R.id.profile_pic);
            holder.post_image = (ImageView)view.findViewById(R.id.post_image);
            holder.like_icon = (ImageView)view.findViewById(R.id.like_icon);
            holder.share_icon = (ImageView)view.findViewById(R.id.share_icon);
            holder.more_icon = (ImageView)view.findViewById(R.id.more_icon);
            holder.username = (TextView)view.findViewById(R.id.username);
            holder.time = (TextView)view.findViewById(R.id.time_stamp);
            holder.location_stamp = (TextView)view.findViewById(R.id.location_stamp);
            holder.item_title = (TextView)view.findViewById(R.id.item_title);
            holder.description = (TextView)view.findViewById(R.id.description);
            holder.price_id = (TextView)view.findViewById(R.id.price_id);
            holder.like_count = (TextView)view.findViewById(R.id.like_count);
            holder.share_count = (TextView)view.findViewById(R.id.share_count);
            holder.sold = (TextView)view.findViewById(R.id.sold);
            holder.itemsold_tv = (MyTextView) view.findViewById(R.id.item_sold);
            
            view.setTag( holder );
        }else
            holder=(ViewHolder)view.getTag();

        final NewsfeedData row_pos = rowItems.get(position);


        holder.username.setText(row_pos.getName());
        holder.time.setText(row_pos.getTimeStamp());
        holder.item_title.setText(row_pos.getHeading());
        holder.description.setText(row_pos.getDescription());
        holder.price_id.setText(""+row_pos.getPrice());
        holder.like_count.setText(row_pos.getLike());
        holder.share_count.setText("1");
        holder.location_stamp.setText(row_pos.getCity());

        holder.like_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(row_pos.getLiked().equals("0")){
					if(sharedpreferences.getString(c.isfb,"").equals("0")){
						
						new SendLike().execute(c.like_url+row_pos.getPostId()+"/","0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
					}
					else{
						new SendLike().execute(c.like_url+row_pos.getPostId()+"/","1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
					}
					holder.like_count.setText(""+(Integer.parseInt(row_pos.getLike())+1));
					rowItems.get(position).setLike(""+(Integer.parseInt(row_pos.getLike())+1));
					rowItems.get(position).setLiked("1");
				}
				else{
					if(sharedpreferences.getString(c.isfb,"").equals("0")){
						
						new SendLike().execute(c.like_url+row_pos.getPostId()+"/","0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
					}
					else{
						new SendLike().execute(c.like_url+row_pos.getPostId()+"/","1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
					}
					holder.like_count.setText(""+(Integer.parseInt(row_pos.getLike())-1));
					rowItems.get(position).setLike(""+(Integer.parseInt(row_pos.getLike())-1));
					rowItems.get(position).setLiked("0");
					
					
				}
			}
		});
        
        if(row_pos.getIsSold().equals("1")){
        	Log.d("isSoldTest5", "true");
        	holder.itemsold_tv.setVisibility(View.VISIBLE);
        	holder.sold.setVisibility(View.INVISIBLE);
        	
        }
        else{
        	holder.sold.setVisibility(View.VISIBLE);
        	holder.itemsold_tv.setVisibility(View.INVISIBLE);
        	Log.d("isSoldTest6", "false");
        }
        
        //imageLoader.DisplayImage(constant.profile_url+row_pos.getUserId()+"/",holder.imageView1);
        Picasso.with(context)
                .load(constant.profile_url+row_pos.getUserId()+"/")
                .resize(100,100)
                .centerCrop()
                .into(holder.profile_pic);

//        AnimationDrawable animation = new AnimationDrawable();
//        if(row_pos.getCurrImg()==1){
//        	new setAnimation(row_pos, holder.post_image,1).execute();
//        }
        new setBitmap(row_pos,holder.post_image,1,position).execute();
        
        holder.sold.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Log.d(MyProfileNewsfeedAdapter.class.getName(),"sold click");
				Intent i = new Intent(context, FloatingIsSoldActivity.class);
				i.putExtra("position",position);
				((Activity)context).startActivityForResult(i,result_sold);
				
			}
		});
        
        
       
        //view.setOnClickListener(new OnItemClickListener(position));
        //Log.d("asfgasfgaisfias",""+rowItems.size());
        return view;
    }
    
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", ""+requestCode);
        if (requestCode == result_sold) {
            if(resultCode == Activity.RESULT_OK){
            	
                String result=data.getStringExtra("keypressed");
                NewsfeedData row_pos = rowItems.get(data.getIntExtra("position", 0));
                MyConstants c = new MyConstants();
               
                if(result.equals("yes")){
                	
                	
                	
                	if(row_pos.getIsSold().equals("0")){
    					if(sharedpreferences.getString(c.isfb,"").equals("0")){
    						
    						new Sendsold().execute(c.sold_url+row_pos.getPostId()+"/","0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
    					}
    					else{
    						new Sendsold().execute(c.sold_url+row_pos.getPostId()+"/","1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
    					}
//    					holder.like_count.setText(""+(Integer.parseInt(row_pos.getLike())+1));
//    					rowItems.get(position).setLike(""+(Integer.parseInt(row_pos.getLike())+1));
//    					rowItems.get(position).setLiked("1");
    				}
    				else{
    					if(sharedpreferences.getString(c.isfb,"").equals("0")){
    						
    						new Sendsold().execute(c.sold_url+row_pos.getPostId()+"/","0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
    					}
    					else{
    						new Sendsold().execute(c.sold_url+row_pos.getPostId()+"/","1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
    					}
//    					holder.like_count.setText(""+(Integer.parseInt(row_pos.getLike())-1));
//    					rowItems.get(position).setLike(""+(Integer.parseInt(row_pos.getLike())-1));
//    					rowItems.get(position).setLiked("0");
    					
    					
    				}
                }
                
                
            }
            
        }
        
    }
    

    private class OnItemClickListener implements OnClickListener{

        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(context,mPosition+"",Toast.LENGTH_SHORT).show();
		}
    }
    
    private class setBitmap  extends AsyncTask<String,Void,Bitmap>{

    	private ImageView imageAnim;
    	private int imgNum ;
    	private int position;
    	private NewsfeedData item;
    	
    	public setBitmap(NewsfeedData item,ImageView imageAnim,int imgNum,int position){
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
	    	item = rowItems.get(position); 
	    	String filename = item.getPostId() + "_" + imgNum + ".jpg";
			String filePath = sdIconStorageDir.toString()+"/" + filename;
			File file = new File(filePath);
			Bitmap bitmap = null;
			if(!file.exists()){
				bitmap = new DownloadImage(constant.postimage_url+item.getPostId()+"/"+1+"/",item.getPostId(),1, context).getBitmap();
			}else{
				bitmap = getBitamp(1,item.getPostId());
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

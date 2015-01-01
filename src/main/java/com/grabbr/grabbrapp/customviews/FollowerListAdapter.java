package com.grabbr.grabbrapp.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.FloatingProfileActivity;
import com.grabbr.grabbrapp.daos.FollowersData;
import com.grabbr.grabbrapp.services.Follow;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arun on 13/12/14.
 */
public class FollowerListAdapter extends BaseAdapter {

    private ArrayList<FollowersData> myList = new ArrayList<FollowersData>();
    private LayoutInflater inflater;
    private Context context;
    private SharedPreferences sharedpreferences;
    private MyConstants constant;
    String follow_follwing;
    

    public FollowerListAdapter(Context context, ArrayList<FollowersData> myList,String f){
        this.myList = myList;
        this.context = context;
        follow_follwing = f;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class MyViewHolder {
    	MyTextView  name;
        ImageView profilepic;
        RelativeLayout follow;
        ImageView followbutton;
        MyTextView followtext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MyViewHolder mViewHolder;
        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.follow_following, null);
            mViewHolder = new MyViewHolder();
            mViewHolder.name = (MyTextView ) view.findViewById(R.id.name);
            mViewHolder.profilepic = (ImageView) view.findViewById(R.id.profile_pic2);
            mViewHolder.follow = (RelativeLayout) view.findViewById(R.id.follow);
            mViewHolder.followbutton = (ImageView)view.findViewById(R.id.follow_button);
            mViewHolder.followtext = (MyTextView)view.findViewById(R.id.follow_text);
            
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) view.getTag();
        }

        final FollowersData row_pos = myList.get(position);
        mViewHolder.name.setText(row_pos.getName());
        Picasso.with(context)
                .load(constant.profile_url+row_pos.getUserID()+"/")
                .resize(100,100)
                .centerCrop()
                .into(mViewHolder.profilepic);
        mViewHolder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,row_pos.getUserID(),Toast.LENGTH_SHORT).show();
                
                Follow follow = new Follow(context,mViewHolder.followbutton,mViewHolder.followtext);
                follow.execute(constant.follow_url, row_pos.getUserID());
                if(follow_follwing.equals("follower")){
                	if(row_pos.isFollowed().equals("1")){
                		mViewHolder.followtext.setText("Follow");
                		myList.get(position).setFollowed("0");
                	}
                	
                	else{
                		mViewHolder.followtext.setText("UnFollow");
                		myList.get(position).setFollowed("1");
                	}
                }
                else if(follow_follwing.equals("following")){
                	if(row_pos.isFollowed().equals("1")){
                		mViewHolder.followtext.setText("Follow");
                		myList.get(position).setFollowed("0");
                	}
                	
                	else{
                		mViewHolder.followtext.setText("UnFollow");
                		myList.get(position).setFollowed("1");
                	}
                }
            }
        });
        
        
        mViewHolder.name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, FloatingProfileActivity.class);
				i.putExtra("userid",row_pos.getUserID());
				((Activity)context).startActivity(i);
			}
		});
        mViewHolder.profilepic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, FloatingProfileActivity.class);
				i.putExtra("userid",row_pos.getUserID());
				((Activity)context).startActivity(i);
				
			}
		});
        
        Toast.makeText(context,""+row_pos.isFollowed(),Toast.LENGTH_SHORT).show();
        if(follow_follwing.equals("follower")){
        	if(row_pos.isFollowed().equals("1")){
        		mViewHolder.followtext.setText("UnFollow");
        	}
        	
        	else{
        		mViewHolder.followtext.setText("Follow");
        	}
        }
        else if(follow_follwing.equals("following")){
        	if(row_pos.isFollowed().equals("1")){
        		mViewHolder.followtext.setText("UnFollow");
        	}
        	
        	else{
        		mViewHolder.followtext.setText("Follow");
        	}
        }
        
        return view;
    }


}

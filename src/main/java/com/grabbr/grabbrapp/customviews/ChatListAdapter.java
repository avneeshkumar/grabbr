package com.grabbr.grabbrapp.customviews;

import java.util.List;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.activities.FloatingChatActivity;
import com.grabbr.grabbrapp.daos.ChatListData;
import com.grabbr.grabbrapp.utils.Globalchatconnection;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChatListAdapter extends BaseAdapter {
	private Context context;
    List<ChatListData> rowItems;
    private SharedPreferences sharedpreferences;
    private MyConstants constant;
    private static LayoutInflater inflater=null;
    int count1 = 0;

    public ChatListAdapter(Context context){
        this.context = context;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
    }

    public void setRowItems(List<ChatListData> rowItems) {
		this.rowItems = rowItems;
		count1 = rowItems.size();
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
        MyTextView time_stamp;
        MyTextView buddy_name;
        RelativeLayout rl;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view =convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.chat_display, null);
            holder = new ViewHolder();
            holder.profile_pic = (RoundedImageView)view.findViewById(R.id.profile_pic);
            holder.time_stamp = (MyTextView) view.findViewById(R.id.time_stamp);
            holder.buddy_name = (MyTextView) view.findViewById(R.id.buddy_name);
            holder.rl = (RelativeLayout) view.findViewById(R.id.relativelayout);
            view.setTag( holder );
        }else
            holder=(ViewHolder)view.getTag();
        /*******************Read from the database and add here*****************************/
        final ChatListData row_pos = rowItems.get(position);


        holder.buddy_name.setText(row_pos.getName());
        holder.time_stamp.setText("2 hrs ago");
       
        //imageLoader.DisplayImage(constant.profile_url+row_pos.getUserId()+"/",holder.imageView1);
        Picasso.with(context)
                .load(constant.profile_url+row_pos.getId()+"/")
                .resize(100,100)
                .centerCrop()
                .into(holder.profile_pic);
        holder.rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Globalchatconnection.loginError==1){
					Toast.makeText(context, "unable to login to cha", Toast.LENGTH_SHORT).show();				
				}else{
					Intent i = new Intent(context, FloatingChatActivity.class);
					i.putExtra("username", row_pos.getName());
					i.putExtra("userid", row_pos.getId());
					((Activity)context).startActivity(i);
				}	
				
			}
		});
       
        view.setOnClickListener(new OnItemClickListener(position));

        return view;
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

}

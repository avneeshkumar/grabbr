package com.grabbr.grabbrapp.customviews;

import java.util.List;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.daos.NotificationData;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class NotificationListAdapter extends BaseAdapter {
	private Context context;
    List<NotificationData> rowItems;
    private SharedPreferences sharedpreferences;
    private MyConstants constant;
    private static LayoutInflater inflater=null;
    int count1 = 0;

    public NotificationListAdapter(Context context){
        this.context = context;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
    }

    public void setRowItems(List<NotificationData> rowItems) {
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
        MyTextView content;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view =convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.notification_view, null);
            holder = new ViewHolder();
            holder.profile_pic = (RoundedImageView)view.findViewById(R.id.profile_pic);
            holder.time_stamp = (MyTextView) view.findViewById(R.id.time_stamp);
            holder.content = (MyTextView) view.findViewById(R.id.content);
            view.setTag( holder );
        }else
            holder=(ViewHolder)view.getTag();

        NotificationData row_pos = rowItems.get(position);


        holder.content.setText(row_pos.getText());
        holder.time_stamp.setText(row_pos.getTimestamp());
       
        //imageLoader.DisplayImage(constant.profile_url+row_pos.getUserId()+"/",holder.imageView1);
        Picasso.with(context)
                .load(constant.profile_url+row_pos.getUserid2()+"/")
                .resize(100,100)
                .centerCrop()
                .into(holder.profile_pic);

       
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

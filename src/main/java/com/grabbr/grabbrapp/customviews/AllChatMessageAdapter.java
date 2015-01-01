package com.grabbr.grabbrapp.customviews;

import java.util.List;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.daos.AllMessages;
import com.grabbr.grabbrapp.utils.MyConstants;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class AllChatMessageAdapter extends BaseAdapter {
	private Context context;
    List<AllMessages> rowItems;
    private SharedPreferences sharedpreferences;
    private MyConstants constant;
    private static LayoutInflater inflater=null;
    int count1 = 0;

    public AllChatMessageAdapter(Context context){
        this.context = context;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
    }

    public void setRowItems(List<AllMessages> rowItems) {
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
        MyTextView message;

    }
    

    public View getView(int position, View convertView, ViewGroup parent) {

        View view =convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.incoming_outgoing_message, null);
            holder = new ViewHolder();
            holder.message = (MyTextView)view.findViewById(R.id.message);
            view.setTag( holder );
        }else
            holder=(ViewHolder)view.getTag();
        /*******************Read from the database and add here*****************************/
        final AllMessages row_pos = rowItems.get(position);

        if(row_pos.getWho().equals("1")){
        	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	params.weight = 1.0f;
        	params.gravity = Gravity.LEFT;
        	holder.message.setBackgroundResource(R.drawable.incomingmsg_outline);
        	holder.message.setTextColor(context.getResources().getColor(R.color.textgrey));
        	holder.message.setLayoutParams(params);
        }
        else{
        	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	params.weight = 1.0f;
        	params.gravity = Gravity.RIGHT;
        	holder.message.setTextColor(context.getResources().getColor(android.R.color.white));
        	holder.message.setBackgroundResource(R.drawable.outgoingmsg_outline);
        	holder.message.setLayoutParams(params);
        }
        holder.message.setText(row_pos.getMessage());
        
       
        
       
        view.setOnClickListener(new OnItemClickListener(position));

        return view;
    }
    
    
    
    public void updaterowitem(AllMessages test){
    	rowItems.add(test);
    	notifyDataSetChanged();
    	
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

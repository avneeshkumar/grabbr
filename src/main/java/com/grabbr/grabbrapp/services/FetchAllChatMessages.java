package com.grabbr.grabbrapp.services;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import com.grabbr.grabbrapp.customviews.AllChatMessageAdapter;
import com.grabbr.grabbrapp.daos.AllMessages;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.PackageDB;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ListView;

public class FetchAllChatMessages extends AsyncTask<String,Void , ArrayList<ArrayList<String>> > {

    private Context context;
    private SharedPreferences sharedpreferences;
    public AllChatMessageAdapter adapter;
    private MyConstants constant;
    private HttpClient httpclient;
    private ListView lv;
    ArrayList<AllMessages> chatlist;
    String id;

    public FetchAllChatMessages(Context context,ListView lv2,ArrayList<AllMessages> chatlist,String id,AllChatMessageAdapter adapter) {
        this.context = context;
        this.lv = lv2;
        this.chatlist = chatlist;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        this.id=id;
        httpclient = new DefaultHttpClient();
        this.adapter = adapter;
        
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    

	@Override
	protected ArrayList<ArrayList<String>> doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		
		ArrayList<ArrayList<String>>arrayList = new PackageDB(context,id).showTableValues(); 
		
		return arrayList;
	}
	
	@Override
	protected void onPostExecute(ArrayList<ArrayList<String>> arrayList) {
		//System.out.println(arrayList.size());
		for (int i = 0; i < arrayList.size(); i++) {
	
			AllMessages ri = new AllMessages();
			
			ArrayList<String> temp = arrayList.get(i);
			ri.setId(temp.get(0));
			ri.setWho((temp.get(1)));
			ri.setMessage(temp.get(2));
			ri.setTimestamp(temp.get(3));
			chatlist.add(ri);
		}
		adapter.setRowItems(chatlist);
		lv.setAdapter(adapter);
		//lv.setExpanded(true);
		
		
		
		
		
	}
	


}


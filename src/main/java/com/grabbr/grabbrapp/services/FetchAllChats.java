package com.grabbr.grabbrapp.services;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import com.grabbr.grabbrapp.customviews.ExpandableHeightListView;
import com.grabbr.grabbrapp.customviews.ChatListAdapter;
import com.grabbr.grabbrapp.daos.ChatListData;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.PackageDB;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class FetchAllChats extends AsyncTask<String,Void , ArrayList<ArrayList<String>> > {

    private Context context;
    private SharedPreferences sharedpreferences;
    public ChatListAdapter adapter;
    private MyConstants constant;
    private HttpClient httpclient;
    private ExpandableHeightListView lv;
    ArrayList<ChatListData> chatlist;

    public FetchAllChats(Context context,ExpandableHeightListView lv,ArrayList<ChatListData> chatlist) {
        this.context = context;
        this.lv = lv;
        this.chatlist = chatlist;
        constant = new MyConstants();
        sharedpreferences = context.getSharedPreferences(constant.MyPREFERENCES, Context.MODE_PRIVATE);
        httpclient = new DefaultHttpClient();
        adapter = new ChatListAdapter(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    

	@Override
	protected ArrayList<ArrayList<String>> doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		System.out.println("aaya");
		ArrayList<ArrayList<String>>arrayList = new PackageDB(context).showUserTableValues(); 
		
		return arrayList;
	}
	
	@Override
	protected void onPostExecute(ArrayList<ArrayList<String>> arrayList) {
		//System.out.println(arrayList.size());
		for (int i = 0; i < arrayList.size(); i++) {
	
			ChatListData ri = new ChatListData();
			
			ArrayList<String> temp = arrayList.get(i);
			ri.setId(temp.get(0));
			ri.setName(temp.get(1));
			chatlist.add(ri);
		}
		adapter.setRowItems(chatlist);
		lv.setAdapter(adapter);
		lv.setExpanded(true);
		
		
		
		
		
	}
	


}


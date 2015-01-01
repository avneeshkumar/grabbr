package com.grabbr.grabbrapp.fragments;

import java.util.ArrayList;
import java.util.List;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.NewsfeedListAdapter;
import com.grabbr.grabbrapp.daos.NewsfeedData;
import com.grabbr.grabbrapp.services.FetchMorePosts;
import com.grabbr.grabbrapp.services.FetchPosts;
import com.grabbr.grabbrapp.utils.MyConstants;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class NewsfeedFragment extends Fragment {
	
	 public  NewsfeedListAdapter adapter;
	 private List<NewsfeedData> rowItems;
	 FetchPosts fp ;
	 FetchMorePosts fetchMorePosts;
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        return inflater.inflate(R.layout.layout_5a, null, false);
        
    }

    @SuppressLint("NewApi")
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new NewsfeedListAdapter(getActivity());
        rowItems = new ArrayList<NewsfeedData>();
        
        final ListView lv =(ListView) getView().findViewById(R.id.list);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        lv.setOnScrollListener(new OnScrollListener() {
		
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == SCROLL_STATE_IDLE) {
					if(adapter.getCount1()>0){
				        if (view.getLastVisiblePosition() >= adapter.getCount1() - 1) {
				            //load more list items:
				        	adapter.setCount1(adapter.getCount1()+new MyConstants().noOfpostOnetime);
				        	fetchMorePosts = new FetchMorePosts(getActivity(),
				        			lv,adapter
				        			,(NewsfeedData) view.getItemAtPosition(view.getCount()-1));
				        	String more_url = new MyConstants().morepostview_url;
				        	fetchMorePosts.execute(more_url);
				        }
					}
				}
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
        
        fp = new FetchPosts(getActivity(),lv,rowItems,adapter);
        fp.execute(new MyConstants().postview_url);
    }
        
    @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		fp.cancel(true);
	}
    
}

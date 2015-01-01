package com.grabbr.grabbrapp.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import com.grabbr.grabbrapp.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class Globalchatconnection extends AsyncTask<Void, Void, Void> implements MessageListener{

	public static XMPPConnection connection;
	public static int loginError =1;
	public static int isChatActivityOn =0;
	private String password; 
	private String username;
	Context con;
	public static NotificationManager NM;
	public static Notification notify;
	public static PendingIntent pending;
	public Globalchatconnection(String username,String password,Context context) {
		this.username = username;
		this.password = password;
		con=context;
	}
	
	
	
	@SuppressLint("NewApi")
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        NM=(NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //notify=new Notification(android.R.drawable.stat_notify_more,"New message",System.currentTimeMillis());
        notify = new Notification.Builder(con).setContentTitle("New message Received").setContentText("New message received").setSmallIcon(R.drawable.grabbr_image).setSound(soundUri).build();
        pending=PendingIntent.getActivity(con,0, new Intent(),0);
    }
	
	
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		ConnectionConfiguration config = new ConnectionConfiguration(new MyConstants().chat_url,5222);
		
		config.setDebuggerEnabled(true);
	    config.setSecurityMode(SecurityMode.disabled);
	    
	    
	    //SASLAuthentication.supportSASLMechanism("PLAIN", 0);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			config.setTruststoreType("AndroidCAStore");
			config.setTruststorePassword(null);
			config.setTruststorePath(null);
		} else {
			config.setTruststoreType("BKS");
		    String path = System.getProperty("javax.net.ssl.trustStore");
		    if (path == null)
		        path = System.getProperty("java.home") + File.separator + "etc"
		            + File.separator + "security" + File.separator
		            + "cacerts.bks";
		    config.setTruststorePath(path);
		}
		if(loginError==1){
	    connection = new XMPPConnection(config);
	    try {
			connection.connect(); 
			System.out.println(new MyConstants().id+"   "+password);
			connection.login(username,password.replaceAll(" ", password));
			loginError=0;
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		    connection.addPacketListener(new PacketListener() {
		    	@Override
	            public void processPacket(Packet packet) {

	                Message message = (Message) packet;
	                if (message.getBody() != null) {
	                    String fromName = StringUtils.parseBareAddress(message
	                            .getFrom());
	                    Log.i("XMPPClient", "Got text [" + message.getBody()
	                            + "] from [" + fromName + "]");
	                    
	                    String[] parts = fromName.split("@");
	                    PackageDB chatdb= new PackageDB(con);
	                    String json = null;
	                    
	                    if(!chatdb.isUserAvailable(parts[0])){
		                    chatdb.createTablewithUserID(parts[0]);
		                    DefaultHttpClient httpClient = new DefaultHttpClient();
		                    HttpGet httpGet = new HttpGet(new MyConstants().getnamefromid_url+parts[0]+"/");
	
		                    HttpResponse httpResponse;
		                    
							try {
								httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
			                    InputStream instream = null;
			                    instream = httpEntity.getContent();
			                    json = MyUtils.convertStreamToString(instream);
			                    json = json.replaceAll("\"", "");
			                    System.out.println(json);
			                    chatdb.insertIntoUserTable(parts[0], json);
			                    chatdb.insertIntoTablewithID("1", message.getBody(), parts[0]);
			                    if(isChatActivityOn==0){
			                    	notify.setLatestEventInfo(con,"New Message Received",message.getBody(),pending);
			                        NM.notify(0, notify);
			                    }
			                    
			                    
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
		                    
	                	}
	                    else{
	                    	chatdb.insertIntoTablewithID("1", message.getBody(), parts[0]);
	                    	if(isChatActivityOn==0){
		                    	notify.setLatestEventInfo(con,"New Message Received",message.getBody(),pending);
		                        NM.notify(0, notify);
		                    }
	                    }
	                    
	                    
	                    
	                
	                    
	                    
	                    
	                    
	                    

	                }
	            }

				
				
	        }, filter);
	    
			
			} catch (XMPPException e1) {
				// TODO Auto-generated catch block
				loginError=1;
				e1.printStackTrace();
			}
		}
	   
	    

		return null;
	}
	

	@Override
	public void processMessage(Chat arg0, Message arg1) {
		// TODO Auto-generated method stub
		
	}

}

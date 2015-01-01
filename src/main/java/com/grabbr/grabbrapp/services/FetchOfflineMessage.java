package com.grabbr.grabbrapp.services;

import java.io.File;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.PackageDB;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class FetchOfflineMessage extends IntentService {
	
	public static XMPPConnection connection;
	public static int loginError =1;
	public static int isChatActivityOn =0;
	private String password; 
	private String username;
	Context con;
	public static NotificationManager NM;
	public static Notification notify;
	public static PendingIntent pending;
	SharedPreferences sharedPreferences;
	
	
	public FetchOfflineMessage() {
		super("FetchOfflineMessage");
		// TODO Auto-generated constructor stub
		
	}
	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		super.onCreate();
		con = FetchOfflineMessage.this;
		sharedPreferences = getSharedPreferences(new MyConstants().MyPREFERENCES, Context.MODE_PRIVATE);
		username = sharedPreferences.getString(new MyConstants().id, "");
		password = sharedPreferences.getString(new MyConstants().chatpassword, "");
		NM=(NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //notify=new Notification(android.R.drawable.stat_notify_more,"New message",System.currentTimeMillis());
        notify = new Notification.Builder(con).setContentTitle("New message Received").setContentText("New message received").setSmallIcon(android.R.drawable.stat_notify_more).setSound(soundUri).build();
        pending=PendingIntent.getActivity(con,0, new Intent(),0);
	}
	

	@SuppressLint("DefaultLocale")
	@Override
	protected void onHandleIntent(Intent intent) {
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
	                    if(isChatActivityOn==0){
	                    	notify.setLatestEventInfo(con,fromName,message.getBody(),pending);
	                        NM.notify(0, notify);
	                    }
	                    String[] parts = fromName.split("@");
	                    PackageDB chatdb= new PackageDB(con);
	                    chatdb.createTablewithUserID(parts[0]);
	                    chatdb.insertIntoUserTable(parts[0], "testing offline chats");
	                    chatdb.insertIntoTablewithID("1", message.getBody(), parts[0]);
	                    
	                    
	                    
	                    
	                    

	                }
	            }

				
				
	        }, filter);
	    
			
			} catch (XMPPException e1) {
				// TODO Auto-generated catch block
				loginError=1;
				e1.printStackTrace();
			}
		}
	   
	    

		
		

	}
}

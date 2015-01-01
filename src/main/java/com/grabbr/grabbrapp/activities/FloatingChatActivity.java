package com.grabbr.grabbrapp.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

import com.grabbr.grabbrapp.R;
import com.grabbr.grabbrapp.customviews.MyTextView;
import com.grabbr.grabbrapp.customviews.RoundedImageView;
import com.grabbr.grabbrapp.customviews.AllChatMessageAdapter;
import com.grabbr.grabbrapp.daos.AllMessages;
import com.grabbr.grabbrapp.services.FetchAllChatMessages;
import com.grabbr.grabbrapp.utils.Globalchatconnection;
import com.grabbr.grabbrapp.utils.MyConstants;
import com.grabbr.grabbrapp.utils.PackageDB;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class FloatingChatActivity extends Activity implements MessageListener {
	
	RoundedImageView profile_pic;
	MyTextView buddyname;
	EditText messagesender;
	PackageDB chatdb;
	ImageButton sendbutton;
	Chat chat;
	ListView lv;
	public  AllChatMessageAdapter adapter;
	ArrayList<AllMessages> rowItems;
	XMPPConnection connection;
	SharedPreferences sharedPreferences;
	ImageButton back_button;
	ChatManager chatManager;
	String username;
	String id;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_floating_chat);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		params.height = height - dpToPx(112);
		/******************Display getting over here****************************************************/
		Intent i = getIntent();
		username = i.getStringExtra("username");
		id = i.getStringExtra("userid");
		profile_pic = (RoundedImageView) findViewById(R.id.profile_pic);
		buddyname = (MyTextView) findViewById(R.id.person_name);
		sendbutton = (ImageButton) findViewById(R.id.send_button);
		messagesender = (EditText) findViewById(R.id.message_sender);
		back_button = (ImageButton) findViewById(R.id.back_icon);
		Globalchatconnection.isChatActivityOn=1;
		sharedPreferences = getSharedPreferences(new MyConstants().MyPREFERENCES, Context.MODE_PRIVATE);
		adapter = new AllChatMessageAdapter(getApplicationContext());
			
		try {
			login(id+"@"+new MyConstants().chat_url);
			
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new AllChatMessageAdapter(getApplicationContext());
	    rowItems = new ArrayList<AllMessages>();
		
	    lv = (ListView) findViewById(R.id.list);
	    
	    /**
	     *  Listview Scroll to the end of the list after updating the list
	     */
	    
	    lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	    lv.setStackFromBottom(true);
		
		messagesender.setHint("Write to "+username);
		buddyname.setText(username);
		chatdb = new PackageDB(getApplicationContext(),id);
		chatdb.createTable();
		chatdb.insertIntoUserTable(id, username);
		new FetchAllChatMessages(getApplicationContext(),lv,rowItems,id,adapter).execute(new MyConstants().notification_url);
		Picasso.with(getApplicationContext())
        .load(new MyConstants().profile_url+id+"/")
        .resize(100,100)
        .centerCrop()
        .into(profile_pic);
		chatdb.showTableValues();
		if(Globalchatconnection.loginError==1){
			Toast.makeText(getApplicationContext(), "unable to login to cha", Toast.LENGTH_SHORT).show();
			finish();
		}	
		
		
		
		
		sendbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!messagesender.getText().toString().replaceAll("\\s", "").equals("")){
					
					try {
						System.out.println();
						sendMessage(messagesender.getText().toString());
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					chatdb.insertIntoTable("0", messagesender.getText().toString());
					
					AllMessages test = new AllMessages();
					test.setId("1");
					test.setTimestamp("0");
					test.setMessage(messagesender.getText().toString());
					
					test.setWho("0");
					adapter.updaterowitem(test);
					//adapter.notifyDataSetChanged();
					messagesender.setText("");
					lv.invalidate();
					
				}
				
			}
		});
		
		back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		
		
	}
	
	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
	public void login(String to) throws XMPPException, IOException
    {
		
		connection = Globalchatconnection.connection;
		
//		System.out.println(userName+"  "+password);
//		ConnectionConfiguration config = new ConnectionConfiguration(new constants().chat_url,5222);
//		
//		config.setDebuggerEnabled(true);
//	    config.setSecurityMode(SecurityMode.disabled);
//	    
//	    
//	    //SASLAuthentication.supportSASLMechanism("PLAIN", 0);
////		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
////			config.setTruststoreType("AndroidCAStore");
////			config.setTruststorePassword(null);
////			config.setTruststorePath(null);
////		} else {
////			config.setTruststoreType("BKS");
////		    String path = System.getProperty("javax.net.ssl.trustStore");
////		    if (path == null)
////		        path = System.getProperty("java.home") + File.separator + "etc"
////		            + File.separator + "security" + File.separator
////		            + "cacerts.bks";
////		    config.setTruststorePath(path);
////		}
//		
//	    connection = new XMPPConnection(config);
//	    connection.connect();
//	    connection.login(userName,password.replaceAll(" ", password));
//	    PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
//	    connection.addPacketListener(new PacketListener() {
//	    	@Override
//            public void processPacket(Packet packet) {
//
//                Message message = (Message) packet;
//                if (message.getBody() != null) {
//                    String fromName = StringUtils.parseBareAddress(message
//                            .getFrom());
//                    Log.i("XMPPClient", "Got text [" + message.getBody()
//                            + "] from [" + fromName + "]");
//                    String[] parts = fromName.split("@");
//                    chatdb.createTablewithUserID(parts[0]);
//                    chatdb.insertIntoUserTable(parts[0], "testing offline chats");
//                    chatdb.insertIntoTablewithID("1", message.getBody(), parts[0]);
//                    if (fromName.equals(id+"@"+new constants().chat_url)) {
//
//        				String mes = message.getBody();
//        				//chatdb.insertIntoTable("1", mes);
//        			    System.out.println(chat.getParticipant() + " says: " + mes);
//        				
//        				all_messages test = new all_messages();
//        				test.setId("1");
//        				test.setTimestamp("0");
//        				test.setMessage(message.getBody());
//        				
//        				test.setWho("1");
//        				adapter.updaterowitem(test);
//                      
//                  }
//                    
//                    
//                    
//                    
//
//                }
//            }
//
//			
//			
//        }, filter);
	    chatManager = connection.getChatManager();
		chatManager.addChatListener(chatManagerListener);
	    chat = chatManager.createChat(to, this);
    }
	public void sendMessage(String message) throws XMPPException
    {
		
	    chat.sendMessage(message);
    }
	
	ChatManagerListener chatManagerListener = new ChatManagerListener() {
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            chat.addMessageListener(FloatingChatActivity.this);
        }
    };
    public void displayBuddyList(){
	    Roster roster = connection.getRoster();
	    //roster.createEntry(arg0, arg1, arg2)
	    Collection<RosterEntry> entries = roster.getEntries();
	 
	    System.out.println("\n\n" + entries.size() + " buddy(ies):");
	    for(RosterEntry r:entries){
	    	System.out.println(r.getUser());
	    }
	}
	 public void disconnect()
	    {
	    connection.disconnect();
	    }
	 

	@Override
	public void processMessage(Chat chat, Message message) {
		// TODO Auto-generated method stub
		String fromName = StringUtils.parseBareAddress(message.getFrom());
		if(message.getType() == Message.Type.chat){
			if (fromName.equals(id+"@"+new MyConstants().chat_url)) {

				String mes = message.getBody();
				//chatdb.insertIntoTable("1", mes);
			    System.out.println(chat.getParticipant() + " says: " + mes);
				
				AllMessages test = new AllMessages();
				test.setId("1");
				test.setTimestamp("0");
				test.setMessage(message.getBody());
				
				test.setWho("1");
				adapter.updaterowitem(test);
				//lv.setSelection(adapter.getCount()-1);
              
          }
			
			
			
			
			
			
			
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Globalchatconnection.isChatActivityOn=0;
		//disconnect();
	}

	
	
}


package com.grabbr.grabbrapp.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

public class PackageDB {

	SQLiteDatabase mydb;
	Context con;
	TextView textview;
	String USERTABLE = "usertable";
	private static String DBNAME = new MyConstants().DBNAME;    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "list_pacakge";       // THIS IS THE TABLE NAME
	public PackageDB(Context co,String table_name) {
		// TODO Auto-generated constructor stub
		con=co;
		TABLE = "chatlogs_"+table_name;
		createUserTable();
		
		
		//createTable();
	}
	public PackageDB(Context co) {
		// TODO Auto-generated constructor stub
		con=co;
		createUserTable();
	}
	public void createTable(){
        try{
        mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, WHO CHAR(2), MESSAGE TEXT, TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP );");
        mydb.close();
        }catch(Exception e){
            Toast.makeText(con, "Error in creating table", Toast.LENGTH_LONG).show();
        }
    }
	public void createTablewithUserID(String id){
        try{
        mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ "chatlogs_"+id +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, WHO CHAR(2), MESSAGE TEXT, TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP );");
        mydb.close();
        }catch(Exception e){
            Toast.makeText(con, "Error in creating table", Toast.LENGTH_LONG).show();
        }
    }
	public void createUserTable(){
        try{
        mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ USERTABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERID INTEGER UNIQUE, USERNAME TEXT);");
        mydb.close();
        }catch(Exception e){
            Toast.makeText(con, "Error in creating table", Toast.LENGTH_LONG).show();
        }
    }
	 public void insertIntoTable(String who,String text){
		 
	        try{
	            mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
	            mydb.execSQL("INSERT INTO " + TABLE + "(WHO, MESSAGE) VALUES('"+who+"','"+text+"')");
	            
	            mydb.close();
	        }catch(Exception e){
	            //Toast.makeText(con, "Error in inserting into table", Toast.LENGTH_LONG).show();
	        }
	    }
	 public void insertIntoTablewithID(String who,String text,String id){
		 //text = new DatabaseUtils().sqlEscapeString(text);
	        try{
	            mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
	            mydb.execSQL("INSERT INTO " + "chatlogs_"+id  + "(WHO, MESSAGE) VALUES('"+who+"','"+text+"')");
	            
	            mydb.close();
	        }catch(Exception e){
	            //Toast.makeText(con, "Error in inserting into table", Toast.LENGTH_LONG).show();
	        }
	    }
	 public void insertIntoUserTable(String userid,String username){
	        try{
	            mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
	            mydb.execSQL("INSERT INTO " + USERTABLE + "(USERID, USERNAME) VALUES('"+userid+"','"+username+"')");
	            
	            mydb.close();
	        }catch(Exception e){
	            //Toast.makeText(con, "Error in inserting into table", Toast.LENGTH_LONG).show();
	        }
	    }
	 
//	public void updateTable(String pacakge){
//        try{
//            mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
//            mydb.execSQL("UPDATE " + TABLE + " SET MYCOUNT = MYCOUNT+1 WHERE NAME = '"+ pacakge +"'");
//            mydb.close();
//        }catch(Exception e){
//            Toast.makeText(con, "Error encountered", Toast.LENGTH_LONG).show();
//        }
//    }
	public ArrayList<ArrayList<String>> showTableValues(){
		ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
        try{
            mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows  = mydb.rawQuery("SELECT * FROM "+  TABLE+" ORDER BY TIME ASC ", null);
           
 
            
            if(allrows.moveToFirst()){
                do{
                    
                	ArrayList<String> temp= new ArrayList<String>();
                	
                    String ID = allrows.getString(0);
                    String WHO= allrows.getString(1);
                    String MESSAGE= allrows.getString(2);
                    String TIME = allrows.getString(3);
                    //textview.setText(textview.getText().toString()+"\n"+ID+","+NAME+","+MYCOUNT);
                    temp.add(ID);
                    temp.add(WHO);
                    temp.add(MESSAGE);
                    temp.add(TIME);
                    arrayList.add(temp);
                    
                    
 
                    
                    
                }
                while(allrows.moveToNext());
            }
            try {
    			allrows.close();
    		} catch (Exception e) {
    			// TODO: handle exception
    		}finally{
    			mydb.close();
    		}
            
         }catch(Exception e){
            Toast.makeText(con, "Error encountered.", Toast.LENGTH_LONG).show();
        }
        mydb.close();
        return arrayList;
    }
	
	public ArrayList<ArrayList<String>> showUserTableValues(){
		ArrayList<ArrayList<String>>returnval= new ArrayList<ArrayList<String>>();
        try{
            mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows  = mydb.rawQuery("SELECT * FROM "+  USERTABLE, null);
            //System.out.println("MYCOUNT : " + allrows.getCount());
//            Integer cindex = allrows.getColumnIndex("WHO");
//            Integer cindex1 = allrows.getColumnIndex("MESSAGE");
            
            
            if(allrows.moveToFirst()){
                do{
                    
 
                    ArrayList<String> temp=new ArrayList<String>();
                    String userid= allrows.getString(1);
                    String username= allrows.getString(2);
                    
                    //textview.setText(textview.getText().toString()+"\n"+ID+","+NAME+","+MYCOUNT);
                    temp.add(userid);
                    temp.add(username);
                    returnval.add(temp);
                    
                    //System.out.println("ID : "+ ID  + " || WHO " + WHO + "|| MESSAGE : "+ MESSAGE);
                }
                while(allrows.moveToNext());
            }
            try {
    			allrows.close();
    		} catch (Exception e) {
    			// TODO: handle exception
    		}finally{
    			mydb.close();
    		}
            
         }catch(Exception e){
            Toast.makeText(con, "Error encountered.", Toast.LENGTH_LONG).show();
        }
        
       return returnval;
    }
	public boolean isUserAvailable(String id){
		int count=0;
        try{
            mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows  = mydb.rawQuery("SELECT * FROM "+  USERTABLE +" where USERID="+id, null);
            //System.out.println("MYCOUNT : " + allrows.getCount());
//            Integer cindex = allrows.getColumnIndex("WHO");
//            Integer cindex1 = allrows.getColumnIndex("MESSAGE");
            
            
            if(allrows.moveToFirst()){
                do{
                    
 
                    count++;
                    
                    //System.out.println("ID : "+ ID  + " || WHO " + WHO + "|| MESSAGE : "+ MESSAGE);
                }
                while(allrows.moveToNext());
            }
            try {
    			allrows.close();
    		} catch (Exception e) {
    			// TODO: handle exception
    		}finally{
    			mydb.close();
    		}
            
         }catch(Exception e){
            Toast.makeText(con, "Error encountered.", Toast.LENGTH_LONG).show();
        }
       if(count==0)
    	   return false;
       else
    	   return true;
    }
	
	public ArrayList<String> getAllTables(){
		ArrayList<String> arrTblNames = new ArrayList<String>();
		 mydb = con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		Cursor c = mydb.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		
	    if (c.moveToFirst()) {
	        while ( !c.isAfterLast() ) {
	            arrTblNames.add( c.getString( c.getColumnIndex("name")) );
	            c.moveToNext();
	        }
	    }
	    try {
			c.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			mydb.close();
		}
		return arrTblNames;
	}
	
	
	
	
}

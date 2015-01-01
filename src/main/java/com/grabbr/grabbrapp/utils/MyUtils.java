package com.grabbr.grabbrapp.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.grabbr.grabbrapp.activities.MainActivity;
import com.grabbr.grabbrapp.services.FacebookSignIn;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class MyUtils {
	
	
	public HttpResponse sendPost(String url,HttpEntity reqEntity,HttpClient httpclient){
        
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost httppost = new HttpPost(url);
        
        
        
        //File file = new File(realpath);
        
        
        httppost.setEntity(reqEntity);
        
        //System.out.println("executing request " + httppost.getRequestLine());
        
        HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       // httpclient.getConnectionManager().shutdown();
        
        return response;
       
       
        
}
	
	public static String convertStreamToString(InputStream is) {
		
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	   
	    String line = null;
	   
	    try {
	        while ((line = reader.readLine()) != null) {
	        	
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	    	 
	    } finally {
	        try {
	        	
	            is.close();
	        } catch (IOException e) {
	            
	        }
	    }
	    return sb.toString();
	}
	
	public void getUserData(final Session session, SessionState state,final Context context,final SharedPreferences sp,final MainActivity av)
	 {
	     if (state.isOpened())
	     {
	         Request.newMeRequest(session, new Request.GraphUserCallback()
	         {
	             public void onCompleted(GraphUser user, Response response)
	             {
	                 if (response != null)
	                 {
	                     try
	                     {
	                         String name = user.getName();
	                         String id = user.getId();
	                         // If you asked for email permission
	                         MyConstants c = new MyConstants();
	                         String email = (String) user.getProperty("email");
	                         new FacebookSignIn(context,sp,av).execute(c.login_url,id,session.getAccessToken(),"1",email);
	                         
	                         //Log.e("FBDATA", "Name: " + name + " Email: " + email + "AccessToken:"+session.getAccessToken());
	                         
	                     }
	                     catch (Exception e)
	                     {
	                         e.printStackTrace();
	                         Log.d("FBDATA", "Exception e");
	                     }

	                 }
	             }
	         }).executeAsync();
	     }
	 }
	
	
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	
	
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	
	public FileBody createfilebodyfrombmp(Context con,Bitmap bm){
		
		
		
		
		File file = new File(con.getCacheDir(),"test.jpg");
        try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        //Convert bitmap to byte array
        Bitmap bitmap = bm;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			fos.write(bitmapdata);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        
        //File file = new File(realpath);
        FileBody bin = new FileBody(file);
        return bin;
		
		
	}


    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {

            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                //Read byte from input stream

                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;

                //Write byte from output stream
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public long getFileSize(final File file) {
        if (file == null || !file.exists())
            return 0;
        if (!file.isDirectory())
            return file.length();
        final List<File> dirs = new LinkedList<File>();
        dirs.add(file);
        long result = 0;
        while (!dirs.isEmpty()) {
            final File dir = dirs.remove(0);
            if (!dir.exists())
                continue;
            final File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length == 0)
                continue;
            for (final File child : listFiles) {
                result += child.length();
                if (child.isDirectory())
                    dirs.add(child);
            }
        }

        return result;
    }
    	
    	public Bitmap get_Picture_bitmap(String imagePath)
        {
    		
    		
    		
    	    long size_file = getFileSize(new File(imagePath));

    	    size_file = (size_file) / 1000;// in Kb now
    	    int ample_size = 1;

    	    if (size_file <= 250) {

    	        System.out.println("SSSSS1111= " + size_file);
    	        ample_size = 2;

    	    } else if (size_file > 251 && size_file < 1500) {

    	        System.out.println("SSSSS2222= " + size_file);
    	        ample_size = 4;

    	    } else if (size_file >= 1500 && size_file < 3000) {

    	        System.out.println("SSSSS3333= " + size_file);
    	        ample_size = 8;

    	    } else if (size_file >= 3000 && size_file <= 4500) {

    	        System.out.println("SSSSS4444= " + size_file);
    	        ample_size = 12;

    	    } else if (size_file >= 4500) {

    	        System.out.println("SSSSS4444= " + size_file);
    	        ample_size = 16;
    	    }

    	    Bitmap bitmap = null;

    	    BitmapFactory.Options bitoption = new BitmapFactory.Options();
    	    bitoption.inSampleSize = ample_size;

    	    Bitmap bitmapPhoto = BitmapFactory.decodeFile(imagePath, bitoption);

    	    ExifInterface exif = null;
    	    try {
    	        exif = new ExifInterface(imagePath);
    	    } catch (IOException e) {
    	        // Auto-generated catch block
    	        e.printStackTrace();
    	    }
    	    int orientation = exif
    	            .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
    	    Matrix matrix = new Matrix();

    	    if ((orientation == 3)) {
    	        matrix.postRotate(180);
    	        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
    	                bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
    	                true);

    	    } else if (orientation == 6) {
    	        matrix.postRotate(90);
    	        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
    	                bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
    	                true);

    	    } else if (orientation == 8) {
    	        matrix.postRotate(270);
    	        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
    	                bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
    	                true);

    	    } else {
    	        matrix.postRotate(0);
    	        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
    	                bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
    	                true);

    	    }

    	    return bitmap;

    	}
    

}

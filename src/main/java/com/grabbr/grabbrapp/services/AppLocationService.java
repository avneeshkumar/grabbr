package com.grabbr.grabbrapp.services;
import java.io.IOException;

import com.grabbr.grabbrapp.activities.MainActivity;
import com.grabbr.grabbrapp.activities.NewsfeedActivity;
import com.grabbr.grabbrapp.services.SendLocation;
import java.util.List;
import java.util.Locale;

import com.grabbr.grabbrapp.utils.MyConstants;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class AppLocationService extends Service implements LocationListener {

	// saving the context for later use
	private final Context mContext;
	public static int locationsent=0;
	// if GPS is enabled
	boolean isGPSEnabled = false;
	// if Network is enabled
	boolean isNetworkEnabled = false;
	// if Location co-ordinates are available using GPS or Network
	public boolean isLocationAvailable = false;

	// Location and co-ordinates coordinates
	Location mLocation;
	double mLatitude;
	double mLongitude;
	SharedPreferences sharedpreferences;

	// Minimum time fluctuation for next update (in milliseconds)
	private static final long TIME = 30000;
	// Minimum distance fluctuation for next update (in meters)
	private static final long DISTANCE = 20;

	// Declaring a Location Manager
	protected LocationManager mLocationManager;
	MyConstants c;
	NewsfeedActivity mainActivity;

	public AppLocationService(Context context,NewsfeedActivity newsfeedActivity, SharedPreferences sharedpreference) {
		this.mContext = context;
		mLocationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);
		this.sharedpreferences = sharedpreference;
		c = new MyConstants();
		mainActivity =newsfeedActivity;

	}

	/**
	 * Returs the Location
	 * 
	 * @return Location or null if no location is found
	 */
	public Location getLocation() {
		try {

			// Getting GPS status
			isGPSEnabled = mLocationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// If GPS enabled, get latitude/longitude using GPS Services
			if (isGPSEnabled) {
				mLocationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, TIME, DISTANCE, this);
				if (mLocationManager != null) {
					mLocation = mLocationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (mLocation != null) {
						mLatitude = mLocation.getLatitude();
						mLongitude = mLocation.getLongitude();
						isLocationAvailable = true; // setting a flag that
						
						sendlocationtoserver();					// location is available
						return mLocation;
					}
				}
			}

			// If we are reaching this part, it means GPS was not able to fetch
			// any location
			// Getting network status
			isNetworkEnabled = mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (isNetworkEnabled) {
				mLocationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, this);
				if (mLocationManager != null) {
					mLocation = mLocationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (mLocation != null) {
						mLatitude = mLocation.getLatitude();
						mLongitude = mLocation.getLongitude();
						isLocationAvailable = true; // setting a flag that
						sendlocationtoserver();						// location is available
						return mLocation;
					}
				}
			}
			// If reaching here means, we were not able to get location neither
			// from GPS not Network,
			
				// so asking user to open GPS
				askUserToOpenGPS();
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		// if reaching here means, location was not available, so setting the
		// flag as false
		isLocationAvailable = false;
		return null;
	}

	/**
	 * Gives you complete address of the location
	 * 
	 * @return complete address in String
	 */
	public String getLocationAddress() {

		if (isLocationAvailable) {

			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			// Get the current location from the input parameter list
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				/*
				 * Return 1 address.
				 */
				addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
			} catch (IOException e1) {
				e1.printStackTrace();
				return ("IO Exception trying to get address:" + e1);
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(mLatitude) + " , "
						+ Double.toString(mLongitude)
						+ " passed to address service";
				e2.printStackTrace();
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
				String addressText = String.format(
						"%s, %s, %s",
						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "",
						// Locality is usually a city
						address.getLocality(),
						// The country of the address
						address.getCountryName());
				// Return the text
				return addressText;
			} else {
				return "No address found by the service: Note to the developers, If no address is found by google itself, there is nothing you can do about it.";
			}
		} else {
			return "Location Not available";
		}

	}

	

	/**
	 * get latitude
	 * 
	 * @return latitude in double
	 */
	public double getLatitude() {
		if (mLocation != null) {
			mLatitude = mLocation.getLatitude();
		}
		return mLatitude;
	}

	/**
	 * get longitude
	 * 
	 * @return longitude in double
	 */
	public double getLongitude() {
		if (mLocation != null) {
			mLongitude = mLocation.getLongitude();
		}
		return mLongitude;
	}
	
	/**
	 * close GPS to save battery
	 */
	public void closeGPS() {
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(AppLocationService.this);
		}
	}

	/**
	 * show settings to open GPS
	 */
	public void askUserToOpenGPS() {
		AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		mAlertDialog.setTitle("Location not available, Open GPS?")
		.setMessage("Activate GPS to use use location services?")
		.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
				}
			})
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					}
				}).show();
	}

	/** 
	 * Updating the location when location changes
	 */
	public void onLocationChanged(Location location) {
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
	}
	
	public void sendlocationtoserver(){
		if(sharedpreferences.getString(c.isfb,"").equals("0")){
            Log.d("Applocation Service","Sending location");
			new SendLocation(""+getLatitude(),""+getLongitude()).execute(c.sendlocation_url,"0",sharedpreferences.getString(c.username,""),sharedpreferences.getString(c.password,""));
			
		}
		else{
			Log.d("NewsfeedActivity", "Sending location");
			new SendLocation(""+getLatitude(),""+getLongitude()).execute(c.sendlocation_url,"1",sharedpreferences.getString(c.fb_id,""),sharedpreferences.getString(c.fb_token,""),sharedpreferences.getString(c.username,""));
		}
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

}
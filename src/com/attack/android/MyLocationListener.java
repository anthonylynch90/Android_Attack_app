package com.attack.android;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class MyLocationListener implements LocationListener{
	private Context context;
	private Database database;
	
	public MyLocationListener(Context cntext){
		this.context = cntext;
		database = new Database(context);
	}


	public void onLocationChanged(Location loc){
		String text = loc.getLatitude()+", " + loc.getLongitude();
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show(); 
		database.addLocation(text);
		Log.e("location", text);
	}


public void onProviderDisabled(String provider)

{

Toast.makeText( context, "Gps Disabled", Toast.LENGTH_SHORT ).show();

}




public void onProviderEnabled(String provider)

{

Toast.makeText(context, "Gps Enabled", Toast.LENGTH_SHORT).show();


}




public void onStatusChanged(String provider, int status, Bundle extras)

{


}

}/* End of Class MyLocationListener */




package com.attack.android;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
 
public class SMS extends Service{
	private String location;
	@Override  
	public IBinder onBind(Intent arg0) {
		return null;
	} 

	@Override
	public void onStart(Intent intent, int startid) {
		super.onStart(intent, startid);
		Bundle getvars = intent.getExtras();
		if(getvars != null) { 
			location = getvars.getString("location");
		}
		char[] array = location.toCharArray();
		location = location.replaceAll(" ", "");
		String loc = "http://maps.google.com/?q="+location;
		Log.e("message", location);
		sendSMS(getString(R.string.this_person_needs_your_help_they_are_at_)+loc);
		
	}
    
    private void sendSMS(String message)
    {  
    	Database db = new Database(this);
    	
    	Cursor cursor = db.getNumbers();
    	db.onStop();
    	Log.e("message", message);
    	if(cursor!=null){
    		while(cursor.moveToNext()){
    			String phoneNumber = cursor.getString(cursor.getColumnIndex("number"));
    			Log.e("number", phoneNumber);
    			SmsManager sms = SmsManager.getDefault();
    			sms.sendTextMessage(phoneNumber, null, message, null, null);
    		}
    	}
    }

}

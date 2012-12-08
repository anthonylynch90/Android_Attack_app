package com.attack.android;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
public class LoadPage  extends Activity{
	 private final int DISPLAY_LENGHT = 2000; 
	        
	 public void onCreate(Bundle icicle) {
		 super.onCreate(icicle);
	     setContentView(R.layout.start_up);
	 
	     new Handler().postDelayed(new Runnable(){
	 
	  
	         public void run() {
	    		 startUpActivity();
	    	 }
	 
	     }, DISPLAY_LENGHT);
	 
	 }
	 
	 public void startUpActivity(){
		 Intent mainIntent = new Intent(getBaseContext(), CameraView.class);
		 
         startActivity(mainIntent);

         this.finish();
	 }
	 

}

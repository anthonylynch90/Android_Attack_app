package com.attack.android;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CameraView extends Activity implements SurfaceHolder.Callback{
	private MediaRecorder recorder; 
	private LocationManager locManager;
	private LocationListener locListener;
	private SurfaceHolder holder;
	public  static Button activate;
	private boolean recording = false;
	private boolean match;
	private static Context context;
	private DeactivateDialog dialog;
	private int videoNum;
	private int timerNum = 0;
	private Intent sms;
	public VoiceRecognition voice;
	private Database db = new Database(this);

	
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                recorder = new MediaRecorder();
                if(getIntent() != null) { 
        			Bundle extras = getIntent().getExtras();
        			videoNum = extras != null ? extras.getInt("value") : 0;
        		}
        		
                //sets the view to the surface_view.xml file.
                setContentView(R.layout.surface_view);

                SurfaceView cameraView = (SurfaceView) findViewById(R.id.surface_camera);
                holder = cameraView.getHolder();
                holder.addCallback(this);
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                recorder.setPreviewDisplay(holder.getSurface());
                context = this;
                dialog = new DeactivateDialog(context);
                onClick();
                checkGPS();
                createGpsIntent();
                resetVoiceRecognition(); 	         
        }
        
        
        private void checkGPS() {
    		LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
    		boolean gpsEnabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
    		if(!gpsEnabled){
    			createDialog();
    		}
    	}
        
        public void createDialog(){
    		AlertDialog.Builder alert = new AlertDialog.Builder(this);
    		alert.setTitle(R.string.gps_message);
    		alert.setPositiveButton(R.string.gps_settings, new DialogInterface.OnClickListener() {
    
    		    public void onClick(DialogInterface dialog, int which) {
    				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
    				return;
    		    }
    		});

    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	
    		    public void onClick(DialogInterface dialog, int which) {
    		        return;
    		    }
    		});
    		alert.create();
    		alert.show();
    	}

		private void initRecorder() {
			recorder.reset();
            recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

            CamcorderProfile cpHigh = CamcorderProfile
                    .get(CamcorderProfile.QUALITY_HIGH);
            recorder.setProfile(cpHigh);
            recorder.setOutputFile("/sdcard/videocapture_example"+videoNum+".mp4");
            recorder.setMaxDuration(8000);
            
            recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
            try {
                recorder.prepare(); 
            } catch (IOException e) {
                e.printStackTrace();
                finish();
            }
            
        }
        
	private void onClick() {
		activate = (Button) findViewById(R.id.activate);
		final Button deactivate = (Button) findViewById(R.id.deactivate);
		final Button settings = (Button) findViewById(R.id.settings);
		final TextView timer = (TextView) findViewById(R.id.timer);
		activate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { 
            	activate.setVisibility(View.INVISIBLE);
            	if(voice != null){
            		voice.recognizer.destroy();
            		voice = null;
            	}
            	deactivate.setVisibility(View.VISIBLE);
            	timer.setVisibility(View.VISIBLE);
            	settings.setVisibility(View.INVISIBLE);
            	checkRecording();
            	startAlert();
            }
        });
		
		deactivate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { 
            	dialog.showDialog();
            } 
        });
		
		settings.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {  
            	locManager.removeUpdates(locListener);
            	if(voice != null){
            		voice.recognizer.destroy();
            		voice = null;
            	}
            	Intent settings = new Intent(getBaseContext(), Settings.class);
            	settings.putExtra("value", videoNum);
            	onDestroy();
        		startActivity(settings);
            }
        });
	}
	
	private void startAlert() {
		final Button activate = (Button) findViewById(R.id.activate);
		final Button deactivate = (Button) findViewById(R.id.deactivate);
		final Button settings = (Button) findViewById(R.id.settings);
		final TextView timer = (TextView) findViewById(R.id.timer);
		CountDownTimer start = new CountDownTimer(10000,1000){

            @Override
            public void onTick(long miliseconds){
            	if(miliseconds/1000 >= 0 && dialog.check()!=true){
            		String val = getResources().getString(R.string.seconds_remaining);
            		timer.setText(val +(miliseconds/1000));
            	}
            	if(dialog.check()==true){
            		timer.setText(R.string.send_has_been_deactivated);
            	}
            	match = dialog.check();
            	if(match == true&&timerNum==0){
            		checkRecording();
            		Toast.makeText(getApplicationContext(), R.string.information_not_sent_, 5000).show();
            	}
            }

            @Override
            public void onFinish(){
            	final String loc = db.getLocation();
            	final String[] personalInfo = db.getPersonalDetails();
            	final Cursor contacts = db.getContacts();
            	
            	if(match == false){
            		resetVoiceRecognition();
            		sendSms(loc);
            		checkRecording();
            			if(db.hasGmail()){
            				Thread s = new Thread(new Runnable(){
            			
        						public void run() {
        							String args[] = db.getGmail();
                    				GmailSender sender = new GmailSender(args[0], args[1]);
                    				
                    				Cursor c = db.getEmailContacts();
                    				while(c.moveToNext()){
                    					try {

                							Log.e(args[0], args[1]);
											sender.sendMail(loc, args[0], c.getString(c.getColumnIndex("emailAddress")));
										} catch (Exception e) {
											Log.e("SendMail", e.getMessage(), e);
										} 
                    				}
        						}
                    			
                    		}); 
                    		s.start();
                    		
            				
            			}
            		Toast.makeText(getApplicationContext(), "Information sent", 5000).show();
            	}
            }
        }.start();
	}
	
	public void resetVoiceRecognition(){
		if(db.getActivationState().equals("ON")){
			Log.e("is", "Is on");
			voice = new VoiceRecognition(context);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ||keyCode == KeyEvent.KEYCODE_HOME) {
	    	Button activate = (Button) findViewById(R.id.activate);
	    	if(activate.getVisibility() == View.VISIBLE){
	    		moveTaskToBack(true);
	    		onDestroy();
	            return true;
	    	}
	    	return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	private void sendSms(String location){
		sms = new Intent(this, SMS.class);
		sms.putExtra("location", location);
    	this.startService(sms);
	}
	
	private void createGpsIntent() {
		String l = db.getLocation();
		if(l == null){
			db.addLocation("No Value");
		}
		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locManager.getBestProvider(criteria, false);
		locManager.getLastKnownLocation(provider);
		locListener = new MyLocationListener(this);
		locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000, 2, locListener);
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	public void checkRecording() {
	    if (recording) {
	    	recorder.stop();
	    	recording = false;
	    	videoNum = videoNum+1;
	    	timerNum = timerNum+1;
	        // Let's initRecorder so we can record again
	       // initRecorder();
	        Intent camera = new Intent(this, CameraView.class);
	        camera.putExtra("value", videoNum);
	        super.finish();
	        startActivity(camera);
	    } else {
	    	initRecorder();
	        recording = true;
	        recorder.start();
	    }
	}

	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) { 
	    if (recording) {
	        recorder.stop();
	        recording = false;
	    }
	    recorder.release();
	    finish();
	}
		
	public void onDestroy(){
		super.onDestroy();
		try{
			this.stopService(sms);
		}catch(Exception e){}
		try{
			voice.recognizer.destroy();
		}catch(Exception e){}
		if(locManager != null){
			locManager.removeUpdates(locListener);
		}
		
	}
	

}

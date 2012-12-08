package com.attack.android;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

public class VoiceRecognition extends CameraView {
	 public RecognitionListener listener;
	 private String TAG = "Voice word:";
	 private boolean helpRequested = false; 
	 public SpeechRecognizer recognizer;
	 private Database db = new Database(this);
	 private Context context;
	 
	 public VoiceRecognition(Context ctx){
		 context = ctx;
		 startVoiceRecognitionActivity(ctx);
	 }
	 
        private void startVoiceRecognitionActivity(final Context ctx) {
        	final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,5000);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.attack.android");
 
            recognizer = SpeechRecognizer.createSpeechRecognizer(ctx);
            listener = new RecognitionListener() {
             
                public void onResults(Bundle results) {
                    ArrayList<String> voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(results != null){
                    	for(String result:voiceResults){
                    		Log.d(TAG, result);
                    		if(result.equals(db.getActivationWord())){
                    			recognizer.destroy();
                    			activate.performClick();
                    			helpRequested = true;
                    			break;
                    		}
                    	}
                    }
                    if(helpRequested == false){
                    	recognizer.destroy();
                    	recognizer.stopListening();
                    	startVoiceRecognitionActivity(context);
                     }
                }

            
                public void onReadyForSpeech(Bundle params) {
                    Log.d(TAG, "Ready for speech");
                }

       
                public void onError(int error) {
                    Log.d(TAG, "Error listening for speech: " + error);
                }

       
                public void onBeginningOfSpeech() {
                    Log.d(TAG, "Speech starting");
                }

        
                public void onBufferReceived(byte[] buffer) {
                    // TODO Auto-generated method stub

                }

   
                public void onEndOfSpeech() {
                	startVoiceRecognitionActivity(ctx);
                }

            
                public void onEvent(int eventType, Bundle params) {
                    // TODO Auto-generated method stub

                }

            
                public void onPartialResults(Bundle partialResults) {
                    // TODO Auto-generated method stub
                }

            
                public void onRmsChanged(float rmsdB) {
                	if(db.getDecibelState().equals("ON")){
                		Log.e(TAG, new Integer((int) rmsdB).toString());
                		if(rmsdB >50){
                			listener = null;
                			recognizer.stopListening();
                			recognizer.destroy();
                			
                			activate.performClick();
                		}
                	}                	
                }
            };
            recognizer.setRecognitionListener(listener);
            recognizer.startListening(intent);
        }
}

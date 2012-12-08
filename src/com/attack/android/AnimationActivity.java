package com.attack.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class AnimationActivity extends Activity {
    /** Called when the activity is first created. */
	private AnimationDrawable rocketAnimation;
	private TextView text;
	private ImageView rocketImage;
	private Button next;
	private ScrollView scroll;
	private Button back;
	private View lay;
	private int index = 1;

	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.animation);
	  back = (Button) findViewById(R.id.back);
	  scroll = (ScrollView) findViewById(R.id.scroll1);
	  next = (Button) findViewById(R.id.next);
	  lay = (View) findViewById(R.id.tutLayout);
	  rocketImage = (ImageView) findViewById(R.id.tutriol);
	  text = (TextView) findViewById(R.id.tutText);
	  lay.setBackgroundColor(Color.WHITE);
	  rocketImage.setBackgroundResource(R.drawable.animation1);
	  rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
	  text.setText("When you want to activate the Attack app, press the panic button.");
	  text.setTextSize(20);
	  text.setTextColor(Color.BLACK);
	  back.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				index = index-2;
				next.performClick();
			}
			  
		  });
	  next.setOnClickListener(new OnClickListener(){

		public void onClick(View arg0) {
			index++;
			switch(index){
			case 1:
				scroll.scrollTo(0,  0);
				rocketImage.setBackgroundResource(R.drawable.animation1);
				rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
				back.setVisibility(View.INVISIBLE);
				text.setText("When you want to activate the Attack app, press the panic button.");
				onWindowFocusChanged (true);
				break;
			case 2:
				scroll.scrollTo(0,  0);
				rocketImage.setBackgroundResource(R.drawable.animation2);
				rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
				back.setVisibility(View.VISIBLE);
				text.setText("When it is in active state, you can deativate the app by pressing " +
						"the deactivate button. You then enter your 4 pin code and press ok to deactivate it. You only have 10 seconds to do so");
				onWindowFocusChanged (true);
				break;
			case 3:
				scroll.scrollTo(0,  0);
				rocketImage.setBackgroundResource(R.drawable.animation3);
				rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
				text.setText("Click settings to change App operation. click on contacts and add new contact. This will be the person you will send the " +
						"information to. You can add as many contacts as you want. you can delete a contact by holding the button of their name");
				onWindowFocusChanged (true);
				break;
			case 4:
				scroll.scrollTo(0,  0);
				rocketImage.setBackgroundResource(R.drawable.animation6);
				rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
				text.setText("When you first click the Email contacts button, you will be asked to enter in your Gmail address and " +
						"the password to access that Gmail account in emergency. when you fill in the form you can then add " +
						"Email contacts like you did with phone contacts.");
				next.setText("Next");
				onWindowFocusChanged (true);
				break;
			case 5:
				scroll.scrollTo(0,  0);
				rocketImage.setBackgroundResource(R.drawable.animation4);
				rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
				text.setText("when you click word activation, you can set the word you want to activate the app." +
						"press save when you typed it in. if you want to turn the word activation on, click it on at the bottom. " +
						"you can also activate a decibel system where it will go off when it reaches a certain noise level.");
				next.setText("Next");
				onWindowFocusChanged (true);
				break;
			case 6:
				scroll.scrollTo(0,  0);
				rocketImage.setBackgroundResource(R.drawable.animation5);
				rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
				next.setText("Finish");
				text.setText("Click change password to change your old password to a new one. click change details if" +
						" you want to change personal information");
				onWindowFocusChanged (true);
				break;
			case 7:
				Intent loadPage = new Intent(getApplicationContext(), LoadPage.class);
				startActivity(loadPage);
				AnimationActivity.this.finish();
				break;
			}
			
		}
		  
	  });
	 
	}
	
	 @Override
	  public void onWindowFocusChanged (boolean hasFocus)
	  {
		 rocketAnimation.start();
	  }
}
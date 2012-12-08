package com.attack.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SubMenuViews extends Activity {
	private Database database = new Database(this);
	private String itemPressed;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_menu_view);
		
		if(getIntent() != null) { 
			Bundle extras = getIntent().getExtras();
			itemPressed = extras != null ? extras.getString("buttonPressed"):"";
			sort(itemPressed);
		}
	}
	
	public void sort(String item){	
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(4);
		final ViewGroup view = (ViewGroup) findViewById(R.id.layout);
		final EditText edit1 = (EditText) findViewById(R.id.edit1);
		final EditText edit2 = (EditText) findViewById(R.id.edit2);
		final EditText edit3 = (EditText) findViewById(R.id.edit3);
		
		final TextView view1 = (TextView) findViewById(R.id.view1);
		final TextView view2 = (TextView) findViewById(R.id.view2);
		final TextView view3 = (TextView) findViewById(R.id.view3);
		final TextView explanation = (TextView) findViewById(R.id.explanation);
		final View view4 = (View) findViewById(R.id.linearLayout2);
		final View view5 = (View) findViewById(R.id.linearLayout3);
		final Button save = (Button) findViewById(R.id.save);
		
		if(item.equals("Change details")){
			view1.setText(R.string.first_name);
			view2.setText(R.string.second_name);
			view3.setText(R.string.address);
			edit3.setMinLines(3);
			String[] values = database.getPersonalDetails(); 
			edit1.setText(values[0]);	
			edit2.setText(values[1]);
			edit3.setText(values[2]);
		}
		
		if(item.equals("Email Contacts")){
			view1.setText(R.string.mailer_name);
			edit2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			view2.setText(R.string.email_address);
			view.removeView(edit3);
		}
		if(item.equals("Change password")){
			edit1.setFilters(FilterArray);
			edit2.setFilters(FilterArray);
			edit3.setFilters(FilterArray);
			
			edit1.setRawInputType(InputType.TYPE_CLASS_NUMBER);
			edit2.setRawInputType(InputType.TYPE_CLASS_NUMBER);
			edit3.setRawInputType(InputType.TYPE_CLASS_NUMBER);
			view1.setText(R.string.enter_old_pin);
			view2.setText(R.string.enter_new_pin);
			view3.setText(R.string.re_enter_new_pin);
		}
		if(item.equals("Add new contact")){
			view1.setText(R.string.first_name);
			view2.setText(R.string.second_name);
			view3.setText(R.string.number);
			edit3.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		}
		
		if(item.equals("Word activation")){
			view1.setText(R.string.please_enter_the_word_you_want_to_use_to_activate_the_alert);
			view1.setTextSize(20);
			edit1.setText(database.getActivationWord());
			explanation.setVisibility(View.VISIBLE);
			explanation.setTextColor(0xff0000ff);
			final TextView label = (TextView) findViewById(R.id.label2);
			final ImageView img = (ImageView) findViewById(R.id.icon2);
			final TextView label2 = (TextView) findViewById(R.id.label13);
			final ImageView img2 = (ImageView) findViewById(R.id.icon13);	
			view4.setVisibility(View.VISIBLE);
			String val = database.getActivationState();
			
			if(val.equals("ON")){
				img.setImageResource(R.drawable.on);
				label.setText(R.string.word_activation_on);
			}
			else{
				img.setImageResource(R.drawable.off);
				label.setText(R.string.word_activation_off);
			}
			view4.setClickable(true);
			view4.setOnClickListener(new View.OnClickListener(){

				public void onClick(View v) {
					if(database.getActivationState().equals("OFF")){
						label.setText(R.string.word_activation_on);
						database.setActivationWordState("ON");
						img.setImageResource(R.drawable.on);						
					}
					else{
						label.setText(R.string.word_activation_off);
						database.setActivationWordState("OFF");
						img.setImageResource(R.drawable.off);
						label2.setText(R.string.decibel_system_off);
						database.setDecibelState("OFF");
						img2.setImageResource(R.drawable.off);
					}
				}
				
			});
					
			view5.setVisibility(View.VISIBLE);
			String val2 = database.getDecibelState();
			if(val2.equals("ON")){
				img2.setImageResource(R.drawable.on);
				label2.setText(R.string.decibel_system_on);
			}
			else{
				img2.setImageResource(R.drawable.off);
				label2.setText(R.string.decibel_system_off);
			}
			view5.setClickable(true);
			view5.setOnClickListener(new View.OnClickListener(){

				public void onClick(View v) {
					if(database.getDecibelState().equals("OFF")){
						if(database.getActivationState().equals("ON")){
							label2.setText(R.string.decibel_system_on);
							database.setDecibelState("ON");
							img2.setImageResource(R.drawable.on);
						}
						else{
							Toast.makeText(getBaseContext(), R.string.you_must_enable_word_activation_first, 2000).show();
						}
					}
					else{
						label2.setText(R.string.decibel_system_off);
						database.setDecibelState("OFF");
						img2.setImageResource(R.drawable.off);
					}
				}
				
			});
			
			explanation.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View arg0) {
					openDialog();
				}
			});
			view.removeView(edit2);
			view.removeView(edit3);
		}
		save.setOnClickListener(new OnClickListener(){
		
			public void onClick(View v) { 
				if(itemPressed.equals("Word activation")){
					String word = edit1.getText().toString();
					if(!word.equals("")){
						database.updateWordActivation(word);
					}
					else{
						Toast.makeText(getApplicationContext(), R.string.field_is_empty, 2000).show();
					}
				}
				if(itemPressed.equals("Email Contacts")){
					String name = edit1.getText().toString();
					String address = edit2.getText().toString();
					if(!name.equals("")&&!address.equals("")){
						database.saveEmailContact(name, address);
						Intent email = new Intent(getApplicationContext(), EmailContacts.class);
						SubMenuViews.this.finish();
						startActivity(email);
					}
					else{
						Toast.makeText(getApplicationContext(), R.string.field_is_empty, 2000).show();
					}
				}
				if(itemPressed.equals("Change details")){
					String firstName = edit1.getText().toString();
					String secondName = edit2.getText().toString();
					String address = edit3.getText().toString();
					if(!firstName.trim().equals("")&&!secondName.trim().equals("")&&!address.trim().equals("")){
						ContentValues args = new ContentValues();
						args.put("firstName", firstName);
						args.put("secondName", secondName);
						args.put("address", address);
						database.updateTableUser(args);
						Intent i = new Intent(getApplicationContext(), Settings.class);
				        SubMenuViews.this.finish();
				        startActivity(i);
					}
					else{
						Toast.makeText(getApplicationContext(), R.string.one_of_the_fields_are_empty, 2000).show();
					}
				}
				if(itemPressed.equals("Add new contact")){
					String firstName = edit1.getText().toString();
					String secondName = edit2.getText().toString();
					firstName = firstName +" "+ secondName;
					String number = edit3.getText().toString();
					if(!number.trim().equals("")&&!firstName.trim().equals("")){
						ContentValues args = new ContentValues();
						args.put("name", firstName);
						args.put("number", number);
						database.updateTableContacts(args);
						Intent i = new Intent(getApplicationContext(), Contacts.class);
				        SubMenuViews.this.finish();
				        startActivity(i);
					}
					else{
						Toast.makeText(getApplicationContext(), R.string.one_of_the_fields_are_empty, 2000).show();
					}
				}
				
				if(itemPressed.equals("Change password")){
					String oldValue = edit1.getText().toString();
					String newValue = edit2.getText().toString();
					String newValue2 = edit3.getText().toString();
					if(newValue.equals(newValue2)){
						boolean match = database.checkPin(oldValue, getApplicationContext());
						if(match == true){
							ContentValues args = new ContentValues();
							args.put("securityPin", newValue);
							database.updateTableUser(args);
							Intent i = new Intent(getApplicationContext(), Settings.class);
					        SubMenuViews.this.finish();
					        startActivity(i);
						}
						else{
							Toast.makeText(getApplicationContext(), R.string.old_pin_does_not_match, 2000).show();
						}
					}
					else{
						Toast.makeText(getApplicationContext(), R.string.re_enter_value_does_not_match, 2000).show();
					}
				}
			}		
		});
	}
	
	
	public void openDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final TextView input = new TextView(this);
		input.setText(R.string.explanation);
		input.setTextSize(20);
		alert.setView(input);
		alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
				return;
		    }
		});
		alert.create();
		alert.show();
	}
	
	
}



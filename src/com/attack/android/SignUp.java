package com.attack.android;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//This is the first class that is opened when you open the application
//it will check to if you have signed up already and if so it will continue on the attackAppliction activity.
//if it is the first time using the app, you will have to sign up all your information for the app to work
public class SignUp extends Activity{
	private Database database;
	
	public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.main);
         database = new Database(this);
         database.createTables();
         checkDatabase();
         onClick();
	 }
	
	private void checkDatabase() {
		if(database.checkdetails(this) == true){
			Intent loadPage = new Intent(this, LoadPage.class);
			startActivity(loadPage);
			SignUp.this.finish();
		}
		else{
			Toast.makeText(this, R.string.please_enter_your_details_for_intial_sign_up, 3000).show();
		}
	}
	
	private void onClick() {
		Button save = (Button) findViewById(R.id.save);
		final Intent loadPage = new Intent(this, AnimationActivity.class);
        final EditText firstName = (EditText) findViewById(R.id.firstName);
    	final EditText secondName = (EditText) findViewById(R.id.secondName);
    	final EditText address = (EditText) findViewById(R.id.address);
    	final EditText pin1 = (EditText) findViewById(R.id.securityPin);
    	final EditText pin2 = (EditText) findViewById(R.id.securityPin2);
        save.setOnClickListener(new View.OnClickListener() {
       	public void onClick(View v) {    		
       		if(!firstName.getText().toString().trim().equals("") && !secondName.getText().toString().trim().equals("") 
       				&& !address.getText().toString().trim().equals("")&&!pin2.getText().toString().equals("")
       				&&!pin2.getText().toString().equals("")){
       			if(pin1.getText().toString().equals(pin2.getText().toString())){
       				if(pin1.getText().length() == 4){
       					ContentValues values = new ContentValues();
       					values.put("ID", 1);
       					values.put("firstName", firstName.getText().toString());
       					values.put("secondName", secondName.getText().toString());
       					values.put("address", address.getText().toString());
       					values.put("securityPin", pin1.getText().toString());
       					values.put("Location", "Default");
       					database.saveInfo(values);
       					database.addWordActivation("help");
       					startActivity(loadPage);
       					SignUp.this.finish();
       				}
       				else{
       					Toast.makeText(getApplicationContext(), R.string.pin_must_be_4_digits
           						, 2000).show();
       				}
       			}
       			else{
       				Toast.makeText(getApplicationContext(), R.string.security_pin_does_not_match, 2000).show();
       			}
       		}
       		else{
       			Toast.makeText(getApplicationContext(), R.string.must_enter_all_information, 2000).show();
       		}
       	}
        });
	}
	
	public void onDestroy() {
        super.onDestroy();
    }
	 
}


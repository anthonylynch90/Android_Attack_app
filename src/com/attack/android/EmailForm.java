package com.attack.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EmailForm extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_menu_view);
		final EditText edit1 = (EditText) findViewById(R.id.edit1);
		final EditText edit2 = (EditText) findViewById(R.id.edit2);
		final EditText edit3 = (EditText) findViewById(R.id.edit3);
		
		final TextView view1 = (TextView) findViewById(R.id.view1);
		final TextView view2 = (TextView) findViewById(R.id.view2);
		final TextView view3 = (TextView) findViewById(R.id.view3);
		
		final Button save = (Button) findViewById(R.id.save);
		
		view1.setText(R.string.your_gmail_address);
		edit1.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		view2.setText(R.string.password);
		edit2.setTransformationMethod(PasswordTransformationMethod.getInstance());
		view3.setText(R.string.renter_password);
		edit3.setTransformationMethod(PasswordTransformationMethod.getInstance());
		
		save.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				if(!edit1.equals("")&&!edit2.equals("")&&!edit3.equals("")){
					String pass1 = edit2.getText().toString();
					String pass2 = edit3.getText().toString();
					if(pass1.equals(pass2)){
						Database db = new Database(getApplicationContext());
						db.saveGmail(edit1.getText().toString(), edit2.getText().toString());
						Intent email = new Intent(getApplicationContext(), EmailContacts.class);
						startActivity(email);
					}
					else{
						Toast.makeText(getApplicationContext(), R.string.passwords_dont_match, 1000).show();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), R.string.didnt_fill_out_all_the_forms, 1000).show();
				}
			}
			
		});
		
		
	}
	
	

}

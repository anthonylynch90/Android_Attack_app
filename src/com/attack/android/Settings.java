package com.attack.android;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Settings extends ListActivity {
	private String[] values;
	private String itemPressed;
	private ListView listView;
	private Context context;
	private int videoNum;
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
	this.context = this;
	if(getIntent() != null) { 
		Bundle extras = getIntent().getExtras();
		videoNum = extras != null ? extras.getInt("value") : 0;
	}
	this.values = new String[]{getResources().getString(R.string.contacts), getResources().getString(R.string.email_contacts), 
			getResources().getString(R.string.word_activation), getResources().getString(R.string.personal_info), 
			getResources().getString(R.string.change_password), getResources().getString(R.string.change_details), 
			getResources().getString(R.string.information), getResources().getString(R.string.help)};
    
	SettingsArrayAdapter adapter = new SettingsArrayAdapter(this, values);
	setListAdapter(adapter);
  }
  
  @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		itemPressed = (String) getListAdapter().getItem(position);
		if(itemPressed.equals("Contacts")){
			Intent contacts = new Intent(this, Contacts.class);
			startActivity(contacts);
		}
		else if(itemPressed.equals("Email Contacts")){
			Database database = new Database(this);
			if(!database.hasGmail()){
				createDialog();
			}
			else{
				Intent email = new Intent(this, EmailContacts.class);
				startActivity(email);
			}
		}
		else if(itemPressed.equals("Help")){
			Intent loadPage = new Intent(getApplicationContext(), AnimationActivity.class);
			startActivity(loadPage);
		}
		else{
			Intent subMenuView = new Intent(this, SubMenuViews.class);
			subMenuView.putExtra("buttonPressed", itemPressed);
			startActivity(subMenuView);
		}
	}
  
  public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        Intent i = new Intent(this, CameraView.class);
	        this.finish();
	        i.putExtra("value", videoNum);
	        startActivity(i);
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
  
  public void createDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		TextView view = new TextView(this);
		view.setText(R.string.for_you_to_be_able_to_send_emails_you_must_supply_your_gmail_and_password_for_emergency_send_);
		view.setTextSize(20);
		alert.setView(view);
		alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
				Intent emailForm = new Intent(getApplicationContext(), EmailForm.class);
				Settings.this.finish();
				startActivity(emailForm);
				return;
		    }
		});

		alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	
		    public void onClick(DialogInterface dialog, int which) {
		    	return;
		    }
		});
		alert.create();
		alert.show();
	}
  
}
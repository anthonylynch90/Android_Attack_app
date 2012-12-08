package com.attack.android;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EmailContacts extends ListActivity {
	private String[] values;
	private String itemPressed;
	private ListView listView;
	private Context context;
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
	this.context = this;
	Database db = new Database(this);
	Cursor cursor = db.getEmailContacts();
	int i = 0;
	if(cursor.getCount()>0){
		this.values = new String[cursor.getCount()+1];
		while(cursor.moveToNext()){
			values [i] = cursor.getString(cursor.getColumnIndex("name"));
			i++;
         }
		
		values[i++] = getString(R.string.add_new_email_contact);
		
	}else{
		Toast.makeText(this, "empty", 500).show();
		String val = getResources().getString(R.string.add_new_email_contact);
		this.values = new String[]{val};
		
	}
	EmailArrayAdaptor adapter = new EmailArrayAdaptor(this, values);
	setListAdapter(adapter);
  }
  
  @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		itemPressed = (String) getListAdapter().getItem(position);
		if(itemPressed.equals("Add new email contact")){
			Intent subMenuView = new Intent(this, SubMenuViews.class);
			subMenuView.putExtra("buttonPressed", "Email Contacts");
			EmailContacts.this.finish();
			startActivity(subMenuView);
		}
		else{
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
    		alert.setTitle(R.string.do_you_want_to_delete_+itemPressed+R.string._from_contacts);
    		alert.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int which) {
    				Database db = new Database(context);
    				db.deleteEmailContact(itemPressed);
    				Intent contacts = new Intent(context, EmailContacts.class);
    				EmailContacts.this.finish();
    				startActivity(contacts);
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
  public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent settings = new Intent(context, Settings.class);
			EmailContacts.this.finish();
			startActivity(settings);
	    	return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}

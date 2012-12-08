package com.attack.android;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Contacts extends ListActivity {
	private String[] values;
	private String itemPressed;
	private ListView listView;
	private Context context;
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
	this.context = this;
	Database db = new Database(this);
	Cursor cursor = db.getContacts(context);
	int i = 0;
	if(cursor.getCount()>0){
		this.values = new String[cursor.getCount()+1];
		while(cursor.moveToNext()){
			values [i] = cursor.getString(cursor.getColumnIndex("name"));
			i++;
         }
		values[i++] = getString(R.string.add_new_contact);
	}else{
		Toast.makeText(this, R.string.empty, 500).show();
		String val = getResources().getString(R.string.add_new_contact);
		this.values = new String[]{val};
	}
	ContactsArrayAdaptor adapter = new ContactsArrayAdaptor(this, values);
	setListAdapter(adapter);
  }
  
  @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		itemPressed = (String) getListAdapter().getItem(position);
		if(itemPressed.equals("Add new contact")){
			Intent subMenuView = new Intent(this, SubMenuViews.class);
			subMenuView.putExtra("buttonPressed", itemPressed);
			Contacts.this.finish();
			startActivity(subMenuView);
		}
		else{
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
    		
    		TextView view = new TextView(this);
    		view.setText("Do you want to delete "  +itemPressed+" from contacts?");
    		view.setTextSize(25);
    		alert.setView(view);
    		alert.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int which) {
    				Database db = new Database(context);
    				db.deleteContact(itemPressed);
    				Intent contacts = new Intent(context, Contacts.class);
    				Contacts.this.finish();
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
  
}

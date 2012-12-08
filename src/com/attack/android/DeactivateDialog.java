package com.attack.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class DeactivateDialog{
	private Context context;
	private Boolean acceptedPin = false;
	private String text;
	
	public DeactivateDialog(Context cntxt) {
        this.context = cntxt;
	 }

	public void showDialog(){
		final EditText textEdit = new EditText(context);
		// Builder
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(R.string.enter_pin);
		
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(4);
		textEdit.setFilters(FilterArray);
		textEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
		textEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
		alert.setView(textEdit);

		alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        text = textEdit.getText().toString();
		        Database db = new Database(context);
		    	acceptedPin = db.checkPin(text, context);
		    	if(acceptedPin!=true){
		    		Toast.makeText(context, R.string.incorrect_pin, 1000).show();
		    		showDialog();
		    	}
		    }
		});

		alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        return;
		    }
		});
		// Dialog
		AlertDialog dialog = alert.create();
		dialog.show();
		dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}
	
	public boolean check(){
		Database db = new Database(context);
    	acceptedPin = db.checkPin(text, context);
		return acceptedPin;
	}
}

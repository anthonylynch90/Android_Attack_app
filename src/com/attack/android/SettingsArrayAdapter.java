package com.attack.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	public SettingsArrayAdapter(Context context, String[] values) {
		super(context, R.layout.settings_menu, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.settings_menu, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		LinearLayout linearLayout = (LinearLayout) rowView.findViewById(R.id.linearLayout);
		textView.setText(values[position]);
		String s = values[position];
		if (s.startsWith("Change password")) {
			imageView.setImageResource(R.drawable.password);
		} 
		if (s.startsWith("Contacts")) {
			imageView.setImageResource(R.drawable.contacts);
		}
		if (s.startsWith("Word activation")) {
			imageView.setImageResource(R.drawable.speech);
		}
		if (s.startsWith("Email Contacts")) {
			imageView.setImageResource(R.drawable.enail);
		}
		if (s.startsWith("Personal info")) {
			linearLayout.removeView(imageView);
			textView.setTextSize(20);
			textView.setBackgroundColor(0xff0000ff);
		}
		if (s.startsWith("Information")) {
			linearLayout.removeView(imageView);
			textView.setTextSize(20);
			textView.setBackgroundColor(0xff0000ff);
		}
		if (s.startsWith("Change details")) {
			imageView.setImageResource(R.drawable.details);
		} 
		if (s.startsWith("Help")) {
			imageView.setImageResource(R.drawable.help_symbol);
		}

		return rowView;
	}
}

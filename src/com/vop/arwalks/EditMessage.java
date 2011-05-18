package com.vop.arwalks;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.data.Location;

public class EditMessage extends FullscreenActivity{

	private Location l;
	private EditText te;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.edit_message);
		int id = getIntent().getIntExtra("id", 0);
		if (id == 0)
			finish();
		else {
			l = DBWrapper.getLocation(id);
			if (l == null)
				finish();
		}

		te = (EditText) findViewById(R.id.walk_name);
		TextView tv = (TextView) findViewById(R.id.walk_title);
		te.setText(l.getName());
		tv.setText(l.getName());
	}

	/**
	 * action when button is pressed
	 * 
	 * @param v
	 */
	public void saveMessage(View v) {
		String name = te.getText().toString();
		l.setName(name);
		DBWrapper.save(l);
		finish();
	}
}

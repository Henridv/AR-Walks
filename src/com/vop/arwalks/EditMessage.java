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
	private EditText newName;
	private EditText newDescription;

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
		
		TextView titel = (TextView) findViewById(R.id.message_title);
		titel.setText(l.getName());
		newName = (EditText) findViewById(R.id.message_new_name);
		newName.setText(l.getName());
		newDescription = (EditText) findViewById(R.id.message_new_description);
		newDescription.setText(l.getName());

	}

	/**
	 * action when button is pressed
	 * 
	 * @param v
	 */
	public void saveMessage(View v) {
		String name = newName.getText().toString();
		String description = newName.getText().toString();
		l.setName(name);
		l.setDescription(description);
		DBWrapper.save(l);
		finish();
	}
}

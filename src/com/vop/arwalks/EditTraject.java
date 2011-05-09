package com.vop.arwalks;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vop.arwalks.R;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.data.Traject;

/**
 * change existing traject
 * 
 * @author gbostoen
 * 
 */
public class EditTraject extends FullscreenActivity {
	private Traject t;
	private EditText te;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.edit_walk);
		int walk_id = getIntent().getIntExtra("walk_id", 0);
		if (walk_id == 0)
			finish();
		else {
			t = DBWrapper.getTraject(walk_id);
			if (t == null)
				finish();
		}

		te = (EditText) findViewById(R.id.walk_name);
		TextView tv = (TextView) findViewById(R.id.walk_title);
		te.setText(t.getName());
		tv.setText(t.getName());
	}

	/**
	 * action when button is pressed
	 * 
	 * @param v
	 */
	public void saveWalk(View v) {
		String name = te.getText().toString();
		t.setName(name);
		DBWrapper.save(t);
		finish();
	}
}

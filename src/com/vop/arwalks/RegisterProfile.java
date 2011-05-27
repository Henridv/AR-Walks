package com.vop.arwalks;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.data.Person;

/**
 * register profile
 * 
 * @author gbostoen
 * 
 */
public class RegisterProfile extends FullscreenActivity {
	private EditText	emailText;
	private EditText	passText;
	private EditText	nameText;
	private EditText	telefoonNr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_profile);
		String email = getIntent().getStringExtra("email");
		String pass = getIntent().getStringExtra("password");
		emailText = (EditText) findViewById(R.id.widget31);
		emailText.setText(email);
		passText = (EditText) findViewById(R.id.widget33);
		passText.setText(pass);
		nameText = (EditText) findViewById(R.id.widget29);
		telefoonNr = (EditText) findViewById(R.id.widget37);
	}

	/**
	 * register action
	 * 
	 * @param v
	 */
	public void register(View v) {
		if (passText.getText().length() > 0 && emailText.getText().length() > 0
				&& nameText.getText().length() > 0
				&& telefoonNr.getText().length() > 0) {
			Person p = new Person(nameText.getText().toString(), telefoonNr.getText().toString(), passText.getText().toString(), emailText.getText().toString());
			DBWrapper.save(p);
			this.finish();
		} else
			Toast.makeText(getApplicationContext(), "Credentials not valid!", Toast.LENGTH_LONG).show();
	}
}

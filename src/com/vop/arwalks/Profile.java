package com.vop.arwalks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.vop.arwalks.R;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

/**
 * profile
 * 
 * @author gbostoen
 * 
 */
public class Profile extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiel_layout);
		updateProfile();
	}

	/**
	 * edit action
	 * 
	 * @param v
	 */
	public void edit_klik(View v) {
		Intent myIntent = new Intent(Profile.this, EditProfile.class);
		Profile.this.startActivity(myIntent);
	}

	/**
	 * password action
	 * 
	 * @param v
	 */
	public void password_klik(View v) {
		Intent myIntent = new Intent(Profile.this, EditPassword.class);
		Profile.this.startActivity(myIntent);

	}

	@Override
	public void onResume() {
		super.onResume();
		updateProfile();
	}

	/**
	 * method that updates profile
	 */
	private void updateProfile() {
		VopApplication app = (VopApplication) getApplicationContext();
		int id = Integer.parseInt(app.getState().get("userid"));
		Person p = DBWrapper.getProfile(id);
		TextView naam = new TextView(this);
		naam = (TextView) findViewById(R.id.profname);
		naam.setText("Name: " + p.getName());
		TextView tel = new TextView(this);
		tel = (TextView) findViewById(R.id.profphone);
		tel.setText("Phone: " + p.getPhone());
		TextView email = new TextView(this);
		email = (TextView) findViewById(R.id.profmail);
		email.setText("E-mail: " + p.getEmail());
	}

}

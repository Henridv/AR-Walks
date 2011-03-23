package com.vop.augumented;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class StartupActivity extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startupactivity_layout);
	}
	//go-klik
	public void go_klik(View v) {
		EditText emailbox = (EditText)findViewById(R.id.invul_box);
		VopApplication app = (VopApplication)getApplicationContext();
		app.putState("login", "true");
		
		Person p = DBWrapper.getProfile(emailbox.getText().toString());
		
		if (p != null) {
			app.putState("userid", p.getId().toString());
	    	Intent myIntent = new Intent(StartupActivity.this, Hoofdmenu.class);
	    	StartupActivity.this.startActivity(myIntent);
		} else {
			Toast.makeText(getApplicationContext(), "emailaddress not valid", Toast.LENGTH_SHORT).show();
		}
	}
	//menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.hoofdmenu_menu, menu);
	    return true;
	}

}

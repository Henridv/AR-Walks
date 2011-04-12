package com.vop.augumented;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class Profiel extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiel_layout);
		VopApplication app = (VopApplication) getApplicationContext();
		int id = Integer.parseInt(app.getState().get("userid"));
		Person p = DBWrapper.getProfile(id);
		TextView naam = new TextView(this);
		naam=(TextView)findViewById(R.id.name); 
	    naam.setText("name: "+p.getName());
	    TextView tel = new TextView(this);
		tel=(TextView)findViewById(R.id.phone); 
	    tel.setText("phone number: "+p.getPhone());
	    TextView email = new TextView(this);
		email=(TextView)findViewById(R.id.mail); 
	    email.setText("e-mail: "+p.getEmail());
	    Button editprof = (Button)findViewById(R.id.profedit);

	}
	
	public void edit_klik(View v) {
		Intent myIntent = new Intent(Profiel.this, Hoofdmenu.class);
		Profiel.this.startActivity(myIntent);
	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.profiel_menu, menu);
		return true;
	}
}

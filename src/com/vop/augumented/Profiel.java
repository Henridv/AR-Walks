package com.vop.augumented;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
		naam=(TextView)findViewById(R.id.textView1); 
	    naam.setText("naam: "+p.getName());
	    TextView tel = new TextView(this);
		tel=(TextView)findViewById(R.id.textView2); 
	    tel.setText("phone number: "+p.getPhone());
	    TextView email = new TextView(this);
		email=(TextView)findViewById(R.id.textView3); 
	    email.setText("e-mail: "+p.getEmail());


	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.profiel_menu, menu);
		return true;
	}
}

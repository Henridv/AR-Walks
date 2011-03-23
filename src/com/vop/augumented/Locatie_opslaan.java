package com.vop.augumented;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Locatie_opslaan extends FullscreenActivity {
	private Location loc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locatie_opslaan_layout);
		/*Location loc = Location(Integer id, String name, String description,
				Double latitute, Double longitude, Double altitude, String date,
				Integer persId)*/
		EditText titel_edit = (EditText) findViewById(R.id.titel);
		String titel = titel_edit.getText().toString();
		EditText info_edit = (EditText) findViewById(R.id.info);
		String info = info_edit.getText().toString();
		VopApplication app = (VopApplication) getApplicationContext();
		Integer id = Integer.parseInt(app.getState().get("userid"));
		double lng = Double.parseDouble(app.getState().get("long"));
		double lat = Double.parseDouble(app.getState().get("lat"));
		double alt = Double.parseDouble(app.getState().get("alt"));
		loc = new Location(titel, info,lat, lng, alt, "default",id);		
	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.berichten_menu, menu);
		return true;
	}
	public void go_klik(View v) {
		DBWrapper.save(loc);
		
		Intent myIntent = new Intent(Locatie_opslaan.this, Locaties.class);
		Locatie_opslaan.this.startActivity(myIntent);
	}
}
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locatie_opslaan_layout);
	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.berichten_menu, menu);
		return true;
	}

	public void go_klik(View v) {
		EditText titel_edit = (EditText) findViewById(R.id.titel);
		String titel = titel_edit.getText().toString();
		EditText info_edit = (EditText) findViewById(R.id.info);
		String info = info_edit.getText().toString();
		VopApplication app = (VopApplication) getApplicationContext();
		Integer id = Integer.parseInt(app.getState().get("userid"));
		double lng = app.getLng();
		double lat = app.getLat();
		double alt = app.getAlt();
		Location loc = new Location(titel, info, lat, lng, alt, "default", id);
		DBWrapper.save(loc);
		finish();
	}
}
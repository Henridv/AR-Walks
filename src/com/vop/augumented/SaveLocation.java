package com.vop.augumented;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

public class SaveLocation extends FullscreenActivity {

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

	public void saveLocation(View v) {
		EditText name = (EditText) findViewById(R.id.loc_name);
		String locName = name.getText().toString();
		EditText descr = (EditText) findViewById(R.id.loc_descr);
		String locDescr = descr.getText().toString();
		
		VopApplication app = (VopApplication) getApplicationContext();
		Integer id = Integer.parseInt(app.getState().get("userid"));
		double lng = app.getLng();
		double lat = app.getLat();
		double alt = app.getAlt();
		Location loc = new Location(locName, locDescr, lat, lng, alt, "default", id);
		DBWrapper.save(loc);
		finish();
	}
}
package com.vop.arwalks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.vop.services.LocationService;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;

public class SaveLocation extends FullscreenActivity {
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locatie_opslaan_layout);
		intent = new Intent(this, LocationService.class);
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
		Location loc = new Location(locName, locDescr, lat, lng, alt, id);
		DBWrapper.save(loc);
		finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		startService(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopService(intent);
	}
}

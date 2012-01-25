package com.vop.arwalks;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;

public class AddLocation extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_location);
	}

	public void saveLocation(View v) {
		EditText name = (EditText) findViewById(R.id.loc_name);
		String locName = name.getText().toString();

		VopApplication app = (VopApplication) getApplicationContext();
		Integer id = Integer.parseInt(app.getState().get("userid"));
		double lng = app.getLng();
		double lat = app.getLat();
		double alt = app.getAlt();

		Location loc = new Location(locName, lat, lng, alt, id);
		DBWrapper.save(loc);
		finish();
	}
}

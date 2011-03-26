package com.vop.augumented;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;

import android.app.Activity;
import android.content.Context;

class Bereken_gegevens implements Callable<Marker[]> {
	Context context;
	Activity activiteit;

	public Bereken_gegevens(Context kontekst, Activity activity) {
		context = kontekst;
		activiteit = activity;
	}

	@Override
	public Marker[] call() throws Exception {
		VopApplication app = (VopApplication) context;
		double lng = Double.parseDouble(app.getState().get("long"));
		double lat = Double.parseDouble(app.getState().get("lat"));
		double alt = Double.parseDouble(app.getState().get("alt"));
		double roll = Double.parseDouble(app.getState().get("roll"));
		ArrayList<com.vop.tools.data.Location> loc = DBWrapper.getLocations(2);
		Marker POI[] = new Marker[loc.size()];
		int j = 0;
		for (com.vop.tools.data.Location l : loc) {
			POI[j] = new Marker(l.getName(), l.getDescription(),
					l.getLongitude(), l.getLatitute(), alt, lat, lng, alt, roll);
			j++;
		}
		activiteit.removeDialog(1);
		return POI;
	}

}

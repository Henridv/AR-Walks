package com.vop.augumented;

import java.util.ArrayList;

import overlays.Marker;

import com.vop.augumented.R;
import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Traject;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListView_Locaties extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		construeer();
		super.onCreate(savedInstanceState);
		VopApplication app = (VopApplication) getApplicationContext();
		Marker POI[] =app.getPunten();
		String res[] = new String[POI.length];
		for(int i = 0;i<POI.length;i++) res[i] = POI[i].getTitel(); 
		setListAdapter(new ArrayAdapter<String>(this,
				R.layout.trajecten_layout, res));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VopApplication app = (VopApplication) getApplicationContext();
				Marker POI[] = app.getPunten();
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						POI[position].getTitel() +" "+POI[position].getInfo(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	public void construeer() {
		VopApplication app = (VopApplication) getApplicationContext();
		Marker POI[];
		ArrayList<com.vop.tools.data.Location> loc = DBWrapper.getLocations(2);
		POI = new Marker[loc.size()];
		int j = 0;
		for (com.vop.tools.data.Location l : loc) {
			POI[j] = new Marker(l.getName(), l.getDescription(),
					l.getLongitude(), l.getLatitute(), l.getAltitude(),
					getApplicationContext());
			j++;
		}
		app.setPunten(POI);
		Toast toast = Toast.makeText(getApplicationContext(), "update voltooid", Toast.LENGTH_SHORT);
		toast.show();
	}
}

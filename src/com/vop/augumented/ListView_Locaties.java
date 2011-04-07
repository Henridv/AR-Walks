package com.vop.augumented;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;

public class ListView_Locaties extends FullscreenActivity {
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		VopApplication app = (VopApplication) getApplicationContext();
		setContentView(R.layout.listview_locaties_layout);
		ListView myListView = (ListView) findViewById(R.id.myListView);
		myListView.setCacheColorHint(Color.WHITE);
		myListView.setBackgroundColor(Color.WHITE);
		// Create the array list of to do items
		final ArrayList<String> todoItems = new ArrayList<String>();
		// Create the array adapter to bind the array to the listview
		final ArrayAdapter<String> aa;
		aa = new ArrayAdapter<String>(this, R.layout.list_black_text, todoItems);
		// Bind the array adapter to the listview.
		myListView.setAdapter(aa);

		Marker POI[] = app.getPunten();
		for (int i = 0; i < POI.length; i++) {
			todoItems.add(0, POI[i].getTitel());
			aa.notifyDataSetChanged();
		}
		myListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VopApplication app = (VopApplication) getApplicationContext();
				Marker POI[] = app.getPunten();
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						POI[POI.length - position - 1].getTitel(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
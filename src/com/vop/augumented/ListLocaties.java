package com.vop.augumented;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.vop.overlays.Marker;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;

public class ListLocaties extends FullscreenListActivity {
	private VopApplication app;
	private Activity activity;
	private String[] res;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		app=(VopApplication) getApplicationContext();
		lv.setTextFilterEnabled(true);
		activity = this;
		updateLocaties();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VopApplication app = (VopApplication) getApplicationContext();
				Marker POI[] = app.getPunten();
				// When clicked, show a dialog with the TextView text
				AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
				dialog.setTitle(POI[position].getTitle());
				dialog.setMessage(POI[position].getInfo());
				dialog.show();
			}
		});
	}
	public void updateLocaties(){
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Bezig met inladen van punten", true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				app.construeer2(activity);
				Marker POI[]=app.getPunten();
				res = new String[POI.length];
				for(int i = 0;i<POI.length;i++) res[i] = POI[i].getTitle();
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						setListAdapter(new ArrayAdapter<String>(activity, R.layout.trajecten_layout, res));
						
					}
				});
				
			}
		}).start();
		
	}
}

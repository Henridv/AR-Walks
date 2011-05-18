package com.vop.arwalks;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;

/**
 * alternative list view of locations
 * 
 * @author gbostoen
 * 
 */
public class ListLocaties extends FullscreenListActivity {
	private Activity activity;
	private String[] res;
	ArrayList<com.vop.tools.data.Location> loc;
	VopApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		app = (VopApplication) getApplicationContext();
		activity = this;
		updateLocaties();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a dialog with the TextView text
				AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
				dialog.setTitle(loc.get(position).getName());
				dialog.setMessage(loc.get(position).getDescription());
				dialog.show();
			}
		});
	}

	/**
	 * updating locations in new thread
	 */
	private void updateLocaties() {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading locations. Please wait...", true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				loc = DBWrapper.getLocations(Integer.parseInt(app.getState().get("userid")));
				res = new String[loc.size()];
				for (int i = 0; i < res.length; i++)
					res[i] = loc.get(i).getName();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						setListAdapter(new ArrayAdapter<String>(activity, R.layout.list_layout, res));
					}
				});
			}
		}).start();

	}
}

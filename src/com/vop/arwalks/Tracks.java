package com.vop.arwalks;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.vop.ar.EditTraject;
import com.vop.ar.StartEenWandeling;
import com.vop.arwalks.R;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Traject;

/**
 * tours
 * 
 * @author gbostoen
 * 
 */
public class Tracks extends FullscreenListActivity {
	ArrayList<Traject> trajecten;
	private Activity activity;
	private String[] res;
	private ArrayAdapter<Traject> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		activity = this;
		
		/**
		 * Short click starts the walk in an augmented view
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				VopApplication app = (VopApplication) getApplicationContext();
				app.setTraject(trajecten.get(position));
				Toast.makeText(Tracks.this, "not yet implemented", Toast.LENGTH_SHORT).show();
			}
		});

		/**
		 * Long click opens a dialog to walk/edit/delete the walk
		 */
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final CharSequence[] items = { "Walk", "Edit", "Delete" };

				AlertDialog.Builder builder = new AlertDialog.Builder(Tracks.this);
				builder.setTitle(trajecten.get(position).toString());
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Walk")) {
							Intent myIntent = new Intent(Tracks.this, StartEenWandeling.class);
							myIntent.putExtra("walk_id", trajecten.get(position).getId());
							Tracks.this.startActivity(myIntent);
						} else if (items[item].equals("Edit")) {
							Intent myIntent = new Intent(Tracks.this, EditTraject.class);
							myIntent.putExtra("walk_id", trajecten.get(position).getId());
							startActivity(myIntent);
						} else if (items[item].equals("Delete")) {
							DBWrapper.delete(trajecten.get(position));
							trajecten.remove(position);
							adapter.notifyDataSetChanged();
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.trajecten_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.traject_update:
			updateWalks();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Update every time the activity is resumed
	 */
	@Override
	public void onResume() {
		super.onResume();
		updateWalks();
	}

	/**
	 * Updates traject list
	 */
	private void updateWalks() {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Bezig met inladen van trajecten", true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				trajecten = DBWrapper.getTrajects2();

				res = new String[trajecten.size()];
				{
					for (int i = 0; i < trajecten.size(); i++) {
						res[i] = "'" + trajecten.get(i) + "' by "
								+ trajecten.get(i).getPerson();
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						dialog.dismiss();
						adapter = new ArrayAdapter<Traject>(activity, R.layout.trajecten_layout, trajecten);
						setListAdapter(adapter);
					}
				});
			}
		}).start();
	}
}

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.vop.ar.AugmentedReality;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Track;

/**
 * tours
 * 
 * @author gbostoen
 * 
 */
public class Tracks extends FullscreenListActivity {
	ArrayList<Track> tracks;
	private Activity activity;
	private String[] res;
	private ArrayAdapter<Track> adapter;

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
				Intent myIntent = new Intent(Tracks.this, StartTrack.class);
				myIntent.putExtra("walk_id", tracks.get(position).getId());
				Tracks.this.startActivity(myIntent);
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
				builder.setTitle(tracks.get(position).toString());
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Walk")) {
							Intent myIntent = new Intent(Tracks.this, AugmentedReality.class);
							
							myIntent.putExtra("type", "track");
							VopApplication app = (VopApplication) getApplicationContext();
							app.setTrack(DBWrapper.getTrack(tracks.get(position).getId()));
							
							Tracks.this.startActivity(myIntent);
						} else if (items[item].equals("Edit")) {
							Intent myIntent = new Intent(Tracks.this, EditTraject.class);
							myIntent.putExtra("walk_id", tracks.get(position).getId());
							startActivity(myIntent);
						} else if (items[item].equals("Delete")) {
							DBWrapper.delete(tracks.get(position));
							tracks.remove(position);
							adapter.notifyDataSetChanged();
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				return true;
			}
		});
		updateWalks();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tracks, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.traject_update:
			updateWalks();
			return true;
		case R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Updates traject list
	 */
	private void updateWalks() {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading tracks. Please wait...", true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				tracks = DBWrapper.getTrajects2();

				res = new String[tracks.size()];
				{
					for (int i = 0; i < tracks.size(); i++) {
						res[i] = "'" + tracks.get(i) + "' by "
								+ tracks.get(i).getPerson();
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						dialog.dismiss();
						adapter = new ArrayAdapter<Track>(activity, R.layout.list, tracks);
						setListAdapter(adapter);
					}
				});
			}
		}).start();
	}
}

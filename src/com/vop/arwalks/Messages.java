package com.vop.arwalks;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

import com.vop.arwalks.R;
import com.vop.map.MessageOnMap;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;
import com.vop.tools.data.Track;

public class Messages extends FullscreenListActivity {

	ArrayList<Location> locations;
	private Activity activity;
	private String[] res;
	private ArrayAdapter<Location> adapter;
	VopApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (VopApplication) getApplicationContext();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		updateMessages();

		activity = this;

		/**
		 * Short click starts the walk in an augmented view
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent myIntent = new Intent(Messages.this, MessageOnMap.class);
				myIntent.putExtra("id", locations.get(position).getId());
				Messages.this.startActivity(myIntent);
			}
		});

		/**
		 * Long click opens a dialog to walk/edit/delete the walk
		 */
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final CharSequence[] items = { "Watch", "Edit", "Delete" };

				AlertDialog.Builder builder = new AlertDialog.Builder(Messages.this);
				builder.setTitle(locations.get(position).toString());
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Watch")) {
							Intent myIntent = new Intent(Messages.this, ShowMessage.class);
							myIntent.putExtra("id", locations.get(position).getId());
							Messages.this.startActivity(myIntent);
						} else if (items[item].equals("Edit")) {
							Intent myIntent = new Intent(Messages.this, EditMessage.class);
							myIntent.putExtra("id", locations.get(position).getId());
							startActivity(myIntent);
						} else if (items[item].equals("Delete")) {
							DBWrapper.delete(locations.get(position));
							locations.remove(position);
							adapter.notifyDataSetChanged();
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				return true;
			}
		});
		//updateWalks();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.message_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.message_post:
			Intent myIntent = new Intent(Messages.this, AddMessage.class);
			myIntent.putExtra("type", "locations");
			Messages.this.startActivity(myIntent);
			updateMessages();
			return true;
		case R.id.message_update:
			//updateWalks();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Updates message list
	*/ 
	private void updateMessages() {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading messages. Please wait...", true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				locations = DBWrapper.getLocations(Integer.parseInt(app.getState().get("userid")));

				res = new String[locations.size()];
				for (int i = 0; i < res.length; i++)
					res[i] = locations.get(i).getName();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						adapter = new ArrayAdapter<Location>(activity, R.layout.list_layout, locations);
						setListAdapter(adapter);
					}
				});			
			}
		}).start();
	}
		
}
	package com.vop.augumented;

import java.util.ArrayList;

import android.app.AlertDialog;
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

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Traject;

public class Trajecten extends FullscreenListActivity {
	ArrayList<Traject> trajecten;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateWalks();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		/**
		 * Short click starts the walk in an augmented view
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				VopApplication app = (VopApplication) getApplicationContext();
				app.setTraject(trajecten.get(position));
				Toast.makeText(Trajecten.this, "not yet implemented", Toast.LENGTH_SHORT).show();
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

				AlertDialog.Builder builder = new AlertDialog.Builder(Trajecten.this);
				builder.setTitle(trajecten.get(position).toString());
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Walk")) {
							Intent myIntent = new Intent(Trajecten.this,StartEenWandeling.class);
							myIntent.putExtra("walk_id",trajecten.get(position).getId());
							Trajecten.this.startActivity(myIntent);
							//Toast.makeText(Trajecten.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						} else if (items[item].equals("Edit")) {
							Intent myIntent = new Intent(Trajecten.this, EditTraject.class);
							myIntent.putExtra("walk_id", trajecten.get(position).getId());
							startActivity(myIntent);
						} else if (items[item].equals("Delete")) {
							DBWrapper.delete(trajecten.get(position));
							updateWalks();
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
		trajecten = DBWrapper.getTrajects();

		String[] res = new String[trajecten.size()];
		{
			for (int i = 0; i < trajecten.size(); i++) {
				res[i] = "'" + trajecten.get(i) + "' by "
						+ trajecten.get(i).getPerson();
			}
		}

		setListAdapter(new ArrayAdapter<String>(this, R.layout.trajecten_layout, res));
	}

}

package com.vop.arwalks;

import java.util.ArrayList;

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

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Message;

public class Messages extends FullscreenListActivity {

	private ArrayList<Message> messages;
	private String[] res;
	private ArrayAdapter<Message> adapter;
	private VopApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (VopApplication) getApplicationContext();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		updateMessages();

		/**
		 * Short click starts the walk in an augmented view
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent myIntent = new Intent(Messages.this, ShowMessage.class);
				myIntent.putExtra("id", messages.get(position).getId());
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
				builder.setTitle(messages.get(position).toString());
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Watch")) {
							Intent myIntent = new Intent(Messages.this, ShowMessage.class);
							myIntent.putExtra("id", messages.get(position).getId());
							Messages.this.startActivity(myIntent);
						} else if (items[item].equals("Edit")) {
							Intent myIntent = new Intent(Messages.this, EditMessage.class);
							myIntent.putExtra("id", messages.get(position).getId());
							startActivity(myIntent);
						} else if (items[item].equals("Delete")) {
							DBWrapper.delete(messages.get(position));
							messages.remove(position);
							adapter.notifyDataSetChanged();
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				return true;
			}
		});

		setContentView(R.layout.list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.messages, menu);
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
			updateMessages();
			return true;
		case R.id.home:
			this.finish();
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
				messages = DBWrapper.getMessages(Integer.parseInt(app.getState().get("userid")));

				res = new String[messages.size()];
				for (int i = 0; i < res.length; i++)
					res[i] = messages.get(i).getName();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						adapter = new ArrayAdapter<Message>(getApplicationContext(), R.layout.list, messages);
						setListAdapter(adapter);
					}
				});
			}
		}).start();
	}

}
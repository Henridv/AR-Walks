package com.vop.arwalks;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.vop.ar.FriendProfile;
import com.vop.arwalks.R;
import com.vop.popup.ActionItem;
import com.vop.popup.QuickAction;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

/**
 * add a friend
 * 
 * @author gbostoen
 * 
 */
public class AddFriend extends FullscreenListActivity {
	private VopApplication app;
	private Activity activity;
	private String[] res;
	ArrayList<Person> p1;
	ArrayList<Person> p2;
	Vibrator vibrator;

	QuickAction qa;
	static private ActionItem actie1 = new ActionItem();
	static private ActionItem actie2 = new ActionItem();
	private int positie;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		app = (VopApplication) getApplicationContext();
		lv.setTextFilterEnabled(true);
		activity = this;
		updateNotAddedPersons();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// menu opstartten
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				vibrator.vibrate(60);
				positie = position;
				qa = new QuickAction(view);

				actie1.setTitle("View profile");
				// actie1.setIcon(getResources().getDrawable(R.drawable.chart));
				actie1.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						if (positie > p2.size() - 1) {
							int pos = positie - p2.size();
							Intent myIntent = new Intent(AddFriend.this, FriendProfile.class);
							myIntent.putExtra("profielid", p1.get(pos).getId());
							AddFriend.this.startActivity(myIntent);
						} else {
							Intent myIntent = new Intent(AddFriend.this, FriendProfile.class);
							myIntent.putExtra("profielid", p2.get(positie).getId());
							AddFriend.this.startActivity(myIntent);
						}
					}
				});
				actie2.setTitle("Send message");
				actie2.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						if (positie > p2.size() - 1) {
							int pos = positie - p2.size();
							DBWrapper.addFriend(Integer.parseInt(app.getState().get("userid")), p1.get(pos).getId());
							updateNotAddedPersons();
						} else {
							DBWrapper.addFriend(Integer.parseInt(app.getState().get("userid")), p2.get(positie).getId());
							updateNotAddedPersons();
						}
					}
				});
				qa.addActionItem(actie1);
				qa.addActionItem(actie2);
				qa.setAnimStyle(QuickAction.ANIM_AUTO);
				qa.show();
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final CharSequence[] items = { "Add friend", "View profile" };

				AlertDialog.Builder builder = new AlertDialog.Builder(AddFriend.this);
				builder.setTitle(res[position]);
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (position > p2.size() - 1) {
							int pos = position - p2.size();
							if (items[item].equals("Add friend")) {
								DBWrapper.addFriend(Integer.parseInt(app.getState().get("userid")), p1.get(pos).getId());
								updateNotAddedPersons();
							} else if (items[item].equals("View profile")) {
								Intent myIntent = new Intent(AddFriend.this, FriendProfile.class);
								myIntent.putExtra("profielid", p1.get(pos).getId());
								AddFriend.this.startActivity(myIntent);
							}
						} else {
							if (items[item].equals("add")) {
								DBWrapper.addFriend(Integer.parseInt(app.getState().get("userid")), p2.get(position).getId());
								updateNotAddedPersons();
							} else if (items[item].equals("profiel")) {
								Intent myIntent = new Intent(AddFriend.this, FriendProfile.class);
								myIntent.putExtra("profielid", p2.get(position).getId());
								AddFriend.this.startActivity(myIntent);
							}
						}

					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				return true;
			}
		});
	}

	/**
	 * update all persons in list
	 */
	private void updateNotAddedPersons() {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading people. Please wait...", true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				p1 = DBWrapper.getNotAddedPersons(Integer.parseInt(app.getState().get("userid")));
				p2 = DBWrapper.getPeopelWhoAddedYou(Integer.parseInt(app.getState().get("userid")));
				res = new String[p1.size() + p2.size()];
				for (int i = 0; i < p2.size(); i++)
					res[i] = p2.get(i).getEmail() + " +++";
				for (int i = 0; i < p1.size(); i++)
					res[i + p2.size()] = p1.get(i).getEmail();
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

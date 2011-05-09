package com.vop.arwalks;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.vop.arwalks.R;
import com.vop.popup.ActionItem;
import com.vop.popup.QuickAction;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

/**
 * friends
 * 
 * @author gbostoen
 * 
 */
public class Friends extends FullscreenListActivity {

	static ArrayList<Person> vrienden;
	private VopApplication app;
	static private ActionItem actie1 = new ActionItem();
	static private ActionItem actie2 = new ActionItem();
	static private ActionItem actie3 = new ActionItem();
	Vibrator vibrator;
	private int positie;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateFriends();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		app = (VopApplication) getApplicationContext();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// menu opstartten
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				vibrator.vibrate(60);
				positie = position;
				final QuickAction qa = new QuickAction(view);

				actie1.setTitle("View profile");
				// actie1.setIcon(getResources().getDrawable(R.drawable.chart));
				actie1.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(Friends.this, FriendProfile.class);
						myIntent.putExtra("profielid", vrienden.get(positie).getId());
						Friends.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie2.setTitle("Send message");
				actie2.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Toast.makeText(Friends.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						qa.dismiss();
					}
				});
				actie3.setTitle("Remove friend");
				actie3.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						DBWrapper.deleteFriend(Integer.parseInt(app.getState().get("userid")), vrienden.get(positie).getId());
						updateFriends();
						qa.dismiss();
					}
				});
				qa.addActionItem(actie1);
				qa.addActionItem(actie2);
				qa.addActionItem(actie3);
				qa.setAnimStyle(QuickAction.ANIM_AUTO);
				qa.show();
			}
		});
	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.vrienden_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.update_friends:
			updateFriends();
			return true;
		default:
			// add a friend!!

			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		updateFriends();
	}

	/**
	 * update current friends
	 */
	private void updateFriends() {
		VopApplication app = (VopApplication) getApplicationContext();
		int id = Integer.parseInt(app.getState().get("userid"));

		vrienden = DBWrapper.getFriends(id);

		String[] res = new String[vrienden.size()];
		{
			for (int i = 0; i < vrienden.size(); i++) {
				res[i] = vrienden.get(i).getName();
			}
		}

		setListAdapter(new ArrayAdapter<String>(this, R.layout.vrienden_layout, res));

	}
}
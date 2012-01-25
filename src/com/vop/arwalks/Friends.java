package com.vop.arwalks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.vop.popup.ActionItem;
import com.vop.popup.QuickAction;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class Friends extends FullscreenListActivity {

	private ArrayList<Person> friends;
	private VopApplication app;
	static private ActionItem actie1 = new ActionItem();
	static private ActionItem actie2 = new ActionItem();
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
						myIntent.putExtra("profielid", friends.get(positie).getId());
						Friends.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie2.setTitle("Remove friend");
				actie2.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						DBWrapper.deleteFriend(Integer.parseInt(app.getState().get("userid")), friends.get(positie).getId());
						updateFriends();
						qa.dismiss();
					}
				});
				qa.addActionItem(actie1);
				qa.addActionItem(actie2);
				qa.setAnimStyle(QuickAction.ANIM_AUTO);
				qa.show();
			}
		});
		setContentView(R.layout.list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.friends, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.update_friends:
			updateFriends();
			return true;
		case R.id.home:
			this.finish();
			return true;
		case R.id.add_friend:
			Intent myIntent = new Intent(this, AddFriend.class);
			this.startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		updateFriends();
	}

	private void updateFriends() {
		VopApplication app = (VopApplication) getApplicationContext();
		int id = Integer.parseInt(app.getState().get("userid"));

		friends = DBWrapper.getFriends(id);
		
		List<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();

		for (Person f : friends) {
			HashMap<String, String> row = new HashMap<String, String>();
			row.put("name", f.getName());
			row.put("phone", f.getPhone());
			data.add(row);
		}
		SimpleAdapter adapter = new SimpleAdapter(this,
				data,
				R.layout.row,
				new String[] {"name", "phone"},
				new int[] {R.id.toptext, R.id.bottomtext});
		setListAdapter(adapter);
	}
}
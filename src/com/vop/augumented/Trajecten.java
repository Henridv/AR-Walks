package com.vop.augumented;

import java.util.ArrayList;

import com.vop.augumented.R;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.data.Traject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Trajecten extends FullscreenListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// test hallo
		ArrayList<Traject> trajecten = DBWrapper.getTrajects();

		String[] res = new String[trajecten.size()];
		{
			for (int i = 0; i < trajecten.size(); i++) {
				res[i] = "naam traject: " + trajecten.get(i).getName() + ", "
						+ "aangemaakt door " + trajecten.get(i).getPerson();
			}
		}

		setListAdapter(new ArrayAdapter<String>(this,
				R.layout.trajecten_layout, res));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
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
			updateTrajects();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void updateTrajects() {
		// send request to server
	}

}

package com.vop.augumented;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;

public class Vrienden extends FullscreenActivity {

	private DBWrapper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vrienden_layout);
		db = new DBWrapper();
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
		case R.id.refresh:
			Toast toast = Toast.makeText(getApplicationContext(),
					"u selecteerde:" + db.getFriends(0), Toast.LENGTH_SHORT);
			toast.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
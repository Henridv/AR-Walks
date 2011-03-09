package com.vop.augumented;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vop.tools.FullscreenActivity;

public class Trajecten extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.trajecten_layout);
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
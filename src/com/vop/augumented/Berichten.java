package com.vop.augumented;

import com.vop.tools.FullscreenActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class Berichten extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.berichten_layout);
	}
	//menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.berichten_menu, menu);
	    return true;
	}
}
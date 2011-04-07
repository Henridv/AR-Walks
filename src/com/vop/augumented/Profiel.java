package com.vop.augumented;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.vop.tools.FullscreenActivity;

public class Profiel extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiel_layout);

	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.profiel_menu, menu);
		return true;
	}
}

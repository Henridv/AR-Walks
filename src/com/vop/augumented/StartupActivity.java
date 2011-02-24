package com.vop.augumented;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StartupActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.startupactivity_layout);
	}
	//go-klik
	public void go_klik(View v){
    	Intent myIntent = new Intent(StartupActivity.this, Hoofdmenu.class);
    	StartupActivity.this.startActivity(myIntent);
	}

}

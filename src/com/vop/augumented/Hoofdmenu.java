package com.vop.augumented;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vop.tools.FullscreenActivity;

public class Hoofdmenu extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.hoofdmenu_layout);
	}
	//knoppen
	public void locaties_klik(View v){
    	Intent myIntent = new Intent(Hoofdmenu.this, Locaties.class);
    	Hoofdmenu.this.startActivity(myIntent);
	}
	public void trajecten_klik(View v){
    	Intent myIntent = new Intent(Hoofdmenu.this, Trajecten.class);
    	Hoofdmenu.this.startActivity(myIntent);
	}
	public void profiel_klik(View v){
    	Intent myIntent = new Intent(Hoofdmenu.this, Profiel.class);
    	Hoofdmenu.this.startActivity(myIntent);
	}
	public void vrienden_klik(View v){
    	Intent myIntent = new Intent(Hoofdmenu.this, Vrienden.class);
    	Hoofdmenu.this.startActivity(myIntent);
	}
	public void berichten_klik(View v){
    	Intent myIntent = new Intent(Hoofdmenu.this, Berichten.class);
    	Hoofdmenu.this.startActivity(myIntent);
	}
	public void uitloggen_klik(View v){
    	Intent myIntent = new Intent(Hoofdmenu.this, StartupActivity.class);
    	Hoofdmenu.this.startActivity(myIntent);
	}
	//menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.hoofdmenu_menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.optie_1:
	    	Toast toast = Toast.makeText(getApplicationContext(), "u selecteerde:" +item.getTitle(), Toast.LENGTH_SHORT);
	    	toast.show();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
}
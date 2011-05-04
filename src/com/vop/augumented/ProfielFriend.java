package com.vop.augumented;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.data.Person;
/**
 * profile of friend
 * @author gbostoen
 *
 */
public class ProfielFriend extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profielfriendlayout);
		updateProfile();
	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.profiel_menu, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateProfile();
	}
	
	private void updateProfile(){
		int id = getIntent().getIntExtra("profielid", 0);
		Person p = DBWrapper.getProfile(id);
		TextView naam = new TextView(this);
		naam=(TextView)findViewById(R.id.profname); 
	    naam.setText("Name: "+p.getName());
	    TextView tel = new TextView(this);
		tel=(TextView)findViewById(R.id.profphone); 
	    tel.setText("Phone: "+p.getPhone());
	    TextView email = new TextView(this);
		email=(TextView)findViewById(R.id.profmail); 
	    email.setText("E-mail: "+p.getEmail());
	}
	
}

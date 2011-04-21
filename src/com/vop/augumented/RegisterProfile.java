package com.vop.augumented;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class RegisterProfile extends FullscreenActivity {
	VopApplication app;
	Vibrator vibrator;
	EditText emailText;
	EditText passText;
	EditText nameText;
	EditText telefoonNr;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (VopApplication) getApplicationContext();
		setContentView(R.layout.registerprofile_layout);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		String email = getIntent().getStringExtra("email");
		String pass = getIntent().getStringExtra("password");
		emailText = (EditText) findViewById(R.id.widget31);
		emailText.setText(email);
		passText = (EditText) findViewById(R.id.widget33);
		passText.setText(pass);
		nameText = (EditText) findViewById(R.id.widget29);
		telefoonNr=(EditText) findViewById(R.id.widget37);
	}
	public void register(View v) {
		if(passText.getText().length()>0 && emailText.getText().length()>0 && nameText.getText().length() >0 && telefoonNr.getText().length()> 0){
			Person p=new Person(nameText.getText().toString(), telefoonNr.getText().toString(), passText.getText().toString(), emailText.getText().toString());
			DBWrapper.save(p);
			this.finish();
		}
		else{
			Toast.makeText(getApplicationContext(), "Credentials not valid!", Toast.LENGTH_LONG).show();
		}
	}
}

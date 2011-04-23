package com.vop.augumented;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class ToevoegenVriend extends FullscreenListActivity {
	private VopApplication app;
	private Activity activity;
	private String[] res;
	ArrayList<Person> p;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		app=(VopApplication) getApplicationContext();
		lv.setTextFilterEnabled(true);
		activity = this;
		updateNotAddedPersons();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a dialog with the TextView text
				AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
				dialog.setTitle("test");
				dialog.setMessage("test");
				dialog.show();
			}
		});
	}
	public void updateNotAddedPersons(){
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Bezig met inladen van personen", true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				p=DBWrapper.getNotAddedPersons(Integer.parseInt(app.getState().get("userid")));
				res=new String[p.size()];
				for(int i=0;i<res.length;i++)res[i]=p.get(i).getEmail();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						setListAdapter(new ArrayAdapter<String>(activity, R.layout.trajecten_layout, res));
						
					}
				});
			}
		}).start();
		
	}
}

package com.vop.augumented;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenListActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class ToevoegenVriend extends FullscreenListActivity {
	private VopApplication app;
	private Activity activity;
	private String[] res;
	ArrayList<Person> p1;
	ArrayList<Person> p2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		app=(VopApplication) getApplicationContext();
		lv.setTextFilterEnabled(true);
		activity = this;
		updateNotAddedPersons();

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final CharSequence[] items = { "add","profiel"};

				AlertDialog.Builder builder = new AlertDialog.Builder(ToevoegenVriend.this);
				builder.setTitle(res[position]);
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if(position > p2.size()-1){
							int pos = position - p2.size();
							if(items[item].equals("add")){
								DBWrapper.addFriend(Integer.parseInt(app.getState().get("userid")), p1.get(pos).getId());
								updateNotAddedPersons();
							}
							else if(items[item].equals("profiel")){
								Intent myIntent = new Intent(ToevoegenVriend.this,ProfielFriend.class);
								myIntent.putExtra("profielid",p1.get(pos).getId());
								ToevoegenVriend.this.startActivity(myIntent);
							}
						}
						else{
							if(items[item].equals("add")){
								DBWrapper.addFriend(Integer.parseInt(app.getState().get("userid")), p2.get(position).getId());
								updateNotAddedPersons();
							}
							else if(items[item].equals("profiel")){
								Intent myIntent = new Intent(ToevoegenVriend.this,ProfielFriend.class);
								myIntent.putExtra("profielid",p2.get(position).getId());
								ToevoegenVriend.this.startActivity(myIntent);
							}
						}
						
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				return true;
			}
		});
	}
	public void updateNotAddedPersons(){
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Bezig met inladen van personen", true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				p1=DBWrapper.getNotAddedPersons(Integer.parseInt(app.getState().get("userid")));
				p2=DBWrapper.getPeopelWhoAddedYou(Integer.parseInt(app.getState().get("userid")));
				res=new String[p1.size()+p2.size()];
				for(int i=0;i<p2.size();i++)res[i]=p2.get(i).getEmail()+" +++";
				for(int i=0;i<p1.size();i++) res[i+p2.size()]=p1.get(i).getEmail();
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

package com.vop.tools.data;


import java.util.ArrayList;

import com.vop.augumented.R;
import com.vop.tools.DBWrapper;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViews extends ListActivity {
	  
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.trajecten_layout, res));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener(){
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	      // tijdelijk toast tonen
	      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	          Toast.LENGTH_SHORT).show();
	    }
	  });
	}
	
	static ArrayList<Traject> trajecten = DBWrapper.getTrajects();
	
	String[] res = new String[trajecten.size()]; {
		for (int i = 0; i < trajecten.size(); i++){
			res[i] = "naam traject: "+ trajecten.get(i).getName()+", "+"aangemaakt door "+trajecten.get(i).getPerson();		
		}
	}
}


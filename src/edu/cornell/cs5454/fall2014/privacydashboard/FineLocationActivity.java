package edu.cornell.cs5454.fall2014.privacydashboard;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FineLocationActivity extends ActionBarActivity {
	Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finelocation);

		btnBack = (Button) findViewById(R.id.buttonBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent mainIntent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(mainIntent);
				finish();
			}
		});

		try {

			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				List<String> appList = (ArrayList<String>)extras.get(MainActivity.FINE_LOCATION);
				
				String[] pkgArray = appList.toArray(new String[appList.size()]);
				Arrays.sort(pkgArray);
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, pkgArray);

				ListView listView = (ListView) findViewById(R.id.listView2);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(mMessageClickedHandler);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Create a message handling object as an anonymous class.
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			// Do something in response to the click
		}
	};
}

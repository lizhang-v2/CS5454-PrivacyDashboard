package edu.cornell.cs5454.fall2014.privacydashboard;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	public final static String INSTALLED_TIME = "InstalledTime";
	public final static String FINE_LOCATION = "FineLocation";
	public final static String CONTACT = "Contact";
	public final static String CALENDAR = "Calendar";
	public final static String CAMERA = "Camera";
	public final static String CALL_LOG = "CallLog";
	
	Button btnInstalledTime;
	Button btnFineLocation;
	Button btnContact;
	Button btnCalendar;
	Button btnCamera;
	Button btnCallLog;
	
	TextView tvInstalledTime;
	TextView tvFineLocation;
	TextView tvContact;
	TextView tvCalendar;
	TextView tvCamera;
	TextView tvCallLog;
	
	Format dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	HashMap<String, ArrayList<String>> apps = new HashMap<String, ArrayList<String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getAppInfo();
		
		tvInstalledTime = (TextView) findViewById(R.id.textView_installedTime);
		tvFineLocation = (TextView) findViewById(R.id.textView_fineLocation);
		tvContact = (TextView) findViewById(R.id.textView_Contact);
		tvCalendar = (TextView) findViewById(R.id.textView_Calendar);
		tvCamera = (TextView) findViewById(R.id.textView_Camera);
		tvCallLog = (TextView) findViewById(R.id.textView_CallLog);
		
		tvInstalledTime.setText(String.valueOf(apps.get(INSTALLED_TIME).size()));
		tvFineLocation.setText(String.valueOf(apps.get(FINE_LOCATION).size()));
		tvContact.setText(String.valueOf(apps.get(CONTACT).size()));
		tvCalendar.setText(String.valueOf(apps.get(CALENDAR).size()));
		tvCamera.setText(String.valueOf(apps.get(CAMERA).size()));
		tvCallLog.setText(String.valueOf(apps.get(CALL_LOG).size()));
		
		btnInstalledTime = (Button) findViewById(R.id.buttonInstalledTime);
		btnInstalledTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent installedTimeIntent = new Intent(getApplicationContext(),
						InstalledTimeActivity.class);
				installedTimeIntent.putExtra(INSTALLED_TIME, apps.get(INSTALLED_TIME));
				startActivity(installedTimeIntent);
				finish();
			}
		});
		
		btnFineLocation = (Button) findViewById(R.id.buttonFineLocation);
		btnFineLocation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent fineLocationIntent = new Intent(getApplicationContext(),
						FineLocationActivity.class);
				fineLocationIntent.putExtra(FINE_LOCATION, apps.get(FINE_LOCATION));
				startActivity(fineLocationIntent);
				finish();
			}
		});
		
		btnContact = (Button) findViewById(R.id.buttonContact);
		btnContact.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent contactIntent = new Intent(getApplicationContext(),
						ContactActivity.class);
				contactIntent.putExtra(CONTACT, apps.get(CONTACT));
				startActivity(contactIntent);
				finish();
			}
		});
		
		btnCalendar = (Button) findViewById(R.id.buttonCalendar);
		btnCalendar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),
						CalendarActivity.class);
				intent.putExtra(CALENDAR, apps.get(CALENDAR));
				startActivity(intent);
				finish();
			}
		});
		
		btnCamera = (Button) findViewById(R.id.buttonCamera);
		btnCamera.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),
						CameraActivity.class);
				intent.putExtra(CAMERA, apps.get(CAMERA));
				startActivity(intent);
				finish();
			}
		});
		
		btnCallLog = (Button) findViewById(R.id.buttonCallLog);
		btnCallLog.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),
						CallLogActivity.class);
				intent.putExtra(CALL_LOG, apps.get(CALL_LOG));
				startActivity(intent);
				finish();
			}
		});
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
	
	private void getAppInfo() {
		try {
			apps.clear();
			
			List<PackageInfo> packages = getPackageManager()
					.getInstalledPackages(
							PackageManager.GET_UNINSTALLED_PACKAGES);
			
			ArrayList<String> pkgInstalledTime = new ArrayList<String>();
			ArrayList<String> pkgFineLocation = new ArrayList<String>();
			ArrayList<String> pkgContact = new ArrayList<String>();
			ArrayList<String> pkgCalendar = new ArrayList<String>();
			ArrayList<String> pkgCamera = new ArrayList<String>();
			ArrayList<String> pkgCallLog = new ArrayList<String>();
			String packName;
			for (PackageInfo pack : packages) {
				packName = pack.packageName;

				Date date = new Date(pack.firstInstallTime);
				pkgInstalledTime.add(dateFormat.format(date) + " | " + packName);
				
				int ret_fineLocation = getPackageManager().checkPermission(
						android.Manifest.permission.ACCESS_FINE_LOCATION,
						packName);
				if(ret_fineLocation != -1) {
					pkgFineLocation.add(packName);
				}

				int ret_contact = getPackageManager().checkPermission(
						android.Manifest.permission.READ_CONTACTS,
						packName);
				if(ret_contact != -1) {
					pkgContact.add(packName);
				}
				
				int ret_calendar = getPackageManager().checkPermission(
						android.Manifest.permission.READ_CALENDAR,
						packName);
				if(ret_calendar != -1) {
					pkgCalendar.add(packName);
				}
				
				int ret_camera = getPackageManager().checkPermission(
						android.Manifest.permission.CAMERA,
						packName);
				if(ret_camera != -1) {
					pkgCamera.add(packName);
				}
				
				int ret_callLog = getPackageManager().checkPermission(
						android.Manifest.permission.READ_CALL_LOG,
						packName);
				if(ret_callLog != -1) {
					pkgCallLog.add(packName);
				}

			}
			
			apps.put(INSTALLED_TIME, pkgInstalledTime);
			apps.put(FINE_LOCATION, pkgFineLocation);
			apps.put(CONTACT, pkgContact);
			apps.put(CALENDAR, pkgCalendar);
			apps.put(CAMERA, pkgCamera);
			apps.put(CALL_LOG, pkgCallLog);


		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

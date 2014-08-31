package com.example.trackme;

import java.util.Deque;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	TextView tvMain;
	TextView tvLocation;
	double latitude, longitude;
	final int EXITED_AREA = 0;
	final int ENTERED_AREA = 0;
	LocationManager locationManager;
	String locationProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set up accelerometer
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sm.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION).size() != 0) {
			Sensor s = sm.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION).get(0);
			sm.registerListener(new AccelerationTracker(), s,
					SensorManager.SENSOR_DELAY_NORMAL);
		}

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		String locationProvider = locationManager.getBestProvider(crit, true);
		Location loc = locationManager.getLastKnownLocation(locationProvider);
		latitude = loc.getLatitude();
		longitude = loc.getLongitude();
		
		Intent intent = new Intent(this, LocationUpdateReceiver.class);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), EXITED_AREA, intent, 0);
	    locationManager.addProximityAlert(latitude, longitude, 1, -1, pendingIntent);

		setContentView(R.layout.activity_main);
		tvMain = (TextView) findViewById(R.id.tvMain);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvMain.setText("Maximum Accelerations");
		tvLocation.setText(loc.getLatitude() + ", " + loc.getLongitude());
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

	private class AccelerationTracker implements SensorEventListener {
		Deque<Float> prevXs;

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent e) {
		}
	}

	private class LocationUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				Location loc = locationManager.getLastKnownLocation(locationProvider);
				tvLocation.setText(loc.getLatitude() + ",, " + loc.getLongitude());
//				int i = 7 / 0;
			}
		}
	}
}

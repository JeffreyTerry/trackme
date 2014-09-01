package com.example.trackme;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in
																		// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in
																	// Millisecond

	ArrayList<Long> locationChangeTimes;
	ArrayList<Double> locationLatitudes, locationLongitudes;
	TextView tvMain;
	TextView tvLocation;
	double latitude = 0, longitude = 0, oLatitude = 0, oLongitude = 0;
	LocationManager locationManager;
	String locationProvider;
	long startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvMain = (TextView) findViewById(R.id.tvMain);
		tvLocation = (TextView) findViewById(R.id.tvLocation);

		locationChangeTimes = new ArrayList<Long>();
		locationLatitudes = new ArrayList<Double>();
		locationLongitudes = new ArrayList<Double>();
		startTime = System.currentTimeMillis();

		// location stuff
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE,
				new MyLocationListener());

		tvMain.setText("No location changes registered yet");
		tvLocation.setText("Global: (" + getLatitude() + ", " + getLongitude()
				+ ")" + "\nNetwork: (" + getNetworkLatitude() + ", "
				+ getNetworkLongitude() + ")");
	}

	private double getLatitude() {
		Location loc = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (loc != null) {
			return loc.getLatitude();
		}
		return 0;
	}

	private double getLongitude() {
		Location loc = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (loc != null) {
			return loc.getLongitude();
		}
		return 0;
	}

	private double getNetworkLatitude() {
		Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (loc != null) {
			return loc.getLatitude();
		}
		return 0;
	}

	private double getNetworkLongitude() {
		Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (loc != null) {
			return loc.getLongitude();
		}
		return 0;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location l) {
			// TODO Auto-generated method stub
			// Toast.makeText(MainActivity.this, "Location changed",
			// Toast.LENGTH_LONG).show();

			locationChangeTimes.add(System.currentTimeMillis() - startTime);
			locationLatitudes.add(l.getLatitude());
			locationLongitudes.add(l.getLongitude());

			String tvMainText = tvMain.getText().toString();
			if (tvMainText.contentEquals("No location changes registered yet")) {
				tvMainText = "\n";
			}
			if (locationChangeTimes.size() > 20) {
				tvMainText = tvMainText.substring(tvMainText.indexOf('\n') + 1);
			}
			tvMainText += "Update time: "
					+ locationChangeTimes.get(locationChangeTimes.size() - 1)
					+ "\n";

			tvMain.setText(tvMainText);

			tvLocation
					.setText("Last GPS Position: (" + l.getLatitude() + ", "
							+ l.getLongitude() + ")"
							+ "\nLast Network Position: ("
							+ getNetworkLatitude() + ", "
							+ getNetworkLongitude() + ")");
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

}

package com.example.trackme;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	private static final int ALARM_REQUEST_CODE = 897121234;
	private static final long UPDATE_FREQUENCY = 150000;
	private static final long TRACKER_TIMEOUT = 7200000;
	private ListView mainListView;
	private MainListViewAdapter mainArrayAdapter;

	private ArrayList<Double> speeds;
	private ArrayList<Location> locations;
	private ArrayList<Location> currentLocationPacket;
	private LocationUpdateReceiver locationUpdateReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		killAllLocationAlarms();

		speeds = new ArrayList<Double>();
		locations = new ArrayList<Location>();
		currentLocationPacket = new ArrayList<Location>();
		Button bViewMap = (Button) findViewById(R.id.bViewMap);
		bViewMap.setOnClickListener(this);

		initializeListView();
		IntentFilter i = new IntentFilter();
		i.addAction("com.example.trackme.AlarmBroadcastReceiver.SpeedChange");
		locationUpdateReceiver = new LocationUpdateReceiver();
		registerReceiver(locationUpdateReceiver, i);
		startLocationAlarm(UPDATE_FREQUENCY);
	}

	private void initializeListView() {
		mainListView = (ListView) findViewById(R.id.lvMain);
		ArrayList<MainListViewItem> values = new ArrayList<MainListViewItem>();
		mainArrayAdapter = new MainListViewAdapter(this,
				values);

		mainListView.setAdapter(mainArrayAdapter);
	}

	private void startLocationAlarm(long updateInterval) {
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
		PendingIntent broadcastIntent = PendingIntent.getBroadcast(this,
				ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + updateInterval, updateInterval,
				broadcastIntent);
	}

	private void killAllLocationAlarms() {
		Intent intentStop = new Intent(this, AlarmBroadcastReceiver.class);
		PendingIntent senderstop = PendingIntent.getBroadcast(this,
				ALARM_REQUEST_CODE, intentStop,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManagerStop = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManagerStop.cancel(senderstop);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if((locations.size() * UPDATE_FREQUENCY) >= TRACKER_TIMEOUT) {
			locations.clear();
			speeds.clear();
			killAllLocationAlarms();
			startLocationAlarm(AlarmBroadcastReceiver.DEFAULT_TIME_BETWEEN_UPDATES);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		speeds.clear();
		speeds = null;
		locations.clear();
		locations = null;
		mainArrayAdapter.clear();
		mainArrayAdapter = null;

		killAllLocationAlarms();
		unregisterReceiver(locationUpdateReceiver);

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bViewMap:
			Intent mapIntent = new Intent(this, MapActivity.class);
			Bundle extras = new Bundle();
			if (locations.size() > 0) {
				extras.putParcelableArrayList("locations", locations);
				double[] speedArr = new double[speeds.size()];
				for (int i = 0; i < speedArr.length; i++) {
					speedArr[i] = speeds.get(i);
				}
				extras.putDoubleArray("speeds", speedArr);
				mapIntent.putExtras(extras);
			} else {
				mapIntent.putExtras(extras);
			}
			startActivity(mapIntent);
			break;
		}
	}

	private class LocationUpdateReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			Location location = (Location) intent
					.getParcelableExtra("location");
			if (location != null) {
				currentLocationPacket.add(location);

				// calculate speed here
				if (currentLocationPacket.size() == AlarmBroadcastReceiver.DEFAULT_NUMBER_OF_UPDATES) {
					double totalSpeed = 0;
					for (int i = 0; i < currentLocationPacket.size(); i++) {
						totalSpeed += currentLocationPacket.get(i).getSpeed();
					}

					// add the location in the middle of the current packet
					locations.add(currentLocationPacket
							.get(currentLocationPacket.size() / 2));
					// add the average speed between the locations in the
					// current packet
					speeds.add(totalSpeed / locations.size());

					MainListViewItem newItem = new MainListViewItem(locations.get(locations.size() - 1), speeds.get(speeds.size() - 1));
					mainArrayAdapter.add(newItem);
					
					currentLocationPacket.clear();
					
					// stop tracking user at a certain time after last use
					if ((locations.size() * UPDATE_FREQUENCY) >= TRACKER_TIMEOUT) {
						killAllLocationAlarms();
					}
				}
			}
		}
	}
}

package com.example.trackme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class AlarmBroadcastReceiver extends BroadcastReceiver implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
	public static int DEFAULT_NUMBER_OF_UPDATES = 3;
	public static int DEFAULT_TIME_BETWEEN_UPDATES = 500;
	private int numOfLocationUpdatesLeft = DEFAULT_NUMBER_OF_UPDATES;
	private Context context;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;

	@Override
	public void onReceive(Context c, Intent i) {
		context = c;

		mGoogleApiClient = new Builder(c).addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult r) {
	}

	@Override
	public void onConnected(Bundle b) {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(DEFAULT_TIME_BETWEEN_UPDATES);
		mLocationRequest.setNumUpdates(numOfLocationUpdatesLeft);

		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onLocationChanged(Location l) {
		Intent i = new Intent(
				"com.example.trackme.AlarmBroadcastReceiver.SpeedChange");
		i.putExtra("location", l);
		context.sendBroadcast(i);

		if (numOfLocationUpdatesLeft == 1) {
			mGoogleApiClient.unregisterConnectionCallbacks(this);
			mGoogleApiClient.unregisterConnectionFailedListener(this);
			mGoogleApiClient.disconnect();
		}

		numOfLocationUpdatesLeft--;
	}

	@Override
	public void onConnectionSuspended(int i) {
	}
}
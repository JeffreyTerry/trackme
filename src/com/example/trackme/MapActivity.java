package com.example.trackme;

import java.util.ArrayList;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Activity {
	MapFragment map;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.fMap)).getMap();
        
        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("locations") && extras.containsKey("speeds")) {
        	ArrayList<Location> locations = extras.getParcelableArrayList("locations");
        	double[] speeds = extras.getDoubleArray("speeds");
        	
        	// Zoom in to the current location
        	Location currentLocation = locations.get(locations.size() - 1);
        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 16));
        	
        	// Draw lines to connect each location
        	PolylineOptions po = new PolylineOptions().geodesic(true);
        	LatLng currLatLng;
        	for(int i = 0; i < locations.size(); i++) {
        		currLatLng = new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
        		po.add(currLatLng);
        		map.addMarker(new MarkerOptions().position(currLatLng).title("Speed: " + speeds[i]));
        	}
        	map.addPolyline(po);
        }
    }
}
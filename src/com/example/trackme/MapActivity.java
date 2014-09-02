package com.example.trackme;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Activity {
	MapFragment map;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
     // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.fMap)).getMap();
        
        Bundle extras = getIntent().getExtras();
        double[] latitudes = extras.getDoubleArray("latitudes");
        double[] longitudes = extras.getDoubleArray("longitudes");
        long[] times = extras.getLongArray("times");
        int numOfLatitudes = latitudes.length;
        int numOfLongitudes = latitudes.length;
        int numOfTimes = times.length;
        
        
        if(numOfLatitudes != 0 && numOfLongitudes != 0 && numOfTimes != 0){
        	Toast.makeText(this, "(" + latitudes[numOfLatitudes - 1] + ", " + longitudes[numOfLongitudes - 1] + ")", Toast.LENGTH_SHORT).show();
        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitudes[numOfLatitudes - 1], longitudes[numOfLongitudes - 1]), 16));
        	PolylineOptions po = new PolylineOptions().geodesic(true);
        	for(int i = 0; i < numOfLatitudes; i++){
        		po.add(new LatLng(latitudes[i], longitudes[i]));
        		if(times[i] > 120000){  // if the user was at this location for more than 120 seconds
        			int halfmax = 3600000 / 2;
        			int blue = (int) Math.max(0, 255 * (1 - (double) times[i] / halfmax));
        			int red = (int) Math.max(0, 255 * ((double) times[i] / halfmax - 1));
        			int green = 255 - blue - red;
        			long time = (int) times[i];
        			map.addCircle(new CircleOptions()
        		     .center(new LatLng(latitudes[i], longitudes[i]))
        		     .radius(Math.max(time / 10000.0, 50.0))
        		     .strokeColor(Color.RED)
        		     .strokeWidth(3)
        		     .fillColor(Color.rgb(red, green, blue))); // 14400000.0f (for 4 hours max)
        			map.addMarker(new MarkerOptions().position(new LatLng(latitudes[i], longitudes[i])).title("Time: " + (time % 3600000 > 0? time % 3600000 + " hours, ": "") + time % 60000 + " minutes"));
        		}
        	}
        	map.addPolyline(po);
        }

        

        // Polylines are useful for marking paths and routes on the map.
//        map.addPolyline(new PolylineOptions().geodesic(true)
//                .add(new LatLng(-33.866, 151.195))  // Sydney
//                .add(new LatLng(-18.142, 178.431))  // Fiji
//                .add(new LatLng(21.291, -157.821))  // Hawaii
//                .add(new LatLng(37.423, -122.091))  // Mountain View
//        );
    }
}
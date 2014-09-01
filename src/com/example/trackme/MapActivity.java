package com.example.trackme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
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
        int numOfLatitudes = latitudes.length;
        int numOfLongitudes = latitudes.length;
        
        
        if(numOfLatitudes != 0 && numOfLongitudes != 0){
        	Toast.makeText(this, "(" + latitudes[numOfLatitudes - 1] + ", " + longitudes[numOfLongitudes - 1] + ")", Toast.LENGTH_SHORT).show();
        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitudes[numOfLatitudes - 1], longitudes[numOfLongitudes - 1]), 16));
        	PolylineOptions po = new PolylineOptions().geodesic(true);
        	for(int i = 0; i < numOfLatitudes; i++){
        		po.add(new LatLng(latitudes[i], longitudes[i]));
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
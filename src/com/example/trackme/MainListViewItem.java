package com.example.trackme;

import android.location.Location;

public class MainListViewItem {
	public Location location;
	public double speed;
	public MainListViewItem(Location l, double s){
		location = l;
		speed = s;
	}
}

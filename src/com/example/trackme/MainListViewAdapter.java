package com.example.trackme;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainListViewAdapter extends ArrayAdapter<MainListViewItem>{
	private Context context;
	private ArrayList<MainListViewItem> items;
	
	public MainListViewAdapter(Context context, ArrayList<MainListViewItem> items) {
		super(context, R.layout.main_list_item, items);
		
		this.context = context;
		this.items = items;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.main_list_item, parent, false);
		TextView tvNumber = (TextView) rowView.findViewById(R.id.tvItemNumber);
		TextView tvSpeed = (TextView) rowView.findViewById(R.id.tvItemSpeed);
		TextView tvLocation = (TextView) rowView.findViewById(R.id.tvItemLocation);
		String speedString = String.format("%.2f m/s", items.get(position).speed);
		String latitudeString = String.format("%.6f", items.get(position).location.getLatitude());
		String longitudeString = String.format("%.6f", items.get(position).location.getLongitude());
		tvNumber.setText("" + position);
		tvSpeed.setText("Speed: " + speedString);
		tvLocation.setText("Loc: " + "(" + latitudeString + ", " + longitudeString + ")");
		return rowView;
	}
}

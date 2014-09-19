package com.example.trackme;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * NO LONGER USED
 * 
 * @author jterry
 *
 */
public class SpeedChangeEvent implements Parcelable {
	private Location initialLocation;
	private Location finalLocation;
	private double speed;

	/**
	 * 
	 * @param initialLocation
	 *            The location of the phone at the beginning of the speed
	 *            calculation
	 * @param finalLocation
	 *            The location of the phone at the end of the speed calculation
	 * @param speed
	 *            The speed of the user, in meters per second
	 */
	public SpeedChangeEvent(Location initialLocation, Location finalLocation,
			double speed) {
		this.speed = speed;
		this.initialLocation = initialLocation;
		this.finalLocation = finalLocation;
	}

	public Location getInitialLocation() {
		return initialLocation;
	}

	public Location getFinalLocation() {
		return finalLocation;
	}

	public double getSpeed() {
		return speed;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(speed);
		dest.writeParcelable(initialLocation, flags);
		dest.writeParcelable(finalLocation, flags);
	}

	public static final Parcelable.Creator<SpeedChangeEvent> CREATOR = new Parcelable.Creator<SpeedChangeEvent>() {
		public SpeedChangeEvent createFromParcel(Parcel in) {
			return new SpeedChangeEvent(in);
		}

		public SpeedChangeEvent[] newArray(int size) {
			return new SpeedChangeEvent[size];
		}
	};

	private SpeedChangeEvent(Parcel in) {
		speed = in.readDouble();
		initialLocation = in.readParcelable(Location.class.getClassLoader());
		finalLocation = in.readParcelable(Location.class.getClassLoader());
	}
}

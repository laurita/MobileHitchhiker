package com.example.mobilehitchhiker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationUpdate {

	public double[] latitude = null;
	public double[] longitude = null;
	Context mContext;

	public LocationUpdate(Context mContext) {
		this.mContext = mContext;
	}

	public void start() {
		LocationManager locMgr = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new LocationListener() {
            public void  onLocationChanged(Location location) {
                if (location != null) {
                	Toast t1 = Toast.makeText(mContext, "New location latitude [" + 
                        location.getLatitude() +
                        "] longitude [" + 
                        location.getLongitude()+"]",
                        Toast.LENGTH_SHORT);
                	t1.show();
                	latitude[latitude.length] = location.getLatitude();
                	longitude[longitude.length] = location.getLongitude();
                }
            }

            public void  onProviderDisabled(String provider) {
            }

            public void  onProviderEnabled(String provider) {
            }

            public void  onStatusChanged(String provider, 
                            int status, Bundle extras) {
            }
        };
	}

	public Location getLocation() {
		LocationManager locationManager;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) mContext.getSystemService(context);
		String provider = LocationManager.GPS_PROVIDER;
		Location location = locationManager.getLastKnownLocation(provider);
		return location;
	}
}
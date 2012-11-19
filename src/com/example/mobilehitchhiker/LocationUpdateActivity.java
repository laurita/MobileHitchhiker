package com.example.mobilehitchhiker;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationUpdateActivity extends Activity
{
	LocationManager locMgr = null;
	LocationListener locListener = null;
	double[] latitude;
	double[] longitude;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locListener = new LocationListener()
        {
            public void  onLocationChanged(Location location)
            {
                if (location != null)
                {
                	latitude[latitude.length] = location.getLatitude();
                	longitude[longitude.length] = location.getLongitude();
                    Toast.makeText(getBaseContext(),
                        "New location latitude [" + 
                        location.getLatitude() +
                        "] longitude [" + 
                        location.getLongitude()+"]",
                        Toast.LENGTH_SHORT).show();
                }
            }

            public void  onProviderDisabled(String provider)
            {
            }

            public void  onProviderEnabled(String provider)
            {
            }

            public void  onStatusChanged(String provider, 
                            int status, Bundle extras)
            {
            }
        };
    }

    @Override
    public void onResume() {
    	super.onResume();

        locMgr.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,			// minTime in ms
            0,			// minDistance in meters
            locListener);
    }

    @Override
    public void onPause() {
    	super.onPause();
    	locMgr.removeUpdates(locListener);
    }
}
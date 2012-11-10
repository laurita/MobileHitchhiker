package com.example.mobilehitchhiker;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TripMap extends MapActivity {

	private static final String CLASSTAG = TripMap.class
			.getSimpleName();
	private static final int MAX_ADDRESSES = 3;
	Geocoder gc;
	//List<Address> startList;
	//List<Address> endList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.v(Constants.LOGTAG, " " + TripMap.CLASSTAG + " onCreate");
		
		MobileHitchhikerApplication.Trip trip;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_map);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		if (application.getAim() == MobileHitchhikerApplication.TO_CREATE) {
			trip = application.getTrip();
			displayMapWithTrip(trip, application, mapView);
		}
		else if (application.getAim() == MobileHitchhikerApplication.TO_FIND) {
			trip = application.findBestTrip(application.getTrip());
			displayMapWithTrip(trip, application, mapView);
		}
	}

	public void displayMapWithTrip(MobileHitchhikerApplication.Trip trip,
			MobileHitchhikerApplication app, MapView mapView) {
		
		Log.v(Constants.LOGTAG, " " + TripMap.CLASSTAG + " displayMapWithTrip");
		String startLocation = app.getTrip().getStartLocation();
		String endLocation = app.getTrip().getEndLocation();
		
		Log.v(Constants.LOGTAG, " " + TripMap.CLASSTAG + " start location is " + startLocation);
		Log.v(Constants.LOGTAG, " " + TripMap.CLASSTAG + " end location is " + endLocation);
		
		Geocoder coder = new Geocoder(this);
        List<Address> startList;
        List<Address> endList;
        int startLat = 0;
        int startLon = 0;
        int endLat = 0;
        int endLon = 0;
        
        try 
        {
        	startList = coder.getFromLocationName(startLocation, MAX_ADDRESSES);
            if (startList == null) {
                Log.d(Constants.LOGTAG, "############Start not correct #########");
            }
            Address start = startList.get(0);

            Log.v(Constants.LOGTAG, "lat="+ start.getLatitude() + "&long="+ start.getLongitude());

            startLat = (int) (start.getLatitude()  * 1E6);
            startLon = (int) (start.getLongitude() * 1E6);
        }
        catch(Exception e)
        {
            Log.d(Constants.LOGTAG, "MY_ERROR : ############Start Not Found");
        }
        
        try 
        {
            endList = coder.getFromLocationName(endLocation, MAX_ADDRESSES);
            if (endList == null) {
                Log.d(Constants.LOGTAG, "############End not correct #########");
            }
            Address end = endList.get(0);

            Log.v(Constants.LOGTAG, "lat="+ end.getLatitude() + "&long="+ end.getLongitude());
            
            endLat = (int) (end.getLatitude()  * 1E6);
            endLon = (int) (end.getLongitude() * 1E6);

        }
        catch(Exception e)
        {
            Log.d(Constants.LOGTAG, "MY_ERROR : ############End Not Found");
        }
		
        List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.androidmarker);
		TripItemizedOverlay itemizedoverlay = new TripItemizedOverlay(drawable,
				this);

		GeoPoint startPoint = new GeoPoint(startLat, startLon);
		OverlayItem overlayitem = new OverlayItem(startPoint,
				"Your trip starts here!", startLocation);

		GeoPoint endPoint = new GeoPoint(endLat, endLon);
		OverlayItem overlayitem2 = new OverlayItem(endPoint,
				"Your trip ends here!", endLocation);

		itemizedoverlay.addOverlay(overlayitem);
		itemizedoverlay.addOverlay(overlayitem2);
		mapOverlays.add(itemizedoverlay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.trip_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}

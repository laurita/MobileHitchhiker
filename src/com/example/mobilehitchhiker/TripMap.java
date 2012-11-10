package com.example.mobilehitchhiker;

import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TripMap extends MapActivity {

	private static final String CLASSTAG = TripMap.class.getSimpleName();
	Geocoder gc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
		Log.v(Constants.LOGTAG, " " + TripMap.CLASSTAG + " onCreate");

		MobileHitchhikerApplication.Trip trip;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_map);
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		if (application.getAim() == MobileHitchhikerApplication.TO_CREATE) {
			trip = application.getTrip();
			displayMapWithTrip(trip, application, mapView);
		}

		// TODO: findBestTrip is done twice!!!
		else if (application.getAim() == MobileHitchhikerApplication.TO_FIND) {
			trip = application.findBestTrip(application.getTrip());
			displayMapWithTrip(trip, application, mapView);
		}
	}

	public void displayMapWithTrip(MobileHitchhikerApplication.Trip trip,
			MobileHitchhikerApplication app, MapView mapView) {

		// By default the start/end locations are initialized to the trip with
		// info that was filled in in the form
		String startLocation = app.getTrip().getStartLocation();
		String endLocation = app.getTrip().getEndLocation();

		Log.v(Constants.LOGTAG, " " + TripMap.CLASSTAG + " displayMapWithTrip");

		switch (app.getAim()) {
		case MobileHitchhikerApplication.TO_CREATE:
			startLocation = app.getTrip().getStartLocation();
			endLocation = app.getTrip().getEndLocation();
		case MobileHitchhikerApplication.TO_FIND:
			startLocation = app.getFoundTrip().getStartLocation();
			endLocation = app.getFoundTrip().getEndLocation();
		}

		Log.v(Constants.LOGTAG, " " + TripMap.CLASSTAG + " start location is "
				+ startLocation);
		Log.v(Constants.LOGTAG, " " + TripMap.CLASSTAG + " end location is "
				+ endLocation);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.androidmarker);
		TripItemizedOverlay itemizedoverlay = new TripItemizedOverlay(drawable,
				this);

		int startLat = (int) (trip.getStart().getLatitude() * 1E6);
		int startLon = (int) (trip.getStart().getLongitude() * 1E6);

		GeoPoint startPoint = new GeoPoint(startLat, startLon);
		OverlayItem overlayitem = new OverlayItem(startPoint,
				"Your trip starts here!", startLocation);

		int endLat = (int) (trip.getEnd().getLatitude() * 1E6);
		int endLon = (int) (trip.getEnd().getLongitude() * 1E6);

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

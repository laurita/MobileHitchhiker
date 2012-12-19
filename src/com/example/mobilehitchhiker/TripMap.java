package com.example.mobilehitchhiker;

import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
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
		Log.v(Config.LOGTAG, " " + TripMap.CLASSTAG + " onCreate");

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

		else if (application.getAim() == MobileHitchhikerApplication.TO_FIND) {
			trip = application.getTrip();
			displayMapWithTrip(trip, application, mapView);
		}
	}

	public void displayMapWithTrip(MobileHitchhikerApplication.Trip trip,
			MobileHitchhikerApplication app, MapView mapView) {
		Address start = null;
		Address end = null;
		String startAddress;
		String endAddress;

		Log.v(Config.LOGTAG, " " + TripMap.CLASSTAG + " displayMapWithTrip");

		if (app.getAim() == MobileHitchhikerApplication.TO_CREATE) {
			start = app.getTrip().getStart();
			end = app.getTrip().getEnd();
		} else {
			start = app.getFoundTrip().getStart();
			end = app.getFoundTrip().getEnd();
		}

		startAddress = start.getAddressLine(0);
		endAddress = end.getAddressLine(0);

		Log.v(Config.LOGTAG, " " + TripMap.CLASSTAG + " start location is "
				+ startAddress);
		Log.v(Config.LOGTAG, " " + TripMap.CLASSTAG + " end location is "
				+ endAddress);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.androidmarker);
		TripItemizedOverlay itemizedoverlay = new TripItemizedOverlay(drawable,
				this);

		int startLat = (int) (trip.getStart().getLatitude() * 1E6);
		int startLon = (int) (trip.getStart().getLongitude() * 1E6);

		GeoPoint startPoint = new GeoPoint(startLat, startLon);
		OverlayItem overlayitem = new OverlayItem(startPoint, trip.getFStart(),
				startAddress);

		int endLat = (int) (trip.getEnd().getLatitude() * 1E6);
		int endLon = (int) (trip.getEnd().getLongitude() * 1E6);

		GeoPoint endPoint = new GeoPoint(endLat, endLon);
		OverlayItem overlayitem2 = new OverlayItem(endPoint, trip.getFEnd(),
				endAddress);

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
		case R.id.menu_show_contacts:
			MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
			new AlertDialog.Builder(this)
					.setTitle("Contact details")
					.setMessage(application.getContact())
					.setPositiveButton(
							"Continue",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// pass
								}
							}).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}

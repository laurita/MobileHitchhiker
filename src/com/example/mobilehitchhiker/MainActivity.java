package com.example.mobilehitchhiker;

import java.util.List;
import com.example.mobilehitchhiker.MobileHitchhikerApplication.Trip;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.Menu;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	private static final String CLASSTAG = MainActivity.class.getSimpleName();
	private static final int MENU_CREATE_TRIP = Menu.FIRST;
	private static final int MENU_FIND_TRIP = Menu.FIRST + 1;
	private static final int MAX_ADDRESSES = 3;
	private EditText startLocation;
	private EditText endLocation;
	private Button buttonShow;
	private Button buttonFind;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG + " onCreate");

		
		setContentView(R.layout.main);

		buttonShow = (Button) findViewById(R.id.button_show);
		buttonFind = (Button) findViewById(R.id.button_find);

		startLocation = (EditText) findViewById(R.id.start_location);
		endLocation = (EditText) findViewById(R.id.end_location);

		buttonShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
				application.setAim(MobileHitchhikerApplication.TO_CREATE);
				Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
						+ " CreateTrip button clicked");
				handleShowMap(application.getAim());
			}
		});

		buttonFind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
				application.setAim(MobileHitchhikerApplication.TO_FIND);
				Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
						+ " FindTrip button clicked");
				handleShowMap(application.getAim());
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG + " onResume");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MainActivity.MENU_CREATE_TRIP, 0, R.string.menu_create_trip)
				.setIcon(android.R.drawable.ic_menu_more);
		menu.add(0, MainActivity.MENU_FIND_TRIP, 1, R.string.menu_find_trip)
				.setIcon(android.R.drawable.ic_menu_more);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
		switch (item.getItemId()) {
		case MENU_CREATE_TRIP:
			application.setAim(MobileHitchhikerApplication.TO_CREATE);
			Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
					+ " CreateTrip menu item clicked");
			handleShowMap(application.getAim());
			return true;
		case MENU_FIND_TRIP:
			application.setAim(MobileHitchhikerApplication.TO_FIND);
			Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
					+ " FindTrip menu item clicked");
			handleShowMap(application.getAim());
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void handleShowMap(int aim) {
		
		MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG + " handleShowMap");

		if ((startLocation.getText() == null)
				|| startLocation.getText().toString().equals("")) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.alert_label)
					.setMessage(R.string.start_location_not_supplied_message)
					.setPositiveButton(
							"Continue",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							}).show();
			return;
		} else if ((endLocation.getText() == null)
				|| endLocation.getText().toString().equals("")) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.alert_label)
					.setMessage(R.string.end_location_not_supplied_message)
					.setPositiveButton(
							"Continue",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
			return;
		}

		String startLocationString = startLocation.getText().toString();
		String endLocationString = endLocation.getText().toString();

		MobileHitchhikerApplication.Trip trip = application.new Trip(
				startLocationString, endLocationString);
		addTripAddresses(trip);
		application.setTrip(trip);

		switch (application.getAim()) {
		case MobileHitchhikerApplication.TO_CREATE:
			application.addTripToTripList(trip);
		case MobileHitchhikerApplication.TO_FIND:
			MobileHitchhikerApplication.Trip foundTrip = application
					.findBestTrip(trip);
			application.setFoundTrip(foundTrip);
		}
		
		Intent intent = new Intent(Constants.INTENT_ACTION_SHOW_TRIP);
		startActivity(intent);
	}

	public void addTripAddresses(Trip trip) {
		Geocoder coder = new Geocoder(this);
		List<Address> startList;
		List<Address> endList;

		try {
			startList = coder.getFromLocationName(trip.getStartLocation(),
					MAX_ADDRESSES);
			if (startList == null) {
				Log.d(Constants.LOGTAG,
						"############Start not correct #########");
			}
			Address start = startList.get(0);
			trip.setStart(start);
			trip.setStart(start);
			Log.v(Constants.LOGTAG, "lat=" + start.getLatitude() + "&long="
					+ start.getLongitude());
		} catch (Exception e) {
			Log.d(Constants.LOGTAG, "MY_ERROR : ############Start Not Found");
		}

		try {
			endList = coder.getFromLocationName(trip.getEndLocation(),
					MAX_ADDRESSES);
			if (endList == null) {
				Log.d(Constants.LOGTAG, "############End not correct #########");
			}
			Address end = endList.get(0);
			trip.setEnd(end);
			trip.setEnd(end);
			Log.v(Constants.LOGTAG,
					"lat=" + end.getLatitude() + "&long=" + end.getLongitude());
		} catch (Exception e) {
			Log.d(Constants.LOGTAG, "MY_ERROR : ############End Not Found");
		}

	}

}

package com.example.mobilehitchhiker;

import java.util.List;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
	private CheckBox startCheckBox;
	private MobileHitchhikerApplication.Trip trip;
	public LocationUpdate locUpdate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG + " onCreate");

		locUpdate = new LocationUpdate(this);
		locUpdate.start();

		setContentView(R.layout.main);

		startCheckBox = (CheckBox) findViewById(R.id.checkBoxStart);

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
				Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
						+ " Aim is : " + application.getAim());
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
		// FIXME
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
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG + " Aim is : "
				+ application.getAim());

		if (!startCheckBox.isChecked() && (startLocation.getText() == null)
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
		try {
			if (startCheckBox.isChecked()) {
				// TODO: start address should be passed as pair of coordinates.
				// TODO: How to create Address having lat and lon ???????
				// double startLatitude =
				// application.locationAct.latitude[application.locationAct.latitude.length];
				// double startLongitude =
				// application.locationAct.longitude[application.locationAct.longitude.length];
				double startLatitude = locUpdate.latitude[0];
				double startLongitude = locUpdate.longitude[0];

				trip = application.new Trip(getAddressFromLocation(
						startLatitude, startLongitude),
						getAddressFromString(endLocationString));
			} else {
				// FIXME pop up
				trip = application.new Trip(
						getAddressFromString(startLocationString),
						getAddressFromString(endLocationString));
			}

			application.setTrip(trip);

			if (application.getAim() == MobileHitchhikerApplication.TO_CREATE) {
				application.addTripToTripList(trip);
			} else {
				MobileHitchhikerApplication.Trip foundTrip = application
						.findBestTrip(trip);
				application.setFoundTrip(foundTrip);
			}

			Intent intent = new Intent(Constants.INTENT_ACTION_SHOW_TRIP);
			startActivity(intent);
		} catch (InvalidAddressException e) {
			Log.d(Constants.LOGTAG, "Cia bus sesko popupas");
			new AlertDialog.Builder(this)
					.setTitle(R.string.alert_label)
					.setMessage(R.string.location_not_resolved_message)
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
	}

	public Address getAddressFromString(String str)
			throws InvalidAddressException {
		Geocoder coder = new Geocoder(this);
		List<Address> addressList;
		Address address = null;

		try {
			addressList = coder.getFromLocationName(str, MAX_ADDRESSES);
			if (addressList == null) {
				Log.d(Constants.LOGTAG,
						"############Address not correct #########");
			}
			address = addressList.get(0);

			Log.v(Constants.LOGTAG, "lat=" + address.getLatitude() + "&long="
					+ address.getLongitude());
		} catch (Exception e) {
			Log.d(Constants.LOGTAG, "MY_ERROR : ############Address Not Found");
			throw new InvalidAddressException(str);
			// FIXME: pop up "address not found" and go back
		}

		return address;
	}

	public Address getAddressFromLocation(double latitude, double longitude) {
		Geocoder coder = new Geocoder(this);
		List<Address> addressList;
		Address address = null;

		try {
			addressList = coder.getFromLocation(latitude, longitude,
					MAX_ADDRESSES);
			if (addressList == null) {
				Log.d(Constants.LOGTAG,
						"############Address not correct #########");
			}
			address = addressList.get(0);

			Log.v(Constants.LOGTAG, "lat=" + address.getLatitude() + "&long="
					+ address.getLongitude());
		} catch (Exception e) {
			Log.d(Constants.LOGTAG, "MY_ERROR : ############Address Not Found");
		}

		return address;
	}
}

class InvalidAddressException extends Exception {

	void InvaidAddressException() {
	}

	InvalidAddressException(String strMessage) {
		super(strMessage);
	}
}
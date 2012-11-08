package com.example.mobilehitchhiker;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.Menu;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	private static final String CLASSTAG = MainActivity.class
			.getSimpleName();
	private static final int MENU_CREATE_TRIP = Menu.FIRST;
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

		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
				+ " before setting OnClickListener");
		
		buttonShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
						+ " CreateTrip button clicked");
				handleShowMap();
			}
		});
		
		buttonFind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
						+ " FindTrip button clicked");
				handleFindTrip();
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
		menu.add(0, MainActivity.MENU_CREATE_TRIP, 0,
				R.string.menu_create_trip).setIcon(
				android.R.drawable.ic_menu_more);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_CREATE_TRIP:
			handleShowMap();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void handleFindTrip() {
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
				+ " handleFindTrip");
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

		// use the "Application" to store global state (can go beyond primitives
		// and Strings -
		// beyond extras - if needed)
		MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
		String startLocationString = startLocation.getText().toString();
		String endLocationString = endLocation.getText().toString();
		
		MobileHitchhikerApplication.Trip trip = application.new Trip(startLocationString, endLocationString);
				
		application.setTrip(trip);
		application.setAim(MobileHitchhikerApplication.TO_FIND);
		
		// call next Activity, SHOW_TRIP
		Intent intent = new Intent(Constants.INTENT_ACTION_SHOW_TRIP);
		startActivity(intent);
	}
	
	private void handleShowMap() {
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG
				+ " handleShowMap");

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

		// use the "Application" to store global state (can go beyond primitives
		// and Strings -
		// beyond extras - if needed)
		MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
		String startLocationString = startLocation.getText().toString();
		String endLocationString = endLocation.getText().toString();
		
		MobileHitchhikerApplication.Trip trip = application.new Trip(startLocationString, endLocationString);
		
		application.setTrip(trip);
		
		application.addTripToTripList(trip);
		
		application.setAim(MobileHitchhikerApplication.TO_CREATE);
		
		// call next Activity, SHOW_TRIP
		Intent intent = new Intent(Constants.INTENT_ACTION_SHOW_TRIP);
		startActivity(intent);
	}

}

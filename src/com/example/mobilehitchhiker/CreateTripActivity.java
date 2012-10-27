package com.example.mobilehitchhiker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
//import android.view.Menu;

public class CreateTripActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_trip);

		Button buttonSource = (Button) this.findViewById(R.id.button03);
		Button buttonDest = (Button) this.findViewById(R.id.button04);
		//EditText editStartTime = (EditText) this.findViewById(R.id.edittext1);
		//EditText editEndTime = (EditText) this.findViewById(R.id.edittext2);

		View.OnClickListener b3Listener = new myAddButtonListener(
				new AddSourceActivity());
		View.OnClickListener b4Listener = new myAddButtonListener(
				new AddDestActivity());

		buttonSource.setOnClickListener(b3Listener);
		buttonDest.setOnClickListener(b4Listener);
		
		//editStartTime.setCursorVisible(true);
		//editEndTime.setCursorVisible(true);
		
	}

	public class myAddButtonListener implements View.OnClickListener {

		Activity localActivity;

		public myAddButtonListener(Activity act) {
			this.localActivity = act;
		}

		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(CreateTripActivity.this,
					this.localActivity.getClass());
			CreateTripActivity.this.startActivity(myIntent);
		}

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_create_trip, menu); return
	 * true; }
	 */
}

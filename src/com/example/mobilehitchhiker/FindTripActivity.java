package com.example.mobilehitchhiker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FindTripActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_trip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_find_trip, menu);
        return true;
    }
}

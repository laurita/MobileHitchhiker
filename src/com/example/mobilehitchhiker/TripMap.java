package com.example.mobilehitchhiker;

import java.io.IOException;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TripMap extends MapActivity {
	
	private static final int MAX_ADDRESSES = 3;
	Geocoder gc;
	List<Address> startList;
	List<Address> endList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_map);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        MobileHitchhikerApplication application = (MobileHitchhikerApplication) getApplication();
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
		String startLocation = application.getStartLocation();
		String endLocation = application.getEndLocation();
		
		gc = new Geocoder(this);
		
		try {
			startList = gc.getFromLocationName(startLocation, MAX_ADDRESSES);
			endList = gc.getFromLocationName(endLocation, MAX_ADDRESSES);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		int startLat = (int) (startList.get(0).getLatitude() * 1E6);
		int startLon = (int) (startList.get(0).getLongitude() * 1E6);
		
		int endLat = (int) (endList.get(0).getLatitude() * 1E6);
		int endLon = (int) (endList.get(0).getLongitude() * 1E6);
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		TripItemizedOverlay itemizedoverlay = new TripItemizedOverlay(drawable, this);
		
		GeoPoint startPoint = new GeoPoint(startLat, startLon);
		OverlayItem overlayitem = new OverlayItem(startPoint, "Your trip starts here!", startLocation);
		
		GeoPoint endPoint = new GeoPoint(endLat, endLon);
		OverlayItem overlayitem2 = new OverlayItem(endPoint, "Your trip ends here!", endLocation);
		
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

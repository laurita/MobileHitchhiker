package com.example.mobilehitchhiker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.Application;
import android.location.Address;
import android.location.Location;
import android.util.Log;

public class MobileHitchhikerApplication extends Application {

	private static final String CLASSTAG = MobileHitchhikerApplication.class
			.getSimpleName();
	private ArrayList<Trip> trips;
	private Trip trip;
	private int aim;
	private Trip foundTrip;
	public static final int TO_CREATE = 1;
	public static final int TO_FIND = 2;
	public static final float MAX_DIST = 500000; // distance in meters

	public MobileHitchhikerApplication() {
		super();
		this.trips = new ArrayList<Trip>();
		// Add some trips to trip list for testing
		Trip trip1 = new Trip("Brixen, Italy", "Rovereto, Italy");
		Address trip1startAddress = new Address(Locale.ITALY);
		Address trip1endAddress = new Address(Locale.ITALY);
		trip1startAddress.setLatitude(46.70288030);
		trip1startAddress.setLongitude(11.69295620);
		trip1endAddress.setLatitude(45.85758350);
		trip1endAddress.setLongitude(11.02945110);
		trip1.setStart(trip1startAddress);
		trip1.setEnd(trip1endAddress);
		trips.add(trip1);

		Trip trip2 = new Trip("Laives, Italy", "Trento, Italy");
		Address trip2startAddress = new Address(Locale.ITALY);
		Address trip2endAddress = new Address(Locale.ITALY);
		trip2startAddress.setLatitude(46.42711360);
		trip2startAddress.setLongitude(11.33729140);
		trip2endAddress.setLatitude(46.06969240);
		trip2endAddress.setLongitude(11.12108860);
		trip2.setStart(trip2startAddress);
		trip2.setEnd(trip2endAddress);
		trips.add(trip2);

		Trip trip3 = new Trip("Bolzano, Italy", "Rovereto");
		Address trip3startAddress = new Address(Locale.ITALY);
		Address trip3endAddress = new Address(Locale.ITALY);
		trip3startAddress.setLatitude(46.49829530);
		trip3startAddress.setLongitude(11.35475820);
		trip3endAddress.setLatitude(45.85758350);
		trip3endAddress.setLongitude(11.02945110);
		trip3.setStart(trip3startAddress);
		trip3.setEnd(trip3endAddress);
		trips.add(trip3);

		// locationAct = new LocationUpdateActivity();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public int getAim() {
		return this.aim;
	}

	public void setAim(int aim) {
		this.aim = aim;
	}

	public Trip getTrip() {
		return this.trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public Trip getFoundTrip() {
		return this.foundTrip;
	}

	public void setFoundTrip(Trip trip) {
		this.foundTrip = trip;
	}

	public Trip findBest(double lat, double lon) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://10.0.2.2:5000/journeys?lat="+Double.toString(lat) + "&long=" + Double.toString(lon));
		HttpResponse response;

		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();

			InputStream instream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					instream));

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			String result = sb.toString();

			JSONArray json = (JSONArray) new JSONParser().parse(result);
			JSONObject o = (JSONObject) json.get(0);
			Trip trip = new Trip((String) o.get("start"), (String) o.get("end"));
			
			Address tripstartAddress = new Address(Locale.ITALY);
			Address tripendAddress = new Address(Locale.ITALY);
			
		
			tripstartAddress.setLatitude((Double) o.get("start_lat"));
			tripstartAddress.setLongitude((Double) o.get("start_long"));
			tripendAddress.setLatitude((Double) o.get("end_lat"));
			tripendAddress.setLongitude((Double) o.get("end_long"));
			trip.setStart(tripstartAddress);
			trip.setEnd(tripendAddress);
							
			
			instream.close();
			
			return trip;
			
		} catch (Exception e) {
			return new Trip("foo", "bar");
		}

	}

	public Trip findBestTrip(Trip trip) {
		Log.v(Constants.LOGTAG, " " + MobileHitchhikerApplication.CLASSTAG
				+ " findBestTrip");

		Address start = trip.getStart();
		Address end = trip.getEnd();

		double startLat = start.getLatitude();
		double startLon = start.getLongitude();
		double endLat = end.getLatitude();
		double endLon = end.getLongitude();
		float minDist = MAX_DIST;
		// Initialize foundTrip to the first one. Might cause problems later !!!
		return findBest(startLat, startLon);
		/*Trip foundTrip = trips.get(0);

		for (int i = 0; i < trips.size(); i++) {

			double startLatx = trips.get(i).getStart().getLatitude();
			double startLonx = trips.get(i).getStart().getLongitude();
			double endLatx = trips.get(i).getEnd().getLatitude();
			double endLonx = trips.get(i).getEnd().getLongitude();
			float startDistance = distance(startLat, startLon, startLatx,
					startLonx);
			float endDistance = distance(endLat, endLon, endLatx, endLonx);
			float distance = startDistance + endDistance;
			Log.v(Constants.LOGTAG, " " + MobileHitchhikerApplication.CLASSTAG
					+ i + ": " + distance);
			if (distance < minDist) {
				minDist = distance;
				foundTrip = trips.get(i);
			}
		}
		return foundTrip;
		*/
	}

	private float distance(double latA, double lonA, double latB, double lonB) {
		Location locationA = new Location("passenger trip start");

		locationA.setLatitude(latA);
		locationA.setLongitude(lonA);

		Location locationB = new Location("some trip start");

		locationB.setLatitude(latB);
		locationB.setLongitude(lonB);

		float distance = locationA.distanceTo(locationB);
		return distance;
	}

	public void addTripToTripList(Trip trip) {
		this.trips.add(trip);
	}

	public ArrayList<Trip> getTripList() {
		return this.trips;
	}

	public class Trip {
		private String startLocation;
		private String endLocation;
		private Address start;
		private Address end;

		public Trip(Address start, Address end) {
			this.start = start;
			this.end = end;
		}
		

		public Trip(String start, String end) {
			this.startLocation = start;
			this.endLocation = end;
		}

		@Override
		public String toString() {
			return "Trip: start location: " + this.start.getAddressLine(0)
					+ " , end location: " + this.end.getAddressLine(0);
		}

		public Address getStart() {
			return this.start;
		}

		public void setStart(Address start) {
			this.start = start;
		}

		public Address getEnd() {
			return this.end;
		}

		public void setEnd(Address end) {
			this.end = end;
		}
	}
}

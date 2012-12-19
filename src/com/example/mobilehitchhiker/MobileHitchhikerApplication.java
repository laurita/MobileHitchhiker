package com.example.mobilehitchhiker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.Application;
import android.location.Address;
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
	private Calendar start_date = Calendar.getInstance();
	private String contact = "";

	public MobileHitchhikerApplication() {
		super();
		this.trips = new ArrayList<Trip>();
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

	public void setStartDate(Calendar c) {
		this.start_date = c;
	}

	public String getStartDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(this.start_date.getTime());
	}

	public void setContact(String c) {
		this.contact = c;
	}

	public String getContact() {
		return this.contact;
	}

	public Trip getFoundTrip() {
		return this.foundTrip;
	}

	public void setFoundTrip(Trip trip) {
		this.foundTrip = trip;
	}

	public Trip findBest(double start_lat, double start_lon, double end_lat,
			double end_lon) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://10.0.2.2:5000/trips?start_lat="
				+ Double.toString(start_lat) + "&start_long="
				+ Double.toString(start_lon) + "&end_lat="
				+ Double.toString(end_lat) + "&end_long="
				+ Double.toString(end_lon) + "&date=" + this.getStartDate());
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

			this.setContact((String) o.get("contact"));

			instream.close();

			return trip;

		} catch (Exception e) {
			return new Trip("foo", "bar");
		}

	}

	public Trip findBestTrip(Trip trip) {
		Log.v(Config.LOGTAG, " " + MobileHitchhikerApplication.CLASSTAG
				+ " findBestTrip");

		Address start = trip.getStart();
		double startLat = start.getLatitude();
		double startLon = start.getLongitude();
		Address end = trip.getEnd();
		double endLat = end.getLatitude();
		double endLon = end.getLongitude();
		// Initialize foundTrip to the first one. Might cause problems later !!!
		return findBest(startLat, startLon, endLat, endLon);
	}

	public void addTripToTripList(Trip trip) {
		JSONObject obj = new JSONObject();
		obj.put("start_date", this.getStartDate());
		obj.put("contact", this.getContact());
		obj.put("start", this.trip.getFStart());
		obj.put("end", this.trip.getFEnd());
		obj.put("start_long", this.trip.getStart().getLongitude());
		obj.put("start_lat", this.trip.getStart().getLatitude());
		obj.put("end_long", this.trip.getEnd().getLongitude());
		obj.put("end_lat", this.trip.getEnd().getLatitude());

		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpPost httpost = new HttpPost("http://10.0.2.2:5000/trips");

		StringEntity se;
		try {
			se = new StringEntity(obj.toJSONString());
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");

			ResponseHandler responseHandler = new BasicResponseHandler();
			httpclient.execute(httpost, responseHandler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.trips.add(trip);
	}

	public ArrayList<Trip> getTripList() {
		return this.trips;
	}

	public class Trip {
		private Address start;
		private String fstart = "";
		private String fend = "";
		private Address end;

		public Trip(Address start, Address end) {
			this.start = start;
			this.end = end;
		}

		public Trip(Address start, Address end, String fstart, String fend) {
			this.start = start;
			this.end = end;
			this.fstart = fstart;
			this.fend = fend;
		}

		public Trip(String start, String end) {
			this.fstart = start;
			this.fend = end;
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

		public String getFStart() {
			return this.fstart;
		}

		public String getFEnd() {
			return this.fend;
		}
	}
}

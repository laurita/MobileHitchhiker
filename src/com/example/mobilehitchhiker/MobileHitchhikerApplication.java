package com.example.mobilehitchhiker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Application;

public class MobileHitchhikerApplication  extends Application {

	private ArrayList<Trip> trips;
	private Trip trip;
	private int aim;
	public static final int TO_CREATE = 1;
	public static final int TO_FIND = 2;
	
	public MobileHitchhikerApplication() {
        super();
        this.trips = new ArrayList<Trip>();
     // Add some trips to trip list for testing
    	trips.add(new Trip("Brixen, Italy", "Rovereto, Italy"));
    	trips.add(new Trip("Laives, Italy", "Trento, Italy"));
    	trips.add(new Trip("Bolzano, Italy", "Rovereto"));
    	trips.add(new Trip("Kaunas, Lithuania", "Vilnius, Lithuania"));
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
    
    public Trip findBestTrip(Trip trip) {
    	// TODO: should find the best trip match for the given trip
    	return trip;
    }
    
    public void addTripToTripList(Trip trip) {
    	this.trips.add(trip);
    }
    
    public List<Trip> getTripList() {
    	return this.trips;
    }
    
    public class Trip {
    	private String startLocation;
    	private String endLocation;
    	
    	public Trip(String start, String end) {
    		this.startLocation = start;
    		this.endLocation = end;
    	}
    	
    	@Override
    	public String toString() {
    		return "Trip: start location: " + this.startLocation + " , end location: " + this.endLocation;
    	}
    	
    	public String getStartLocation() {
            return this.startLocation;
        }
        
        public void setStartLocation(String startLocation) {
            this.startLocation = startLocation;
        }
        
        public String getEndLocation() {
            return this.endLocation;
        }
        
        public void setEndLocation(String endLocation) {
            this.endLocation = endLocation;
        }
    }
}


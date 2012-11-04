package com.example.mobilehitchhiker;

import android.app.Application;

public class MobileHitchhikerApplication  extends Application {

    private String startLocation;
	private String endLocation;

	public MobileHitchhikerApplication() {
        super();
    }
	
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
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


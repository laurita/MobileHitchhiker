package com.example.mobilehitchhiker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        Button buttonCreate = (Button) this.findViewById(R.id.button01);
        Button buttonFind = (Button) this.findViewById(R.id.button02);
        
        View.OnClickListener b1Listener = new myButtonListener(new CreateTripActivity());
        View.OnClickListener b2Listener = new myButtonListener(new FindTripActivity());
        
        buttonCreate.setOnClickListener(b1Listener);
        buttonFind.setOnClickListener(b2Listener);
    }
    
    public class myButtonListener implements View.OnClickListener {
    	
    	Activity nextActivity;
    	
    	public myButtonListener(Activity act) {
    		this.nextActivity = act;
    	}
    	
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(MainActivity.this, this.nextActivity.getClass());	    	
	    	MainActivity.this.startActivity(myIntent);		
		}
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    */
}

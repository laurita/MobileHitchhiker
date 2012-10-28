package com.example.mobilehitchhiker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private static final String CLASSTAG = MainActivity.class
			.getSimpleName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        Button buttonCreate = (Button) findViewById(R.id.button_create);
        Button buttonFind = (Button) findViewById(R.id.button_find);
        
        buttonCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreate();
            }
        });
        buttonFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFind();
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void handleCreate() {
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG + " handleCreate");
		
		// call next Activity, CREATE
		Intent intent = new Intent(Constants.INTENT_ACTION_CREATE);
		startActivity(intent);
	}

    private void handleFind() {
		Log.v(Constants.LOGTAG, " " + MainActivity.CLASSTAG + " handleFind");
		
		// call next Activity, FIND
		Intent intent = new Intent(Constants.INTENT_ACTION_FIND);
		startActivity(intent);
	}
    
    
}

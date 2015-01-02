package com.example.test_a;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Session extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session);
		
		final ImageButton AMbtn = (ImageButton) findViewById(R.id.AMbtn);
		final ImageButton PMbtn = (ImageButton) findViewById(R.id.PMbtn);
		
		AMbtn.setOnClickListener(new View.OnClickListener() {
			
            public void onClick(View v) {
                
            	Toast.makeText(Session.this, " AM Session Selected ! ", Toast.LENGTH_SHORT).show();
                
            	Intent activityChangeIntent = new Intent(Session.this, Tabs.class);
            	activityChangeIntent.putExtra("firstKeyName","AM");
    		    Session.this.startActivity(activityChangeIntent);
    		    
    		    finish();
            }
        });		
		
		PMbtn.setOnClickListener(new View.OnClickListener(){	
			
            public void onClick(View v){

            	Toast.makeText(Session.this, " PM Session Selected ! ", Toast.LENGTH_SHORT).show();
            	
            	Intent activityChangeIntent = new Intent(Session.this, Tabs.class);              
            	activityChangeIntent.putExtra("firstKeyName","PM");
            	Session.this.startActivity(activityChangeIntent);
            	
            	finish();
                
            }
        });
		
		/* extra */
		
		
	}

}

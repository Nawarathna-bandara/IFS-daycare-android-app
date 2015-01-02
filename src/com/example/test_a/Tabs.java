package com.example.test_a;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class Tabs extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_tabs);
			
		try
		{
		    Resources res = getResources(); 
		    TabHost tabHost = getTabHost();  
		    TabHost.TabSpec spec; 
		    Intent intent;  
		    tabHost.clearAllTabs();
		    
		    Intent myIntent = getIntent();
	    	String session = myIntent.getStringExtra("firstKeyName");
	    	
		    intent = new Intent().setClass(this, Kidlist.class);
		    intent.putExtra("firstKeyName",session);
		    intent.putExtra("secondKeyName","TODDLER");
		    spec = tabHost.newTabSpec("tab1").setIndicator("TODDLERS", res.getDrawable(R.drawable.ic_launcher)).setContent(intent);
		    tabHost.addTab(spec);
	
		    intent = new Intent().setClass(this, Kidlist.class);
		    intent.putExtra("firstKeyName",session);
		    intent.putExtra("secondKeyName","ELDER");
		    spec = tabHost.newTabSpec("tab2").setIndicator("ELDERS", res.getDrawable(R.drawable.ic_launcher)).setContent(intent);
		    tabHost.addTab(spec);
	
		    tabHost.setCurrentTab(0);
		}
		catch(Exception e)
		{
			
		}
	}

}
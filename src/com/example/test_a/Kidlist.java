
package com.example.test_a;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class Kidlist extends ListActivity{
    
	Semaphore spost = new Semaphore(1);
	
	ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> attendedlist = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    
    String session;
    String urlGet = null;
    Button btn;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {	
        
    	Intent myIntent = getIntent(); // gets the previously created intent
    	String session = myIntent.getStringExtra("firstKeyName"); // will return "FirstKeyValue"
    	String catagory= myIntent.getStringExtra("secondKeyName");
    	
    	urlGet="http://ifsjuniorslkservice.corpnet.ifsworld.com/h/members/"+session+"/"+catagory;
    	
    	super.onCreate(savedInstanceState);
    	LayoutInflater inflater = LayoutInflater.from(this);
    	View viewInflatedFromXml = inflater.inflate(R.layout.activity_kidlist, null);
    	setContentView(viewInflatedFromXml);
    	
    	btn = (Button) findViewById(R.id.attd_btn);
    	 	
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);
	            
	    OnClickListener listener = new OnClickListener(){
	    	
	    	@Override
	    	public void onClick(View v) {
	    		   
	    		MarkAttendences();
	    	}
	    };
	    
	    kidListGet();
	    
	    btn.setOnClickListener(listener);    
	    setListAdapter(adapter);
    }
    
    public void kidListGet(){
    	
    	btn.setEnabled(false);
    	list.clear();
		attendedlist.clear();
		new doRequest(Kidlist.this, null, enumRequest.GET, urlGet).execute();
	}
    
    public void kidListPost(String json){
    	
    	new doRequest(Kidlist.this, json, enumRequest.POST, urlGet).execute();
    }
    
    public void LoadKidList(JSONArray data){
    	try {
			getStringListFromJsonArray(data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(list.isEmpty())
    		btn.setEnabled(false);
    	else
    		btn.setEnabled(true);
    	
    	adapter.notifyDataSetChanged();
    }
    
    public void MarkAttendences(){
    	
    	/** Getting the checked items from the listview */
		SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
		int itemCount = getListView().getCount();
		
		for(int i=itemCount-1; i >= 0; i--){
			if(checkedItemPositions.get(i)){	    				
				attendedlist.add(list.get(i));
				adapter.remove(list.get(i));				
			}
		}	
		
		markAttendence();
		checkedItemPositions.clear();
	}
    
    public void markAttendence(){
    	
    	if(!attendedlist.isEmpty()){
    	JSONArray jsonAraay = new JSONArray(attendedlist);
    	kidListPost(jsonAraay.toString());
    	}
    	kidListGet();
    	
	}
    
    private class doRequest extends AsyncTask<Void, JSONArray, JSONArray>{
		
    	private AndrestClient rest = new AndrestClient();
    	private Context context = null;
		private String json = null;
		private enumRequest method = null;
		private String url = "";		
		private ProgressDialog dialog;		
		
		
		@Override
	    protected void onPreExecute() {
	    	dialog=new ProgressDialog(context);
	        dialog.setMessage("Loading ");
	        dialog.show();
	    }
		
		public doRequest(Context context, String json, enumRequest method, String url){
			this.context = context;
			this.json = json;
			this.method = method;
			this.url = url;
		}
		
		@Override
		protected JSONArray doInBackground(Void... arg0) {
			
			try {
				spost.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				JSONArray jar =rest.request(url, method, json); // Do request
				return jar;
			} catch (RESTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(JSONArray data){
			super.onPostExecute(data);
			// Display based on error existence
			
			if(enumRequest.GET==method)
				LoadKidList(data);
				
			spost.release();
			
			if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
		}
		
	}

	public void getStringListFromJsonArray(JSONArray jArray) throws JSONException {
	    for (int i = 0; i < jArray.length(); i++) {
	      String val = null;
			try {
				val = jArray.getString(i);
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
			list.add(val);
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	    	Intent activityChangeIntent = new Intent(Kidlist.this, Session.class);
	    	Kidlist.this.startActivity(activityChangeIntent);
	    	
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}

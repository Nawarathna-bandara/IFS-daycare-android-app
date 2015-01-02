package com.example.test_a;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	String name_txt;
	String pwd_txt;
	EditText name,pwd;
	Button loginbtn;
	private ProgressDialog dialog;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginbtn = (Button) findViewById(R.id.button1);
		name = (EditText) findViewById(R.id.username);
		pwd = (EditText) findViewById(R.id.password);
		
		loginbtn.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
				
				/*if(name.getText().toString().isEmpty()){
					Toast.makeText(context, " Username cannot be empty ! ", duration).show();
				}
				else if(pwd.getText().toString().isEmpty()){
					Toast.makeText(context, " Password cannot be empty ! ", duration).show();		
				}
				else*/ if(!check_connection()){ 
					Toast.makeText(context, " Connection failed !", duration).show();         		
				}
				else{
				
					loginbtn.setEnabled(false);
				
					String UserName = name.getText().toString();
					String PassWord = pwd.getText().toString();
					
					PassWord = "1234";
					UserName = "testuser";
					
					String hashedPass = md5(PassWord);
					hashedPass = hashedPass.toUpperCase();
					
					String auth = "http://ifsjuniorslkservice.corpnet.ifsworld.com/h/members/Authentication/"+UserName+"/"+hashedPass;

					new doRequest(Login.this, null, enumRequest.GET, auth).execute();
					
				}
            }
        });
	}
	
	private String md5(String s) {
	    try {
	        MessageDigest m = MessageDigest.getInstance("MD5");
	        m.update(s.getBytes(), 0, s.length());
	        BigInteger i = new BigInteger(1,m.digest());
	        return String.format("%1$032x", i);         
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	private boolean checkLoginData(Context context,JSONArray data){		

		String authentication = "FALSE";
		
		try {
			if(data!=null)
				authentication = data.getString(0);
			
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		if(authentication.equals("TRUE")){
			
			if (dialog.isShowing()) {
				dialog.setMessage(" Login Successfull ! ");
	        }	
			//Toast.makeText(context, " Login successfull ! ", Toast.LENGTH_SHORT).show();
			Intent tosession = new Intent(Login.this, Session.class);
			Login.this.startActivity(tosession);			
			this.finish();
			
		}else
			if (dialog.isShowing()) {
				dialog.setMessage(" Incorrect login data ! ! ");
	        }
			//Toast.makeText(context, " Incorrect login data !", Toast.LENGTH_SHORT).show();
			loginbtn.setEnabled(true);
			dialog.dismiss();
		
		return true;
	}
	
	
	private boolean check_connection(){
		
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) 
	    	return true;
	    else
	    	return false;	
	}
	
	private class doRequest extends AsyncTask<Void, JSONArray, JSONArray>{
		
		private AndrestClient rest = new AndrestClient();
		private Context context = null;
		private String json = null;
		private enumRequest method = null;
		private String url = "";
		
		
		public doRequest(Context context, String json, enumRequest method, String url){
			this.context = context;
			this.json = json;
			this.method = method;
			this.url = url;
		}
		
		@Override
	    protected void onPreExecute() {
	    	dialog=new ProgressDialog(context);
	        dialog.setMessage(" Logging in.. ");
	        dialog.show();
	    }
		
		@Override
		protected JSONArray doInBackground(Void... arg0) {
			try {
				return rest.request(url, method, json); // Do request
			} catch (Exception e) {
				return null;
			}	
		}
		
		@Override
		protected void onPostExecute(JSONArray data){
			super.onPostExecute(data);
			checkLoginData(context,data);				
		}
					
	}
}

package com.example.imagebutton;

import java.io.DataOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity implements LocationListener{
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
//DISPLAY PAGE CODE
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();	
		try
		{
			File cacheDir = getCacheDir();
			File file = new File(cacheDir.getAbsoluteFile(), "mydata.txt");
			FileOutputStream fos = new FileOutputStream(file, false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mydata.callNumber);
			oos.writeObject(mydata.message);
			oos.writeObject(mydata.msgNumber);
			oos.close();
			fos.close();
		}
		catch(Exception e)
		{
			
		}
		
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		try
		{
			File cacheDir = getCacheDir();
			File file = new File(cacheDir.getAbsoluteFile(), "mydata.txt");
			FileInputStream fos = new FileInputStream(file);
			ObjectInputStream oos = new ObjectInputStream(fos);
			mydata.callNumber = (String)oos.readObject();
			mydata.message = (String)oos.readObject();
			mydata.msgNumber = (Vector<String>)oos.readObject();
			oos.close();
			fos.close();
		}
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//ANDROID MENU CODE
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    	case R.id.action_settings:
        		Intent intent = new Intent("android.intent.action.Settings");
    	    	startActivity(intent);
    	    	return true;
        	case R.id.action_about:
	        	Intent intent2 = new Intent("android.intent.action.About");
	    	    startActivity(intent2);
	          
	            return true;
	            
	        case R.id.action_exit:
	        	finish();
	        	//System.exit(0);;
	           return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	//NOT USED ANYWHERE!
	public void callAlert(View view)
	{
		
		SmsManager sms = SmsManager.getDefault();
		for(int i=0;i<mydata.msgNumber.size();i++)
		{
			sms.sendTextMessage(mydata.msgNumber.elementAt(i).toString(), null, mydata.message, null, null);
			Toast.makeText(getApplicationContext(), "Message Sent to : " + mydata.msgNumber.elementAt(i).toString(), Toast.LENGTH_LONG).show();
		}
		Toast.makeText(getApplicationContext(), "All Messages sent successfully ", Toast.LENGTH_LONG).show();
		Intent callIntent  = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:" + mydata.callNumber));
		startActivity(callIntent);
		Toast.makeText(getApplicationContext(), "Outgoing call : " + mydata.callNumber, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		String str = mydata.message;
		if(str.contains(" http://maps.google"))
		{
			str = str.substring(0, str.lastIndexOf("=")+1);
			str+=(location.getLatitude() + "," + location.getLongitude());
		}
		else
		{
			str+= (" http://maps.google.com?q=" + location.getLatitude() + "," + location.getLongitude());
		}
		mydata.message = str;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}

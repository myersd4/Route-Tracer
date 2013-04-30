package com.myersd.routetracker;

import com.myersd.routetracker.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.preference.Preference;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements GoogleMap.OnMyLocationChangeListener {

	private GoogleMap map;
	private Button startButton, stopButton, centerButton;
	private boolean centerFlag = false;
	static final LatLng GRIFFIN = new LatLng( 39.030985, -84.466555 ) ;
	private LatLng startPosition;
	private static final int group1 = 1;
	public static SharedPreferences prefs;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		float initlat = 0, initlng = 0;
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefs = this.getPreferences(MODE_PRIVATE);
		initlat = prefs.getFloat("startLat", (float) 39.030985);
		initlng = prefs.getFloat("startLng", (float) -84.466555);
		startPosition = new LatLng(initlat, initlng);
		

		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap() ;    
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition,18)); // I was testing around Griffin Hall so this was just a convenience thing 
		map.setMyLocationEnabled(true) ;
		map.setOnMyLocationChangeListener(this);
		
		startButton = (Button)findViewById(R.id.startButton);
		stopButton = (Button)findViewById(R.id.stopButton);
		centerButton = (Button)findViewById(R.id.centerButton);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	 @Override
	    public boolean onOptionsItemSelected( MenuItem item )
	    {
	        switch( item.getItemId( ) ) {
	            case R.id.action_settings:
	                Intent intent = new Intent( ) ;
	                intent.setClass( this, prefActivity.class ) ;
	                startActivityForResult( intent, 0 ) ; // result code, if >= 0 it will be passed to the callback
	                return true ;
	            default:
	                return super.onOptionsItemSelected( item ) ;
	        }
	    }

	@Override
	public void onMyLocationChange(Location location) {
		
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		final LatLng center = new LatLng(lat,lng);
		
		
		startButton.setEnabled(true);
		stopButton.setEnabled(true);
		centerButton.setEnabled(true);
		
		if(centerFlag){
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 18));
			map.addPolyline(new PolylineOptions().add(startPosition, center).width(5).color(Color.BLUE));
		}
		
		startButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				centerFlag = true;
				startPosition = center;
			}
		});
		
		stopButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				centerFlag = false;
			}
		});
		
		centerButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 18));
			}
		});
		
	}
	
	public void startClick(Location location){
		
	}
	
}

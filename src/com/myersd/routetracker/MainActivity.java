package com.myersd.routetracker;

import com.myersd.routetracker.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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
	private LatLng prevPosition;
	private static final int group1 = 1;
	public static SharedPreferences prefs;
	private double distance;
	
	private static final int ZOOM = 16;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		float initlat = 0, initlng = 0;
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefs = this.getPreferences(MODE_PRIVATE);
		
		int type = prefs.getInt("mapType", 0);
		
		initlat = prefs.getFloat("startLat", (float) 39.030985);
		initlng = prefs.getFloat("startLng", (float) -84.466555);
		startPosition = new LatLng(initlat, initlng);
		
		

		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap() ;    
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition,ZOOM)); // I was testing around Griffin Hall so this was just a convenience thing 
		map.setMyLocationEnabled(true) ;
		map.setOnMyLocationChangeListener(this);
		setType(type);
		
		OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			public void onSharedPreferenceChanged(SharedPreferences prefs, String mapType) {
				int currenttype = prefs.getInt("mapType", 0);
				setType(currenttype);
				
			}
		};
		
		prefs.registerOnSharedPreferenceChangeListener(listener);
		
		startButton = (Button)findViewById(R.id.startButton);
		stopButton = (Button)findViewById(R.id.stopButton);
		centerButton = (Button)findViewById(R.id.centerButton);
		
		
		
	}
	
	private void setType(int mapType){
		
		switch(mapType)
		{
		case 0:
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case 1:
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case 2:
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case 3:
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		}
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
	                startActivityForResult( intent, 0 ) ; 
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
		Location prevLocation = new Location("Previous Location");
		
		
		startButton.setEnabled(true);
		stopButton.setEnabled(true);
		centerButton.setEnabled(true);
		
		if(centerFlag){
			map.moveCamera(CameraUpdateFactory.newLatLng(center));
			map.addPolyline(new PolylineOptions().add(prevPosition, center).width(5).color(Color.BLUE));
			prevLocation.setLatitude(prevPosition.latitude);
			prevLocation.setLongitude(prevPosition.longitude);
			distance += location.distanceTo(prevLocation);
			prevPosition = center;
		}
		
		startButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				map.clear();
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, ZOOM));
				centerFlag = true;
				prevPosition = center;
				map.addMarker(new MarkerOptions().position(center).title("Starting Point").snippet("The Beginning...").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			}
		});
		
		stopButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				centerFlag = false;
				map.addMarker(new MarkerOptions().position(center).title("Ending Point").snippet(String.format("Distance Traveled: %f meters", distance)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			}
		});
		
		centerButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, ZOOM));
			}
		});
		
	}
	
	
}

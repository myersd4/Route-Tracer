/*MainActivity.java - Holds most of the logic for the program.
 * Dan Myers
 * CSC 494
 */
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
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements GoogleMap.OnMyLocationChangeListener {

	private GoogleMap map;
	private Button startButton, stopButton, centerButton;
	private boolean inProgressFlag = false;		//Variable that holds wether the map should be centered.
	static final LatLng GRIFFIN = new LatLng( 39.030985, -84.466555 ) ;	//Default starting location before user defines their own.
	private LatLng startPosition;	//Starting position
	private LatLng prevPosition;	//Position used for line drawing.
	public static SharedPreferences prefs;	
	private double distance;	//Total distance traveled.
	private long startTime, endTime, totalTime; 	//Used to hold trip time.
	
	private static final int ZOOM = 16;		//Default zoom level
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		float initlat = 0, initlng = 0;
		
		//Get preference manager.
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefs = this.getPreferences(MODE_PRIVATE);
		
		int type = prefs.getInt("mapType", 0);	//Get map type;
		
		initlat = prefs.getFloat("startLat", (float) 39.030985);	//Get starting latitude.
		initlng = prefs.getFloat("startLng", (float) -84.466555);	//Get starting longitude.
		startPosition = new LatLng(initlat, initlng);
		
		

		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap() ;  //Initialize map variable.  
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition,ZOOM)); 	//Default starting location.
		map.setMyLocationEnabled(true) ;	
		map.setOnMyLocationChangeListener(this);	
		setType(type);	//Set map type.
	
		
		//Initialize buttons.
		startButton = (Button)findViewById(R.id.startButton);	
		stopButton = (Button)findViewById(R.id.stopButton);
		centerButton = (Button)findViewById(R.id.centerButton);
		
		
		
	}
	
	/*Sets the map to the type selected by the user*/
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
		
		//Submenu for picking map type.
		SubMenu sm = menu.addSubMenu(50, 50, 99, "Map Layers");
		sm.add(1, 1, 1, "Normal");
		sm.add(1, 2, 2, "Satellite");
		sm.add(1, 3, 3, "Terrain");
		sm.add(1, 4, 4, "Hybrid");
		return true;
	}
	
	//Menu click handler.
	 @Override
	    public boolean onOptionsItemSelected( MenuItem item )
	    {
	        switch( item.getItemId( ) ) {
	        	case 1:
	        		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	        		return true;
	        	case 2:
	        		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	        		return true;
	        	case 3:
	        		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	        		return true;
	        	case 4:
	        		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	        		return true;
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
		
		//Get current location.
		final double lat = location.getLatitude();
		final double lng = location.getLongitude();
		final LatLng center = new LatLng(lat,lng);
		Location prevLocation = new Location("Previous Location");
		
		//Enable Buttons
		startButton.setEnabled(true);
		stopButton.setEnabled(true);
		centerButton.setEnabled(true);
		
		//If route is being tracked.
		if(inProgressFlag){
			map.moveCamera(CameraUpdateFactory.newLatLng(center)); //Center camera.
			map.addPolyline(new PolylineOptions().add(prevPosition, center).width(5).color(Color.BLUE));	//Draw line.
			
			//Set previous to current.
			prevLocation.setLatitude(prevPosition.latitude);
			prevLocation.setLongitude(prevPosition.longitude);
			prevPosition = center;
			
			//Add to distance.
			distance += location.distanceTo(prevLocation);
		}
		
		startButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				map.clear();	//Clear any previous routes.
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, ZOOM));	//Center camera
				inProgressFlag = true;	
				prevPosition = center;	//Set previous to current.
				startTime = System.currentTimeMillis();	//Start the timer.
				
				//Add marker.
				map.addMarker(new MarkerOptions().position(center).title("Starting Point").snippet(String.format("Latitude: %f \t Longitude: %f", lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			}
		});
		
		stopButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				inProgressFlag = false;
				endTime = System.currentTimeMillis();	//Stop the timer.
				totalTime = (((endTime - startTime)/1000)/60);	//Calculate time in minutes.
				
				//Add marker and display information.
				map.addMarker(new MarkerOptions().position(center).title("Ending Point").snippet(String.format("You traveled %f meters in %d minutes. \n Latitude: %f \t Longitude: %f ", distance, totalTime, lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			}
		});
		
		centerButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, ZOOM)); //Center the camera
			}
		});
		
	}
	
	
}

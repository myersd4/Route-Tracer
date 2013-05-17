/*startPosFrag.java - lets user select the default starting position.
 * Dan Myers
 * CSC 494
 */

package com.myersd.routetracker;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment ;
import android.widget.Toast;



public class startPosFrag extends PreferenceFragment {
	
	private EditTextPreference lat, lng;
	
	public startPosFrag(){};
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.startpospref);
		
		/*Uses preference changed listeners to validate input before commit it to the defaut preferences file
		 */
		
		
		lat = (EditTextPreference)getPreferenceScreen().findPreference("lattxt_key");
		lat.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference preference, Object value) {
				double latitude = 0;
				try{
					latitude = Double.parseDouble((String)value);
				}catch(Exception e){
					Toast.makeText(getActivity(), "Latitude must be a number", Toast.LENGTH_SHORT).show();
					return false;
				}
				if(latitude > 90 || latitude < -90){
					Toast.makeText(getActivity(), "Latitude must be between -90 and 90 degress", Toast.LENGTH_SHORT).show();
					return false;
				}
				
				if(MainActivity.prefs.edit().putFloat("startLat", (float) latitude).commit()){
					Toast.makeText(getActivity(), String.format("Latitude set to %f", latitude), Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(), "Error writing to file.", Toast.LENGTH_SHORT).show();
					return false;
				}
				
				return true;
			}
			
		});
		
		lng = (EditTextPreference)getPreferenceScreen().findPreference("lngtxt_key");
		
		lng.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object value) {
				
				double longitude = 0;
				try{
					longitude = Double.parseDouble((String)value);
				}catch(Exception e){
					Toast.makeText(getActivity(), "Longitude must be a number", Toast.LENGTH_SHORT).show();
					return false;
				}
				if(longitude > 180 || longitude < -180){
					Toast.makeText(getActivity(), "Longitude must be between -180 and 180 degress", Toast.LENGTH_SHORT).show();
					return false;
				}
				
				if(MainActivity.prefs.edit().putFloat("startLng", (float) longitude).commit()){
					Toast.makeText(getActivity(), String.format("Longitude set to %f", longitude), Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(), "Error writing to file.", Toast.LENGTH_SHORT).show();
					return false;
				}
				
				
				return true;
			}
			
		});
		
	}
}

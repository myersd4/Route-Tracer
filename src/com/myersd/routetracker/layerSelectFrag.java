package com.myersd.routetracker;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class layerSelectFrag extends PreferenceFragment {

	private CheckBoxPreference normal, satellite, terrain, hybrid;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.layerselectpref);

		normal = (CheckBoxPreference)getPreferenceScreen().findPreference("nrmlcb_key");
		satellite = (CheckBoxPreference)getPreferenceScreen().findPreference("satcb_key");
		terrain = (CheckBoxPreference)getPreferenceScreen().findPreference("tercb_key");
		hybrid = (CheckBoxPreference)getPreferenceScreen().findPreference("hybcb_key");

		normal.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference arg0) {

				normal.setChecked(true);
				satellite.setChecked(false);
				terrain.setChecked(false);
				hybrid.setChecked(false);
				if(MainActivity.prefs.edit().putInt("mapType", 0).commit()){
					Toast.makeText(getActivity(), "Map type set to normal.", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(), "Error setting map type", Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;

			}

		});


		satellite.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference arg0) {

				satellite.setChecked(true);
				normal.setChecked(false);
				terrain.setChecked(false);
				hybrid.setChecked(false);
				if(MainActivity.prefs.edit().putInt("mapType", 1).commit()){
					Toast.makeText(getActivity(), "Map type set to satellite.", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(), "Error setting map type", Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			}

		});

		terrain.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference arg0) {

				terrain.setChecked(true);
				normal.setChecked(false);
				satellite.setChecked(false);
				hybrid.setChecked(false);
				if(MainActivity.prefs.edit().putInt("mapType", 2).commit()){
					Toast.makeText(getActivity(), "Map type set to terrain.", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(), "Error setting map type", Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			}



		});

		hybrid.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference arg0) {

				hybrid.setChecked(true);
				satellite.setChecked(false);
				terrain.setChecked(false);
				normal.setChecked(false);
				if(MainActivity.prefs.edit().putInt("mapType", 3).commit()){
					Toast.makeText(getActivity(), "Map type set to hybrid.", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(), "Error setting map type", Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			}


		});
	}
}

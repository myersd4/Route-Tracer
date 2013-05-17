package com.myersd.routetracker;

import java.util.List ;
import android.os.Bundle ;
import android.preference.PreferenceActivity ;
import android.preference.PreferenceManager ;

public class prefActivity extends PreferenceActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState ) ;
        PreferenceManager.setDefaultValues( this, R.xml.startpospref, false ) ;
    }
    
    @Override
    public void onBuildHeaders( List<Header> target )
    {
        loadHeadersFromResource( R.xml.preferences, target ) ;
    }
}
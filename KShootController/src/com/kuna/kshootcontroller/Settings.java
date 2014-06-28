package com.kuna.kshootcontroller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Settings extends PreferenceActivity  {	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_settings);
	}

	
	@Override 
	public SharedPreferences getSharedPreferences(String name, int mode) {
		return super.getSharedPreferences("settings", mode);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.settings, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.search:
			startActivity(new Intent(getApplicationContext(), ServerSearch.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    		return true;
    	case R.id.help:
    		Log.i("iBeatCon", "Help");
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://xuserv.net/ibc/")));
    		return true;
    	case R.id.reset:
    		Log.i("iBeatCon", "Restore to Default");
    		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    		SharedPreferences.Editor editor = settings.edit();
    		editor.clear();
    		editor.commit();
			Toast.makeText(getApplicationContext(), "Settings resetted.", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			finish();
			return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
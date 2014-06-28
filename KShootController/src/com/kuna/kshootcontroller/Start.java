package com.kuna.kshootcontroller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/*
 * Start.java
 * Work: load information, find connection to server, show controller or preference window.
 * some source may be refered from xuserv;
 * https://github.com/xuserv/iBeatConAndroid/blob/master/src/com/kuna/ibeatcon_android/Join.java
 */

public class Start extends Activity {
	private Handler h;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Log.i("iBeatCon", "Application Launched");
        
	    // attach event
        try {
    	    SharedPreferences setting = getSharedPreferences("settings", MODE_PRIVATE);
    	    String ip = setting.getString("ip", "");
    	    String ZoomValue = setting.getString("zoom", "");
    	    //String port = setting.getString("port", "");
    	    
    	    if (ip != "" | ZoomValue != "") {
    	    		Log.i("iBeatCon", "Connecting to Server");
    	    		Log.i("iBeatCon", "IP Address : " + ip);
    	    		Log.i("iBeatCon", "Zoom Value : " + ZoomValue);
    	    		//Log.i("iBeatCon", "Server Port: "+ port);
    	    		ConCommon.zoomval = Integer.parseInt(ZoomValue);
    	    		ConCommon.cc = new ConClient(ip, 10070);
    	    		CanvasView.bluekey = setting.getBoolean("bluekey", false);
    	    		CanvasView.blackpanel = setting.getBoolean("blackpanel", false);
    	    		ConCommon.vb_feedback = setting.getBoolean("feedback", false);
    	    		ConCommon.scr2btn = setting.getBoolean("scr2btn", false);
    	    } else {
            	firstRun();
    	    }
        } catch (Exception e) {
        	firstRun();
        }
	    
	    h = new Handler() {
	    	@Override
	    	public void handleMessage(Message msg) {
	    		if (msg.what == 1) {
	    			ConCommon.HandlerStack.remove(h);
	    			
					// start activity
	    			Log.i("iBeatCon", "Connected.");
					ConCommon.controller = new Controller();
					startActivity(new Intent(getApplicationContext(), Controller.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
	    		}
	    		if (msg.what == -1) {
	    			ConCommon.HandlerStack.remove(h);
	    			
	    			Log.i("iBeatCon", "Cannot Connect into Server");
	    			Toast.makeText(getApplicationContext(), "Failed to Connect. Check setting.", Toast.LENGTH_SHORT).show();
	    			startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    			finish();
	    		}
	    		super.handleMessage(msg);
	    	}
	    };
	    
	    ConCommon.HandlerStack.add(h);
	}

	private void firstRun() {
		Log.i("iBeatCon", "First Run");
		Toast.makeText(getApplicationContext(), "Welcome! Please do setting to use controller.", Toast.LENGTH_SHORT).show();       		
		startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}
	
}

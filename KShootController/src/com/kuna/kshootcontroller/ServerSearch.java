package com.kuna.kshootcontroller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ServerSearch extends Activity {
    private boolean searching = true;
    private Context c = this;
    private int ipNum = 0;
    private Handler h;
    private TextView stat;
    private boolean isThreadWorking = false;
    private boolean foundIP = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serversearch);
        
        stat = (TextView)findViewById(R.id.status);
        Button b = (Button)findViewById(R.id.button1);
        
        stat.setText("Searching IPs ... 0/256");
        
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isThreadWorking = false;
				searching = false;
				Toast.makeText(c, "Search Canceled", Toast.LENGTH_LONG).show();
				finish();
			}
		});

	    h = new Handler() {
	    	@Override
	    	public void handleMessage(Message msg) {
	    		if (msg.what == -1) {
	    			Log.i("IPSEARCH", "cant find 192.168.0." + Integer.toString(ipNum));
	    			isThreadWorking = false;
	    		} else if (msg.what == 1) {
	    			Log.i("IPSEARCH", "find 192.168.0." + Integer.toString(ipNum));
	    			
	        	    SharedPreferences setting = getSharedPreferences("settings", MODE_PRIVATE);
	        	    Editor edit = setting.edit();
	        	    edit.putString("ip", "192.168.0." + Integer.toString(ipNum));
	        	    edit.commit();
	        	    
	        	    ConCommon.ConnectClose();

	    			foundIP = true;
	    			isThreadWorking = false;
	    			searching = false;
	    			finish();
	    		}
	    		super.handleMessage(msg);
	    	}
	    };
	    ConCommon.HandlerStack.add(h);
	            
        Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// search All IPs
				for (ipNum = 0; ipNum < 256 && searching; ipNum++) {
					while (isThreadWorking) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (!searching)
						break;
					
					runOnUiThread(new Runnable() {
						public void run() {
					        stat.setText("Searching IPs ... " + Integer.toString(ipNum) + "/256");
						}
					});
					
					ConCommon.Connect("192.168.0." + Integer.toString(ipNum), 10070);
					isThreadWorking = true;
				}

				if (!foundIP) {
					runOnUiThread(new Runnable() {
						public void run() {
					Toast.makeText(c, "Cant find IP...", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		});
        t.start();
    }
    
    @Override
    protected void onDestroy() {
	    ConCommon.HandlerStack.remove(h);
    	super.onDestroy();
    }
}

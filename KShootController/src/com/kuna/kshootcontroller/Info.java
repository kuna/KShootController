package com.kuna.kshootcontroller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        
        TextView ver = (TextView)findViewById(R.id.app_version);
        TextView dev = (TextView)findViewById(R.id.author);
        TextView git = (TextView)findViewById(R.id.github);
        TextView se = (TextView)findViewById(R.id.server);
        
		try {
			String version = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			ver.setText(version);
		} catch (NameNotFoundException e) {
			// NOOOOOOOOOO, IT CAN'T BE! YOUR ANDROID DENIED GET PACKAGE NAME FROM YOUR DEVICE!
		}

        dev.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/lazykuna")));
        	}
        });

        git.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KShootController")));
        	}
        });

        se.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://kgdgwn98.snucse.org/KShootConServer.exe")));
        	}
        });
    }
}
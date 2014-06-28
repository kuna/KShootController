package com.kuna.kshootcontroller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Controller extends Activity {
	public CanvasView cv;
	public boolean button[] = new boolean[6];
	
	public int[] pressKey = {32,33,34,35,36,37,38};
	public int[] releaseKey = {64,65,66,67,68,69,70};
	public int[] scrKey = {'q', 'w', 'p', 'o'};
	public int[] scrreleaseKey = {0, 0, 0, 0};
	
	public ConElement[] ce;


	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("iBeatCon", "Controller Started");

		final SharedPreferences setting = getSharedPreferences("settings", MODE_PRIVATE);
		if (Build.VERSION.SDK_INT >= 11) {
			if (setting.getBoolean("hw_accel", true)) {
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		}

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (Build.VERSION.SDK_INT <= 7) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		if (displayMetrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
			Log.i("iBeatCon", "Display : Phone");
		} else if (displayMetrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
			Log.i("iBeatCon", "Display : Tablet (like Galaxy Tab 10.1)");
		} else if (displayMetrics.densityDpi == DisplayMetrics.DENSITY_XHIGH | displayMetrics.densityDpi <= 480) {
			Log.i("iBeatCon", "Display : Tablet2 (like Nexus 10)");
		} else {
			Log.i("iBeatCon", "Dispaly : Undefined (Load Default)");			
		}
		if (Build.VERSION.SDK_INT >= 14) {
			if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
				Log.i("iBeatCon", "Hardware Button Unknown Device");
				requestWindowFeature(Window.FEATURE_NO_TITLE);
			} else {
				Log.i("iBeatCon", "No Hardware Button Default Device");
				hideSystemBar();
			}
		}

		setContentView(R.layout.activity_controller);
		LinearLayout layout = (LinearLayout)findViewById(R.id.canvas_layout);

		int size_height = displayMetrics.heightPixels;
		int size_width = displayMetrics.widthPixels;
		
		// initalize ConElement
		loadControllerElement();

		// create canvas for drawing
		cv = new CanvasView(this, ce);
		layout.addView(cv);
	}
	
	private void loadControllerElement() {
		// load bitmap
		Bitmap normal = ConCommon.getBitmapFromResId(this, R.drawable.whb);
		Bitmap press = ConCommon.getBitmapFromResId(this, R.drawable.blp);

		Bitmap bnormal = ConCommon.getBitmapFromResId(this, R.drawable.bbb);
		Bitmap bpress = ConCommon.getBitmapFromResId(this, R.drawable.bbp);

		Bitmap snormal = ConCommon.getBitmapFromResId(this, R.drawable.bsb);
		Bitmap spress = ConCommon.getBitmapFromResId(this, R.drawable.bsp);

		Bitmap scrLnormal = ConCommon.getBitmapFromResId(this, R.drawable.scrbl);
		Bitmap scrLpress = ConCommon.getBitmapFromResId(this, R.drawable.scrpl);
		Bitmap scrRnormal = ConCommon.getBitmapFromResId(this, R.drawable.scrbr);
		Bitmap scrRpress = ConCommon.getBitmapFromResId(this, R.drawable.scrpr);
		
		// 
		ControllerSizer.initalize();
		
		ce = new ConElement[11];
		ce[0] = new ConButton(ControllerSizer.GetButtonRect(0, this), normal, press, 10, 50);
		ce[1] = new ConButton(ControllerSizer.GetButtonRect(1, this), normal, press, 11, 51);
		ce[2] = new ConButton(ControllerSizer.GetButtonRect(2, this), normal, press, 12, 52);
		ce[3] = new ConButton(ControllerSizer.GetButtonRect(3, this), normal, press, 13, 53);

		ce[4] = new ConButton(ControllerSizer.GetButtonRect(4, this), bnormal, bpress, 14, 54);
		ce[5] = new ConButton(ControllerSizer.GetButtonRect(5, this), bnormal, bpress, 15, 55);

		ce[6] = new ConButton(ControllerSizer.GetButtonRect(6, this), snormal, spress, 16, 56);

		
		ConButton cb;
		Rect r;
		cb = (ConButton) ce[4];
		r = ControllerSizer.GetButtonRect(4, this);
		cb.setInputRect(r.left-50, r.top, r.right+100, r.bottom + 150);
		
		cb = (ConButton) ce[5];
		r = ControllerSizer.GetButtonRect(5, this);
		cb.setInputRect(r.left-50, r.top, r.right+100, r.bottom + 150);
		
		if (ConCommon.scr2btn){
			ce[7] = new ConButton(ControllerSizer.GetButtonRect(7, this), 
					ConCommon.getBitmapFromResId(this, R.drawable.scrbl1), ConCommon.getBitmapFromResId(this, R.drawable.scrpl1), 20, 60);
			ce[8] = new ConButton(ControllerSizer.GetButtonRect(8, this),
					ConCommon.getBitmapFromResId(this, R.drawable.scrbl2), ConCommon.getBitmapFromResId(this, R.drawable.scrpl2), 20, 60);
			ce[9] = new ConButton(ControllerSizer.GetButtonRect(9, this),
					ConCommon.getBitmapFromResId(this, R.drawable.scrbr1), ConCommon.getBitmapFromResId(this, R.drawable.scrpr1), 20, 60);
			ce[10] = new ConButton(ControllerSizer.GetButtonRect(10, this),
					ConCommon.getBitmapFromResId(this, R.drawable.scrbr2), ConCommon.getBitmapFromResId(this, R.drawable.scrpr2), 20, 60);

			cb = (ConButton) ce[7];
			r = ControllerSizer.GetButtonRect(7, this);
			cb.setInputRect(r.left-50, r.top-50, r.right+50, r.bottom+100);
			
			cb = (ConButton) ce[8];
			r = ControllerSizer.GetButtonRect(8, this);
			cb.setInputRect(r.left, r.top-50, r.right+50, r.bottom+100);
			
			cb = (ConButton) ce[9];
			r = ControllerSizer.GetButtonRect(9, this);
			cb.setInputRect(r.left-50, r.top-50, r.right+50, r.bottom+100);
			
			cb = (ConButton) ce[10];
			r = ControllerSizer.GetButtonRect(10, this);
			cb.setInputRect(r.left, r.top-50, r.right+50, r.bottom+100);
		} else {
			Point p;
			p = ControllerSizer.GetScrPoint(0, this);
			ce[7] = new ConScratch(p.x, p.y, ControllerSizer.GetScrRadius(this), scrLnormal, scrLpress, 20, 60, 21, 61);
			p = ControllerSizer.GetScrPoint(1, this);
			ce[8] = new ConScratch(p.x, p.y, ControllerSizer.GetScrRadius(this), scrRnormal, scrRpress, 22, 62, 23, 63);

			ConScratch cs;
			cs = (ConScratch) ce[7];
			cs.setTouchRadius(ControllerSizer.GetScrTouchRadius(this));
			cs = (ConScratch) ce[8];
			cs.setTouchRadius(ControllerSizer.GetScrTouchRadius(this));
		}
	}

	void hideSystemBar() {
		if (Build.VERSION.SDK_INT >= 19) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.GONE
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		} else {
			 getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
					 | View.GONE);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);		
		if (Build.VERSION.SDK_INT >= 14) {
			if (ViewConfiguration.get(this).hasPermanentMenuKey() == false | hasFocus) {
				hideSystemBar();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		for (ConElement e: ce) {
			if (e==null) continue;
			e.destroy();
		}
		ConClient.Close();
		super.onDestroy();
	}
	
	/*
	 * menu
	 * 
	 */

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.join, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.reconnect:
    		Log.i("iBeatCon", "Reconnect");
    		ConCommon.ConnectClose();
    		ConCommon.Connect(this);
    		return true;
    	case R.id.settings:
    		Log.i("iBeatCon", "Settings");
    		startActivity(new Intent(getApplicationContext(), Settings.class));
    		return true;
    	case R.id.exit:
    		Log.i("iBeatCon", "Exit");
    		ConCommon.ConnectClose();
    		finish();
    		return true;
    	case R.id.info:
    		Log.i("iBeatCon", "Info");
    		startActivity(new Intent(getApplicationContext(), Info.class));
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	
	
	/*
	 * Touchevent
	 * 
	 */
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// moved to CanvasView.java - for px
		return super.onTouchEvent(event);
	}
}

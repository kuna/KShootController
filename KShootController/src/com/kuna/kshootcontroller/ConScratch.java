package com.kuna.kshootcontroller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/*
 * We dont need timer thread
 * because only thing we need to do is just sending message
 * WHEN TOUCH EVENT OCCURED. no other things necessary.
 */

public class ConScratch implements ConElement {
	private Point centerPos;
	private int radius;
	private int touchradius;
	private Bitmap scrBitmap;
	private Bitmap scrPrsBitmap;
	private boolean isPressed = false;
	
	private Thread mScratch = null;
	private boolean doScratchThread = true;
	
	private double angle;
	private double rotation;
	private int prevRotation;
	
	private static final int SCR_CLOCK = 2;
	private static final int SCR_CLOCKWISE = 1;
	private static final int SCR_NOPE = 0;
	private int prevState = 0;
	
	private int ld, lu, rd, ru;

	//private double avgScratch;
	
	public ConScratch(int x, int y, int r, Bitmap bitmap, Bitmap prsbitmap, int ld, int lu, int rd, int ru) {
		centerPos = new Point(x, y);
		radius = r;
		touchradius = radius;
		scrBitmap = bitmap;
		scrPrsBitmap = prsbitmap;
		
		this.ld = ld;
		this.lu = lu;
		this.rd = rd;
		this.ru = ru;
		
		// we use thread for stable scratching!
		// init thread
		mScratch = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (doScratchThread) {
						double angleDiff = getRadianDiff(rotation, angle);
						rotation = angle;
						
						// key input
						if (angleDiff > 0.02) {
							prevRotation++;
						} else if (angleDiff < -0.02) {
							prevRotation--;
						} else {
							prevRotation = 0;
						}

						if (prevRotation > 1) prevRotation = 1;
						if (prevRotation < -1) prevRotation = -1;
						if (prevRotation > 0) {
							setScratchStatus(SCR_CLOCK);
						} else if (prevRotation < 0) {
							setScratchStatus(SCR_CLOCKWISE);
						} else {
							//Log.i("SCROLL", "NOPE");
							setScratchStatus(SCR_NOPE);
						}
						
						Thread.sleep(1000/30);
					}
				} catch (Exception e) {
					Log.e("ERROR", e.toString());
				}
			}
		});
		doScratchThread = true;
		mScratch.start();
	}
	
	private void setScratchStatus(int stat) {
		if (stat != prevState) {
			switch (prevState) {
			case SCR_CLOCK:
				//Log.i("SCR", "CLOCK FINSIHED");
				ConCommon.SendData(ru);
				break;
			case SCR_CLOCKWISE:
				//Log.i("SCR", "CLOCKWISE FINSIHED");
				ConCommon.SendData(lu);
				break;
			}
			
			prevState = stat;
		}
		
		switch(stat) {
		case SCR_CLOCK:
			//Log.i("SCR", "CLOCK PRESS");
			ConCommon.SendData(rd);
			break;
		case SCR_CLOCKWISE:
			//Log.i("SCR", "CLOCKWISE PRESS");
			ConCommon.SendData(ld);
			break;
		}
	}
	
	public void setTouchRadius(int r) {
		touchradius = r;
	}
	
	public double GetDist(int x, int y) {
		return Math.sqrt( Math.pow(x - centerPos.x, 2) + Math.pow(y - centerPos.y, 2) ); 
	}

	private double getRadianDiff(double sRad, double eRad) {
		double r = eRad - sRad;
		if (r > Math.PI) r=r-Math.PI*2;
		if (r < -Math.PI) r=Math.PI*2+r;
		return r;
	}

	private double getRadianOfPointer(float cent_x, float cent_y, float pos_x, float pos_y) {
		double v = Math.toDegrees( Math.atan2( (pos_y-cent_y), (pos_x-cent_x) ) );
		return v;
	}
	
	public void moveEvent(int x, int y) {
		angle = getRadianOfPointer(centerPos.x, centerPos.y, x, y);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.translate(centerPos.x, centerPos.y);
		canvas.rotate((float) rotation);
		Bitmap scratchLED;
		if (isPressed) {
			scratchLED = scrPrsBitmap;
		} else {
			scratchLED = scrBitmap;
		}
		canvas.drawBitmap(scratchLED, null, new Rect(-radius, -radius, radius, radius), null);
		canvas.restore();
	}

	@Override
	public void mouseInput(Point[] pt, int desc) {
		Point inPt = null;
		
		for (Point p: pt) {
			if (p == null) continue;
			if (GetDist(p.x, p.y) < touchradius) {
				inPt = p;
				break;
			}
		}
		
		if (inPt != null) {
			moveEvent(inPt.x, inPt.y);
			isPressed = true;
		} else {
			isPressed = false;
		}
	}

	@Override
	public void destroy() {
		// kill thread
		Log.i("SCRATCH", "scratch destroy");
		doScratchThread = false;
		//mScratch.interrupt();
	}
}

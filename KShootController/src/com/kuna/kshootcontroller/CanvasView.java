package com.kuna.kshootcontroller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

	public static boolean bluekey = false;
	public static boolean blackpanel = false;
	public Bitmap[] button = new Bitmap[2];
	public Bitmap[] bottombutton = new Bitmap[2];
	public Bitmap scrPanel;
	public Bitmap scratch;
	public Bitmap light_white;
	public Bitmap light_red;	
	
	public Bitmap bg;
	public Rect rbg;
	
	public ConElement ce[];

	public CanvasView(Context context, ConElement ce[]) {
		super(context);

		// value init
		/*
		button[0] = getBitmapFromResId(R.drawable.bkb);
		button[0] = getBitmapFromResId(R.drawable.bkp);
		bottombutton[0] = getBitmapFromResId(R.drawable.blb);
		bottombutton[0] = getBitmapFromResId(R.drawable.blp);

		scratch = getBitmapFromResId(R.drawable.scratch);*/
		bg = ConCommon.getBitmapFromResId(this, R.drawable.bg);
		rbg = new Rect(0, 0, ConCommon.getScreenWidth((Activity)context), ConCommon.getScreenHeight((Activity)context));
		
		this.ce = ce;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw background
		canvas.drawBitmap(bg, null, rbg, null);
		
		// draw elements
		for (ConElement e: ce) {
			if (e==null) continue;
			e.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		Point points[] = new Point[10];
		int pointCnt = event.getPointerCount();
		if (pointCnt > 10) pointCnt = 10;
		
		for (int c=0; c<event.getPointerCount(); c++) {					
			if (event.getAction() == MotionEvent.ACTION_UP || 
					event.getAction() == MotionEvent.ACTION_POINTER_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
				continue;	// UP EVENT should be ignored

			int x = (int)event.getX(c);
			int y = (int)event.getY(c);
			points[c] = new Point(x, y);
		}
		
		// give points to all elements
		for (ConElement e: ce) {
			if (e==null) continue;
			e.mouseInput(points, 0);
		}
		
		this.postInvalidate();
		
		return true;
	}
}

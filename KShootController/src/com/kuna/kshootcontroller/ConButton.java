package com.kuna.kshootcontroller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class ConButton implements ConElement {
	private Rect rdraw;
	private Rect rinput;
	private Bitmap prsBit;
	private Bitmap normalBit;
	private boolean isPressed;
	
	private int pn, un;

	public ConButton(int x, int y, int wid, int hei, Bitmap normal, Bitmap press, int pn, int un) {
		setRect(x, y, wid, hei);
		rinput = rdraw;
		prsBit = press;
		normalBit = normal;
		isPressed = false;
		
		this.pn = pn;
		this.un = un;
	}
	
	public ConButton(Rect r, Bitmap normal, Bitmap press, int pn, int un) {
		setRect(r.left, r.top, r.right, r.bottom);
		rinput = rdraw;
		prsBit = press;
		normalBit = normal;
		isPressed = false;
		
		this.pn = pn;
		this.un = un;
	}
	
	public Bitmap getBitmap() {
		if (isPressed) {
			return prsBit;
		} else {
			return normalBit;
		}
	}
	
	public void changePress(boolean prs) {
		if (isPressed && !prs) {
			// pressed -> unpressed
			ConCommon.SendData(un);
		} else if (!isPressed && prs) {
			// unpressed -> pressed
			ConCommon.SendData(pn);
		}
		isPressed = prs;
	}
	
	public void setRect(int x, int y, int wid, int hei) {
		rdraw = new Rect(x, y, x+wid, y+hei);
	}
	
	public void setInputRect(int x, int y, int wid, int hei) {
		rinput = new Rect(x, y, x+wid, y+hei);
	}

	public boolean contains(int x, int y) {
		return rinput.contains(x, y);
	}

	@Override
	public void draw(Canvas canvas) {
		if (isPressed) {
			canvas.drawBitmap(prsBit, null, rdraw, null);
		} else {
			canvas.drawBitmap(normalBit, null, rdraw, null);
		}
	}

	@Override
	public void mouseInput(Point[] pt, int desc) {
		// check input
		boolean in = false;
		for (Point p: pt) {
			if (p == null) continue;
			if (contains(p.x, p.y)) {
				in = true;
				break;
			}
		}
		changePress(in);
	}

	@Override
	public void destroy() {
		// do nothing ...
	}
}

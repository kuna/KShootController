package com.kuna.kshootcontroller;

import android.graphics.Canvas;
import android.graphics.Point;

public interface ConElement {
	public void draw(Canvas canvas);
	public void mouseInput(Point pt[], int desc);
	public void destroy();
}

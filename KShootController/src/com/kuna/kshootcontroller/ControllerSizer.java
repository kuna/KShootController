package com.kuna.kshootcontroller;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;

public class ControllerSizer {
	private static Rect r[];
	private static Point p[];
	
	public static void initalize() {
		r = new Rect[11];
		r[0] = new Rect(256, 200, 166, 166);
		r[1] = new Rect(456, 200, 166, 166);
		r[2] = new Rect(656, 200, 166, 166);
		r[3] = new Rect(856, 200, 166, 166);

		r[4] = new Rect(368, 436, 133, 80);
		r[5] = new Rect(780, 436, 133, 80);

		r[6] = new Rect(602, 6, 80, 80);

		r[7] = new Rect(96, 50, 63, 126);
		r[8] = new Rect(159, 50, 63, 126);
		r[9] = new Rect(1059, 50, 63, 126);
		r[10] = new Rect(1122, 50, 63, 126);
		
		p = new Point[2];
		p[0] = new Point(159, 113);
		p[1] = new Point(1122, 113);
	}
	
	private static double GetRatio(Activity a) {
		return (double)ConCommon.getScreenWidth(a) / 1280;
	}
	
	private static Rect GetRatioRect(Rect r, Activity a) {
		double ratio = GetRatio(a);
		return new Rect((int)(r.left * ratio), (int)(r.top * ratio), 
				(int)(r.right * ratio), (int)(r.bottom * ratio));
	}
	
	private static Point GetRatioPoint(Point p, Activity a) {
		double ratio = GetRatio(a);
		return new Point((int)(p.x * ratio), (int)(p.y*ratio));
	}
	
	public static Rect GetButtonRect(int num, Activity a) {
		return GetRatioRect(r[num], a);
	}
	
	public static Point GetScrPoint(int num, Activity a) {
		return GetRatioPoint(p[num], a);
	}
	
	public static int GetScrRadius(Activity a) {
		return (int) (63 * GetRatio(a));
	}
	
	public static int GetScrTouchRadius(Activity a) {
		return (int) (128 * GetRatio(a));
	}
}

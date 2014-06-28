package com.kuna.kshootcontroller;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class ConCommon {
	public static Stack<Handler> HandlerStack = new Stack<Handler>();
	public static int zoomval = 100;
	public static boolean vb_feedback = false;
	public static boolean scr2btn = false;
	public static ConClient cc;	// initalize necessary!
	public static Activity controller;
	private final static boolean debug_noconnect = false;

	public static Bitmap getBitmapFromResId(View a, int id) {
		return ((BitmapDrawable)a.getResources().getDrawable(id)).getBitmap();
	}
	
	public static Bitmap getBitmapFromResId(Activity a, int id) {
		return ((BitmapDrawable)a.getResources().getDrawable(id)).getBitmap();
	}
	
	public static int getScreenWidth(Activity a) {
		Display display = a.getWindowManager().getDefaultDisplay(); 
		return display.getWidth();
	}
	
	public static int getScreenHeight(Activity a) {
		Display display = a.getWindowManager().getDefaultDisplay(); 
		return display.getHeight();
	}
	
	public static void ConnectClose() {
		ConClient.Close();
	}
	
	public static void Connect(String ip, int port) {
		cc.Connect(ip, port);
	}
	
	public static void Connect(Activity a) {
		SharedPreferences setting = a.getSharedPreferences("settings", Activity.MODE_PRIVATE);
		String ip = setting.getString("ip", "");
		int port = 10070;
		Connect(ip, port);
	}
	
	public static void SendData(int data) {
		if (!debug_noconnect)
			ConCommon.cc.Send(data);
	}

	public static void SendMessage(int msg) {
		for (int i=0; i<HandlerStack.size(); i++)
			HandlerStack.get(i).obtainMessage(msg, 0, 0, null).sendToTarget();
	}
}

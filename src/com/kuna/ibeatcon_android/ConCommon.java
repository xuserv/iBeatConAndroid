package com.kuna.ibeatcon_android;

import java.util.Stack;

import android.app.Activity;
import android.os.Handler;

public class ConCommon {
	public static ConClient cc;
	public static Activity controller;
	public static Stack<Handler> HandlerStack = new Stack<Handler>();
	
	public static boolean is2P = false;
	public static boolean keyonly = false;
	public static int zoomval = 100;
	public static boolean debug = false;
	public static boolean debug_noconnect = false;
	
	public static void SendMessage(int msg) {
		for (int i=0; i<HandlerStack.size(); i++)
			HandlerStack.get(i).obtainMessage(msg, 0, 0, null).sendToTarget();
	}
}
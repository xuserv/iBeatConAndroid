package com.kuna.ibeatcon_android;

import android.app.Activity;
import android.app.PendingIntent.OnFinished;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class Controller extends Activity {
	public static boolean isScratchPressed = false;
	public static boolean[] isButtonPressed = new boolean[7];
	
	public static Rect r_scr;
	public static Rect r_button[] = new Rect[7];
	public int id_scr;
	private ControllerSizer cs = new ControllerSizer();
	private CanvasView cv;
	private boolean isScrkeyPressed = false;
	
	public ImageView obj_scr;
	public TextView[] obj_btn = new TextView[7];
	
	public int[] pressKey = {32,33,34,35,36,37,38};
	public int[] releaseKey = {64,65,66,67,68,69,70};
	
	private Thread mScratch = null;
	private boolean doScratchThread = false;
	private double mScratchSpeed = 0;
	private double mScratchFriction = 1;
	public static double mScratchRotation = 0;
	
	private double mTouchAngle = -1;	// backup
	
	@Override
	public void onBackPressed() {
		if (!ConCommon.debug_noconnect)
			ConCommon.cc.Close();
		doScratchThread = false;
		super.onBackPressed();
	}
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
		
		DisplayMetrics displayMetrics = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int size_height = displayMetrics.heightPixels;
		int size_width = displayMetrics.widthPixels;
		
		// vals for set
		if (ConCommon.is2P) {
			cs.Preset_2P_S();
		} else {
			cs.Preset_1P_S();
		}
		cs.SetZoomSize(ConCommon.zoomval);
		
		// set rects for touch event
		r_scr = cs.GetScrDataRect(size_width, size_height);
		r_button = cs.GetButtonRect(size_width, size_height);
		
		// create canvas for drawing
		cv = new CanvasView(this);
		this.setContentView(cv);
		
		// start scratching controller
		UpdateControllerPosition();
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// scratch
		int ind = event.getActionIndex();
		int Actval = event.getAction() & MotionEvent.ACTION_MASK;
		float x = event.getX(ind);
		float y = event.getY(ind);
		
		if (Actval == MotionEvent.ACTION_DOWN || Actval == MotionEvent.ACTION_POINTER_DOWN) {
			if (!isScratchPressed) {
				if (GetDist(x, y, r_scr.left, r_scr.top) < r_scr.right) {
					id_scr = ind;
					mTouchAngle = getRadianOfPointer(r_scr.left, r_scr.top, x, y);
					mScratchSpeed = 0;	// just press to stop signal ...?
					isScratchPressed = true;
				}
			}
		}
		if (Actval == MotionEvent.ACTION_MOVE && id_scr == ind) {
			if (isScratchPressed) {
				double angle = getRadianOfPointer(r_scr.left, r_scr.top, x, y);
				double angleDiff = getRadianDiff(mTouchAngle, angle);
				
				//Log.v("SCR", Double.toString(angleDiff));
				mScratchSpeed = angleDiff;
				
				mTouchAngle = angle;
			}
		}
		if ((Actval == MotionEvent.ACTION_UP || Actval == MotionEvent.ACTION_POINTER_UP) && id_scr == ind) {
			if (isScratchPressed) {
				//double angle = getRadianOfPointer(r_scr.left, r_scr.top, x, y);
				//double angleDiff = getRadianDiff(scr_tangle, angle);
				// angleDiff = last scratch move
				
				isScratchPressed = false;
			}
		}
		
		// button
		boolean[] p = new boolean[7];
		for (int c=0; c<event.getPointerCount(); c++) {
			if (c == event.getActionIndex() && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP))
				continue;	// UP EVENT should be ignored
			
			x = event.getX(c);
			y = event.getY(c);
			
			// check area
			for (int i=0; i<7; i++) {
				if (r_button[i].contains((int)x, (int)y))
					p[i] = true;
			}
		}
		CmpPrs(isButtonPressed, p);
		isButtonPressed = p;
		
		return super.onTouchEvent(event);
	}
	
	
	// common function
	public void SendData(int i) {
		if (!ConCommon.debug_noconnect)
			ConCommon.cc.Send( i );
	}
	public void PressButton(int i) {
		SendData(pressKey[i]);
	}
	public void ReleaseButton(int i) {
		SendData(releaseKey[i]);
	}
	public void CmpPrs(boolean[] org, boolean[] diff) {
		for (int i=0; i<7; i++) {
			if (!org[i] && diff[i])
				PressButton(i);
			else if (org[i] && !diff[i])
				ReleaseButton(i);
		}
	}
	
	public double getRadianDiff(double sRad, double eRad) {
		double r = eRad - sRad;
		if (r > Math.PI) r=r-Math.PI*2;
		if (r < -Math.PI) r=Math.PI*2+r;
		return r;
	}
	
	public double getRadianOfPointer(float cent_x, float cent_y, float pos_x, float pos_y) {
		double v = Math.toDegrees( Math.atan2( (pos_y-cent_y), (pos_x-cent_x) ) );
		return v;
	}
	
	public void UpdateControllerPosition() {
		mScratch = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (doScratchThread) {
						// reduce scratch speed
						if (mScratchSpeed > 0) {
							mScratchSpeed -= mScratchFriction*0.1;
							if (mScratchSpeed < 0) mScratchSpeed = 0;
						} else if (mScratchSpeed < 0) {
							mScratchSpeed += mScratchFriction*0.1;
							if (mScratchSpeed > 0) mScratchSpeed = 0;
						}
						
						// change rotation value
						mScratchRotation += mScratchSpeed*3;
						
						// check scratch
						if (mScratchSpeed > 1) {
							SendData(42);
							Log.v("Scratch", "PRESS");
							isScrkeyPressed = true;
						}
						if (mScratchSpeed < -1) {
							SendData(42);
							Log.v("Scratch", "PRESS");
							isScrkeyPressed = true;
						}
						if (mScratchSpeed < 1 && mScratchSpeed > -1 && isScrkeyPressed) {
							SendData(74);
							Log.v("Scratch", "UP");
							isScrkeyPressed = false;
						}

						Thread.sleep(1000/30);
					}
				} catch (Exception e) {
					Log.v("ERROR", e.toString());
				}
			}
		});
		doScratchThread = true;
		
		mScratch.start();
	}
	
	public float CalculateTorque(float radianDiff) {
		return (float) ((radianDiff - mScratchSpeed) / (1 + mScratchFriction*0.1));
	}
	
	public void AddTorqueToScratch(float val) {
		mScratchSpeed += val;
	}
	
	public double GetDist(float x1, float y1, float x2, float y2) {
		return Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2, 2) );
	}
}

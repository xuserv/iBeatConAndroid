/**
 * iBeatCon Server 0.9.2
 * 9,7,8 ; Scratch
 * 42,74 ; Start
 * 32,33,34,35,36,37,38 ; Key Press
 * 64,65,66,67,68,69,70 ; Key Release
 */

package com.kuna.ibeatcon_android;

import android.app.Activity;
import android.app.PendingIntent.OnFinished;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	public static boolean isStartPressed = false;
	public static boolean isScratchPressed = false;
	public static boolean[] isButtonPressed = new boolean[7];
	
	public static Rect r_start;
	public static Rect r_scr;
	public static Rect r_button[] = new Rect[7];
	public int id_scr;
	private ControllerSizer cs = new ControllerSizer();
	private CanvasView cv;
	private boolean isScrkeyPressed = false;
	private boolean isScrUp = false;
	
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
		doScratchThread = false;
		finish();
		super.onBackPressed();
	}
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
		DisplayMetrics displayMetrics = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int size_height = displayMetrics.heightPixels;
		int size_width = displayMetrics.widthPixels;
				
		switch(displayMetrics.densityDpi) {
		case DisplayMetrics.DENSITY_HIGH:
			Log.i("Display", "Phone");
			if (ConCommon.keyonly) {
				cs.Preset_Keyonly();
			} else if (ConCommon.is2P) {
				cs.Preset_2P_S();
			} else {
				cs.Preset_1P_S();
			}
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			Log.i("Display", "Tablet");
			if (ConCommon.keyonly) {
				cs.Preset_Keyonly();
			} else if (ConCommon.is2P) {
				cs.Preset_2P_M();
			} else {
				cs.Preset_1P_M();
			}
			break;
		}
		cs.SetZoomSize(ConCommon.zoomval);
		
		// set rects for touch event
		r_start = cs.GetStartRect(size_width, size_height);
		r_scr = cs.GetScrDataRect(size_width, size_height);
		r_button = cs.GetButtonRect(size_width, size_height);
		
		// create canvas for drawing
		cv = new CanvasView(this);
		this.setContentView(cv);
		
		// start scratching controller
		UpdateControllerPosition();
	};
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.join, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.settings:
    		startActivity(new Intent(this, Settings.class));
    		finish();
    		return true;   		
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// start
		boolean s = false;
		for (int c=0; c<event.getPointerCount(); c++) {
					
		if (c == event.getActionIndex() && (event.getAction() == MotionEvent.ACTION_UP || 
			event.getAction() == MotionEvent.ACTION_POINTER_UP || event.getAction() == MotionEvent.ACTION_CANCEL))
			continue;	// UP EVENT should be ignored
					
			float x = event.getX(c);
			float y = event.getY(c);

			if (r_start.contains((int)x, (int)y)) {
				s = true;
			}
		}
			StaBtn(isStartPressed, s);
			isStartPressed = s;
		
		// scratch
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int pointerCount = event.getPointerCount();
		int Actval = event.getAction() & MotionEvent.ACTION_MASK;
		
		if (Actval == MotionEvent.ACTION_DOWN || Actval == MotionEvent.ACTION_POINTER_DOWN) {
			if (!isScratchPressed) {
				float x = event.getX(pointerIndex);
				float y = event.getY(pointerIndex);
				
				if (GetDist(x, y, r_scr.left, r_scr.top) < r_scr.right) {
					id_scr = pointerIndex;
					mTouchAngle = getRadianOfPointer(r_scr.left, r_scr.top, x, y);
					mScratchSpeed = 0;	// just press to stop signal ...?
					isScratchPressed = true;
				}
			}
		}
		
		/*
		if ((Actval == MotionEvent.ACTION_UP || Actval == MotionEvent.ACTION_POINTER_UP
				|| Actval == MotionEvent.ACTION_CANCEL) && id_scr == pointerIndex) {
			if (isScratchPressed) {
				isScratchPressed = false;
			}
		}*/

		if (isScratchPressed) {
			// constantly receive scratch pos
	        for(int i = 0; i < pointerCount; ++i)
	        {
	            pointerIndex = i;
	            pointerId = event.getPointerId(pointerIndex);
	            if(pointerId == id_scr)
	            {
	            	if (Actval == MotionEvent.ACTION_UP || Actval == MotionEvent.ACTION_POINTER_UP
	        				|| Actval == MotionEvent.ACTION_CANCEL) {
	            		isScratchPressed = false;
	            		break;
	            	}
	        				
	                float x = event.getX(pointerIndex);
	                float y = event.getY(pointerIndex);
	                
	                double angle = getRadianOfPointer(r_scr.left, r_scr.top, x, y);
                    double angleDiff = getRadianDiff(mTouchAngle, angle);
                    
                    mScratchSpeed = angleDiff;
                    mTouchAngle = angle;
	            }
	        }
		}
		
		// button
		boolean[] p = new boolean[7];
		for (int c=0; c<event.getPointerCount(); c++) {
			
			if (c == event.getActionIndex() && (event.getAction() == MotionEvent.ACTION_UP || 
					event.getAction() == MotionEvent.ACTION_POINTER_UP || event.getAction() == MotionEvent.ACTION_CANCEL))
				continue;	// UP EVENT should be ignored
			
			float x = event.getX(c);
			float y = event.getY(c);
			
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
			ConCommon.cc.Send(i);
	}
	public void PressButton(int i) {
		SendData(pressKey[i]);
		Log.v("Button", i + " PRESSED");
	}
	public void ReleaseButton(int i) {
		SendData(releaseKey[i]);
		Log.v("Button", i + " RELEASED");
	}
	public void CmpPrs(boolean[] org, boolean[] diff) {
		for (int i=0; i<7; i++) {
			if (!org[i] && diff[i])
				PressButton(i);
			else if (org[i] && !diff[i])
				ReleaseButton(i);
		}
	}
	public void StaBtn(boolean org, boolean diff) {
		if (!org && diff) {
			SendData(42);
		} else if (org && !diff) {
			SendData(74);
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
							SendData(8);
							Log.v("Scratch", "PRESS");
							isScrUp = false;
							isScrkeyPressed = true;
						}
						if (mScratchSpeed < -1) {
							SendData(7);
							Log.v("Scratch", "PRESS2");
							isScrUp = true;
							isScrkeyPressed = true;
						}
						if (mScratchSpeed < 1 && mScratchSpeed > -1 && isScrkeyPressed) {
								SendData(9);
								Log.v("Scratch", "UP2");
								isScrUp = false;
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

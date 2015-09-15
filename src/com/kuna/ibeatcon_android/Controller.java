/**
 * iBeatCon Server 0.9.2 (beyond 1.0.0)
 * 9,7,8 ; Scratch (LCtrl, LShift)
 * 42,74 ; Start
 * 32,33,34,35,36,37,38 ; Key Press
 * 64,65,66,67,68,69,70 ; Key Release
 */

package com.kuna.ibeatcon_android;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Build;
import android.os.Vibrator;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Controller extends Activity {
	CanvasView cv;
	public static boolean isStartPressed = false;
	public static boolean isVefxPressed = false;
	public static boolean isScratchPressed = false;
	public static boolean[] isButtonPressed = new boolean[7];
	public static boolean vb_feedback = false;
	public static boolean touch_scratch = false;
	
	public static Rect r_start;
	public static Rect r_vefx;
	public static Rect r_scr;
	public static Rect r_button[] = new Rect[7];
	public int id_scr;
	public int[] pressKey = {32,33,34,35,36,37,38};
	public int[] releaseKey = {64,65,66,67,68,69,70};
	
	public ImageView obj_scr;
	public TextView[] obj_btn = new TextView[7];
		
	public static double mScratchRotation = 0;
	
	private boolean isScrkeyPressed = false;
	private boolean doScratchThread = false;
	private double mScratchSpeed = 0;
	private double mScratchFriction = 1;
	private double mTouchAngle = -1;	// backup
	private ControllerSizer cs = new ControllerSizer();
	private Thread mScratch = null;
	private String ip;
	private String port2;
	
	@Override
	public void onBackPressed() {			
		mScratch.interrupt();
		ConClient.Close();
		finish();
		super.onBackPressed();
	}
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("iBeatCon", "Controller Started");
		
		final SharedPreferences setting = getSharedPreferences("settings", MODE_PRIVATE);
		ip = setting.getString("ip", "");
		
		if (setting.getBoolean("port", true)) {
			port2 = "10070";
		} else {
			port2 = "2001";
		}
		
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
		
		// load template
		if (displayMetrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
			Log.i("iBeatCon", "Display : Phone");
			if (Build.VERSION.SDK_INT >= 14) {
				if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
					Log.i("iBeatCon", "Hardware Button Phone");
					requestWindowFeature(Window.FEATURE_NO_TITLE);
				} else {
					Log.i("iBeatCon", "No Hardware Button Phone");
					hideSystemBar();
				}
			}
			if (ConCommon.keyonly) {
				cs.Preset_Keyonly();
			} else if (ConCommon.scronly) {
				cs.Preset_Scronly();
			} else if (ConCommon.is2P) {
				cs.Preset_2P_S();
			} else {
				cs.Preset_1P_S();
			}
		} else if (displayMetrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
			Log.i("iBeatCon", "Display : Tablet (like Galaxy Tab 10.1)");
			if (Build.VERSION.SDK_INT >= 14) {
				if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
					Log.i("iBeatCon", "Hardware Button Tablet");
					requestWindowFeature(Window.FEATURE_NO_TITLE);
				} else {
					Log.i("iBeatCon", "No Hardware Button Tablet");
					hideSystemBar();
				}
			}		
			if (ConCommon.keyonly) {
				cs.Preset_Keyonly();
			} else if (ConCommon.scronly) {
				cs.Preset_Scronly();
			} else if (ConCommon.is2P) {
				cs.Preset_2P_M();
			} else {
				cs.Preset_1P_M();
			}
		} else if (displayMetrics.densityDpi == DisplayMetrics.DENSITY_XHIGH | displayMetrics.densityDpi <= 480) {
			Log.i("iBeatCon", "Display : Tablet2 (like Nexus 10)");			
			if (Build.VERSION.SDK_INT >= 14) {
				if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
					Log.i("iBeatCon", "Hardware Button Tablet2");
					requestWindowFeature(Window.FEATURE_NO_TITLE);
				} else {
					Log.i("iBeatCon", "No Hardware Button Tablet2");
					hideSystemBar();
				}
			}
			if (ConCommon.keyonly) {
				cs.Preset_Keyonly();
			} else if (ConCommon.scronly) {
				cs.Preset_Scronly();
			} else if (ConCommon.is2P) {
				cs.Preset_2P_L();
			} else {
				cs.Preset_1P_L();
			}
		} else {
			Log.i("iBeatCon", "Dispaly : Undefined (Load Default)");			
			if (Build.VERSION.SDK_INT >= 14) {
				if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
					Log.i("iBeatCon", "Hardware Button Unknown Device");
					requestWindowFeature(Window.FEATURE_NO_TITLE);
				} else {
					Log.i("iBeatCon", "No Hardware Button Default Device");
					hideSystemBar();
				}
			}
			if (ConCommon.keyonly) {
				cs.Preset_Keyonly();
			} else if (ConCommon.scronly) {
				cs.Preset_Scronly();
			} else if (ConCommon.is2P) {
				cs.Preset_2P_M();
			} else {
				cs.Preset_1P_M();
			}
		}
		
		setContentView(R.layout.activity_controller);
		LinearLayout layout = (LinearLayout)findViewById(R.id.canvas_layout);
		
		cs.SetZoomSize(ConCommon.zoomval);
		
		int size_height = displayMetrics.heightPixels;
		int size_width = displayMetrics.widthPixels;
		
		// set rects for touch event
		r_start = cs.GetStartRect(size_width, size_height);
		r_vefx = cs.GetVefxRect(size_width, size_height);
		r_scr = cs.GetScrDataRect(size_width, size_height);
		r_button = cs.GetButtonRect(size_width, size_height);
		
		// create canvas for drawing
		cv = new CanvasView(this);
		layout.addView(cv);
		
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
    	case R.id.reconnect:
    		Log.i("iBeatCon", "Reconnect");
    		ConClient.Close();
    		ConCommon.cc = new ConClient(ip, Integer.parseInt(port2));
    		return true;
    	case R.id.settings:
    		Log.i("iBeatCon", "Settings");
    		startActivity(new Intent(getApplicationContext(), Settings.class));
    		return true;
    	case R.id.exit:
    		Log.i("iBeatCon", "Exit");
    		mScratch.interrupt();
    		ConClient.Close();
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// scratch
		if (touch_scratch) {
			mScratch.interrupt();
			
			int pointerIndex = event.getActionIndex();
			int pointerId = event.getPointerId(pointerIndex);
			int pointerCount = event.getPointerCount();
			int Actval = event.getAction() & MotionEvent.ACTION_MASK;
			boolean scr = false;
		
			if (Actval == MotionEvent.ACTION_DOWN || Actval == MotionEvent.ACTION_POINTER_DOWN) {
				if (!isScratchPressed) {
					float x = event.getX(pointerIndex);
					float y = event.getY(pointerIndex);
				
					if (GetDist(x, y, r_scr.left, r_scr.top) < r_scr.right) {
						id_scr = pointerIndex;
						scr = true;
						cv.postInvalidate();
					}
				} else {
					// constantly receive scratch pos
					for(int i = 0; i < pointerCount; ++i) {
						pointerIndex = i;
						pointerId = event.getPointerId(pointerIndex);
						if(pointerId == id_scr) {
							if (Actval == MotionEvent.ACTION_UP || Actval == MotionEvent.ACTION_POINTER_UP || Actval == MotionEvent.ACTION_CANCEL) {
								scr = false;
								cv.postInvalidate();
							}
						}
					}
				}
			}
			ScrBtn(scr, isScratchPressed);
			isScratchPressed = scr;
			cv.postInvalidate();
		} else {
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

				if (isScratchPressed) {
					// constantly receive scratch pos
			        for(int i = 0; i < pointerCount; ++i) {
			            pointerIndex = i;
			            pointerId = event.getPointerId(pointerIndex);
			            if(pointerId == id_scr) {
			            	if (Actval == MotionEvent.ACTION_UP || Actval == MotionEvent.ACTION_POINTER_UP || Actval == MotionEvent.ACTION_CANCEL) {
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
		}
        
		
		// start
		boolean s = false;
		for (int c=0; c<event.getPointerCount(); c++) {					
			if (c == event.getActionIndex() && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP || event.getAction() == MotionEvent.ACTION_CANCEL))
				continue;	// UP EVENT should be ignored
					
				float x = event.getX(c);
				float y = event.getY(c);

				if (r_start.contains((int)x, (int)y)) {
					s = true;
				}
		}
		StaBtn(isStartPressed, s);
		isStartPressed = s;	
		
		// vefx
		boolean v = false;
		for (int c=0; c<event.getPointerCount(); c++) {
			if (c == event.getActionIndex() && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP || event.getAction() == MotionEvent.ACTION_CANCEL))
			continue;	// UP EVENT should be ignored
				
			float x = event.getX(c);
			float y = event.getY(c);

			if (r_vefx.contains((int)x, (int)y)) {
				v = true;
			}
		}
		VefxBtn(isVefxPressed, v);
		isVefxPressed = v;
		
		// button
		boolean[] p = new boolean[7];
		for (int c=0; c<event.getPointerCount(); c++) {
			
			if (c == event.getActionIndex() && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP || event.getAction() == MotionEvent.ACTION_CANCEL))
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
	
	public void SendData(int i) {
		if (!ConCommon.debug_noconnect)
			ConCommon.cc.Send(i);
	}
	
	public void PressButton(int i) {
		if (vb_feedback){
			Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(100);
			SendData(pressKey[i]);
			Log.i("iBeatCon", "Button " + i + " Pressed with Vibe");
		} else {
			SendData(pressKey[i]);
			Log.i("iBeatCon", "Button " + i + " Pressed");
		}
	}
	
	public void ReleaseButton(int i) {
		SendData(releaseKey[i]);
		Log.i("iBeatCon", "Button " + i + " Released");
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
			Log.i("iBeatCon", "Start Button Pressed");
		} else if (org && !diff) {
			Log.i("iBeatCon", "Start Button Released");
			SendData(74);
		}
	}
	
	public void VefxBtn(boolean org, boolean diff) {
		if (!org && diff) {
			SendData(43);
			Log.i("iBeatCon", "Vefx Button Pressed");
		} else if (org && !diff) {
			SendData(75);
			Log.i("iBeatCon", "Vefx Button Released");
		}
	}
	
	public void ScrBtn(boolean org, boolean diff) {
		if (!org && diff) {
			SendData(9);
			Log.i("iBeatCon", "Scratch Pressed");
		} else if (org && !diff) {
			Log.i("iBeatCon", "Scratch Released");
			SendData(8);
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
						
						// refresh controller ui
						cv.postInvalidate();
						
						// check scratch
						if (mScratchSpeed < -1) {
							SendData(7);
							Log.i("iBeatCon", "Scracth : Up");
							isScrkeyPressed = true;
						}
						if (mScratchSpeed > 1) {
							SendData(8);
							Log.i("iBeatCon", "Scratch : Down");
							isScrkeyPressed = true;
						}						
						if (mScratchSpeed < 1 && mScratchSpeed > -1 && isScrkeyPressed) {
							SendData(9);
							Log.i("iBeatCon", "Scratch : Stopped");
							isScrkeyPressed = false;
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
	
	public float CalculateTorque(float radianDiff) {
		return (float) ((radianDiff - mScratchSpeed) / (1 + mScratchFriction*0.1));
	}
	
	public void AddTorqueToScratch(float val) {
		mScratchSpeed += val;
	}
	
	public double GetDist(float x1, float y1, float x2, float y2) {
		return Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2, 2) );
	}
	
	void hideSystemBar() {
		if (Build.VERSION.SDK_INT >= 19) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.GONE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		} else {
			 getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.GONE);
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
    public void onStart() {
    	super.onStart();
    	EasyTracker.getInstance(this).activityStart(this);
    }
    
	@Override
    public void onStop() {
    	super.onStop();
    	EasyTracker.getInstance(this).activityStop(this);
    }
}

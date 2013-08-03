package com.kuna.ibeatcon_android;

import android.app.Activity;
import android.app.PendingIntent.OnFinished;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
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
	public boolean isScratchPressed = false;
	public boolean[] isButtonPressed = new boolean[7];
	public double initialRadian=0;
	
	public Rect r_scr;
	public Rect r_button[] = new Rect[7];
	public int id_scr;
	public double scr_angle=0;
	public double scr_tangle=0;
	public double angleDiff = 0;
	private ControllerSizer cs = new ControllerSizer();
	
	public ImageView obj_scr;
	public TextView[] obj_btn = new TextView[7];
	
	public int[] normalRes = new int[2];
	public int[] pressedRes = new int[2];
	public int[] pressKey = {32,33,34,35,36,37,38};
	public int[] releaseKey = {64,65,66,67,68,69,70};
	
	@Override
	public void onBackPressed() {
		ConCommon.cc.Close();
		super.onBackPressed();
	}
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AbsoluteLayout ll = new AbsoluteLayout(this);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
		
		int size_height = 480;
		int size_width = 800;
		
		// vals for set
		if (ConCommon.is2P) {
			cs.Preset_2P_S();
		} else {
			cs.Preset_1P_S();
		}
		cs.SetZoomSize(ConCommon.zoomval);
		
		r_scr = cs.GetScrDataRect(size_width, size_height);
		r_button = cs.GetButtonRect(size_width, size_height);
		
		Rect r_scr_panel = cs.GetScrPanelRect(size_width, size_height);
		TextView b_scr_panel = new TextView(this);
		b_scr_panel.setBackgroundResource(R.drawable.sc_panel);
		ll.addView(b_scr_panel, new AbsoluteLayout.LayoutParams( r_scr_panel.right*2, r_scr_panel.right*2, r_scr_panel.left, r_scr_panel.top));
		
		Rect r_scr_body = cs.GetScrRect(size_width, size_height);
		final ImageView b_scr = new ImageView(this);
		b_scr.setImageBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.scratch) );
		/*b_scr.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					isScratchPressed = true;
					initialRadian = getRadianOfPointer(,,event.getX(), event.getY());
				}
				
				if (event.getAction() == MotionEvent.ACTION_UP ||
						event.getAction() == MotionEvent.ACTION_CANCEL) {
					isScratchPressed = false;
				}
				return false;
			}
		});*/
		//Log.i("SCRATCH", String.format("%d,%d,%d", r_scr_body.left, r_scr_body.top, r_scr_body.right));
		ll.addView(b_scr, new AbsoluteLayout.LayoutParams( r_scr_body.right*2, r_scr_body.right*2,r_scr_body.left, r_scr_body.top));
		obj_scr = b_scr;
		
		// need interval...
		
		// add button
		for (int i=0; i<7; i++) {
			final TextView b = new TextView(this);
			int left = r_button[i].left;
			int top = r_button[i].top;
			int width = r_button[i].width();
			int height = r_button[i].height();
			
			Log.i("BUTTON", String.format("%d, %d, %d, %d",left, top, width,height));
			
			final int pressedRes;
			final int normalRes;
			if (i % 2 == 0) {
				normalRes = R.drawable.whb;
				pressedRes = R.drawable.whp;
			} else {
				normalRes = R.drawable.bkb;
				pressedRes = R.drawable.bkp;
			}
			
			b.setBackgroundResource(normalRes);
			/*
			b.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN ||
							event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
						b.setBackgroundResource(pressedRes);
					}
					if (event.getAction() == MotionEvent.ACTION_UP ||
							event.getAction() == MotionEvent.ACTION_HOVER_EXIT ||
							event.getAction() == MotionEvent.ACTION_CANCEL) {
						b.setBackgroundResource(normalRes);
					}	
					return false;
				}
			});*/
			
			ll.addView(b, new AbsoluteLayout.LayoutParams(width, height, left, top));
			obj_btn[i] = b;
		}
		
		this.setContentView(ll);
		
		// value init
		normalRes[0] = R.drawable.whb;
		normalRes[1] = R.drawable.bkb;
		pressedRes[0] = R.drawable.whp;
		pressedRes[1] = R.drawable.bkp;
		
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
					scr_tangle = getRadianOfPointer(r_scr.left, r_scr.top, x, y);
					isScratchPressed = true;
					Log.i("SCRATCH", "START");
					
					ConCommon.cc.Send(42);
				}
			}
		}
		if (Actval == MotionEvent.ACTION_MOVE && id_scr == ind) {
			
		}
		if ((Actval == MotionEvent.ACTION_UP || Actval == MotionEvent.ACTION_POINTER_UP) && id_scr == ind) {
			if (isScratchPressed) {
				//double angle = getRadianOfPointer(r_scr.left, r_scr.top, x, y);
				//double angleDiff = getRadianDiff(scr_tangle, angle);
				// angleDiff = last scratch move
				
				isScratchPressed = false;
				Log.i("SCRATCH", "END");
				
				ConCommon.cc.Send(74);
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
	public void PressButton(int i) {
		ConCommon.cc.Send( pressKey[i] );
		obj_btn[i].setBackgroundResource( pressedRes[ i%2 ] );
	}
	public void ReleaseButton(int i) {
		ConCommon.cc.Send( releaseKey[i] );
		obj_btn[i].setBackgroundResource( normalRes[ i%2 ] );
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
		if (r < -Math.PI) r=Math.PI*2-r;
		return r;
	}
	
	public double getRadianOfPointer(float cent_x, float cent_y, float pos_x, float pos_y) {
		double v = Math.toDegrees( Math.atan2( (pos_y-cent_y), (pos_x-cent_x) ) );
		return v;
	}
	
	public void UpdateControllerPosition() {
	}
	
	public double GetDist(float x1, float y1, float x2, float y2) {
		return Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2, 2) );
	}
}

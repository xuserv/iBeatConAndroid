package com.kuna.ibeatcon_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.Matrix;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
	public Bitmap[] startRes = new Bitmap[2];
	public Bitmap[] normalRes = new Bitmap[2];
	public Bitmap[] pressedRes = new Bitmap[2];
	public Bitmap scrPanel;
	public Bitmap scratch;
	public Bitmap light_white;
	public Bitmap light_red;
	private ControllerSizer cs = new ControllerSizer();
	private SurfaceHolder mHolder;
	private Thread mThread;
	private boolean isThreadRunning = false;
	
	public CanvasView(Context context) {
		super(context);
		
		// value init
		startRes[0] = getBitmapFromResId(R.drawable.rdn);
		startRes[1] = getBitmapFromResId(R.drawable.rdp);
		normalRes[0] = getBitmapFromResId(R.drawable.whb);
		normalRes[1] = getBitmapFromResId(R.drawable.bkb);
		pressedRes[0] = getBitmapFromResId(R.drawable.whp);
		pressedRes[1] = getBitmapFromResId(R.drawable.bkp);
		
		scrPanel = getBitmapFromResId(R.drawable.sc_panel);
		scratch = getBitmapFromResId(R.drawable.scratch);
		light_white = getBitmapFromResId(R.drawable.white_light);
		light_red = getBitmapFromResId(R.drawable.red_light);
		
		// get holder
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		// create thread
		InitThread();
	}
	
	private void InitThread() {
		mThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (isThreadRunning) {
						Canvas canvas = mHolder.lockCanvas();
						
						canvas.drawColor(Color.WHITE);
						
						// draw start
						Bitmap b;
						if (Controller.isStartPressed) {
							b = startRes[1];
						} else {
							b = startRes[0];
						}
						
						canvas.drawBitmap(b, null, Controller.r_start, null);
						
						// draw buttons
						for (int i=0; i<7; i++) {
							Bitmap b2;
							if (Controller.isButtonPressed[i]) {
								b2 = pressedRes[i%2];
							} else {
								b2 = normalRes[i%2];
							}
							
							canvas.drawBitmap(b2, null, Controller.r_button[i], null);
						}
						
						/** scratch part */
						double ratio_LED = 1.1;
						double ratio_SCR = 0.86;
						
						// draw scratch LED
						canvas.save();
						canvas.translate(Controller.r_scr.left, Controller.r_scr.top);
						canvas.rotate((float) Controller.mScratchRotation);
						Bitmap scratchLED;
						if (Controller.isScratchPressed) {
							scratchLED = light_red;
						} else {
							scratchLED = light_white;
						}
						canvas.drawBitmap(scratchLED, null, new Rect(-(int)(Controller.r_scr.right*ratio_LED), -(int)(Controller.r_scr.right*ratio_LED), 
								(int)(Controller.r_scr.right*ratio_LED), (int)(Controller.r_scr.right*ratio_LED)), null);
						canvas.restore();
						
						// draw scratch
						/*canvas.save();
						canvas.translate(Controller.r_scr.left, Controller.r_scr.top);
						canvas.rotate((float) Controller.mScratchRotation);
						canvas.drawBitmap(scrPanel, null, new Rect(-Controller.r_scr.right, -Controller.r_scr.right, 
								Controller.r_scr.right, Controller.r_scr.right), null);
						canvas.restore();*/
						
						// draw scratch body
						canvas.save();
						canvas.translate(Controller.r_scr.left, Controller.r_scr.top);
						canvas.rotate((float) Controller.mScratchRotation);
						canvas.drawBitmap(scratch, null, new Rect(-(int)(Controller.r_scr.right*ratio_SCR), -(int)(Controller.r_scr.right*ratio_SCR), 
								(int)(Controller.r_scr.right*ratio_SCR), (int)(Controller.r_scr.right*ratio_SCR)), null);
						canvas.restore();
						
						/** scratch part end */
						
						mHolder.unlockCanvasAndPost(canvas);
						
						//this.wait(1000/60);
					}
				} catch (Exception e) {
					Log.e("ERROR", e.toString());
				}
			}
		});
	}
	
	private Bitmap getBitmapFromResId(int id) {
		return ((BitmapDrawable)getResources().getDrawable(id)).getBitmap();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			isThreadRunning = true;
			mThread.start();
			Log.v("INFO" ,"SurfaceView Started");
		} catch (Exception e) {
			// resume doesn't work...
			isThreadRunning = false;
			mThread = null;
			InitThread();
			mThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isThreadRunning = false;
	}
}

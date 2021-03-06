package com.kuna.ibeatcon_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class CanvasView extends View {
	
	public static boolean bluekey = false;
	public static boolean blackpanel = false;
	public Bitmap[] startRes = new Bitmap[2];
	public Bitmap[] vefxRes = new Bitmap[2];
	public Bitmap[] normalRes = new Bitmap[2];
	public Bitmap[] pressedRes = new Bitmap[2];	
	public Bitmap scrPanel;
	public Bitmap scratch;
	public Bitmap light_white;
	public Bitmap light_red;	
	
	public CanvasView(Context context) {
		super(context);
		
		// value init
		startRes[0] = getBitmapFromResId(R.drawable.rdn);
		startRes[1] = getBitmapFromResId(R.drawable.rdp);
		vefxRes[0] = getBitmapFromResId(R.drawable.rdn);
		vefxRes[1] = getBitmapFromResId(R.drawable.rdp);
		
		// change buttons color & draw buttons
		if (bluekey) {
			normalRes[0] = getBitmapFromResId(R.drawable.whb);
			normalRes[1] = getBitmapFromResId(R.drawable.blb);
			pressedRes[0] = getBitmapFromResId(R.drawable.whp);
			pressedRes[1] = getBitmapFromResId(R.drawable.blp);
		} else {
			normalRes[0] = getBitmapFromResId(R.drawable.whb);
			normalRes[1] = getBitmapFromResId(R.drawable.bkb);
			pressedRes[0] = getBitmapFromResId(R.drawable.whp);
			pressedRes[1] = getBitmapFromResId(R.drawable.bkp);
		}
				
		scrPanel = getBitmapFromResId(R.drawable.sc_panel);
		scratch = getBitmapFromResId(R.drawable.scratch);
		light_white = getBitmapFromResId(R.drawable.white_light);
		light_red = getBitmapFromResId(R.drawable.red_light);
	}
	
	private Bitmap getBitmapFromResId(int id) {
		return ((BitmapDrawable)getResources().getDrawable(id)).getBitmap();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// change background & draw background
		if (blackpanel) {
			canvas.drawColor(Color.GRAY);
		} else {
			canvas.drawColor(Color.WHITE);
		}		
		
		// draw start
		Bitmap b;
		if (Controller.isStartPressed) {
			b = startRes[1];
		} else {
			b = startRes[0];
		}
		
		canvas.drawBitmap(b, null, Controller.r_start, null);
		
		Bitmap b2;
		if (Controller.isVefxPressed) {
			b2 = vefxRes[1];
		} else {
			b2 = vefxRes[0];
		}
		
		canvas.drawBitmap(b2, null, Controller.r_vefx, null);
		
		// draw buttons
		for (int i=0; i<7; i++) {
			Bitmap b3;
			if (Controller.isButtonPressed[i]) {
				b3 = pressedRes[i%2];
			} else {
				b3 = normalRes[i%2];
			}
			
			canvas.drawBitmap(b3, null, Controller.r_button[i], null);
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
		
		// draw scratch body
		canvas.save();
		canvas.translate(Controller.r_scr.left, Controller.r_scr.top);
		canvas.rotate((float) Controller.mScratchRotation);
		canvas.drawBitmap(scratch, null, new Rect(-(int)(Controller.r_scr.right*ratio_SCR), -(int)(Controller.r_scr.right*ratio_SCR), 
				(int)(Controller.r_scr.right*ratio_SCR), (int)(Controller.r_scr.right*ratio_SCR)), null);
		canvas.restore();
	}
}

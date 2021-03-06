package com.kuna.ibeatcon_android;

import android.graphics.Rect;

public class ControllerSizer {
	public Rect r_start; // x, y, width, height
	public Rect r_vefx; // x, y, width, height
	public Rect r_scr;						// cent_x, cent_y, radius(height), (null)
	public Rect r_button[] = new Rect[7];	// x, y, width(height), height(height)
	private int zoomsize=100;
	
	// presets
	public void Preset_1P_S() {
		r_start = new Rect(0,0,0,0);
		r_vefx = new Rect(0,0,0,0);
		r_scr = new Rect(-10,10,60,0);
		r_button[0] = new Rect(17,55,28,42);
		r_button[1] = new Rect(27,7,28,42);
		r_button[2] = new Rect(37,55,28,42);
		r_button[3] = new Rect(47,7,28,42);
		r_button[4] = new Rect(57,55,28,42);
		r_button[5] = new Rect(67,7,28,42);
		r_button[6] = new Rect(77,55,28,42);
	}
	
	public void Preset_1P_M() {
		r_start = new Rect(87,7,15,15);
		r_vefx = new Rect(87,25,15,15);
		r_scr = new Rect(-10,10,60,0);
		r_button[0] = new Rect(17,55,28,42);
		r_button[1] = new Rect(27,7,28,42);
		r_button[2] = new Rect(37,55,28,42);
		r_button[3] = new Rect(47,7,28,42);
		r_button[4] = new Rect(57,55,28,42);
		r_button[5] = new Rect(67,7,28,42);
		r_button[6] = new Rect(77,55,28,42);
	}
	
	public void Preset_1P_L() {
		r_start = new Rect(87,7,15,15);
		r_vefx = new Rect(87,25,15,15);
		r_scr = new Rect(-12,10,60,0);
		r_button[0] = new Rect(17,55,28,42);
		r_button[1] = new Rect(27,7,28,42);
		r_button[2] = new Rect(37,55,28,42);
		r_button[3] = new Rect(47,7,28,42);
		r_button[4] = new Rect(57,55,28,42);
		r_button[5] = new Rect(67,7,28,42);
		r_button[6] = new Rect(77,55,28,42);
	}
	
	public void Preset_2P_S() {
		r_start = new Rect(0,0,0,0);
		r_vefx = new Rect(0,0,0,0);
		r_scr = new Rect(110,10,60,0);
		r_button[0] = new Rect(7,55,28,42);
		r_button[1] = new Rect(17,7,28,42);
		r_button[2] = new Rect(27,55,28,42);
		r_button[3] = new Rect(37,7,28,42);
		r_button[4] = new Rect(47,55,28,42);
		r_button[5] = new Rect(57,7,28,42);
		r_button[6] = new Rect(67,55,28,42);	
	}
	
	public void Preset_2P_M() {
		r_start = new Rect(4,7,15,15);
		r_vefx = new Rect(4,25,15,15);
		r_scr = new Rect(110,10,50,0);
		r_button[0] = new Rect(7,55,28,42);
		r_button[1] = new Rect(17,7,28,42);
		r_button[2] = new Rect(27,55,28,42);
		r_button[3] = new Rect(37,7,28,42);
		r_button[4] = new Rect(47,55,28,42);
		r_button[5] = new Rect(57,7,28,42);
		r_button[6] = new Rect(67,55,28,42);
	}
	
	public void Preset_2P_L() {
		r_start = new Rect(4,7,15,15);
		r_vefx = new Rect(4,25,15,15);
		r_scr = new Rect(110,10,50,0);
		r_button[0] = new Rect(7,55,28,42);
		r_button[1] = new Rect(17,7,28,42);
		r_button[2] = new Rect(27,55,28,42);
		r_button[3] = new Rect(37,7,28,42);
		r_button[4] = new Rect(47,55,28,42);
		r_button[5] = new Rect(57,7,28,42);
		r_button[6] = new Rect(67,55,28,42);
	}
	
	public void Preset_Keyonly() {
		r_start = new Rect(85,7,15,15);
		r_vefx = new Rect(85,25,15,15);
		r_scr = new Rect(0,0,0,0);
		r_button[0] = new Rect(12,55,28,42);
		r_button[1] = new Rect(22,7,28,42);
		r_button[2] = new Rect(32,55,28,42);
		r_button[3] = new Rect(42,7,28,42);
		r_button[4] = new Rect(52,55,28,42);
		r_button[5] = new Rect(62,7,28,42);
		r_button[6] = new Rect(72,55,28,42);
	}
	
	public void Preset_Scronly() {
		r_start = new Rect(0,0,0,0);
		r_vefx = new Rect(0,0,0,0);
		r_scr = new Rect(53,53,50,0);
		r_button[0] = new Rect(0,0,0,0);
		r_button[1] = new Rect(0,0,0,0);
		r_button[2] = new Rect(0,0,0,0);
		r_button[3] = new Rect(0,0,0,0);
		r_button[4] = new Rect(0,0,0,0);
		r_button[5] = new Rect(0,0,0,0);
		r_button[6] = new Rect(0,0,0,0);
	}
	
	//
	public void SetZoomSize(int val) {
		zoomsize = val;
		
	}
	
	private int SetZoomSizeAttr(int orgval, int zs) {
		return (50 + (orgval-50)*zs/100);
	}
	private int GetZoomSizeVal(int orgval, int zs) {
		return orgval*zs/100;
	}
	
	public Rect GetScrDataRect(int conv_width, int conv_height) {
		Rect r_scr = new Rect();
		r_scr.set(this.r_scr);
		
		// proc zoom size
		r_scr.top = SetZoomSizeAttr(r_scr.top, zoomsize);
		r_scr.left = SetZoomSizeAttr(r_scr.left, zoomsize);
		r_scr.right = GetZoomSizeVal(r_scr.right, zoomsize);
		
		// convert to real size
		r_scr.left *= conv_width/100;
		r_scr.top *= conv_height/100;
		r_scr.right *= conv_height/100;
		
		return r_scr;
		
	}
	
	public Rect GetStartRect(int conv_width, int conv_height) {
		Rect r_start = new Rect();
		r_start.set(this.r_start);
		
		// proc zoom size
		r_start.top = SetZoomSizeAttr(r_start.top, zoomsize);
		r_start.left = SetZoomSizeAttr(r_start.left, zoomsize);
		r_start.right = GetZoomSizeVal(r_start.right, zoomsize);
		r_start.bottom = GetZoomSizeVal(r_start.bottom, zoomsize);
		
		// convert to real size
		r_start.top = r_start.top*conv_height/100;
		r_start.left = r_start.left*conv_width/100;
		r_start.right = r_start.left + r_start.right*conv_height/100;
		r_start.bottom = r_start.top + r_start.bottom*conv_height/100;
		return r_start;
	}
	
	public Rect GetVefxRect(int conv_width, int conv_height) {
		Rect r_vefx = new Rect();
		r_vefx.set(this.r_vefx);
		
		// proc zoom size
		r_vefx.top = SetZoomSizeAttr(r_vefx.top, zoomsize);
		r_vefx.left = SetZoomSizeAttr(r_vefx.left, zoomsize);
		r_vefx.right = SetZoomSizeAttr(r_vefx.right, zoomsize);
		r_vefx.bottom = SetZoomSizeAttr(r_vefx.bottom, zoomsize);
		
		// convert to real size
		r_vefx.top = r_vefx.top*conv_height/100;
		r_vefx.left = r_vefx.left*conv_width/100;
		r_vefx.right = r_vefx.left + r_vefx.right*conv_height/100;
		r_vefx.bottom = r_vefx.top + r_vefx.bottom*conv_height/100;
		return r_vefx;
	}
	
	public Rect GetScrPanelRect(int conv_width, int conv_height) {
		Rect r = GetScrDataRect(conv_width, conv_height);
		r.left -= r.right;
		r.top -= r.right;
		return r;
	}
	
	public Rect[] GetButtonRect(int conv_width, int conv_height) {
		Rect r_button[] = this.r_button.clone();
		
		// proc zoom size
		for (int i=0; i<7; i++) {
			r_button[i].top = SetZoomSizeAttr(r_button[i].top, zoomsize);
			r_button[i].left = SetZoomSizeAttr(r_button[i].left, zoomsize);
			r_button[i].right = GetZoomSizeVal(r_button[i].right, zoomsize);
			r_button[i].bottom = GetZoomSizeVal(r_button[i].bottom, zoomsize);
		}
		
		// convert to real size
		for (int i=0; i<7; i++) {
			r_button[i].top = r_button[i].top*conv_height/100;
			r_button[i].left = r_button[i].left*conv_width/100;
			r_button[i].right = r_button[i].left + r_button[i].right*conv_height/100;
			r_button[i].bottom = r_button[i].top + r_button[i].bottom*conv_height/100;
		}
		
		return r_button;
	}
}

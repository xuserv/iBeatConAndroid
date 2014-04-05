package com.kuna.ibeatcon_android;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class Join extends Activity {
	
	public Handler h;
	private boolean side_mode;
	private boolean scronly_mode;
	private boolean keyonly_mode;
	private boolean bluekey;
	private boolean blackpanel;
	private boolean vb_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i("iBeatCon", "Application Launched");
        
        // attach event
        SharedPreferences setting = getSharedPreferences("settings", MODE_PRIVATE);
        String ip = setting.getString("ip", "");
        String ZoomValue = setting.getString("zoom", "");
        String port = setting.getString("port", "");
        side_mode = setting.getBoolean("side_mode", false);
        scronly_mode = setting.getBoolean("scronly_mode", false);
        keyonly_mode = setting.getBoolean("keyonly_mode", false);
        bluekey = setting.getBoolean("bluekey", false);
        blackpanel = setting.getBoolean("blackpanel", false);
        vb_feedback = setting.getBoolean("feedback", false);
        if (ip != "" | ZoomValue != "" | port != "") {
        		Log.i("iBeatCon", "Connecting to Server");
        		Log.i("iBeatCon", "IP Address : " + ip);
        		Log.i("iBeatCon", "Zoom Value : " + ZoomValue);
        		Log.i("iBeatCon", "Server Port: "+ port);
        		ConCommon.keyonly = keyonly_mode;
        		ConCommon.scronly = scronly_mode;
        		ConCommon.is2P = side_mode;
        		ConCommon.zoomval = Integer.parseInt(ZoomValue);
        		ConCommon.cc = new ConClient(ip, Integer.parseInt(port));
        		CanvasView.bluekey = bluekey;
        		CanvasView.blackpanel = blackpanel;
        		Controller.vb_feedback = vb_feedback;
        } else {
        		Log.i("iBeatCon", "First Run");
        		Toast.makeText(getApplicationContext(), getString(R.string.str_firstrun), Toast.LENGTH_SHORT).show();       		
        		startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        		finish();
        }
        
        h = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		if (msg.what == 1) {
    				// start activity
        			Log.i("iBeatCon", "Connected.");
    				ConCommon.controller = new Controller();
    				startActivity(new Intent(getApplicationContext(), Controller.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    				finish();
        		}
        		if (msg.what == -1) {
        			Log.i("iBeatCon", "Cannot Connect into Server");
        			Toast.makeText(getApplicationContext(), getString(R.string.str_connfail), Toast.LENGTH_SHORT).show();
        			startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        			finish();
        		}
        		super.handleMessage(msg);
        	}
        };
        
        ConCommon.HandlerStack.add(h);
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

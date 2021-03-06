package com.kuna.ibeatcon_android;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Join extends Activity {
		
	public Handler h;
	private boolean side_mode;
	private boolean scronly_mode;
	private boolean keyonly_mode;
	private String port2;
	Context context = this; // for trick

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // attach event
        SharedPreferences setting = getSharedPreferences("settings", MODE_PRIVATE);
        String ip = setting.getString("ip", "");
        String ZoomValue = setting.getString("zoom", "");
        String mode_sel = setting.getString("mode_sel", "");
        
        if (mode_sel.equals("side_mode")) {
        	side_mode = true;
        } else if (mode_sel.equals("scronly_mode")) {
        	scronly_mode = true;
        } else if (mode_sel.equals("keyonly_mode")) {
        	keyonly_mode = true;
        }
        
        if (setting.getBoolean("port", true)) {
        	port2 = "10070";
        } else {
        	port2 = "2001";
        }
        
        if (ip != "" | ZoomValue != "" | mode_sel != "") {
        		ConCommon.keyonly = keyonly_mode;
        		ConCommon.scronly = scronly_mode;
        		ConCommon.is2P = side_mode;
        		
        		// if zoomvalue is string, clear preferences.
        		try {
        			ConCommon.zoomval = Integer.parseInt(ZoomValue);
        		} catch (NumberFormatException e) {
        			SharedPreferences.Editor setting_edit = setting.edit();
        			setting_edit.clear();
        			setting_edit.commit();
        			Toast.makeText(getApplicationContext(), getString(R.string.str_error_msg), Toast.LENGTH_SHORT).show();   
        			finish();
        		}
        		
        		ConCommon.cc = new ConClient(ip, Integer.parseInt(port2));
        		CanvasView.bluekey = setting.getBoolean("bluekey", false);
        		CanvasView.blackpanel = setting.getBoolean("blackpanel", false);
        		Controller.vb_feedback = setting.getBoolean("feedback", false);
        		Controller.touch_scratch = setting.getBoolean("touch_scratch", false);
        } else {
        		Toast.makeText(getApplicationContext(), getString(R.string.str_firstrun), Toast.LENGTH_SHORT).show();       		
        		startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        		finish();
        }
        
        h = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		if (msg.what == 1) {
    				// start activity
    				ConCommon.controller = new Controller();
    				startActivity(new Intent(getApplicationContext(), Controller.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    				finish();
        		} else if (msg.what == -1) {
        			if (!((Activity)context).isFinishing()) { // is activity dead avoid trick
            			h.removeMessages(msg.what);
            			AlertDialog.Builder builder = new AlertDialog.Builder(Join.this);
            			builder.setTitle(getString(R.string.title_connfail)).setIcon(android.R.attr.alertDialogIcon).setMessage(getString(R.string.content_connfail)).setCancelable(false)
            			
            			.setPositiveButton(getString(R.string.action_reconnect), new DialogInterface.OnClickListener() {    
            			    @Override    
            			    public void onClick(DialogInterface dialog, int id) {    
            			    	startActivity(new Intent(getApplicationContext(), Join.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            	    			finish();
            			    }    
            			})    
            			  
            			.setNeutralButton(getString(R.string.action_settings), new DialogInterface.OnClickListener() {    
            				@Override    
            				public void onClick(DialogInterface dialog, int id) {    
            					startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            	    			finish();
            				}    
            			})
            			  
            			.setNegativeButton(getString(R.string.action_exit), new DialogInterface.OnClickListener() {    
            			    @Override
            			    public void onClick(DialogInterface dialog, int id) {
            			    	finish();
            			    }    
            			}).show();
        	        }
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

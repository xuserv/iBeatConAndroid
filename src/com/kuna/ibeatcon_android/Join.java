package com.kuna.ibeatcon_android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Join extends Activity {
	
	public Handler h;
	private boolean side_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // attach event
        final Activity _this = this;
        SharedPreferences setting = getSharedPreferences("settings", MODE_PRIVATE);
        String ip = setting.getString("ip", "");
        String ZoomValue = setting.getString("zoom", "");
        side_mode = setting.getBoolean("side_mode", false);
        if (ip != "") {
        		Log.i("IP Address : ", ip);
        		Log.i("Zoom Value : ", ZoomValue);
        		ConCommon.is2P = side_mode;
        		ConCommon.zoomval = Integer.parseInt(ZoomValue);
        		ConCommon.cc = new ConClient(ip, 2001);
        } else {
        		Toast.makeText(getApplicationContext(), "[FIRST RUN]" + "\r\n" + "Please Setting Your iBeatCon!", Toast.LENGTH_SHORT).show();
        		Intent intent = new Intent(getApplicationContext(), Settings.class);
        		startActivity(intent);
        		finish();
        }
        
        h = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		if (msg.what == 1) {
    				// start activity
    				ConCommon.controller = new Controller();
    				Intent i = new Intent(_this, Controller.class);
    				startActivity(i);
    				finish();
        		}
        		if (msg.what == -1) {
        			Toast.makeText(getApplicationContext(), "[!] Cannot Conntect into iBeatCon Server" + "\r\n" + "Please Make Sure Server Program is Currently Running" + "\r\n" + "or Your IP has been changed.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    startActivity(intent);
                    finish();
        		}
        		super.handleMessage(msg);
        	}
        };
        
        ConCommon.HandlerStack.add(h);
    }
}

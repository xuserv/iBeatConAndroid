package com.kuna.ibeatcon_android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {
	
	private boolean side_mode;
	private boolean keyonly_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        
        final SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor settings2 = settings.edit();
        final CheckBox cb = (CheckBox) findViewById(R.id.cb_2p);
        final CheckBox cb2 = (CheckBox) findViewById(R.id.cb_ko);
        final TextView t = (TextView) findViewById(R.id.edit_address);
        final TextView zv = (TextView) findViewById(R.id.edit_zoom);
        
        side_mode = settings.getBoolean("side_mode", false);
        keyonly_mode = settings.getBoolean("keyonly_mode", false);
        
        if (keyonly_mode == true) {
        	cb2.toggle();
        }
        
        if (side_mode == true) {
        	cb.toggle();
        }
        
        String ip = settings.getString("ip", "");
        t.setText(ip);
        Button b = (Button) findViewById(R.id.btn_join);
        b.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
                String ip_input = t.getText().toString();
                settings2.putString("ip", ip_input);
                                
                String ZoomValue = zv.getText().toString();
        		settings2.putString("zoom", ZoomValue);
        		
        		if (cb.isChecked()) {
        			settings2.putBoolean("side_mode", true);        			
        		} else {
        			settings2.putBoolean("side_mode", false);
        		}
        		
        		if (cb2.isChecked()) {
        			settings2.putBoolean("keyonly_mode", true);
        		} else {
        			settings2.putBoolean("keyonly_mode", false);
        		}
        		
        		settings2.commit();
        		finish();
			}
		});
    }
}

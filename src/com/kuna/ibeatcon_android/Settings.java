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
	
	public Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        
        final SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor settings2 = settings.edit();
        
        String ip = settings.getString("ip", "");
        final TextView t = (TextView) findViewById(R.id.edit_address);
        t.setText(ip);
        Button b = (Button) findViewById(R.id.btn_join);
        b.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
                String ip_input = t.getText().toString();
                settings2.putString("ip", ip_input);
                settings2.commit();
                
                TextView zv = (TextView) findViewById(R.id.edit_zoom);
                String ZoomValue = zv.getText().toString();
        		settings2.putString("zoom", ZoomValue);
        		settings2.commit();
        		
        		CheckBox cb = (CheckBox) findViewById(R.id.cb_2p);
        		if (cb.isChecked()) {
        			settings2.putBoolean("side_mode", true);
        			settings2.commit();
        		} else {
        			settings2.putBoolean("side_mode", false);
        			settings2.commit();
        		}
        		
        		finish();
			}
		});
    }
}

package com.kuna.ibeatcon_android;

import com.google.analytics.tracking.android.EasyTracker;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {
	
	private boolean side_mode;
	private boolean scronly_mode;
	private boolean keyonly_mode;
	private boolean bluekey;
	private boolean blackpanel;
	private boolean vb_feedback;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        final SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor settings2 = settings.edit();        
        final CheckBox cb1 = (CheckBox)findViewById(R.id.cb_bp);
        final CheckBox cb2 = (CheckBox)findViewById(R.id.cb_ns);
        final CheckBox cb3 = (CheckBox)findViewById(R.id.cb_bk);
        final CheckBox cb4 = (CheckBox)findViewById(R.id.cb_vb);
        final TextView t = (TextView)findViewById(R.id.edit_address);
        final TextView zv = (TextView)findViewById(R.id.edit_zoom);
        final TextView ci = (TextView)findViewById(R.id.client_id);
        final Spinner mode_select = (Spinner)findViewById(R.id.mode_select);
        final ArrayAdapter<CharSequence> Mode = ArrayAdapter.createFromResource(this, R.array.array_list, android.R.layout.simple_spinner_item);
        
        String ip = settings.getString("ip", "");
        side_mode = settings.getBoolean("side_mode", false);
        keyonly_mode = settings.getBoolean("keyonly_mode", false);
        scronly_mode = settings.getBoolean("scronly_mode", false);
        bluekey = settings.getBoolean("bluekey", false);
        blackpanel = settings.getBoolean("blackpanel", false);
        vb_feedback = settings.getBoolean("feedback", false);
        String new_server = settings.getString("port", "");
        
        Mode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode_select.setAdapter(Mode);
        
        t.setText(ip);
        if (ConClient.msg != null) {
        	ci.setText(getString(R.string.str_ci) + " " + ConClient.msg + ".");
        }
               
        if (side_mode) {
        	mode_select.setSelection(1);
        }
        
        if (keyonly_mode) {
        	mode_select.setSelection(2);
        }
        
        if (scronly_mode) {
        	mode_select.setSelection(3);
        }
        
        if (blackpanel) {
        	cb1.toggle();
        }       
        
        if (new_server.equals("10070")) {
        	cb2.toggle();
        }
        
        if (bluekey) {
        	cb3.toggle();
        }
        
        if (vb_feedback) {
        	cb4.toggle();
        }

        Button b = (Button) findViewById(R.id.btn_join);
        b.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
                String ip_input = t.getText().toString();
                settings2.putString("ip", ip_input);
                                
                String ZoomValue = zv.getText().toString();
        		settings2.putString("zoom", ZoomValue);
        		
        		if (mode_select.getSelectedItemPosition() == 1) {
                	settings2.putBoolean("side_mode", true);
                } else {
                	settings2.putBoolean("side_mode", false);
                } 
        		if (mode_select.getSelectedItemPosition() == 2) {
                	settings2.putBoolean("keyonly_mode", true);
                } else {
                	settings2.putBoolean("keyonly_mode", false);
                }
        		if (mode_select.getSelectedItemPosition() == 3) {
                	settings2.putBoolean("scronly_mode", true);
                } else {
                	settings2.putBoolean("scronly_mode", false);
                }
        		
        		if (cb1.isChecked()) {
        			settings2.putBoolean("blackpanel", true);
        		} else {
        			settings2.putBoolean("blackpanel", false);
        		}
        		
        		if (cb2.isChecked()) {
        			settings2.putString("port", "10070");
        		} else {
        			settings2.putString("port", "2001");
        		}
        		
        		if (cb3.isChecked()) {
        			settings2.putBoolean("bluekey", true);
        		} else {
        			settings2.putBoolean("bluekey", false);
        		}
        		
        		if (cb4.isChecked()) {
        			settings2.putBoolean("feedback", true);
        		} else {
        			settings2.putBoolean("feedback", false);
        		}
        			
        		settings2.commit();
        		Toast.makeText(getApplicationContext(), getString(R.string.str_save_msg), Toast.LENGTH_SHORT).show();
        		startActivity(new Intent(getApplicationContext(), Join.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        		finish();
			}
		});
        
        Button b2 = (Button) findViewById(R.id.btn_reset);
        b2.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        			settings2.clear();
        			settings2.commit();
        			Toast.makeText(getApplicationContext(), getString(R.string.str_reset_msg), Toast.LENGTH_SHORT).show();
        			startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        			finish();
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.settings, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.help:
    		Log.i("iBeatCon", "Help");
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://xuserv.net/ibc/")));
    	default:
    		return super.onOptionsItemSelected(item);
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

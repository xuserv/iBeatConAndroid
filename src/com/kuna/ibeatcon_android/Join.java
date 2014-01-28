package com.kuna.ibeatcon_android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Join extends Activity {
	
	public Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        
        // attach event
        final Activity _this = this;
        SharedPreferences setting = getSharedPreferences("settings", MODE_PRIVATE);
        String ip = setting.getString("ip", "");
        if (ip != "") {
                TextView t = (TextView)findViewById(R.id.edit_address);
                t.setText(ip);
        } else {
                Toast.makeText(getApplicationContext(), "Please Enter Your PC IP Address in Textbox.", Toast.LENGTH_SHORT).show();             
        }
        final TextView t = (TextView) findViewById(R.id.edit_address);
        Button b = (Button) findViewById(R.id.btn_join);
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                String ip_input = t.getText().toString();
                SharedPreferences ip_save = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = ip_save.edit();
                editor.putString("ip", ip_input);
                editor.commit();
				// network initalize
				if (!ConCommon.debug_noconnect)
					ConCommon.cc = new ConClient(t.getText().toString(), 2001);
				else
					h.obtainMessage(1, 0, 0, 0).sendToTarget();
			}
		});
        
        h = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		if (msg.what == 1) {
        			CheckBox cb = (CheckBox) findViewById(R.id.cb_2p);
        			ConCommon.is2P = cb.isChecked();
        			TextView zv = (TextView) findViewById(R.id.edit_zoom);
        			ConCommon.zoomval = Integer.parseInt(zv.getText().toString());
        			
    				// start activity
    				ConCommon.controller = new Controller();
    				Intent i = new Intent(_this, Controller.class);
    				startActivity(i);
        		}
        		if (msg.what == -1) {
					AlertDialog.Builder alert = new AlertDialog.Builder(_this);
					alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					alert.setMessage("연결할 수 없습니다");
					alert.show();
        		}
        		super.handleMessage(msg);
        	}
        };
        
        ConCommon.HandlerStack.add(h);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.join, menu);
        return true;
    }
    
}

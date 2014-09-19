package com.kuna.ibeatcon_android;

import com.google.analytics.tracking.android.EasyTracker;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;

public class Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        
        TextView ver = (TextView)findViewById(R.id.app_version);
        TextView dev = (TextView)findViewById(R.id.textView2);
        TextView main = (TextView)findViewById(R.id.textView4);
        TextView orig = (TextView)findViewById(R.id.textView6);
        TextView man = (TextView)findViewById(R.id.textView8);
        
		try {
			String version = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			ver.setText(version);
		} catch (NameNotFoundException e) {
			// NOOOOOOOOOO, IT CAN'T BE! YOUR DEVICE DENIED GET PACKAGE NAME FROM YOUR DEVICE!
		}

        dev.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/lazykuna")));
        	}
        });
        
        main.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/xuserv")));
        	}
        });
        
        orig.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Harunene")));
        	}
        });
        
        man.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://xuserv.net/ibc")));
        	}
        });
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

package com.kuna.ibeatcon_android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class ConClient {
	private InetSocketAddress s;
	private Socket s2;
	public boolean Initalized = false;
	public String _msg;
	private BufferedReader br;
	private BufferedWriter bw;
	public static String msg;
	
	public ConClient(String ip, int port) {
		Connect(ip, port);
	}
	
	public void Connect(final String ip, final int port) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s = new InetSocketAddress(ip, port);
					s2 = new Socket();
					s2.connect(s, 3000);
					s2.setTcpNoDelay(true);
					
					// After Initalization, Run recv thread & activate send method
					br = new BufferedReader( new InputStreamReader(s2.getInputStream()) );
					bw = new BufferedWriter ( new OutputStreamWriter(s2.getOutputStream()));
					
					StartReadThread();

					ConCommon.SendMessage(1);
				} catch (UnknownHostException e) {
					_msg = "Invalid IP Address";
					Initalized = false;
					Log.e("iBeatCon", _msg);
					e.printStackTrace();
					ConCommon.SendMessage(-1);
					return;
				} catch (Exception e) {
					_msg = e.getMessage();
					Initalized = false;
					e.printStackTrace();
					Log.e("iBeatCon", _msg);
					ConCommon.SendMessage(-1);
					return;
				}
				Initalized = true;
			}
		}).start();
	}
	
	public void Send(int val) {
		if (!Initalized) return;
		
		try {
			bw.write(val);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void StartReadThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (Initalized) {
					try {
						msg = br.readLine();
						if (msg==null) return;
						Log.i("iBeatCon", "CONNECTION "+ msg);
					} catch (Exception e) {
						e.printStackTrace();
						Close();
						break;
					}
				}
			}
		}).start();
	}
	
	public boolean Close() {
		if (!Initalized) return false;
		try {
			Initalized = false;
			bw.close();
			br.close();
			s2.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}

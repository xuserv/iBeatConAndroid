package com.kuna.ibeatcon_android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class ConClient {
	private Socket s;
	public boolean Initalized = false;
	public String _msg;
	
	private BufferedReader br;
	private BufferedWriter bw;
	
	public ConClient(String ip, int port) {
		Connect(ip, port);
	}
	
	public void Connect(final String ip, final int port) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s = new Socket(ip, port);
					s.setTcpNoDelay(true);
					
					// After Initalization, Run recv thread & activate send method
					br = new BufferedReader( new InputStreamReader(s.getInputStream()) );
					bw = new BufferedWriter ( new OutputStreamWriter(s.getOutputStream()));

					ConCommon.SendMessage(1);
				} catch (UnknownHostException e) {
					_msg = "유효하지 않은 주소입니다.";
					Initalized = false;
					e.printStackTrace();
					ConCommon.SendMessage(-1);
					return;
				} catch (Exception e) {
					_msg = e.getMessage();
					Initalized = false;
					e.printStackTrace();
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
				String s="";
				while (Initalized) {
					try {
						s = br.readLine();
						if (s==null) return;
						Log.i("CONNECTION", s);
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
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}

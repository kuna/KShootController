package com.kuna.kshootcontroller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class ConClient {

	private InetSocketAddress s;
	private static Socket s2;
	private static BufferedReader br;
	private static BufferedWriter bw;
	public static boolean Initalized = false;
	public static final int BUFFER_SIZE = 1000;
	public static String msg = null;
	public String _msg;

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
					s2.connect(s, 1500);
					s2.setTcpNoDelay(true);

					// After Initalization, Run recv thread & activate send method
					br = new BufferedReader(new InputStreamReader(s2.getInputStream()));
					bw = new BufferedWriter(new OutputStreamWriter(s2.getOutputStream()));

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
						String recvdata = "";
			            int charsRead = 0;
			            char[] buffer = new char[BUFFER_SIZE];

			            while ((charsRead = br.read(buffer)) != -1) {
			            	// check is pingpong ...?
			            	if ((int)buffer[0] == 100) {
			            		Log.i("PINGPONG", "Got PING signal(100)");
			            		Send(100);
			            		continue;
			            	}
			            	
			                recvdata += new String(buffer).substring(1, charsRead);
			                msg = recvdata.trim().replaceAll("\n|(null)", "");
			            }

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

	public static void Close() {
		if (!Initalized) {
			// Do Nothing
		} else {
			try {
				Initalized = false;
				bw.close();
				br.close();
				s2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
}

package com.jay.serialport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;
import android_serialport_api.SerialPort;

public class SerialRead extends ISerialPortBase {
	
	@Override
	public void init(File device, int baudrate, int flag) {
		super.init(device, baudrate, flag);
		mControlThread = new ReadThread();
	}

	private class ReadThread extends Thread {
		@Override
		public void run() {
			while(mIsRunning) {
				int size = 0;
				try {
					byte[] buffer = new byte[1024];
					if (mInputStream == null) return;
					if(mInputStream.available() != 0){
						size = mInputStream.read(buffer);
					}else{
						try {
							sleep(10);
							continue;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (size > 0) {
						onDataReceived(buffer, size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			Log.d(TAG, "ReadThread end ");
		}
	}
}

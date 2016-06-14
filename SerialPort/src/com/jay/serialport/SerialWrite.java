package com.jay.serialport;

import java.io.File;
import java.io.IOException;

import android.util.Log;

public class SerialWrite extends ISerialPortBase{
	private byte[] mBuffer;
	private Byte mSyncObject = new Byte("0");
	@Override
	public void init(File device, int baudrate, int flag) {
		super.init(device, baudrate, flag);
		mControlThread = new SendingThread();
	}
	
	@Override
	public void writeSerial(String data) {
		if(DEBUG){
			Log.d(TAG, "writeSerial data : " + new String(data));
		}
	}

	private class SendingThread extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				Log.d(TAG, "SendingThread running");
				try {
					if (mOutputStream != null && mBuffer != null) {
						Log.d(TAG, "SendingThread write mBuffer");
						mOutputStream.write(mBuffer);
						mBuffer =null;
					} else {
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
}

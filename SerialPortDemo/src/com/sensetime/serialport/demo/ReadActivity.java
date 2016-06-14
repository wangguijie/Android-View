/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.sensetime.serialport.demo;

import java.io.File;

import com.jay.serialport.ISerialPort;
import com.jay.serialport.ISerialPortBase.IDataCallBack;
import com.jay.serialport.SerialRead;
import com.jay.serialport.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class ReadActivity extends Activity implements IDataCallBack {

	private static final String TAG = "ReadActivity";
	private static final String READ_DEVICE = "/dev/ttyS3";
	SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	ISerialPort mSerialRead;
	byte[] mBuffer = new byte[1024];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console);
		String[] devices = mSerialPortFinder.getAllDevicesPath();
		//String[] devices = mSerialPortFinder.getAllDevices();
		int index = 0;
		boolean hasRead = false;
		for(String str : devices){
			Log.d("MainActivity", "device" + (index++) + " : " + str);
			if(READ_DEVICE.equals(str)){
				hasRead = true;
			}
		}
		if(!hasRead ){
			throw new RuntimeException();
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		mSerialRead = new SerialRead();
		mSerialRead.init(new File(READ_DEVICE), 9600,0);
		mSerialRead.setDataReceivedCallBack(this);
		mSerialRead.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		mSerialRead.release();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDataReceived(SerialPort serialPort, String data) {
		Log.d(TAG, "onDataReceived data : " + data);
		mSerialRead.writeSerial(data);
	}
}

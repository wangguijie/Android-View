package com.jay.serialport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jay.user.UserCardUtil;

import android.util.Log;
import android_serialport_api.SerialPort;

public class ISerialPortBase implements ISerialPort{
	protected static final boolean DEBUG = true;
	protected static final String TAG = "ISerialPortBase";
	protected static final int SERIAL_LENGTH = 8;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	protected InputStream mInputStream;
	protected Thread mControlThread;
	protected IDataCallBack mDataCallBack;
	private String mStrBuffer = new String();
	private String mTempData;
	protected boolean mIsRunning = false;
	public interface IDataCallBack{
		public void onDataReceived(SerialPort serialPort,String data);
	}
	
	@Override
	public void init(File device,int baudrate,int flag) {
		try {
			mSerialPort = new SerialPort(device, baudrate, flag);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
		} catch (SecurityException e) {
			throw new RuntimeException("You do not have read/write permission to the serial port.");
		} catch (IOException e) {
			throw new RuntimeException("The serial port can not be opened for an unknown reason.");
		}
	}

	@Override
	public void start() {
		if(mControlThread == null){
			Log.e(TAG, "start error, you have not init mControlThread");
			return;
		}
		if(mControlThread.isAlive()){
			Log.w(TAG, "start warning, mControlThread isAlive");
			return ;
		}
		mIsRunning = true;
		mControlThread.start();
	}

	@Override
	public void release() {
		closeSerialPort();
		releaseThread();
		releaseStream();
	}
	
	private void releaseStream(){
		if(mOutputStream != null){
			try {
				mOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mOutputStream = null;
		if(mInputStream != null){
			try {
				mInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mInputStream = null;
	}
	
	private void releaseThread(){
		try{
			mControlThread.stop();
		}catch(Exception e){
			
		}
		if (mControlThread != null){
			mIsRunning = false;
			mControlThread = null;
		}
	}
	
	public void setDataReceivedCallBack(IDataCallBack callback){
		mDataCallBack = callback;
	}
	
	public synchronized void onDataReceived(final byte[] buffer, final int size){
		if(DEBUG){
			Log.d(TAG, "onDataReceived data : " + new String(buffer,0,size) );
		}
		mTempData = UserCardUtil.convertStringToHex(new String(buffer,0,size));
		if(DEBUG){
			Log.d(TAG, "onDataReceived ---- 0 ---- mTempData : " + mTempData );
		}
		if(mTempData == null || "".equals(mTempData)){
			Log.e(TAG, "mTempData == null ");
			return;
		}
		mStrBuffer = mStrBuffer.concat(mTempData);
		if(DEBUG){
			Log.d(TAG, "onDataReceived ---- 1 ---- mStrBuffer : " + mStrBuffer );
		}
		if(!mStrBuffer.toString().endsWith("da")){
			return;
		}
		mStrBuffer = mStrBuffer.substring(0, mStrBuffer.length()-2);
		if(DEBUG){
			Log.d(TAG, "onDataReceived ---- 2 ---- mStrBuffer : " + mStrBuffer );
		}
		mTempData = UserCardUtil.convertHexToString(mStrBuffer.toString());
		mStrBuffer = "";
		if(DEBUG){
			Log.d(TAG, "onDataReceived ---- 3 ---- mStrBuffer : " + mStrBuffer );
		}
		if(mDataCallBack == null){
			return;
		}
		mDataCallBack.onDataReceived(mSerialPort, mTempData);
	}
	
	public void writeSerial(String data){
		if(mOutputStream == null){
			Log.w(TAG, "writeSerial data mOutputStream is null !");
			return;
		}
		if(data == null || "".equals(data)){
			Log.w(TAG, "writeSerial data is null !");
			return;
		}
		char[] text = new char[data.length()];
		for (int i=0; i<data.length(); i++) {
			text[i] = data.charAt(i);
		}
		try {
			mOutputStream.write(new String(data).getBytes());
			mOutputStream.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}

	@Override
	public InputStream getInputStream() {
		return mInputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		return mOutputStream;
	}
}

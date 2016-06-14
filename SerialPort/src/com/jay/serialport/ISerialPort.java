package com.jay.serialport;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.jay.serialport.ISerialPortBase.IDataCallBack;

public interface ISerialPort {
	public void closeSerialPort();
	public void init(File device,int baudrate,int flag);
	public void setDataReceivedCallBack(IDataCallBack callback);
	public void start();
	public InputStream getInputStream();
	public OutputStream getOutputStream();
	public void release();
	public void writeSerial(String data);
}

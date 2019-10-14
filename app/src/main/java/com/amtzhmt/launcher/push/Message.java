package com.amtzhmt.launcher.push;

import java.net.SocketAddress;


public final class Message {
	
	public static int version = 11;
	public static final int SERVER_MESSAGE_MIN_LENGTH = 5;
	public static final int CLIENT_MESSAGE_MIN_LENGTH = 50;
	public static final int CMD_0x00 = 0;//心跳包
	public static final int CMD_0x10 = 16;//通用信息
	
	public static final int CMD_0x20 = 32;//自定义信息
	
	protected SocketAddress address;
	protected byte[] data1;
	protected String data;
	
	public Message(SocketAddress address, byte[] data){
		this.address = address;
		this.data1 = data;
	}
	public Message(SocketAddress address, String data){
		this.address = address;
		this.data = data;
	}
	
//	public int getContentLength(){
//		return (int)ByteBuffer.wrap(data, SERVER_MESSAGE_MIN_LENGTH - 2, 2).getChar();
//	}
	
//	public int getCmd(){
//		byte b = data[2];
//		return b & 0xff;
//	}
	
//	public boolean checkFormat(){
//		if(address == null || data == null || data.length < Message.SERVER_MESSAGE_MIN_LENGTH){
//			return false;
//		}
//		int cmd = getCmd();
//		System.out.println("getCmd-->"+cmd);
//		if(cmd != CMD_0x00
//				&& cmd != CMD_0x10
//				&& cmd != CMD_0x20){
//			return false;
//		}
//		int dataLen = getContentLength();
//		if(data.length != dataLen + SERVER_MESSAGE_MIN_LENGTH){
//			return false;
//		}
//		if(cmd ==  CMD_0x10 && dataLen != 0){
//			return false;
//		}
//
//
//
//		if(cmd ==  CMD_0x20 && dataLen < 1){//must has content
//			return false;
//		}
//		return true;
//	}

	public void setData(String data1){
		this.data = data;
	}

	public String getData(){
		return this.data;
	}
	
	public void setData1(byte[] data1){
		this.data1 = data1;
	}
	
	public byte[] getData1(){
		return this.data1;
	}
	
	public void setSocketAddress(SocketAddress address){
		this.address = address;
	}
	
	public SocketAddress getSocketAddress(){
		return this.address;
	}
	
	public static void setVersion(int v){
		if(v < 1 || v > 255){
			return;
		}
		version = v;
	}
	
	public static int getVersion(){
		return version;
	}

}

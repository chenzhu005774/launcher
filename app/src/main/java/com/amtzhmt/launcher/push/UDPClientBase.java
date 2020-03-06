package com.amtzhmt.launcher.push;


import com.amtzhmt.launcher.util.utils.LogUtils;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import de.javawi.jstun.attribute.MessageAttribute;
import de.javawi.jstun.attribute.Username;
import de.javawi.jstun.header.MessageHeader;
import de.javawi.jstun.header.MessageHeaderInterface;
import de.javawi.jstun.util.Utility;


public abstract class UDPClientBase implements Runnable {
	
	protected DatagramSocket ds;
	protected long lastSent = 0;
	protected long lastReceived = 0;
	protected int remotePort = 9966;
	protected int appid = 1;
	protected String uuid;
	protected String remoteAddress = null;
	protected ConcurrentLinkedQueue<Message> mq = new ConcurrentLinkedQueue<Message>();
	
	protected AtomicLong queueIn = new AtomicLong(0);
	protected AtomicLong queueOut = new AtomicLong(0);

	protected int bufferSize = 1024;
	protected int heartbeatInterval = 50;
	
	protected byte[] bufferArray;
	protected ByteBuffer buffer;
	protected boolean needReset = true;
	
	protected boolean started = false;
	protected boolean stoped = false;
	
	protected Thread receiverT;
	protected Worker worker;
	protected Thread workerT;
	
	private long sentPackets;
	private long receivedPackets;



	
	public UDPClientBase(String uuid, int appid, String serverAddr, int serverPort) throws Exception{

		if(appid < 1 || appid > 255){
			throw new IllegalArgumentException("appid must be from 1 to 255");
		}
		if(serverAddr == null || serverAddr.trim().length() == 0){
			throw new IllegalArgumentException("server address illegal: "+serverAddr);
		}

		this.uuid = uuid;
		this.appid = appid;
		this.remoteAddress = serverAddr;
		this.remotePort = serverPort;
	}
	
	protected boolean enqueue(Message message){
		boolean result = mq.add(message);
		if(result == true){
			queueIn.addAndGet(1);
		}
		return result;
	}
	
	protected Message dequeue(){
		Message m = mq.poll();
		if(m != null){
			queueOut.addAndGet(1);
		}
		return m;
	}
	
	private synchronized void init(){
		bufferArray = new byte[bufferSize];
		buffer = ByteBuffer.wrap(bufferArray);
	}
	
	protected synchronized void reset() throws Exception{
		if(needReset == false){
			return;
		}

		if(ds != null){
			try{ds.close();}catch(Exception e){}
		}
		if(hasNetworkConnection() == true){
			ds = new DatagramSocket();
			ds.connect(new InetSocketAddress(remoteAddress,remotePort));
           LogUtils.i("Reuseaddr is enable-->"+ds.getReuseAddress());
			needReset = false;
		}else{
			try{Thread.sleep(1000);}catch(Exception e){}
		}
	}
	
	public synchronized void start() throws Exception{
		if(this.started == true){
			return;
		}
		this.init();
		LogUtils.i("UDP  start login");
		
		receiverT = new Thread(this,"udp-client-receiver");
		receiverT.setDaemon(true);
		synchronized(receiverT){
			receiverT.start();
			receiverT.wait();
		}
		
		worker = new Worker();
		workerT = new Thread(worker,"udp-client-worker");
		workerT.setDaemon(true);
		synchronized(workerT){
			workerT.start();
			workerT.wait();
		}
		
		this.started = true;
	}
	
	public void stop() throws Exception{
		stoped = true;
		if(ds != null){
			try{ds.close();}catch(Exception e){}
			ds = null;
		}
		if(receiverT != null){
			try{receiverT.interrupt();}catch(Exception e){}
		}

		if(workerT != null){
			try{workerT.interrupt();}catch(Exception e){}
		}
	}

	@Override
	public void run(){
		
		synchronized(receiverT){
			receiverT.notifyAll();
		}
		
		while(stoped == false){
			try{
				if(hasNetworkConnection() == false){
					try{
						trySystemSleep();
						Thread.sleep(1000);
					}catch(Exception e){}
					continue;
				}
				reset();
				heartbeat();
				receiveData();
			}catch(java.net.SocketTimeoutException e){
				
			}catch(Exception e){
				e.printStackTrace();
				this.needReset = true;
			}catch(Throwable t){
				t.printStackTrace();
				this.needReset = true;
			}finally{
				if(needReset == true){
					try{
						trySystemSleep();
						Thread.sleep(1000);
					}catch(Exception e){}
				}
				if(mq.isEmpty() == true || hasNetworkConnection() == false){
					try{
						trySystemSleep();
						Thread.sleep(1000);
					}catch(Exception e){}
				}
			}
		}
		if(ds != null){
			try{ds.close();}catch(Exception e){}
			ds = null;
		}
	}
	private void heartbeat() throws Exception{
		if(System.currentTimeMillis() - lastSent < heartbeatInterval * 1000){
			return;
		}
		byte[] buffer = new byte[Message.CLIENT_MESSAGE_MIN_LENGTH];
//		ByteBuffer.wrap(buffer).put(uuid);
		send(uuid);
	}

	
	private void receiveData() throws Exception{
		DatagramPacket dp = new DatagramPacket(bufferArray, bufferArray.length);
//		dp.setPort(12580);
		ds.setSoTimeout(5*1000);
		ds.receive(dp);

		if(dp.getLength() <= 0 || dp.getData() == null || dp.getData().length == 0){
			return;
		}
        byte[] data = dp.getData();

		byte[] typeArray = new byte[2];
		System.arraycopy(data, 0, typeArray, 0, 2);
		int type = Utility.twoBytesToInteger(typeArray);
		JSONObject jSONObject = new JSONObject(new String(data,"UTF-8"));
		LogUtils.i("get message"+type+"  typeArray:"+typeArray+" data:"+jSONObject.toString());
        Message msg = new Message(dp.getSocketAddress(),jSONObject.toString());
        this.enqueue(msg);
		worker.wakeup();
	}
	
	private void ackServer(Message m) throws Exception{
			byte[] buffer = new byte[Message.CLIENT_MESSAGE_MIN_LENGTH];
            send(uuid);
	}
	
	private void send(String data) throws Exception{
		if(data == null){
			return;
		}
		if(ds == null){
			return;
		}

        MessageHeader messageHeader = new MessageHeader(MessageHeaderInterface.MessageHeaderType.BindingRequest);
        messageHeader.setTransactionID(uuid.getBytes());
		LogUtils.i("length--->"+new String(data).trim());
        MessageAttribute info =new Username(data);
        messageHeader.addMessageAttribute(info);
        byte[] sendData = data.getBytes();
//        byte[] sendData = messageHeader.getBytes();
		LogUtils.i("length--->sendData"+new String(sendData).trim());

		DatagramPacket dp = new DatagramPacket(sendData,sendData.length);
		dp.setSocketAddress(ds.getRemoteSocketAddress());
		ds.send(dp);
		lastSent = System.currentTimeMillis();
		this.sentPackets++;
		LogUtils.i("ds.getLocalPort()--?"+"--getRemoteSocketAddress-->"+ds.getRemoteSocketAddress());
	}
	
	public long getSentPackets(){
		return this.sentPackets;
	}
	
	public long getReceivedPackets(){
		return this.receivedPackets;
	}
	
	public void setServerPort(int port){
		this.remotePort = port;
	}
	
	public int getServerPort(){
		return this.remotePort;
	}
	
	public void setServerAddress(String addr){
		this.remoteAddress = addr;
	}
	
	public String getServerAddress(){
		return this.remoteAddress;
	}
	
	public void setBufferSize(int bytes){
		this.bufferSize = bytes;
	}
	
	public int getBufferSize(){
		return this.bufferSize;
	}
	
	public long getLastHeartbeatTime(){
		return lastSent;
	}
	
	public long getLastReceivedTime(){
		return lastReceived;
	}
	
	/*
	 * send heart beat every given seconds
	 */
	public void setHeartbeatInterval(int second){
		if(second <= 0){
			return;
		}
		this.heartbeatInterval = second;
	}
	
	public int getHeartbeatInterval(){
		return this.heartbeatInterval;
	}
	
	public abstract boolean hasNetworkConnection();
	public abstract void trySystemSleep();
	public abstract void onPushMessage(Message message);
	
	class Worker implements Runnable{
		public void run(){
			synchronized(workerT){
				workerT.notifyAll();
			}
			while(stoped == false){
				try{
					handleEvent();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					waitMsg();
				}
			}
		}
		
		private void waitMsg(){
			synchronized(this){
				try{
					this.wait(1000);
				}catch(InterruptedException e){
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		private void wakeup(){
			synchronized(this){
				this.notifyAll();
			}
		}
		
		private void handleEvent() throws Exception{
			Message m = null;
			while(true){
				m = dequeue();
				if(m == null){
					return;
				}
				onPushMessage(m);
			}
			//finish work here, such as release wake lock
		}
		
	}
}

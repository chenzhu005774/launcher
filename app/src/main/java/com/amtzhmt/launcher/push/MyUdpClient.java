package com.amtzhmt.launcher.push;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.amtzhmt.launcher.util.utils.LogUtils;
import com.google.gson.Gson;



public class MyUdpClient extends UDPClientBase {
 Context context ;
	public MyUdpClient(String uuid, int appid, String serverAddr, int serverPort,Context context)
			throws Exception {
		super(uuid, appid, serverAddr, serverPort);
	// TODO Auto-generated constructor stub
	this.context = context;
}

	@Override
	public boolean hasNetworkConnection() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onPushMessage(Message message) {
		if(message == null){
			System.out.println("msg is null");
		}

		if(true) {
			String str = null;
			str = message.getData();
			System.out.println("AMTPush自定义推送信息:" + str);
			LogUtils.showDialog(context,str);
		}
	}

	@Override
	public void trySystemSleep() {
		// TODO Auto-generated method stub

	}




	/**
	 * 发送模拟按键的keyCode，服务端进行响应
	 *
	 * @param keyCode
	 *            按键码
	 */
	public static void sendKeyEvent(final int keyCode) {
		Log.d("chenzhu", "收到的键值：" + keyCode);

		if (keyCode == KeyEvent.KEYCODE_HOME) {
			returnHome_ADB();
			return;
		}
		try {
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(keyCode);
					} catch (Exception e) {
						Log.e("chenzhu", e.toString());
					}
				}
			}.start();
		} catch (Exception e) {

		}
	}
	/**
	 * 模拟home键，通过ADB
	 */
	public static void returnHome_ADB() {
		new Thread() {
			public void run() {
				try {
					String keyCommand = "input keyevent " + KeyEvent.KEYCODE_HOME;
					Runtime runtime = Runtime.getRuntime();
					Process process = runtime.exec(keyCommand);
					Log.i("chenzhu", "keyCommand=" + keyCommand);
				} catch (Exception e) {
					Log.e("chenzhu", e.toString());
				}
			}
		}.start();
	}
	public JsonIMMessage getObject(String message) {

		message = OriginalUtil.TripleDES.decrypt(message);
		Gson gson = new Gson();
		JsonIMMessage json = null;
		try {
			json = gson.fromJson(message, JsonIMMessage.class);
		} catch (Exception ex) {
			json = null;
		}

		return json;
	}

}

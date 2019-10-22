package com.amtzhmt.launcher.push;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;

import com.amtzhmt.launcher.App;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.google.gson.Gson;



public class MyUdpClient extends UDPClientBase {
    Context context ;
	public MyUdpClient(String uuid, int appid, String serverAddr, int serverPort,Context context)throws Exception {
		super(uuid, appid, serverAddr, serverPort);
    	this.context = context;
}

	@Override
	public boolean hasNetworkConnection() {
		return true;
	}


	@Override
	public void onPushMessage(Message message) {
		if(message == null){
			LogUtils.i("msg is null");
		}
		if(true) {
			String str = null;
			str = message.getData();
			LogUtils.i("AMTPush自定义推送信息:" + str+" bbbbbbbbbbbb "+App.context);
			LogUtils.showWindowManagerDialog(App.context);
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
		LogUtils.i( "收到的键值：" + keyCode);

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
						LogUtils.i(  e.toString());
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
					LogUtils.i(  "keyCommand=" + keyCommand);
				} catch (Exception e) {
					LogUtils.i(  e.toString());
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

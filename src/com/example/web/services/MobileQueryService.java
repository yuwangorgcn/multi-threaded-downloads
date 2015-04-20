package com.example.web.services;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.example.web.util.Config;
import com.example.web.util.NumberParser;
import com.example.web.util.StreamTools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MobileQueryService {
	static String TAG = "MobileQueryService";

	/**
	 * 根据number号码调用webservice获取手机归属地<br>
	 * 通过post请求提交数据到服务器
	 * 
	 * @param number
	 *            电话号码
	 * @param handler
	 */
	public static void getAddress(final String number, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream inputstream = MobileQueryService.class
							.getClassLoader().getResourceAsStream(
									"mobile_post.xml");
					byte[] info = StreamTools.getBytes(inputstream);
					String content = new String(info);
					String postxml = content.replace("$mobile", number);
					Log.i(TAG, "send:" + postxml);

					String path = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";
					byte[] data = postxml.getBytes();
					// 通过post请求提交数据到服务器
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 默认是GET
					conn.setRequestMethod("POST");
					conn.setReadTimeout(5000);
					// 设置http协议可以向服务器写数据(默认false)
					conn.setDoOutput(true);
					// 设置http协议的消息头
					conn.setRequestProperty("Content-Type",
							"application/soap+xml; charset=utf-8");
					conn.setRequestProperty("Content-Length", data.length + "");
					// 把我们准备好的data数据写给服务器
					OutputStream os = conn.getOutputStream();
					os.write(data);

					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						// 返回的是一整个xml，我们只需要xml其中的一部分数据而已
						String address = NumberParser.getXmlInfo(is);
						sendMessage(Config.SENT_SUCCESSFULLY, address, handler);

						Log.i(TAG, "response:" + address);
					} else {
						sendMessage(Config.FAILED_TO_SEND, "服务器状态异常:" + code,
								handler);

						Log.e(TAG, "服务器状态异常:" + code);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					sendMessage(Config.FAILED_TO_SEND, e.toString(), handler);

					Log.e(TAG, "FAILED_TO_SEND:" + e.toString());
				}
			}
		}).start();
	}

	/**
	 * 传送handler信息
	 * 
	 * @param msgCode
	 * @param msgContent
	 * @param handler
	 */
	private static void sendMessage(int msgCode, String msgContent,
			Handler handler) {
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("MSG", msgContent);
		msg.setData(bundle);
		msg.what = msgCode;
		handler.sendMessage(msg);
	}
}

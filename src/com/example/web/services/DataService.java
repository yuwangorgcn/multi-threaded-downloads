package com.example.web.services;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.web.util.StreamTools;

public class DataService {
	/**
	 * 通过get请求提交数据到服务器
	 * 
	 * @param path
	 *            服务器servlet的地址
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @return 服务器返回回来的String数据
	 */
	public static void sendDataByGet(final String path, final String name,
			final String password, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					// 解决输入中文乱码
					String param1 = URLEncoder.encode(name, "utf-8");

					String param2 = URLEncoder.encode(password, "utf-8");
					// 格式
					// http://localhost:8080/web/LoginServlet?name=xxx&password=xxx
					URL url = new URL(path + "?" + "name=" + param1
							+ "&password=" + param2);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 默认就是GET，所以可省略
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					// 目前还没发送数据给服务器
					// 只要我们获取任何一个服务器返回的信息，数据就会被提交给服务器，得到服务器返回的流信息
					InputStream is = conn.getInputStream();
					byte[] result = StreamTools.getBytes(is);

					sendMessage(1, new String(result), handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					sendMessage(0, e.toString(), handler);
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

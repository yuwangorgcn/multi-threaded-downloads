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
	 * ͨ��get�����ύ���ݵ�������
	 * 
	 * @param path
	 *            ������servlet�ĵ�ַ
	 * @param name
	 *            �û���
	 * @param password
	 *            ����
	 * @return ���������ػ�����String����
	 */
	public static void sendDataByGet(final String path, final String name,
			final String password, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					// ���������������
					String param1 = URLEncoder.encode(name, "utf-8");

					String param2 = URLEncoder.encode(password, "utf-8");
					// ��ʽ
					// http://localhost:8080/web/LoginServlet?name=xxx&password=xxx
					URL url = new URL(path + "?" + "name=" + param1
							+ "&password=" + param2);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// Ĭ�Ͼ���GET�����Կ�ʡ��
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					// Ŀǰ��û�������ݸ�������
					// ֻҪ���ǻ�ȡ�κ�һ�����������ص���Ϣ�����ݾͻᱻ�ύ�����������õ����������ص�����Ϣ
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
	 * ����handler��Ϣ
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

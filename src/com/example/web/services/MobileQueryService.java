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
	 * ����number�������webservice��ȡ�ֻ�������<br>
	 * ͨ��post�����ύ���ݵ�������
	 * 
	 * @param number
	 *            �绰����
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
					// ͨ��post�����ύ���ݵ�������
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// Ĭ����GET
					conn.setRequestMethod("POST");
					conn.setReadTimeout(5000);
					// ����httpЭ������������д����(Ĭ��false)
					conn.setDoOutput(true);
					// ����httpЭ�����Ϣͷ
					conn.setRequestProperty("Content-Type",
							"application/soap+xml; charset=utf-8");
					conn.setRequestProperty("Content-Length", data.length + "");
					// ������׼���õ�data����д��������
					OutputStream os = conn.getOutputStream();
					os.write(data);

					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						// ���ص���һ����xml������ֻ��Ҫxml���е�һ�������ݶ���
						String address = NumberParser.getXmlInfo(is);
						sendMessage(Config.SENT_SUCCESSFULLY, address, handler);

						Log.i(TAG, "response:" + address);
					} else {
						sendMessage(Config.FAILED_TO_SEND, "������״̬�쳣:" + code,
								handler);

						Log.e(TAG, "������״̬�쳣:" + code);
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

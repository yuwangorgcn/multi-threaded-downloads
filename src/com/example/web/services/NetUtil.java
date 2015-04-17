package com.example.web.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.web.util.StreamTools;

public class NetUtil {
	/**
	 * ��ȡ��ҳhtml������
	 * 
	 * @param address
	 * @param handler
	 */
	public static void getHtml(final String address, final Handler handler) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				String content = "";
				try {
					URL url = new URL(address);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 5��
					conn.setReadTimeout(5000);
					conn.setRequestMethod("GET");

					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						byte[] result = StreamTools.getBytes(is);
						// ����������ɴ�������
						String temp = new String(result);
						if (temp.contains("gb2312")) {
							content = new String(result, "gb2312");
						} else if (temp.contains("gbk")) {
							content = new String(result, "gbk");
							// �Դ����ƣ���Ӷ��ֱ���֧��
						} else {
							content = temp;
						}
						sendMessage(1, content, handler);
					} else {
						sendMessage(0, "������·ʧ��" + code, handler);
						// throw new IllegalStateException("������·ʧ��");
					}
				} catch (Exception e) {
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

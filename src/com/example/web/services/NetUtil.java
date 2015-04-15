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

					} else {
						throw new IllegalStateException("������·ʧ��");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("MSG", content);
				msg.setData(bundle);
				msg.what = 1;
				handler.sendMessage(msg);

			}
		}).start();

	}

}

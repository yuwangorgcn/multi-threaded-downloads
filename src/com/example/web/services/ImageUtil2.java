package com.example.web.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.web.util.StreamTools;

public class ImageUtil2 {
	/**
	 * 获取网路address地址对应的图片,搭配handler
	 * 
	 * @param address
	 * @param handler
	 * @throws Exception
	 */
	public static void getImage(final String address, final Handler handler) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = null;
				// 通过代码模拟浏览器访问图片的流程
				URL url;
				try {
					url = new URL(address);

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					// 5秒
					conn.setConnectTimeout(5000);
					// 获取服务器返回的流
					InputStream is = conn.getInputStream();

					byte[] imageBytes = StreamTools.getBytes(is);
					bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
							imageBytes.length);

					Message msg = new Message();
					msg.obj = bitmap;
					// 成功=1
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					Message msg = new Message();
					// 失败=0
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}
		}).start();
	}

}

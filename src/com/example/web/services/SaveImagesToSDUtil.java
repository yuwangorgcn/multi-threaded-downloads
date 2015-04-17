package com.example.web.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.web.util.StreamTools;

public class SaveImagesToSDUtil {
	/**
	 * ͬImageUtil<br>
	 * ��ȡ��·address��ַ��Ӧ��ͼƬ,���浽SD card
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getImage(String address) throws Exception {
		// ͨ������ģ�����������ͼƬ������
		// http://xxx.xxx/a.jpg, ȡ������a.jpg��������
		int start = address.lastIndexOf("/");
		String iconName = address.substring(start + 1, address.length());

		File file = new File(Environment.getExternalStorageDirectory(),
				iconName);
		FileOutputStream fos = new FileOutputStream(file);

		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// 5��
		conn.setConnectTimeout(5000);
		// ��ȡ���������ص���
		InputStream is = conn.getInputStream();
		byte[] imageBytes = StreamTools.getBytes(is);

		// ͼƬ���浽SD card
		fos.write(imageBytes);
		fos.flush();
		fos.close();

		Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
				imageBytes.length);

		return bitmap;
	}
}

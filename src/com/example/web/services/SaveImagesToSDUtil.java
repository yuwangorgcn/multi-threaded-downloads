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
	 * 同ImageUtil<br>
	 * 获取网路address地址对应的图片,并存到SD card
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getImage(String address) throws Exception {
		// 通过代码模拟浏览器访问图片的流程
		// http://xxx.xxx/a.jpg, 取得最后的a.jpg当作命名
		int start = address.lastIndexOf("/");
		String iconName = address.substring(start + 1, address.length());

		File file = new File(Environment.getExternalStorageDirectory(),
				iconName);
		FileOutputStream fos = new FileOutputStream(file);

		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// 5秒
		conn.setConnectTimeout(5000);
		// 获取服务器返回的流
		InputStream is = conn.getInputStream();
		byte[] imageBytes = StreamTools.getBytes(is);

		// 图片保存到SD card
		fos.write(imageBytes);
		fos.flush();
		fos.close();

		Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
				imageBytes.length);

		return bitmap;
	}
}

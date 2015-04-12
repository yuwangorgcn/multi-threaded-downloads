package com.example.web.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.util.Log;

public class StreamTools {
	public static final String TAG = "StreamTools";

	/**
	 * 把一个inputstream里面的内容转化成一个byte[]
	 */
	public static byte[] getBytes(InputStream is) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		length = is.read(buffer);
		while (length != -1) {
			bos.write(buffer, 0, length);
		}
		is.close();
		bos.flush();
		Log.v(TAG,bos.toByteArray().toString());
		return bos.toByteArray();
	}
}

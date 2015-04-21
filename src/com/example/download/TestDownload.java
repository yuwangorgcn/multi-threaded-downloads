package com.example.download;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.omg.CORBA.PRIVATE_MEMBER;

public class TestDownload {
	private static final String path = "http://192.168.1.101:8080/youdao.exe";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");
		int code = conn.getResponseCode();
		if (code == 200) {
			int length = conn.getContentLength();
			/**
			 * mode 值 含意:<br>
			 * 
			 * "r" 以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。 <br>
			 * "rw" 打开以便读取和写入。如果该文件尚不存在，则尝试创建该文件。<br>
			 * "rws" 打开以便读取和写入，对于 "rw"，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备。<br>
			 * "rwd" 打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到底层存储设备。
			 */
			//产生一个跟服务器文件大小一致的空文件
			RandomAccessFile file = new RandomAccessFile(getFileName(path),
					"rwd");
			// 1.在本地创建一个文件 文件大小要跟服务器文件的大小一致
			file.setLength(length);

			// 2.假设开启3个线程
			int threadNumber = 3;
			int blockSize = length / threadNumber;
			/**
			 * 线程1: 0~blockSize <br>
			 * 线程2: 1*blockSize~2*blockSize <br>
			 * 线程3: 2*blockSize~3*blockSize <br>
			 */
			for (int i = 0; i < threadNumber; i++) {

				int startPosition = i * blockSize;
				int endPosition = (i + 1) * blockSize;
				if (i == (threadNumber - 1)) {
					// 最后一个线程
					endPosition = length;
				}

				DownloadTask task = new DownloadTask(i, path, startPosition,
						endPosition);
				task.start();

			}
		}
		// 获取文件大小

	}

	public static String getFileName(String path) {
		int start = path.lastIndexOf("/") + 1;
		return path.substring(start, path.length());
	}

}

class DownloadTask extends Thread {
	private static final String path = "http://192.168.1.101:8080/youdao.exe";

	int threadId;
	String filePath;// 档案路径
	int startPosition;// 起始位置
	int endPosition;// 结束位置

	public DownloadTask(int threadId, String filePath, int startPosition,
			int endPosition) {
		super();
		this.threadId = threadId;
		this.filePath = filePath;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}

	@Override
	public void run() {

		try {
			URL url = new URL(filePath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//
			conn.setRequestProperty("Range", "bytes=" + startPosition + "-"
					+ endPosition);
			conn.setConnectTimeout(5000);
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");
			InputStream is = conn.getInputStream();

			RandomAccessFile file = new RandomAccessFile(getFileName(path),
					"rwd");
			// 设置数据从那个位置开始写
			file.seek(startPosition);
			byte[] buffer = new byte[1024];
			// 文件长度
			// 当length = -1代表文件读完了
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				file.write(buffer, 0, length);
			}
			file.close();
			System.out.println("线程" + threadId + "下载完毕");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.run();
	}

	public static String getFileName(String path) {
		int start = path.lastIndexOf("/") + 1;
		return path.substring(start, path.length());
	}
}

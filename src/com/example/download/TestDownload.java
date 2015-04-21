package com.example.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.omg.CORBA.PRIVATE_MEMBER;

public class TestDownload {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		URL url = new URL(Config.path);
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
			 * mode ֵ ����:<br>
			 * 
			 * "r" ��ֻ����ʽ�򿪡����ý��������κ� write �������������׳� IOException�� <br>
			 * "rw" ���Ա��ȡ��д�롣������ļ��в����ڣ����Դ������ļ���<br>
			 * "rws" ���Ա��ȡ��д�룬���� "rw"����Ҫ����ļ������ݻ�Ԫ���ݵ�ÿ�����¶�ͬ��д�뵽�ײ�洢�豸��<br>
			 * "rwd" ���Ա��ȡ��д�룬���� "rw"����Ҫ����ļ����ݵ�ÿ�����¶�ͬ��д�뵽�ײ�洢�豸��
			 */
			// ����һ�����������ļ���Сһ�µĿ��ļ�
			RandomAccessFile file = new RandomAccessFile(getFileName(Config.path),
					"rwd");
			// 1.�ڱ��ش���һ���ļ� �ļ���СҪ���������ļ��Ĵ�Сһ��
			file.setLength(length);

			// 2.���迪��3���߳�
			int threadNumber = 3;
			int blockSize = length / threadNumber;
			/**
			 * �߳�1: 0~blockSize <br>
			 * �߳�2: 1*blockSize~2*blockSize <br>
			 * �߳�3: 2*blockSize~3*blockSize <br>
			 */
			for (int i = 0; i < threadNumber; i++) {

				int startPosition = i * blockSize;
				int endPosition = (i + 1) * blockSize;
				if (i == (threadNumber - 1)) {
					// ���һ���߳�
					endPosition = length;
				}

				DownloadTask task = new DownloadTask(i, Config.path, startPosition,
						endPosition);
				task.start();

			}
		}
		// ��ȡ�ļ���С

	}

	public static String getFileName(String path) {
		int start = path.lastIndexOf("/") + 1;
		return path.substring(start, path.length());
	}

}

class DownloadTask extends Thread {

	int threadId;
	String filePath;// ����·��
	int startPosition;// ��ʼλ��
	int endPosition;// ����λ��

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
			// ����һ���ļ����󣬼�¼��ǰĳ���ļ�������λ��
			File positionFile = new File(threadId + ".txt");

			URL url = new URL(filePath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			System.out.println("�߳�" + threadId + "��������" + "  ��ʼλ��"
					+ startPosition + "  ����λ��" + endPosition);

			if (positionFile.exists()) {
				FileInputStream fis = new FileInputStream(positionFile);
				byte[] result = StreamTools.getBytes(fis);
				// byte[]תint����תstrint,����תint
				int newStartPosition = Integer.parseInt(new String(result));

				if (newStartPosition > startPosition) {
					startPosition = newStartPosition;
				}
			}

			conn.setRequestProperty("Range", "bytes=" + startPosition + "-"
					+ endPosition);
			conn.setConnectTimeout(5000);
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");
			InputStream is = conn.getInputStream();

			RandomAccessFile file = new RandomAccessFile(getFileName(Config.path),
					"rwd");
			// �������ݴ��Ǹ�λ�ÿ�ʼд
			file.seek(startPosition);
			byte[] buffer = new byte[1024];
			// �ļ�����
			// ��length = -1�����ļ�������
			int length = 0;

			// ��ǰ�������������ݵ�λ�ã�ͬʱ���ֵ�Ѿ��洢���ļ���λ��
			int currentPosition = startPosition;

			while ((length = is.read(buffer)) != -1) {
				file.write(buffer, 0, length);

				// ��Ҫ��currentPosition��Ϣ�־û����洢�豸
				currentPosition += length;
				String position = currentPosition + "";

				FileOutputStream fos = new FileOutputStream(positionFile);
				fos.write(position.getBytes());
				fos.flush();
				fos.close();

			}

			file.close();
			System.out.println("�߳�" + threadId + "�������");

			// ���߳�������Ϻ󣬰�currentPosition���ļ�ɾ��
			if (positionFile.exists()) {
				positionFile.delete();
			}

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

package com.example.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.web.util.Config;
import com.example.web.util.StreamTools;

public class MutiDownloadActivity extends Activity implements OnClickListener {
	public final String TAG = "MutiDownloadActivity";

	private ProgressBar pb_download;
	private Button bt_download;
	private TextView tv_process;
	private EditText et_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_muti_download);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		pb_download = (ProgressBar) this.findViewById(R.id.pb_download);
		tv_process = (TextView) this.findViewById(R.id.tv_process);
		et_url = (EditText) this.findViewById(R.id.et_url);
		bt_download = (Button) this.findViewById(R.id.bt_download);
		bt_download.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_download:
			String address = et_url.getText().toString().trim();

			if ("".equals(address)) {
				Toast.makeText(this, "·������Ϊ��", Toast.LENGTH_SHORT).show();
				return;
			} else {
				try {
					URL url = new URL(address);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
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
						 * "rws" ���Ա��ȡ��д�룬����
						 * "rw"����Ҫ����ļ������ݻ�Ԫ���ݵ�ÿ�����¶�ͬ��д�뵽�ײ�洢�豸��<br>
						 * "rwd" ���Ա��ȡ��д�룬���� "rw"����Ҫ����ļ����ݵ�ÿ�����¶�ͬ��д�뵽�ײ�洢�豸��
						 */
						// ����һ�����������ļ���Сһ�µĿ��ļ�
						RandomAccessFile file;

						file = new RandomAccessFile(Config.SDCARDPATH
								+ getFileName(address), "rwd");

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

							DownloadTask task = new DownloadTask(i, address,
									startPosition, endPosition);
							task.start();

						}
					}
					// ��ȡ�ļ���С
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		}
	}

	/**
	 * ��·������
	 * 
	 * @param path
	 * @return
	 */
	public String getFileName(String path) {
		int start = path.lastIndexOf("/") + 1;
		return path.substring(start, path.length());
	}

	/**
	 * handler for send thread
	 */
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			if (msg.what == Config.SENT_SUCCESSFULLY) {
				Bundle bundle = msg.getData();
				String message = bundle.getString("MSG");
				// Log.v(TAG, message);

				Toast.makeText(MutiDownloadActivity.this, message, 0).show();

			} else if (msg.what == Config.FAILED_TO_SEND) {
				Bundle bundle = msg.getData();
				String message = bundle.getString("MSG");
				Toast.makeText(MutiDownloadActivity.this, message, 0).show();
			}
		}
	};

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
				File positionFile = new File(Config.SDCARDPATH + threadId
						+ ".txt");

				URL url = new URL(filePath);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				Log.v(TAG, "�߳�" + threadId + "��������" + "  ��ʼλ��" + startPosition
						+ "  ����λ��" + endPosition);

				if (positionFile.exists()) {
					FileInputStream fis = new FileInputStream(positionFile);
					byte[] result = StreamTools.getBytes(fis);

					String str = new String(result);
					if (!"".equals(str)) {
						// byte[]תint����תstring,����תint
						int newStartPosition = Integer.parseInt(str);

						if (newStartPosition > startPosition) {
							startPosition = newStartPosition;
						}
					}

				}

				conn.setRequestProperty("Range", "bytes=" + startPosition + "-"
						+ endPosition);
				conn.setConnectTimeout(5000);
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");
				InputStream is = conn.getInputStream();

				RandomAccessFile file = new RandomAccessFile(Config.SDCARDPATH
						+ getFileName(filePath), "rwd");
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
				Log.v(TAG, "�߳�" + threadId + "�������");

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

	}

}

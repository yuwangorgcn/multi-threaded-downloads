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
				Toast.makeText(this, "路径不得为空", Toast.LENGTH_SHORT).show();
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
						 * mode 值 含意:<br>
						 * 
						 * "r" 以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。 <br>
						 * "rw" 打开以便读取和写入。如果该文件尚不存在，则尝试创建该文件。<br>
						 * "rws" 打开以便读取和写入，对于
						 * "rw"，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备。<br>
						 * "rwd" 打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到底层存储设备。
						 */
						// 产生一个跟服务器文件大小一致的空文件
						RandomAccessFile file;

						file = new RandomAccessFile(Config.SDCARDPATH
								+ getFileName(address), "rwd");

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

							DownloadTask task = new DownloadTask(i, address,
									startPosition, endPosition);
							task.start();

						}
					}
					// 获取文件大小
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		}
	}

	/**
	 * 从路径命名
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
				// 创建一个文件对象，记录当前某个文件的下载位置
				File positionFile = new File(Config.SDCARDPATH + threadId
						+ ".txt");

				URL url = new URL(filePath);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				Log.v(TAG, "线程" + threadId + "正在下载" + "  开始位置" + startPosition
						+ "  结束位置" + endPosition);

				if (positionFile.exists()) {
					FileInputStream fis = new FileInputStream(positionFile);
					byte[] result = StreamTools.getBytes(fis);

					String str = new String(result);
					if (!"".equals(str)) {
						// byte[]转int，先转string,才能转int
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
				// 设置数据从那个位置开始写
				file.seek(startPosition);
				byte[] buffer = new byte[1024];
				// 文件长度
				// 当length = -1代表文件读完了
				int length = 0;

				// 当前读到服务器数据的位置，同时这个值已经存储的文件的位置
				int currentPosition = startPosition;

				while ((length = is.read(buffer)) != -1) {
					file.write(buffer, 0, length);

					// 需要把currentPosition信息持久化到存储设备
					currentPosition += length;
					String position = currentPosition + "";

					FileOutputStream fos = new FileOutputStream(positionFile);
					fos.write(position.getBytes());
					fos.flush();
					fos.close();

				}

				file.close();
				Log.v(TAG, "线程" + threadId + "下载完毕");

				// 当线程下载完毕后，把currentPosition的文件删除
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

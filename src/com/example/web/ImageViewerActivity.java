package com.example.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import com.example.web.services.ImageUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageViewerActivity extends Activity implements OnClickListener {
	EditText et_address;
	ImageView iv_images;
	Button bt_view;
	String address = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		et_address = (EditText) findViewById(R.id.et_address);
		iv_images = (ImageView) findViewById(R.id.iv_images);
		bt_view = (Button) findViewById(R.id.bt_view);
		bt_view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_view:
			// test:192.168.xxx.xxx:8080/tomcat.png
			address = et_address.getText().toString().trim();
			if ("".equals(address)) {
				Toast.makeText(this, "������ͼƬ��ַ", Toast.LENGTH_LONG).show();
				return;
			} else {
				new GetImageTask().execute(address);
			}
			break;

		}
	}

	/**
	 * <���녢��, ̎���и��½��慢��, ̎�����������>
	 * 
	 * @author
	 *
	 */
	class GetImageTask extends AsyncTask<String, int[], Bitmap> {
		Bitmap bitmap = null;

		/**
		 * // �ٱ�����̎��ĺĕr����
		 */
		@Override
		protected Bitmap doInBackground(String... address) {

			try {
				bitmap = ImageUtil.getImage(address[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (e instanceof SocketTimeoutException) {
					//Toast.makeText(ImageViewerActivity.this, "��·���ӳ�ʱ",Toast.LENGTH_LONG).show();
				} else if (e instanceof IOException) {
					//Toast.makeText(ImageViewerActivity.this, "��ȡ���ݴ���",Toast.LENGTH_LONG).show();
				} else {
					//Toast.makeText(ImageViewerActivity.this, "δ֪����",Toast.LENGTH_LONG).show();
				}

				e.printStackTrace();
			}

			return bitmap;
		}

		/**
		 * ��������̎��"ǰ"��������
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		/**
		 * ��������̎��"��"���µ���
		 */
		@Override
		protected void onProgressUpdate(int[]... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

		}

		/**
		 * ��������̎����"��"��������
		 */
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			iv_images.setImageBitmap(result);
		}

		/**
		 * ����������"ȡ��"�r�����£��˕r���� onPostExecute(Bitmap result)
		 */
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();

		}
	}
}
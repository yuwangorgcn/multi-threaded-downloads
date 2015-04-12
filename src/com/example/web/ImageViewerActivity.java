package com.example.web;

import java.io.IOException;
import java.net.SocketTimeoutException;

import com.example.web.services.ImageUtil;

import android.app.Activity;
import android.graphics.Bitmap;
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
			String address = et_address.getText().toString().trim();
			if ("".equals(address)) {
				Toast.makeText(this, "请填入图片地址", Toast.LENGTH_LONG).show();
				return;
			} else {

				try {
					Bitmap bitmap = ImageUtil.getImage(address);
					iv_images.setImageBitmap(bitmap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if (e instanceof SocketTimeoutException) {
						Toast.makeText(this, "网路连接超时", Toast.LENGTH_LONG)
								.show();
					} else if (e instanceof IOException) {
						Toast.makeText(this, "读取数据错误", Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(this, "未知错误", Toast.LENGTH_LONG).show();
					}

					e.printStackTrace();
				}
			}
			break;
		}
	}

}
package com.example.web;

import com.example.web.services.NetUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HtmlViewerActivity extends Activity implements OnClickListener {
	private Button bt_view;
	private EditText et_address;
	private TextView tv_content;
	String TAG = "HtmlViewerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html_viewer);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		et_address = (EditText) findViewById(R.id.et_address);
		tv_content = (TextView) findViewById(R.id.tv_content);
		bt_view = (Button) findViewById(R.id.bt_view);
		bt_view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_view:
			// test:192.168.xxx.xxx:8080/WebProject/NewFile.jsp
			String address = et_address.getText().toString().trim();

			if ("".equals(address)) {
				Toast.makeText(this, "请填入图片地址", Toast.LENGTH_LONG).show();
				return;
			} else {
				try {
					NetUtil.getHtml(address, myHandler);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// Toast.LENGTH_LONG=1，Toast.LENGTH_SHORT=0
					Toast.makeText(this, "获取数据失败", 0).show();
					return;
				}

			}
			break;
		}
	}

	/**
	 * handler for send thread
	 */
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			if (msg.what == 1) {
				Bundle bundle = msg.getData();
				String message = bundle.getString("MSG");
				//Log.v(TAG, message);
				tv_content.setText(message);
			}
		}
	};
}

package com.example.web;

import com.example.web.services.DataService;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private final String TAG = "LoginActivity";

	private EditText et_name, et_password, et_file_path;
	private Button bt_get_login, bt_post_login, bt_httpclient_get_login,
			bt_httpclient_post_login, bt_httpclient_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		et_name = (EditText) this.findViewById(R.id.et_name);
		et_password = (EditText) this.findViewById(R.id.et_password);
		et_file_path = (EditText) this.findViewById(R.id.et_file_path);
		bt_get_login = (Button) this.findViewById(R.id.bt_get_login);
		bt_get_login.setOnClickListener(this);
		bt_post_login = (Button) this.findViewById(R.id.bt_post_login);
		bt_post_login.setOnClickListener(this);
		bt_httpclient_get_login = (Button) this
				.findViewById(R.id.bt_httpclient_get_login);
		bt_httpclient_get_login.setOnClickListener(this);
		bt_httpclient_post_login = (Button) this
				.findViewById(R.id.bt_httpclient_post_login);
		bt_httpclient_post_login.setOnClickListener(this);
		bt_httpclient_login = (Button) this
				.findViewById(R.id.bt_httpclient_login);
		bt_httpclient_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String name = et_name.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		switch (v.getId()) {
		case R.id.bt_get_login:

			if ("".equals(name) || "".equals(password)) {

				Toast.makeText(this, "用户姓名或密码不得为空", 0).show();

				return;
			} else {
				// 通过get请求发送数据到服务器
				String path = getResources().getString(R.string.serlet_url);
				try {
					DataService.sendDataByGet(path, name, password, myHandler);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, "访问网路异常", 0).show();
				}
			}

			break;
		case R.id.bt_post_login:

			if ("".equals(name) || "".equals(password)) {

				Toast.makeText(this, "用户姓名或密码不得为空", 0).show();

				return;
			} else {
				// 通过get请求发送数据到服务器
				String path = getResources().getString(R.string.serlet_url);
				try {
					DataService.sendDataByPost(path, name, password, myHandler);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, "访问网路异常", 0).show();
				}
			}

			break;
		case R.id.bt_httpclient_get_login:

			if ("".equals(name) || "".equals(password)) {

				Toast.makeText(this, "用户姓名或密码不得为空", 0).show();

				return;
			} else {
				// 通过get请求发送数据到服务器
				String path = getResources().getString(R.string.serlet_url);
				try {
					DataService.sendDataByHttpClientGet(path, name, password,
							myHandler);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, "访问网路异常", 0).show();
				}
			}

			break;
		case R.id.bt_httpclient_post_login:

			if ("".equals(name) || "".equals(password)) {

				Toast.makeText(this, "用户姓名或密码不得为空", 0).show();

				return;
			} else {
				// 通过get请求发送数据到服务器
				String path = getResources().getString(R.string.serlet_url);
				try {
					DataService.sendDataByHttpClientPost(path, name, password,
							myHandler);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, "访问网路异常", 0).show();
				}
			}

			break;

		case R.id.bt_httpclient_login:
			String filePath = et_file_path.getText().toString().trim();
			if ("".equals(name) || "".equals(password)) {

				Toast.makeText(this, "用户姓名或密码不得为空", 0).show();

				return;
			} else if ("".equals(filePath)) {
				Toast.makeText(this, "文件路径不得为空", 0).show();

				return;
			} else {
				// 通过get请求发送数据到服务器
				String path = getResources().getString(R.string.serlet_url);
				try {
					DataService.sendDataWithFileByHttpClient(path, name,
							password, filePath, myHandler);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, "访问网路异常", 0).show();
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
				// Log.v(TAG, message);

				Toast.makeText(LoginActivity.this, message, 0).show();

			} else if (msg.what == 0) {
				Bundle bundle = msg.getData();
				String message = bundle.getString("MSG");
				Toast.makeText(LoginActivity.this, message, 0).show();
			}
		}
	};

}

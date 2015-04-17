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

	private EditText et_name, et_password;
	private Button bt_login;

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
		bt_login = (Button) this.findViewById(R.id.bt_login);
		bt_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login:
			String name = et_name.getText().toString().trim();
			String password = et_password.getText().toString().trim();

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

package com.example.web;

import com.example.web.services.MobileQueryService;
import com.example.web.util.Config;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneActivity extends Activity implements OnClickListener {
	private EditText et_mobile_code;
	private Button bt_query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		et_mobile_code = (EditText) this.findViewById(R.id.et_mobile_code);

		bt_query = (Button) this.findViewById(R.id.bt_query);
		bt_query.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_query:
			// test number=13812345678
			// response=13812345678：江苏 连云港 江苏移动快捷通卡
			String number = et_mobile_code.getText().toString().trim();
			MobileQueryService.getAddress(number, myHandler);
			break;
		}
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

				Toast.makeText(PhoneActivity.this, "号码归属地\n" + message, 0)
						.show();

			} else if (msg.what == Config.FAILED_TO_SEND) {
				Bundle bundle = msg.getData();
				String message = bundle.getString("MSG");
				Toast.makeText(PhoneActivity.this, message, 0).show();
			}
		}
	};
}

package com.example.web;

import com.example.web.util.Config;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Sample extends Activity  implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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

				Toast.makeText(Sample.this, message, 0).show();

			} else if (msg.what == Config.FAILED_TO_SEND) {
				Bundle bundle = msg.getData();
				String message = bundle.getString("MSG");
				Toast.makeText(Sample.this, message, 0).show();
			}
		}
	};
}

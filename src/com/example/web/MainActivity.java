package com.example.web;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private Button bt_image_viewer,bt_html_viewer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		bt_image_viewer = (Button) findViewById(R.id.bt_image_viewer);
		bt_image_viewer.setOnClickListener(this);
		bt_html_viewer = (Button) findViewById(R.id.bt_html_viewer);
		bt_html_viewer.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.bt_image_viewer:
			// 相應按鈕的點擊事件
			intent.setClass(MainActivity.this, ImageViewerActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_html_viewer:
			// 相應按鈕的點擊事件
			intent.setClass(MainActivity.this, HtmlViewerActivity.class);
			startActivity(intent);
			break;
		}
	}
}

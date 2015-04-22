package com.example.web;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private Button bt_image_viewer, bt_html_viewer, bt_list_viewer,
			bt_list_viewer_sd, bt_login, bt_phone,bt_muti_download;

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
		bt_list_viewer = (Button) findViewById(R.id.bt_list_viewer);
		bt_list_viewer.setOnClickListener(this);
		bt_list_viewer_sd = (Button) findViewById(R.id.bt_list_viewer_sd);
		bt_list_viewer_sd.setOnClickListener(this);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_login.setOnClickListener(this);
		bt_phone = (Button) findViewById(R.id.bt_phone);
		bt_phone.setOnClickListener(this);
		bt_muti_download = (Button) findViewById(R.id.bt_muti_download);
		bt_muti_download.setOnClickListener(this);
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
		case R.id.bt_list_viewer:
			// 相應按鈕的點擊事件
			intent.setClass(MainActivity.this, ListActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_list_viewer_sd:
			// 相應按鈕的點擊事件
			intent.setClass(MainActivity.this, ListFromSDActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_login:
			// 相應按鈕的點擊事件
			intent.setClass(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_phone:
			// 相應按鈕的點擊事件
			intent.setClass(MainActivity.this, PhoneActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_muti_download:
			// 相應按鈕的點擊事件
			intent.setClass(MainActivity.this, MutiDownloadActivity.class);
			startActivity(intent);
			break;
		}
	}
}

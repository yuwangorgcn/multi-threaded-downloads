package com.example.web;

import java.io.InputStream;
import java.util.List;

import com.example.web.domain.Channel;
import com.example.web.services.ChanneUtil;
import com.example.web.services.ChannelService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {
	private ListView lv;
	private List<Channel> channelList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findView();
		String address = getResources().getString(R.string.server_url);
		InputStream is;
		try {
			is = ChanneUtil.getChanneStream(address);
			channelList = ChannelService.getChannels(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "获取数据失败", 0);
			e.printStackTrace();

		}

		// 填充listview的数据
		lv.setAdapter(new MyAdapter());
	}

	private void findView() {
		// TODO Auto-generated method stub
		lv = (ListView) this.findViewById(R.id.lv);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return channelList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return channelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv = new TextView(ListActivity.this);
			tv.setText(channelList.get(position).getName());
			return tv;
		}

	}
}

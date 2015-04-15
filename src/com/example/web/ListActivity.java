package com.example.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.List;

import com.example.web.ImageViewerActivity.GetImageTask;
import com.example.web.domain.Channel;
import com.example.web.services.ChanneUtil;
import com.example.web.services.ChannelService;
import com.example.web.services.ImageUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {
	private ListView lv;
	private List<Channel> channelList;
	String TAG = "ListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findView();
		String address = getResources().getString(R.string.server_url);
		new GetDataTask().execute(address);
	}

	private void findView() {
		// TODO Auto-generated method stub
		lv = (ListView) this.findViewById(R.id.lv);
	}

	/**
	 * <傳入參數, 處理中更新介面參數, 處理後傳出參數>
	 * 
	 * @author
	 *
	 */
	class GetDataTask extends AsyncTask<String, int[], InputStream> {

		/**
		 * // 再背景中處理的耗時工作
		 */
		@Override
		protected InputStream doInBackground(String... address) {
			InputStream is = null;
			try {
				is = ChanneUtil.getChanneStream(address[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//Toast.makeText(ListActivity.this, "获取数据失败", 0);
				e.printStackTrace();

			}

			return is;
		}

		/**
		 * 背景工作處理"前"需作的事
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		/**
		 * 背景工作處理"中"更新的事
		 */
		@Override
		protected void onProgressUpdate(int[]... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

		}

		/**
		 * 背景工作處理完"後"需作的事
		 */
		@Override
		protected void onPostExecute(InputStream result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				channelList = ChannelService.getChannels(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 填充listview的数据
			lv.setAdapter(new MyAdapter());
		}

		/**
		 * 背景工作被"取消"時作的事，此時不作 onPostExecute(Bitmap result)
		 */
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();

		}
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

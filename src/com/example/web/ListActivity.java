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
import com.example.web.services.ImageUtil2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {
	private ListView lv;

	private List<Channel> channelList;
	private LayoutInflater inflater;
	String TAG = "ListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		// 等同于 inflater
		// =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		inflater = LayoutInflater.from(this);
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
				// Toast.makeText(ListActivity.this, "获取数据失败", 0);
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
			View view = inflater.inflate(R.layout.item_list, null);
			ImageView iv_images = (ImageView) view.findViewById(R.id.iv_images);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			TextView tv_count = (TextView) view.findViewById(R.id.tv_count);

			Channel channel = channelList.get(position);

			tv_name.setText(channel.getName());
			tv_time.setText("播放时间：" + channel.getTime());
			tv_count.setText("点播次数：" + channel.getCount());

			String address = channel.getIcon();
			try {
				Bitmap bitmap = ImageUtil.getImage(address);
				iv_images.setImageBitmap(bitmap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return view;
		}

	}

}

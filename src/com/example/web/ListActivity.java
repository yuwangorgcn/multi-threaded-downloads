package com.example.web;

import java.io.InputStream;
import java.util.List;

import com.example.web.domain.Channel;
import com.example.web.services.ChanneUtil;
import com.example.web.services.ChannelService;
import com.example.web.services.ImageUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends Activity {
	private ListView lv;

	private List<Channel> channelList;
	private LayoutInflater inflater;
	public static float density;
	String TAG = "ListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		// 等同于 inflater
		// =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		inflater = LayoutInflater.from(this);
		findView();
		density = getDisplayDensity();
		String address = getResources().getString(R.string.server_url);
		new GetDataTask().execute(address);
	}

	private void findView() {
		// TODO Auto-generated method stub
		lv = (ListView) this.findViewById(R.id.lv);
	}

	/**
	 * 
	 * @return 屏幕的密度
	 */
	private float getDisplayDensity() {
		// TODO Auto-generated method stub
		// 取得DisplayMetrics数据
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// 屏幕的宽度(像素)
		int width = metrics.widthPixels;
		// 屏幕的高度(像素)
		int height = metrics.heightPixels;
		// 屏幕的密度
		float density = metrics.density;
		// 屏幕的DPI
		int dpi = metrics.densityDpi;

		// Log.i("寬度", String.valueOf(width));
		// Log.i("高度", String.valueOf(height));
		// Log.i("密度", String.valueOf(density));
		// Log.i("DPI", String.valueOf(dpi));
		return density;
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
			WebView wv_images = (WebView) view.findViewById(R.id.wv_item);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			TextView tv_count = (TextView) view.findViewById(R.id.tv_count);

			Channel channel = channelList.get(position);

			tv_name.setText(channel.getName());
			tv_time.setText("播放时间：" + channel.getTime());
			tv_count.setText("点播次数：" + channel.getCount());

			String address = channel.getIcon();
			try {
				// Bitmap bitmap = ImageUtil.getImage(address);
				// int imgWidth = bitmap.getWidth();
				int imgWidth = 120;
				// iv_images.setImageBitmap(bitmap);

				wv_images.setBackgroundResource(R.drawable.default_item);
				wv_images.loadUrl(address);

				/**
				 * item_list中的webview固定60*80(dip)<br>
				 * px=dip*destiny<br>
				 * 1.取得屏幕的destiny<br>
				 * 2.取得图片的长或宽(px)<br>
				 * 3.图片的缩放p%=(60*destiny)/图片的长 or (80*destiny)/图片的宽<br>
				 * <br>
				 * 因为p是百分比，所以要*100，因为是int，所以先*100再除，避免算出0
				 */
				int p = (100 * (80 * (int) density)) / imgWidth;
				Log.v(TAG,p+"%");
				wv_images.setInitialScale(p);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return view;
		}

	}

}

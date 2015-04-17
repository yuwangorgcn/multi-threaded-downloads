package com.example.web;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.web.domain.Channel;
import com.example.web.services.ChanneUtil;
import com.example.web.services.ChannelService;
import com.example.web.services.SaveImagesToSDUtil;

public class ListFromSDActivity extends Activity {
	private ListView lv;

	private List<Channel> channelList;
	private LayoutInflater inflater;
	public static float density;
	String TAG = "ListFromSDActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		inflater = LayoutInflater.from(this);
		// 等同于 inflater
		// =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
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

		// Log.i("宽度", String.valueOf(width));
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
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Channel channel = (Channel) lv.getItemAtPosition(position);
					String channelId = channel.getId();
					Toast.makeText(ListFromSDActivity.this, "提交id="+channelId+"到服务器", 0)
							.show();
				}

			});
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
			View view = inflater.inflate(R.layout.item_list_from_sd, null);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			TextView tv_count = (TextView) view.findViewById(R.id.tv_count);

			Channel channel = channelList.get(position);

			tv_name.setText(channel.getName());
			tv_time.setText("播放时间：" + channel.getTime());
			tv_count.setText("点播次数：" + channel.getCount());

			String address = channel.getIcon();

			// http://xxx.xxx/a.jpg, 取得最后的a.jpg当作命名
			int start = address.lastIndexOf("/");
			String iconName = address.substring(start + 1, address.length());
			// 检查文件是否存在SD card
			File file = new File(Environment.getExternalStorageDirectory(),
					iconName);
			if (file.exists() && file.length() > 0) {
				// 如果存在，直接使用SD card的文件
				iv_item.setImageURI(Uri.fromFile(file));
				Log.v(TAG, "使用缓存");
			} else {
				// 如果不存在才去下载网络上的图片
				Log.v(TAG, "下载图片");
				try {
					Bitmap bitmap = SaveImagesToSDUtil.getImage(address);
					iv_item.setImageBitmap(bitmap);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.v(TAG, "无法下载图片");
					e.printStackTrace();
					iv_item.setImageResource(R.drawable.default_item);
				}
			}

			return view;
		}
	}

}

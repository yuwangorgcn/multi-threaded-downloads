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
		// ��ͬ�� inflater
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
	 * @return ��Ļ���ܶ�
	 */
	private float getDisplayDensity() {
		// TODO Auto-generated method stub
		// ȡ��DisplayMetrics����
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// ��Ļ�Ŀ��(����)
		int width = metrics.widthPixels;
		// ��Ļ�ĸ߶�(����)
		int height = metrics.heightPixels;
		// ��Ļ���ܶ�
		float density = metrics.density;
		// ��Ļ��DPI
		int dpi = metrics.densityDpi;

		// Log.i("���", String.valueOf(width));
		// Log.i("�߶�", String.valueOf(height));
		// Log.i("�ܶ�", String.valueOf(density));
		// Log.i("DPI", String.valueOf(dpi));
		return density;
	}

	/**
	 * <���녢��, ̎���и��½��慢��, ̎�����������>
	 * 
	 * @author
	 *
	 */
	class GetDataTask extends AsyncTask<String, int[], InputStream> {

		/**
		 * // �ٱ�����̎��ĺĕr����
		 */
		@Override
		protected InputStream doInBackground(String... address) {
			InputStream is = null;
			try {
				is = ChanneUtil.getChanneStream(address[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// Toast.makeText(ListActivity.this, "��ȡ����ʧ��", 0);
				e.printStackTrace();

			}

			return is;
		}

		/**
		 * ��������̎��"ǰ"��������
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		/**
		 * ��������̎��"��"���µ���
		 */
		@Override
		protected void onProgressUpdate(int[]... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

		}

		/**
		 * ��������̎����"��"��������
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
			// ���listview������
			lv.setAdapter(new MyAdapter());
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Channel channel = (Channel) lv.getItemAtPosition(position);
					String channelId = channel.getId();
					Toast.makeText(ListFromSDActivity.this, "�ύid="+channelId+"��������", 0)
							.show();
				}

			});
		}

		/**
		 * ����������"ȡ��"�r�����£��˕r���� onPostExecute(Bitmap result)
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
			tv_time.setText("����ʱ�䣺" + channel.getTime());
			tv_count.setText("�㲥������" + channel.getCount());

			String address = channel.getIcon();

			// http://xxx.xxx/a.jpg, ȡ������a.jpg��������
			int start = address.lastIndexOf("/");
			String iconName = address.substring(start + 1, address.length());
			// ����ļ��Ƿ����SD card
			File file = new File(Environment.getExternalStorageDirectory(),
					iconName);
			if (file.exists() && file.length() > 0) {
				// ������ڣ�ֱ��ʹ��SD card���ļ�
				iv_item.setImageURI(Uri.fromFile(file));
				Log.v(TAG, "ʹ�û���");
			} else {
				// ��������ڲ�ȥ���������ϵ�ͼƬ
				Log.v(TAG, "����ͼƬ");
				try {
					Bitmap bitmap = SaveImagesToSDUtil.getImage(address);
					iv_item.setImageBitmap(bitmap);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.v(TAG, "�޷�����ͼƬ");
					e.printStackTrace();
					iv_item.setImageResource(R.drawable.default_item);
				}
			}

			return view;
		}
	}

}

package com.example.web.services;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.web.util.StreamTools;

/**
 * get v.s. post<br>
 * getһ���ύ���������Ƚ�С�����4k���ڲ���ʵͨ����ƴurl�ķ�ʽ<br>
 * post�����ύ�Ƚϴ�����ݣ�form������ʽ���ķ�ʽд��������
 */
public class DataService {

	/**
	 * ͨ��get�����ύ���ݵ�������
	 * 
	 * @param path
	 *            ������servlet�ĵ�ַ
	 * @param name
	 *            �û���
	 * @param password
	 *            ����
	 * @return ���������ػ�����String����
	 */
	public static void sendDataByGet(final String path, final String name,
			final String password, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					// ���������������
					String param1 = URLEncoder.encode(name, "utf-8");

					String param2 = URLEncoder.encode(password, "utf-8");
					// ��ʽ
					// http://localhost:8080/web/LoginServlet?name=xxx&password=xxx
					URL url = new URL(path + "?" + "name=" + param1
							+ "&password=" + param2);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// Ĭ�Ͼ���GET�����Կ�ʡ��
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					// Ŀǰ��û�������ݸ�������
					// ֻҪ���ǻ�ȡ�κ�һ�����������ص���Ϣ�����ݾͻᱻ�ύ�����������õ����������ص�����Ϣ
					InputStream is = conn.getInputStream();
					byte[] result = StreamTools.getBytes(is);

					sendMessage(1, new String(result), handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					sendMessage(0, e.toString(), handler);
				}

			}
		}).start();

	}

	/**
	 * ͨ��post�����ύ���ݵ�������
	 * 
	 * @param path
	 *            ������servlet�ĵ�ַ
	 * @param name
	 *            �û���
	 * @param password
	 *            ����
	 * @return ���������ػ�����String����
	 */
	public static void sendDataByPost(final String path, final String name,
			final String password, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					// ���������������
					String param1 = URLEncoder.encode(name, "utf-8");
					String param2 = URLEncoder.encode(password, "utf-8");

					String data = "name=" + param1 + "&password=" + param2;
					// ��ʽ
					// http://localhost:8080/web/Login.jsp
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// Ĭ�Ͼ���GET
					conn.setRequestMethod("POST");
					conn.setReadTimeout(5000);
					// ����httpЭ������������д����(Ĭ��false)
					conn.setDoOutput(true);
					// ����httpЭ�����Ϣͷ
					conn.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					conn.setRequestProperty("Content-Length", data.length()
							+ "");
					// ������׼���õ�data����д��������
					OutputStream os = conn.getOutputStream();
					os.write(data.getBytes());
					// httpurlconnection�ײ�ʵ��outputstream��һ�����������

					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						byte[] result = StreamTools.getBytes(is);
						sendMessage(1, new String(result), handler);
					} else {
						sendMessage(0, "������״̬�쳣:" + code, handler);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					sendMessage(0, e.toString(), handler);
				}

			}
		}).start();

	}

	/**
	 * httpclient ������ļ򵥰�װ<br>
	 * new HttpClient���൱�ڵõ�һ�������<br>
	 * ͨ��get�����ύ���ݵ�������
	 * 
	 * @param path
	 * @param name
	 * @param password
	 * @param handler
	 */
	public static void sendDataByHttpClientGet(final String path,
			final String name, final String password, final Handler handler) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// ���������������
					String param1 = URLEncoder.encode(name, "utf-8");
					String param2 = URLEncoder.encode(password, "utf-8");
					// ��ʽ
					// http://localhost:8080/web/LoginServlet?name=xxx&password=xxx

					// 1.��ȡһ���������ʵ��
					HttpClient client = new DefaultHttpClient();
					// 2.׼������ĵ�ַ
					HttpGet httpGet = new HttpGet(path + "?" + "name=" + param1
							+ "&password=" + param2);

					// 3.�ûس�������
					HttpResponse response = client.execute(httpGet);
					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						InputStream is = response.getEntity().getContent();
						byte[] result = StreamTools.getBytes(is);
						sendMessage(1, new String(result), handler);
					} else {
						sendMessage(0, "������״̬�쳣:" + code, handler);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendMessage(0, e.toString(), handler);
				}

			}
		}).start();
	}

	/**
	 * httpclient ������ļ򵥰�װ<br>
	 * new HttpClient���൱�ڵõ�һ�������<br>
	 * ͨ��post�����ύ���ݵ�������
	 * 
	 * @param path
	 * @param name
	 * @param password
	 * @param handler
	 */
	public static void sendDataByHttpClientPost(final String path,
			final String name, final String password, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					// 1.��ȡһ���������ʵ��
					HttpClient client = new DefaultHttpClient();
					// 2.׼������ĵ�ַ
					HttpPost httppost = new HttpPost(path);
					// ��ֵ��
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("name", name));
					parameters
							.add(new BasicNameValuePair("password", password));
					// ���������������
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
							parameters, "utf-8");
					// 3.����post���������ʵ��
					httppost.setEntity(entity);
					// 4.�������ݸ�������
					HttpResponse response = client.execute(httppost);

					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						InputStream is = response.getEntity().getContent();
						byte[] result = StreamTools.getBytes(is);
						sendMessage(1, new String(result), handler);
					} else {
						sendMessage(0, "������״̬�쳣:" + code, handler);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendMessage(0, e.toString(), handler);
				}

			}
		}).start();
	}

	/**
	 * ����handler��Ϣ
	 * 
	 * @param msgCode
	 * @param msgContent
	 * @param handler
	 */
	private static void sendMessage(int msgCode, String msgContent,
			Handler handler) {
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("MSG", msgContent);
		msg.setData(bundle);
		msg.what = msgCode;
		handler.sendMessage(msg);
	}
}

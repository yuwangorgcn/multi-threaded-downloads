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
 * get一次提交的数据量比较小，最多4k，内部其实通过组拼url的方式<br>
 * post可以提交比较大的数据，form表单的形式流的方式写到服务器
 */
public class DataService {

	/**
	 * 通过get请求提交数据到服务器
	 * 
	 * @param path
	 *            服务器servlet的地址
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @return 服务器返回回来的String数据
	 */
	public static void sendDataByGet(final String path, final String name,
			final String password, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					// 解决输入中文乱码
					String param1 = URLEncoder.encode(name, "utf-8");

					String param2 = URLEncoder.encode(password, "utf-8");
					// 格式
					// http://localhost:8080/web/LoginServlet?name=xxx&password=xxx
					URL url = new URL(path + "?" + "name=" + param1
							+ "&password=" + param2);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 默认就是GET，所以可省略
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					// 目前还没发送数据给服务器
					// 只要我们获取任何一个服务器返回的信息，数据就会被提交给服务器，得到服务器返回的流信息
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
	 * 通过post请求提交数据到服务器
	 * 
	 * @param path
	 *            服务器servlet的地址
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @return 服务器返回回来的String数据
	 */
	public static void sendDataByPost(final String path, final String name,
			final String password, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					// 解决输入中文乱码
					String param1 = URLEncoder.encode(name, "utf-8");
					String param2 = URLEncoder.encode(password, "utf-8");

					String data = "name=" + param1 + "&password=" + param2;
					// 格式
					// http://localhost:8080/web/Login.jsp
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 默认就是GET
					conn.setRequestMethod("POST");
					conn.setReadTimeout(5000);
					// 设置http协议可以向服务器写数据(默认false)
					conn.setDoOutput(true);
					// 设置http协议的消息头
					conn.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					conn.setRequestProperty("Content-Length", data.length()
							+ "");
					// 把我们准备好的data数据写给服务器
					OutputStream os = conn.getOutputStream();
					os.write(data.getBytes());
					// httpurlconnection底层实现outputstream是一个缓冲输出流

					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						byte[] result = StreamTools.getBytes(is);
						sendMessage(1, new String(result), handler);
					} else {
						sendMessage(0, "服务器状态异常:" + code, handler);
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
	 * httpclient 浏览器的简单包装<br>
	 * new HttpClient就相当于得到一个浏览器<br>
	 * 通过get请求提交数据到服务器
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
					// 解决输入中文乱码
					String param1 = URLEncoder.encode(name, "utf-8");
					String param2 = URLEncoder.encode(password, "utf-8");
					// 格式
					// http://localhost:8080/web/LoginServlet?name=xxx&password=xxx

					// 1.获取一个浏览器的实例
					HttpClient client = new DefaultHttpClient();
					// 2.准备请求的地址
					HttpGet httpGet = new HttpGet(path + "?" + "name=" + param1
							+ "&password=" + param2);

					// 3.敲回车发请求
					HttpResponse response = client.execute(httpGet);
					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						InputStream is = response.getEntity().getContent();
						byte[] result = StreamTools.getBytes(is);
						sendMessage(1, new String(result), handler);
					} else {
						sendMessage(0, "服务器状态异常:" + code, handler);
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
	 * httpclient 浏览器的简单包装<br>
	 * new HttpClient就相当于得到一个浏览器<br>
	 * 通过post请求提交数据到服务器
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

					// 1.获取一个浏览器的实例
					HttpClient client = new DefaultHttpClient();
					// 2.准备请求的地址
					HttpPost httppost = new HttpPost(path);
					// 键值对
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("name", name));
					parameters
							.add(new BasicNameValuePair("password", password));
					// 解决输入中文乱码
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
							parameters, "utf-8");
					// 3.设置post请求的数据实体
					httppost.setEntity(entity);
					// 4.发送数据个服务器
					HttpResponse response = client.execute(httppost);

					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						InputStream is = response.getEntity().getContent();
						byte[] result = StreamTools.getBytes(is);
						sendMessage(1, new String(result), handler);
					} else {
						sendMessage(0, "服务器状态异常:" + code, handler);
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
	 * 传送handler信息
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

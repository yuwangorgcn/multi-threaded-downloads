安卓范例
===================================
  android互联网相关的常用功能<h1>

设置
-----------------------------------


1. 下载并解压[Tomcat7.0 core](https://tomcat.apache.org/download-70.cgi)
2. 环境变量加入CATALINA_ HOME(D:\apache-tomcat-7.0.61), JAVA_ HOME(C:\Program Files\Java\jdk1.8.0_40)
3. cmd:D:\apache-tomcat-7.0.61\bin\startup.bat
4. 浏览器输入[http://localhost:8080/](http://localhost:8080/)检查
5. 下载并安装[httpwatch](https://www.httpwatch.com/download/), 使用IE

### 内容
```   
	<uses-permission android:name="android.permission.INTERNET"/>
```   
* 查看网络图片(AsyncTask)
	* [ImageViewerActivity](https://github.com/JetAircraft/Web-toolkits/blob/master/src/com/example/web/ImageViewerActivity.java)

* 查看html网页(handler/thread)
	* [HtmlViewerActivity](https://github.com/JetAircraft/Web-toolkits/blob/master/src/com/example/web/HtmlViewerActivity.java)
	* 支持多编码 
* 获取xml的数据并用listview呈现(webView)
	* [ListActivity](https://github.com/JetAircraft/Web-toolkits/blob/master/src/com/example/web/ListActivity.java)
* 获取xml的数据并用listview呈现(imageView)
* 先下载图片存到SD card,再读
* 
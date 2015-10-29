package com.lyl.day10downloadpic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	private ImageView iv_picture;
	Handler handler = new Handler(){
		
		//回掉函数，从messageQueue中取出一个信息，可以处理子线程发来的消息
		public void handleMessage(Message msg) {
			
			if(msg.what == 1){
				Bitmap decodeFile = BitmapFactory.decodeFile(getCacheDir()+"/ali.jpg");
				iv_picture.setImageBitmap(decodeFile);
			}else{
				Toast.makeText(MainActivity.this, "获取图片失败", 0).show();
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		iv_picture = (ImageView) findViewById(R.id.iv_picture);
	}

	/*从tomcat服务器获得图片
	 * 1,为了防止浪费流量，我们第一次下载了图片后，把图片放在应用包的缓存中
	 * 当再次获取图片时，先看缓存中是否有图片，有的话，直接从缓存中获取，没有在从服务器下载
	 *2,由于新版本的android中，不允许某个程序让主线程处于阻塞状态，所以，我们在从服务器下载图片时
	 *涉及到阻塞状态，所以我们要在子线程中实现下载图片
	 */
	public void getPicture(View v){
		
		//查看应用包缓冲区
		File file = new File(getCacheDir(), "ali.jpg");
		if(file.exists()){
			Bitmap decodeFile = BitmapFactory.decodeFile(getCacheDir()+"/ali.jpg");
			iv_picture.setImageBitmap(decodeFile);
		}
	}
	
	class Mythrea extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			//发送给server端
			URL url;
			
			try {
				url = new URL("http://localhost:8080/imgs/ali.jpg");
				
				//建立连接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				//连接超时3s
				conn.setConnectTimeout(3000);
				
				//读取缓冲区超时
				conn.setReadTimeout(5000);
				
				//设置请求方法
				conn.setRequestMethod("GET");
				
				conn.connect();		//建立连接
				
				//服务端响应200，说明有着图片
				if(conn.getResponseCode() == 200){
					InputStream in = conn.getInputStream();
					
					FileOutputStream fos = new FileOutputStream(new File(getCacheDir(),"ali.jpg"));
					byte[] b = new byte[1024];
					int len = 0;
					while((len = in.read(b, 0, 1024)) != -1){
						
						fos.write(b, 0, len);
					}
					
					fos.close();
					in.close();
					
					//设置标记，说明缓冲区中有ali图片
					Message mes = new Message();
					mes.what = 1;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

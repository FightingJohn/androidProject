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
		
		//�ص���������messageQueue��ȡ��һ����Ϣ�����Դ������̷߳�������Ϣ
		public void handleMessage(Message msg) {
			
			if(msg.what == 1){
				Bitmap decodeFile = BitmapFactory.decodeFile(getCacheDir()+"/ali.jpg");
				iv_picture.setImageBitmap(decodeFile);
			}else{
				Toast.makeText(MainActivity.this, "��ȡͼƬʧ��", 0).show();
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		iv_picture = (ImageView) findViewById(R.id.iv_picture);
	}

	/*��tomcat���������ͼƬ
	 * 1,Ϊ�˷�ֹ�˷����������ǵ�һ��������ͼƬ�󣬰�ͼƬ����Ӧ�ð��Ļ�����
	 * ���ٴλ�ȡͼƬʱ���ȿ��������Ƿ���ͼƬ���еĻ���ֱ�Ӵӻ����л�ȡ��û���ڴӷ���������
	 *2,�����°汾��android�У�������ĳ�����������̴߳�������״̬�����ԣ������ڴӷ���������ͼƬʱ
	 *�漰������״̬����������Ҫ�����߳���ʵ������ͼƬ
	 */
	public void getPicture(View v){
		
		//�鿴Ӧ�ð�������
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
			
			//���͸�server��
			URL url;
			
			try {
				url = new URL("http://localhost:8080/imgs/ali.jpg");
				
				//��������
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				//���ӳ�ʱ3s
				conn.setConnectTimeout(3000);
				
				//��ȡ��������ʱ
				conn.setReadTimeout(5000);
				
				//�������󷽷�
				conn.setRequestMethod("GET");
				
				conn.connect();		//��������
				
				//�������Ӧ200��˵������ͼƬ
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
					
					//���ñ�ǣ�˵������������aliͼƬ
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

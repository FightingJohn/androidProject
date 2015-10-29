package com.example.day_13opensourcedownload;

import java.io.File;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.event.OnProgressChanged;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText et_path;
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		et_path = (EditText) findViewById(R.id.et_path);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		
	}
	
	public void download(View v){
		
		String path = et_path.getText().toString();
		HttpUtils http = new HttpUtils();
		
		http.download(path, "/sdcard/com.exe", new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "下载成功", 0).show();
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("Fail", arg0.toString());
				Toast.makeText(MainActivity.this, "下载失败", 0).show();

			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				pb.setMax((int)total);
				pb.setProgress((int)current);
				//super.onLoading(total, current, isUploading);
			}
			
			
		});
		
	}
}

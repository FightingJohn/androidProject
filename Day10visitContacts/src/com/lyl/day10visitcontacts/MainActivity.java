package com.lyl.day10visitcontacts;

import com.lyl.bean.Contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	private static final String TAG = "Day10visitContacts";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ContentResolver resolver = getContentResolver();
		
		MyContentObsever observer = new MyContentObsever(new Handler());
		
		//注册监听者
		resolver.registerContentObserver(Uri.parse("content://com.android.contacts/raw_contacts"),
				false, observer);
		
		
	}
	
	//监听器类，监听数据提供者是否发生变化
	class MyContentObsever extends ContentObserver{

		public MyContentObsever(Handler handler) {
			super(handler);
		}
		
		//回调函数，当数据提供者数据发生变化，调用此函数
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			
			Log.i(TAG, "database has been chaged");
		}
		
	}

	public void queryContacts(View v){
		
		ContentResolver resolver = getContentResolver();
		
		//查询raw_contacts获得的contact_id
		Cursor raw_contacts_cursor = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
				new String[]{"contact_id"}, null, null, null);
		
		//通过contact_id获得data表中联系人的信息
		while(raw_contacts_cursor.moveToNext()){
			
			int contact_id = raw_contacts_cursor.getInt(0);
			
			//注意：这里查询表中的mimetype_id会报错，说不存在mimetype_id(int)。用mimetype(String)可以查询到
			Cursor data_cursor = resolver.query(Uri.parse("content://com.android.contacts/data"), 
					new String[]{"mimetype","data1"}, "raw_contact_id=?", new String[]{""+contact_id}, null);
			
			Contact contact = new Contact();
			while(data_cursor.moveToNext()){
				
				String mimetype = data_cursor.getString(0);
				String data = data_cursor.getString(1);
				
				//根据mimetype的类型，获得data对应的值（姓名，电话，email）
				if("vnd.android.cursor.item/email_v2".equals(mimetype)){
					contact.setEmail(data);
				}else if("vnd.android.cursor.item/name".equals(mimetype)){
					contact.setName(data);
				}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
					contact.setPhoneNum(data);
				}		
			}
			
			//打印log，显示一个联系人信息
			Log.i(TAG, contact.toString());
		}
	}
}

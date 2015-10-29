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
		
		//ע�������
		resolver.registerContentObserver(Uri.parse("content://com.android.contacts/raw_contacts"),
				false, observer);
		
		
	}
	
	//�������࣬���������ṩ���Ƿ����仯
	class MyContentObsever extends ContentObserver{

		public MyContentObsever(Handler handler) {
			super(handler);
		}
		
		//�ص��������������ṩ�����ݷ����仯�����ô˺���
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			
			Log.i(TAG, "database has been chaged");
		}
		
	}

	public void queryContacts(View v){
		
		ContentResolver resolver = getContentResolver();
		
		//��ѯraw_contacts��õ�contact_id
		Cursor raw_contacts_cursor = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
				new String[]{"contact_id"}, null, null, null);
		
		//ͨ��contact_id���data������ϵ�˵���Ϣ
		while(raw_contacts_cursor.moveToNext()){
			
			int contact_id = raw_contacts_cursor.getInt(0);
			
			//ע�⣺�����ѯ���е�mimetype_id�ᱨ��˵������mimetype_id(int)����mimetype(String)���Բ�ѯ��
			Cursor data_cursor = resolver.query(Uri.parse("content://com.android.contacts/data"), 
					new String[]{"mimetype","data1"}, "raw_contact_id=?", new String[]{""+contact_id}, null);
			
			Contact contact = new Contact();
			while(data_cursor.moveToNext()){
				
				String mimetype = data_cursor.getString(0);
				String data = data_cursor.getString(1);
				
				//����mimetype�����ͣ����data��Ӧ��ֵ���������绰��email��
				if("vnd.android.cursor.item/email_v2".equals(mimetype)){
					contact.setEmail(data);
				}else if("vnd.android.cursor.item/name".equals(mimetype)){
					contact.setName(data);
				}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
					contact.setPhoneNum(data);
				}		
			}
			
			//��ӡlog����ʾһ����ϵ����Ϣ
			Log.i(TAG, contact.toString());
		}
	}
}

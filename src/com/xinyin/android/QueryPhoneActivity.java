package com.xinyin.android;

import com.xinyin.android.util.DBHelper;
import com.xinyin.android.util.ServiceCtrl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryPhoneActivity extends BaseActivity {
	Button query_btn;
	EditText phone_et;
	TextView result_tv;
	ProgressDialog dialog;
	DBHelper dbHelper;
	SQLiteDatabase database;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_phone);
		dbHelper = new DBHelper(this);
		initView();
		dialog = ProgressDialog.show(QueryPhoneActivity.this, "", "查询中，请稍后....");
		dialog.dismiss();
		query_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = phone_et.getText().toString();
				if(phone.length() < 7){
					result_tv.setText("至少输入手机号码的前7位!");
					return;
				}
				database = dbHelper.getWritableDatabase();
				cursor = database.rawQuery("select * from "
						+ DBHelper.TABLE_NAME_PHONE, null);
				if(!compare(phone, cursor)){
					ContentValues cv = new ContentValues();
					cv.put("phoneNum", phone);
					database.insert(DBHelper.TABLE_NAME_PHONE, null, cv);
				}
				cursor.close();
				database.close();
				QueryTask task = new QueryTask();
				task.execute(phone);
			}
		});
	}
	void initView(){
		query_btn = (Button) findViewById(R.id.query_btn);
		phone_et = (EditText) findViewById(R.id.phone_et);
		result_tv = (TextView) findViewById(R.id.query_result);
	}
	class QueryTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			String str = null;
			ServiceCtrl ctrl = new ServiceCtrl();
			str = ctrl.queryPhone(params[0]);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if(!TextUtils.isEmpty(result)){
				String[] str = result.split("：");
				if(str.length == 2){
					String[] str2 = str[1].split(" ");
					result_tv.setText("手机号:  "+str[0]+"\n\n城    市:  "+str2[0]+str2[1]+"\n\n卡类型:  "+str2[2]);
				}else{
					result_tv.setText(result);
				}
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
			result_tv.setText("");
			super.onPreExecute();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu.FIRST + 1, Menu.NONE, "查询记录");
		menu.add(0, Menu.FIRST + 2, Menu.NONE, "清空查询记录");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		database = dbHelper.getWritableDatabase();
		if (item.getItemId() == Menu.FIRST + 1) {
			cursor = database.rawQuery("select * from "
					+ DBHelper.TABLE_NAME_PHONE, null);
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("最近的查询记录");
			builder.setCursor(cursor, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					cursor.moveToPosition(which);
					phone_et.setText(cursor.getString(1));
					cursor.close();
				}
			}, "phoneNum");
			builder.create().show();
		}
		if (item.getItemId() == Menu.FIRST + 2) {
			database.delete(DBHelper.TABLE_NAME_PHONE, null, null);
		}
		database.close();
		return true;
	}
	private static boolean compare(String order, Cursor c) {
		boolean flag = false;
		while (c.moveToNext()) {
			if (order.equals(c.getString(1))) {
				flag = true;
			}
		}
		return flag;
	}
}

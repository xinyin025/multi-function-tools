package com.xinyin.android;

import com.xinyin.android.entity.IdentityCard;
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
import android.widget.Toast;

public class QueryIdentityActivity extends BaseActivity {
	Button query_btn;
	EditText identity_et;
	ProgressDialog dialog;
	TextView result_tv;
	DBHelper dbHelper;
	SQLiteDatabase database;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_identity);
		initView();
		dbHelper = new DBHelper(this);
		dialog = ProgressDialog.show(QueryIdentityActivity.this, "", "查询中，请稍后....");
		dialog.dismiss();
		query_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String id = identity_et.getText().toString();
				if(TextUtils.isEmpty(id)){
					Toast.makeText(QueryIdentityActivity.this, "请输入15或者18位有效身份证号!", Toast.LENGTH_SHORT).show();
					return;
				}
				database = dbHelper.getWritableDatabase();
				cursor = database.rawQuery("select * from "
						+ DBHelper.TABLE_NAME_IDCARD, null);
				if(!compare(id, cursor)){
					ContentValues cv = new ContentValues();
					cv.put("idNum", id);
					database.insert(DBHelper.TABLE_NAME_IDCARD, null, cv);
				}
				cursor.close();
				database.close();
				QueryIdentity task = new QueryIdentity();
				task.execute(id);
			}
		});
	}
	void initView(){
		query_btn = (Button) findViewById(R.id.query_btn);
		identity_et = (EditText) findViewById(R.id.identity_et);
		result_tv = (TextView) findViewById(R.id.query_result);
	}
	
	class QueryIdentity extends AsyncTask<String, Integer, IdentityCard>{

		@Override
		protected IdentityCard doInBackground(String... params) {
			IdentityCard id = null;
			ServiceCtrl serviceCtrl = new ServiceCtrl();
			id =  serviceCtrl.queryIdentity(params[0]);
			return id;
		}

		@Override
		protected void onPostExecute(IdentityCard result) {
			dialog.dismiss();
			if(result != null){
				String gender = null;
				if("m".equals(result.getGender())){
					gender = "男";
				}else{
					gender = "女";
				}
				result_tv.setText("身份证号："+result.getCode()+"\n出  身  地："+result.getLocation()+"\n生        日："+result.getBirthday()+"\n性        别："+gender);
			}else{
				result_tv.setText("未查到该身份证号的有关信息" +
						"\n请确认输入的身份证号是否正确！");
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
					+ DBHelper.TABLE_NAME_IDCARD, null);
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("最近的查询记录");
			builder.setCursor(cursor, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					cursor.moveToPosition(which);
					identity_et.setText(cursor.getString(1));
					cursor.close();
				}
			}, "idNum");
			builder.create().show();
		}
		if (item.getItemId() == Menu.FIRST + 2) {
			database.delete(DBHelper.TABLE_NAME_IDCARD, null, null);
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

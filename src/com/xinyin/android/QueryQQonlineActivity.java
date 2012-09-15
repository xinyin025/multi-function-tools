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
import android.widget.Toast;

public class QueryQQonlineActivity extends BaseActivity {
	Button query_btn;
	TextView result_tv;
	EditText qq_et;
	String qq;
	ProgressDialog dialog;
	DBHelper dbHelper;
	SQLiteDatabase database;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_qqonline);
		initView();
		dbHelper = new DBHelper(this);
		dialog = ProgressDialog.show(QueryQQonlineActivity.this, "",
				"查询中，请稍候...");
		dialog.dismiss();
		query_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				qq = qq_et.getText().toString().trim();
				if(TextUtils.isEmpty(qq)){
					Toast.makeText(QueryQQonlineActivity.this, "请输入要查询的QQ号！", Toast.LENGTH_SHORT).show();
					return;
				}
				database = dbHelper.getWritableDatabase();
				cursor = database.rawQuery("select * from "
						+ DBHelper.TABLE_NAME_QQ, null);
				if(!compare(qq, cursor)){
					ContentValues cv = new ContentValues();
					cv.put("qqNum", qq);
					database.insert(DBHelper.TABLE_NAME_QQ, null, cv);
				}
				cursor.close();
				database.close();
				QueryQQTask task = new QueryQQTask();
				task.execute(qq);
			}
		});
	}

	void initView() {
		query_btn = (Button) findViewById(R.id.query_btn);
		result_tv = (TextView) findViewById(R.id.result);
		qq_et = (EditText) findViewById(R.id.qq_et);
	}

	class QueryQQTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			ServiceCtrl serviceCtrl = new ServiceCtrl();
			return serviceCtrl.queryQQonline(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if ("Y".equals(result)) {
				result_tv.setText("   Q   Q   ：" + qq + "\n\n当前状态：在线");
			} else if ("N".equals(result)) {
				result_tv.setText("   Q   Q   ：" + qq + "\n\n当前状态：离线");
			} else {
				result_tv.setText("QQ号输入有误!");
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
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
					+ DBHelper.TABLE_NAME_QQ, null);
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("最近的查询记录");
			builder.setCursor(cursor, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					cursor.moveToPosition(which);
					qq_et.setText(cursor.getString(1));
					cursor.close();
				}
			}, "qqNum");
			builder.create().show();
		}
		if (item.getItemId() == Menu.FIRST + 2) {
			database.delete(DBHelper.TABLE_NAME_QQ, null, null);
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

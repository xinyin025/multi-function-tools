package com.xinyin.android;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinyin.android.adapter.QueryKuaidiAdapter;
import com.xinyin.android.entity.KuaidiEntity;
import com.xinyin.android.util.DBHelper;
import com.xinyin.android.util.ServiceCtrl;
import com.xinyin.android.widget.ListViewHeight;

public class QueryKuaidiActivity extends BaseActivity {
	EditText orderid_et;
	Button com_btn, query_btn;
	ListView result_lv;
	String[] coms_zhongwen = { "顺丰", "圆通", "中通", "韵达", "汇通", "宅急送", "德邦",
			"EMS", "申通" ,"如风达","全峰"};
	String[] coms_pinyin = { "shunfeng", "yuantong", "zhongtong", "yunda",
			"huitongkuaidi", "zhaijisong", "debangwuliu", "ems", "shentong","rufengda","quanfengkuaidi" };
	String com, com_c, order;
	TextView result_tv;
	ProgressDialog dialog;
	DBHelper dbHelper;
	SQLiteDatabase database;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_kuaidi);
		dialog = ProgressDialog.show(QueryKuaidiActivity.this, "",
				"查询中，请稍后....");
		dialog.dismiss();
		dbHelper = new DBHelper(this);
		initView();
		query_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				order = orderid_et.getText().toString().trim();
				if (TextUtils.isEmpty(com)) {
					Toast.makeText(QueryKuaidiActivity.this, "请选择快递公司!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(order)) {
					Toast.makeText(QueryKuaidiActivity.this, "请输入运单号!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				database = dbHelper.getWritableDatabase();
				cursor = database.rawQuery("select * from "
						+ DBHelper.TABLE_NAME_KUAIDI, null);
				if(!compare(order, cursor)){
					ContentValues cv = new ContentValues();
					cv.put("kuaidiOrder", order);
					cv.put("com", com);
					cv.put("comName", com_c);
					database.insert(DBHelper.TABLE_NAME_KUAIDI, null, cv);
				}
				cursor.close();
				database.close();
				QueryKuaidi task = new QueryKuaidi();
				task.execute(com, order);
			}
		});
		com_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						QueryKuaidiActivity.this);
				builder.setTitle("请选择快递公司");
				builder.setItems(coms_zhongwen, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						com = coms_pinyin[which];
						com_c = coms_zhongwen[which];
						com_btn.setText(coms_zhongwen[which]);
					}
				});
				builder.create().show();
			}
		});
	}

	void initView() {
		orderid_et = (EditText) findViewById(R.id.orderid_et);
		com_btn = (Button) findViewById(R.id.com_btn);
		query_btn = (Button) findViewById(R.id.query_btn);
		result_lv = (ListView) findViewById(R.id.kuaidi_result_lv);
		result_tv = (TextView) findViewById(R.id.kuaidi_result_tv);
	}

	class QueryKuaidi extends AsyncTask<String, Integer, List<KuaidiEntity>> {

		@Override
		protected List<KuaidiEntity> doInBackground(String... params) {
			ServiceCtrl serviceCtrl = new ServiceCtrl();
			List<KuaidiEntity> list = serviceCtrl.queryKuaidi(params[0],
					params[1]);
			return list;
		}

		@Override
		protected void onPostExecute(List<KuaidiEntity> result) {
			dialog.dismiss();
			if (result.size() != 0) {
				QueryKuaidiAdapter adapter = new QueryKuaidiAdapter(
						QueryKuaidiActivity.this, result);
				result_lv.setAdapter(adapter);
				ListViewHeight.setListViewHeightBasedOnChildren(result_lv);
				result_tv.setText("查询数据由快递100提供，感谢快递100的支持！");
				result_tv.setVisibility(View.VISIBLE);
				result_lv.setEnabled(false);
				result_lv.setVisibility(View.VISIBLE);
			} else {
				result_lv.setVisibility(View.GONE);
				result_tv.setText("运单暂无查询结果，请过一段时间尝试！");
				result_tv.setVisibility(View.VISIBLE);
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
					+ DBHelper.TABLE_NAME_KUAIDI, null);
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("最近的查询记录");
			builder.setCursor(cursor, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					cursor.moveToPosition(which);
					orderid_et.setText(cursor.getString(1));
					com = cursor.getString(2);
					com_btn.setText(cursor.getString(3));
					cursor.close();
				}
			}, "kuaidiOrder");
			builder.create().show();
		}
		if (item.getItemId() == Menu.FIRST + 2) {
			database.delete(DBHelper.TABLE_NAME_KUAIDI, null, null);
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

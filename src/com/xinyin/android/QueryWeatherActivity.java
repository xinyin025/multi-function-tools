package com.xinyin.android;

import java.util.List;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinyin.android.util.DBHelper;
import com.xinyin.android.util.ServiceCtrl;

public class QueryWeatherActivity extends BaseActivity {
	Button query_btn;
	EditText weather_et;
	ProgressDialog dialog;
	ImageView today_img,next_1_img,next_2_img,next_3_img,next_4_img;
	TextView today_time,today_wea,today_temp,today_cloud;
	TextView next_1_time,next_1_wea,next_1_temp;
	TextView next_2_time,next_2_wea,next_2_temp;
	TextView next_3_time,next_3_wea,next_3_temp;
	TextView next_4_time,next_4_wea,next_4_temp;
	LinearLayout result_l;
	TextView result_null;
	DBHelper dbHelper;
	SQLiteDatabase database;
	Cursor cursor;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_weather);
		initView();
		dbHelper = new DBHelper(this);
		dialog = ProgressDialog.show(QueryWeatherActivity.this, "", "查询中，请稍后....");
		dialog.dismiss();
		query_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String cityname = weather_et.getText().toString();
				database = dbHelper.getWritableDatabase();
				cursor = database.rawQuery("select * from "
						+ DBHelper.TABLE_NAME_WEATHER, null);
				if(!compare(cityname, cursor)){
					ContentValues cv = new ContentValues();
					cv.put("cityName", cityname);
					database.insert(DBHelper.TABLE_NAME_WEATHER, null, cv);
				}
				cursor.close();
				database.close();
				QueryWeatherTask task = new QueryWeatherTask();
				task.execute(cityname);
			}
		});
	}
	void initView(){
		query_btn = (Button) findViewById(R.id.query_btn);
		weather_et = (EditText) findViewById(R.id.city_et);
		today_img = (ImageView) findViewById(R.id.today_img);
		next_1_img = (ImageView) findViewById(R.id.next_1_img);
		next_2_img = (ImageView) findViewById(R.id.next_2_img);
		next_3_img = (ImageView) findViewById(R.id.next_3_img);
		next_4_img = (ImageView) findViewById(R.id.next_4_img);
		today_time = (TextView) findViewById(R.id.today_time);
		today_wea = (TextView) findViewById(R.id.today_wea);
		today_temp = (TextView) findViewById(R.id.today_temp);
		today_cloud = (TextView) findViewById(R.id.today_cloud);
		next_1_time = (TextView) findViewById(R.id.next_1_time);
		next_1_wea = (TextView) findViewById(R.id.next_1_wea);
		next_1_temp = (TextView) findViewById(R.id.next_1_temp);
		next_2_time = (TextView) findViewById(R.id.next_2_time);
		next_2_wea = (TextView) findViewById(R.id.next_2_wea);
		next_2_temp = (TextView) findViewById(R.id.next_2_temp);
		next_3_time = (TextView) findViewById(R.id.next_3_time);
		next_3_wea = (TextView) findViewById(R.id.next_3_wea);
		next_3_temp = (TextView) findViewById(R.id.next_3_temp);
		next_4_time = (TextView) findViewById(R.id.next_4_time);
		next_4_wea = (TextView) findViewById(R.id.next_4_wea);
		next_4_temp = (TextView) findViewById(R.id.next_4_temp);
		
		result_l = (LinearLayout) findViewById(R.id.result_l);
		result_null = (TextView) findViewById(R.id.result_null);
	}
	class QueryWeatherTask extends AsyncTask<String, Integer, List<String>>{

		@Override
		protected List<String> doInBackground(String... params) {
			List<String> list = null;
			ServiceCtrl serviceCtrl = new ServiceCtrl();
			list = serviceCtrl.queryWeather(params[0]);
			return list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			dialog.dismiss();
			if(result!=null){
				if(result.size()>2){
//					System.out.println("-----list.size-----"+result.size());
//					for(int i=0;i<result.size();i++){
//						System.out.println("----"+i+"----"+result.get(i));
//					}
					String todayimg = result.get(11).substring(0, result.get(11).indexOf("."));
					int id = getResources().getIdentifier("a_"+todayimg, "drawable", "com.xinyin.android");
//					System.out.println("a_"+todayimg+"---id0---"+id);
					today_img.setImageResource(id);
					today_time.setText((result.get(7).split(" "))[0]);
					today_wea.setText((result.get(7).split(" "))[1]);
					String[] str = result.get(8).split("/");
					if(str.length==2){
						today_temp.setText(str[0]+"~"+str[1]);
					}else{
						today_temp.setText(result.get(8));
					}
					today_cloud.setText(result.get(9));
					
					String next_1_img_str = result.get(16).substring(0, result.get(16).indexOf("."));
					int next_1_id = getResources().getIdentifier("a_"+next_1_img_str, "drawable", "com.xinyin.android");
//					System.out.println("a_"+next_1_img_str+"---id1---"+next_1_id);
					next_1_img.setImageResource(next_1_id);
					String[] str_1 = result.get(12).split(" ");
					next_1_time.setText(str_1[0]);
					next_1_wea.setText(str_1[1]);
					String[] str_1_t = result.get(13).split("/");
					if(str_1_t.length==2){
						next_1_temp.setText(str_1_t[0]+"~"+str_1_t[1]);
					}else{
						next_1_temp.setText(result.get(13));
					}
					
					String next_2_img_str = result.get(21).substring(0, result.get(21).indexOf("."));
					int next_2_id = getResources().getIdentifier("a_"+next_2_img_str, "drawable", "com.xinyin.android");
//					System.out.println("a_"+next_2_img_str+"---id2---"+next_2_id);
					next_2_img.setImageResource(next_2_id);
					String[] str_2 = result.get(17).split(" ");
					next_2_time.setText(str_2[0]);
					next_2_wea.setText(str_2[1]);
					String[] str_2_t = result.get(18).split("/");
					if(str_2_t.length==2){
						next_2_temp.setText(str_2_t[0]+"~"+str_2_t[1]);
					}else{
						next_2_temp.setText(result.get(18));
					}
					
					String next_3_img_str = result.get(26).substring(0, result.get(26).indexOf("."));
					int next_3_id = getResources().getIdentifier("a_"+next_3_img_str, "drawable", "com.xinyin.android");
//					System.out.println("a_"+next_3_img_str+"---id3---"+next_3_id);
					next_3_img.setImageResource(next_3_id);
					String[] str_3 = result.get(22).split(" ");
					next_3_time.setText(str_3[0]);
					next_3_wea.setText(str_3[1]);
					String[] str_3_t = result.get(23).split("/");
					if(str_3_t.length==2){
						next_3_temp.setText(str_3_t[0]+"~"+str_3_t[1]);
					}else{
						next_3_temp.setText(result.get(23));
					}
					
					String next_4_img_str = result.get(31).substring(0, result.get(31).indexOf("."));
					int next_4_id = getResources().getIdentifier("a_"+next_4_img_str, "drawable", "com.xinyin.android");
//					System.out.println("a_"+next_4_img_str+"---id4---"+next_4_id);
					next_4_img.setImageResource(next_4_id);
					String[] str_4 = result.get(27).split(" ");
					next_4_time.setText(str_4[0]);
					next_4_wea.setText(str_4[1]);
					String[] str_4_t = result.get(28).split("/");
					if(str_4_t.length==2){
						next_4_temp.setText(str_4_t[0]+"~"+str_4_t[1]);
					}else{
						next_4_temp.setText(result.get(28));
					}
						
					result_l.setVisibility(View.VISIBLE);
				}else{
					result_null.setText(result.get(0).substring(0, result.get(0).indexOf("http")));
					result_null.setVisibility(View.VISIBLE);
				}
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
			result_l.setVisibility(View.GONE);
			result_null.setVisibility(View.GONE);
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
					+ DBHelper.TABLE_NAME_WEATHER, null);
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("最近的查询记录");
			builder.setCursor(cursor, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					cursor.moveToPosition(which);
					weather_et.setText(cursor.getString(1));
					cursor.close();
				}
			}, "cityName");
			builder.create().show();
		}
		if (item.getItemId() == Menu.FIRST + 2) {
			database.delete(DBHelper.TABLE_NAME_WEATHER, null, null);
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

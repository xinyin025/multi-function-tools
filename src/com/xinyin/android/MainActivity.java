package com.xinyin.android;

import com.baidu.mobstat.StatService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends BaseActivity {
	Button query_number,query_weather,query_identity,query_kuaidi,query_qqonline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		StatService.setAppKey("29ee356771");
		StatService.setAppChannel("Baidu Market");
		StatService.setLogSenderDelayed(10);
		initView();
	}

	void initView(){
		query_number = (Button) findViewById(R.id.query_number_btn);
		query_weather = (Button) findViewById(R.id.query_weather_btn);
		query_identity = (Button) findViewById(R.id.query_identity_btn);
		query_kuaidi = (Button) findViewById(R.id.query_kuaidi_btn);
		query_qqonline = (Button) findViewById(R.id.query_qqonline_btn);
		query_number.setOnClickListener(new MyClickListener());
		query_weather.setOnClickListener(new MyClickListener());
		query_identity.setOnClickListener(new MyClickListener());
		query_kuaidi.setOnClickListener(new MyClickListener());
		query_qqonline.setOnClickListener(new MyClickListener());
	}
	
	class MyClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.query_number_btn:
				intent = new Intent(MainActivity.this,QueryPhoneActivity.class);
				startActivity(intent);
				break;
			case R.id.query_weather_btn:
				intent = new Intent(MainActivity.this,QueryWeatherActivity.class);
				startActivity(intent);
				break;
			case R.id.query_identity_btn:
				intent = new Intent(MainActivity.this,QueryIdentityActivity.class);
				startActivity(intent);
				break;
			case R.id.query_kuaidi_btn:
				intent = new Intent(MainActivity.this,QueryKuaidiActivity.class);
				startActivity(intent);
				break;
			case R.id.query_qqonline_btn:
				intent = new Intent(MainActivity.this,QueryQQonlineActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
			
		}
		
	}
}

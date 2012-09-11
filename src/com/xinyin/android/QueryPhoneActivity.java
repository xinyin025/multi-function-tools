package com.xinyin.android;

import com.xinyin.android.util.ServiceCtrl;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryPhoneActivity extends BaseActivity {
	Button query_btn;
	EditText phone_et;
	TextView result_tv;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_phone);
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
			str = ctrl.queryPhone2(params[0]);
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
}

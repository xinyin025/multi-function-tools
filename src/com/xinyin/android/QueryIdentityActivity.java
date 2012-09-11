package com.xinyin.android;

import com.xinyin.android.entity.IdentityCard;
import com.xinyin.android.util.ServiceCtrl;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryIdentityActivity extends BaseActivity {
	Button query_btn;
	EditText identity_et;
	ProgressDialog dialog;
	TextView result_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_identity);
		initView();
		dialog = ProgressDialog.show(QueryIdentityActivity.this, "", "查询中，请稍后....");
		dialog.dismiss();
		query_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String id = identity_et.getText().toString();
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
}

package com.xinyin.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinyin.android.R;
import com.xinyin.android.entity.KuaidiEntity;

public class QueryKuaidiAdapter extends BaseAdapter {
	private Context context;
	private List<KuaidiEntity> list;
	private int[] bgds = { R.drawable.list_bg_top,
			R.drawable.list_bg_middle, R.drawable.list_bg_bottom };

	public QueryKuaidiAdapter(Context context, List<KuaidiEntity> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		int i = 0;
		if(list == null){
			i = 0;
		}else{
			i = list.size();
		}
		return i;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.kuaidi_result_item, null);
		TextView tv1 = (TextView) convertView.findViewById(R.id.kuaidi_result_time);
		TextView tv2 = (TextView) convertView.findViewById(R.id.kuaidi_result_context);
		tv1.setText(list.get(position).getTime());
		tv2.setText(list.get(position).getContext());
		if (position == 0) {
			convertView.setBackgroundResource(bgds[0]);
		} else if (position == list.size()-1) {
			convertView.setBackgroundResource(bgds[2]);
		} else {
			convertView.setBackgroundResource(bgds[1]);
		}
		return convertView;
	}

}

package com.chunsoft.ttgo.myself;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.ttgo.R;

public class Tk_A extends Activity {
	private List<String> datas;
	private ListView lv_back;
	private TextView tv_title;
	private ProBackAdapter adapter;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tk_a);
		FindView();
		Init();
	}

	private void Init() {
		mContext = Tk_A.this;
		tv_title.setText("ÕÀªı/ €∫Û");
		datas = new ArrayList<String>();
		for (int i = 0; i < 15; i++) {
			datas.add(i + "");
		}
		adapter = new ProBackAdapter(mContext, datas, R.layout.returnitem);
		lv_back.setAdapter(adapter);
	}

	private void FindView() {
		lv_back = (ListView) findViewById(R.id.lv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	public static class ProBackAdapter extends CommonAdapter<String> {

		public ProBackAdapter(Context context, List<String> datas, int layoutId) {
			super(context, datas, layoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder holder, String t) {

		}
	}
}

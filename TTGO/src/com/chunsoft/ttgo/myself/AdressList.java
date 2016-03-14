package com.chunsoft.ttgo.myself;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.ttgo.R;
import com.chunsoft.view.FlexiListView;

public class AdressList extends Activity implements OnClickListener {
	List<String> datas = new ArrayList<>();

	ListAdapter adapter;
	FlexiListView lv;
	private Button add_adress;
	private Context mContext;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adress_list);
		FindView();
		SetData();
		Click();
		adapter = new ListAdapter(AdressList.this, datas, R.layout.adress_item);
		lv.setAdapter(adapter);
	}

	private void SetData() {
		tv_title.setText("管理收货地址");
		for (int i = 0; i < 10; i++) {
			datas.add(i + "");
		}
	}

	private void FindView() {
		mContext = AdressList.this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv = (FlexiListView) findViewById(R.id.flv);
		add_adress = (Button) findViewById(R.id.add_adress);
	}

	private void Click() {
		add_adress.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_adress:
			Intent intent = new Intent(mContext, Add_Adress.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}

class ListAdapter extends CommonAdapter<String> {

	public ListAdapter(Context context, List<String> datas, int layoutId) {
		super(context, datas, layoutId);

	}

	@Override
	public void convert(ViewHolder holder, String t) {

	}

}
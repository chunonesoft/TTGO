package com.chunsoft.ttgo.home;

import com.chunsoft.ttgo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Search_A extends Activity implements OnClickListener{
	private Context mContext;
	private ImageView iv_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_a);
		findView();
		init();
		onClick();
	}
	/*����ʵ����*/
	private void findView()
	{
		iv_search = (ImageView) findViewById(R.id.iv_search);
	}
	/*��ʼ��*/
	private void init()
	{
		mContext = Search_A.this;
	}
	/*�¼�����*/
	private void onClick()
	{
		iv_search.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			Toast.makeText(mContext, "����", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}
}

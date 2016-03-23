package com.chunsoft.ttgo.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chunosft.utils.ToastUtil;
import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ProBean;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;

public class Search_A extends Activity implements OnClickListener,
		IXListViewListener {
	/**
	 * widget statement
	 */
	private ImageView iv_search;
	private XListView lv;
	private EditText et_searsh;
	/**
	 * variable statement
	 */
	private String searchWord;
	private Context mContext;
	private List<ProBean> datas = new ArrayList<ProBean>();
	private ProBean bean;
	private SearchAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_a);
		findView();
		init();
		onClick();
	}

	/* 对象实例化 */
	private void findView() {
		et_searsh = (EditText) findViewById(R.id.et_Search);
		lv = (XListView) findViewById(R.id.lv);
		iv_search = (ImageView) findViewById(R.id.iv_search);
	}

	/* 初始化 */
	private void init() {
		mContext = Search_A.this;
		lv.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);

	}

	/* 事件监听 */
	private void onClick() {
		iv_search.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			searchWord = et_searsh.getText().toString();
			if (searchWord.equals("")) {
				ToastUtil.showShortToast(mContext, "搜索词不能为空");
			} else {
				for (int i = 0; i < 15; i++) {
					bean = new ProBean();
					bean.retcode = "1";
					datas.add(bean);
				}
				adapter = new SearchAdapter(mContext, datas, R.layout.groupitem);
				adapter.notifyDataSetChanged();
				lv.setAdapter(adapter);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		if (datas.isEmpty()) {
			return;
		} else {
			datas.clear();
			for (int i = 0; i < 5; i++) {
				bean = new ProBean();
				bean.retcode = "1";
				datas.add(bean);
			}
			adapter.notifyDataSetChanged();
			// 停止刷新和加载
			onLoad();
		}

	}

	@Override
	public void onLoadMore() {
		if (datas.isEmpty()) {
			return;
		} else {
			for (int i = 0; i < 5; i++) {
				bean = new ProBean();
				datas.add(bean);
			}
			adapter.notifyDataSetChanged();
			onLoad();
		}

	}

	/** 停止加载和刷新 */
	private void onLoad() {
		lv.stopRefresh();
		// 停止加载更多
		lv.stopLoadMore();
		// 设置最后一次刷新时间
		lv.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
	}

	/** 简单的时间格式 */
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"MM-dd HH:mm");

	public static String getCurrentTime(long time) {
		if (0 == time) {
			return "";
		}

		return mDateFormat.format(new Date(time));
	}

	public class SearchAdapter extends CommonAdapter<ProBean> {

		public SearchAdapter(Context context, List<ProBean> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ProBean t) {

		}

	}
}

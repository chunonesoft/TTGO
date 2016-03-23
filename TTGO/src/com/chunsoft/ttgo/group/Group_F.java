package com.chunsoft.ttgo.group;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ProBean;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;

public class Group_F extends Fragment implements IXListViewListener {
	/**
	 * widget statement
	 */
	private XListView lv;
	private TextView tv_title;

	/**
	 * variable statement
	 */
	private Context mContext;
	private List<ProBean> datas = new ArrayList<ProBean>();
	private ProBean bean;
	private GroupAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.group_f, null);
		FindView(view);
		init();
		return view;
	}

	private void init() {
		mContext = getActivity();
		tv_title.setText("����");
		lv.setXListViewListener(this);
		// ���ÿ��Խ����������صĹ���
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);
		for (int i = 0; i < 15; i++) {
			bean = new ProBean();
			bean.retcode = "1";
			datas.add(bean);
		}
		adapter = new GroupAdapter(mContext, datas, R.layout.groupitem);
		lv.setAdapter(adapter);
	}

	private void FindView(View view) {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		lv = (XListView) view.findViewById(R.id.lv);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
	}

	@Override
	public void onRefresh() {
		datas.clear();
		for (int i = 0; i < 5; i++) {
			bean = new ProBean();
			bean.retcode = "1";
			datas.add(bean);
		}
		adapter.notifyDataSetChanged();
		// ֹͣˢ�ºͼ���
		onLoad();
	}

	@Override
	public void onLoadMore() {
		for (int i = 0; i < 5; i++) {
			bean = new ProBean();
			datas.add(bean);
		}
		adapter.notifyDataSetChanged();
		onLoad();
	}

	/** ֹͣ���غ�ˢ�� */
	private void onLoad() {
		lv.stopRefresh();
		// ֹͣ���ظ���
		lv.stopLoadMore();
		// �������һ��ˢ��ʱ��
		lv.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
	}

	/** �򵥵�ʱ���ʽ */
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"MM-dd HH:mm");

	public static String getCurrentTime(long time) {
		if (0 == time) {
			return "";
		}

		return mDateFormat.format(new Date(time));
	}

	public class GroupAdapter extends CommonAdapter<ProBean> {

		public GroupAdapter(Context context, List<ProBean> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ProBean t) {

		}

	}
}

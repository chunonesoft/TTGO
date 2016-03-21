package com.chunsoft.ttgo.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.chunsoft.ab.view.AbOnItemClickListener;
import com.chunsoft.ab.view.AbSlidingPlayView;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.util.MyAdapter;
import com.chunsoft.view.MyGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class Home_F extends Fragment implements OnClickListener,
		OnItemClickListener, OnRefreshListener2<ScrollView> {
	private ImageView iv_search;
	private PullToRefreshScrollView scrollview;
	private LinearLayout layout;
	/** ��ҳ�ֲ� */
	private AbSlidingPlayView viewPager;
	/** �洢��ҳ�ֲ��Ľ��� */
	private ArrayList<View> allListView;
	/** ��ҳ�ֲ��Ľ������Դ */
	private int[] resId = { R.drawable.menu_viewpager_1,
			R.drawable.menu_viewpager_2, R.drawable.menu_viewpager_3,
			R.drawable.menu_viewpager_4, R.drawable.menu_viewpager_5 };

	private int pageIndex = 0;
	private MyGridView gridview;
	MyAdapter adapter;
	List<String> items;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_f,
				null);
		findView(view);

		onClick();
		initListView();
		initView();
		return view;
	}

	/** ����ʵ���� */
	private void findView(View view) {
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		gridview = (MyGridView) view.findViewById(R.id.myGridView);
		viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
		scrollview = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		layout = (LinearLayout) view.findViewById(R.id.layout);
	}

	/** �¼����� */
	private void onClick() {
		iv_search.setOnClickListener(this);
		gridview.setOnItemClickListener(this);
	}

	/** ��ʼ������ */
	private void initView() {
		// ���ò��ŷ�ʽΪ˳�򲥷�
		viewPager.setPlayType(1);
		// ���ò��ż��ʱ��
		viewPager.setSleepTime(2000);
		initViewPager();
		layout.setFocusable(true);
		layout.setFocusableInTouchMode(true);
		layout.requestFocus();

		scrollview.getLoadingLayoutProxy().setLastUpdatedLabel("�ϴ�ˢ��ʱ��");
		scrollview.getLoadingLayoutProxy().setPullLabel("����ˢ��");
		scrollview.getLoadingLayoutProxy().setRefreshingLabel("���ڼ��ظ���");
		scrollview.getLoadingLayoutProxy().setReleaseLabel("�ɿ�����ˢ��");

		// �����������趨
		scrollview.setMode(Mode.PULL_UP_TO_REFRESH);
		scrollview.setOnRefreshListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			Intent intent = new Intent(getActivity(), Search_A.class);
			startActivity(intent);
			break;
		}

	}

	private void initViewPager() {

		if (allListView != null) {
			allListView.clear();
			allListView = null;
		}
		allListView = new ArrayList<View>();

		for (int i = 0; i < resId.length; i++) {
			// ����ViewPager�Ĳ���
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.pic_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(resId[i]);
			allListView.add(view);
		}

		viewPager.addViews(allListView);
		// ��ʼ�ֲ�
		viewPager.startPlay();
		viewPager.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				// ��ת���������
				Intent intent = new Intent(getActivity(), ProductDetail_A.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * ListView��ʼ������
	 */
	private void initListView() {
		items = new ArrayList<String>();
		for (int i = 0; i < 16; i++) {
			items.add("������item " + i);
		}
		adapter = new MyAdapter(getActivity(), items);
		gridview.setAdapter(adapter);
	}

	private void getMoreData() {
		items.add("1");
		items.add("1");
		items.add("1");
		items.add("1");
		items.add("1");
		adapter.notifyDataSetChanged();
		scrollview.onRefreshComplete();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		getMoreData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// ��ת���������
		Intent intent = new Intent(getActivity(), ProductDetail_A.class);
		startActivity(intent);
	}

}

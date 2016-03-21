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
	/** 首页轮播 */
	private AbSlidingPlayView viewPager;
	/** 存储首页轮播的界面 */
	private ArrayList<View> allListView;
	/** 首页轮播的界面的资源 */
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

	/** 对象实例化 */
	private void findView(View view) {
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		gridview = (MyGridView) view.findViewById(R.id.myGridView);
		viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
		scrollview = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		layout = (LinearLayout) view.findViewById(R.id.layout);
	}

	/** 事件监听 */
	private void onClick() {
		iv_search.setOnClickListener(this);
		gridview.setOnItemClickListener(this);
	}

	/** 初始化界面 */
	private void initView() {
		// 设置播放方式为顺序播放
		viewPager.setPlayType(1);
		// 设置播放间隔时间
		viewPager.setSleepTime(2000);
		initViewPager();
		layout.setFocusable(true);
		layout.setFocusableInTouchMode(true);
		layout.requestFocus();

		scrollview.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间");
		scrollview.getLoadingLayoutProxy().setPullLabel("下拉刷新");
		scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在加载更多");
		scrollview.getLoadingLayoutProxy().setReleaseLabel("松开即可刷新");

		// 上拉、下拉设定
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
			// 导入ViewPager的布局
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.pic_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(resId[i]);
			allListView.add(view);
		}

		viewPager.addViews(allListView);
		// 开始轮播
		viewPager.startPlay();
		viewPager.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				// 跳转到详情界面
				Intent intent = new Intent(getActivity(), ProductDetail_A.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * ListView初始化方法
	 */
	private void initListView() {
		items = new ArrayList<String>();
		for (int i = 0; i < 16; i++) {
			items.add("这里是item " + i);
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
		// 跳转到详情界面
		Intent intent = new Intent(getActivity(), ProductDetail_A.class);
		startActivity(intent);
	}

}

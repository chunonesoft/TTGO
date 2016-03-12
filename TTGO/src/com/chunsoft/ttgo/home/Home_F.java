package com.chunsoft.ttgo.home;

import java.util.ArrayList;
import java.util.List;

import com.chunsoft.ab.view.AbOnItemClickListener;
import com.chunsoft.ab.view.AbSlidingPlayView;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.util.MyAdapter;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Home_F extends Fragment implements OnClickListener,OnTouchListener, IXListViewListener{
	private ImageView iv_search;
	private LinearLayout ll1;
	/**首页轮播*/
	private AbSlidingPlayView viewPager;
	/**存储首页轮播的界面*/
	private ArrayList<View> allListView;
	/**首页轮播的界面的资源*/
	private int[] resId = {R.drawable.menu_viewpager_1, R.drawable.menu_viewpager_2, R.drawable.menu_viewpager_3, R.drawable.menu_viewpager_4, R.drawable.menu_viewpager_5 };
	
	private AnimationSet animationSet;
	/**第一次按下屏幕时的Y坐标*/
	float fist_down_Y = 0;
	/**请求数据的页数*/
	private int pageIndex = 0;
	GridView gridview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_f, null);
		findView(view);
		
		onClick();
		initListView();
		initView();
		
		return view;
	}
	
	/**对象实例化*/
	private void findView(View view)
	{
		ll1 = (LinearLayout) view.findViewById(R.id.ll1);
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		gridview = (GridView) view.findViewById(R.id.myGridView);
		viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
		//((PullToRefreshLayout) view.findViewById(R.id.refresh_view))
		//.setOnRefreshListener(new MyListener());
	}
	/**事件监听*/
	private void onClick()
	{
		iv_search.setOnClickListener(this);
	}
	
	/**初始化界面*/
	private void initView()
	{
		//设置播放方式为顺序播放
		viewPager.setPlayType(1);
		//设置播放间隔时间
		viewPager.setSleepTime(2000);
		initViewPager();
		gridview.setOnTouchListener(this);
		//listView.setOnTouchListener(this);
		//listView.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		//listView.setPullLoadEnable(true);
		//listView.setPullRefreshEnable(false);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			Intent intent = new Intent(getActivity(),Search_A.class);
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
			//导入ViewPager的布局
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.pic_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			imageView.setImageResource(resId[i]);
			allListView.add(view);
		}
		
		
		viewPager.addViews(allListView);
		//开始轮播
		viewPager.startPlay();
		viewPager.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				//跳转到详情界面
				Intent intent = new Intent(getActivity(), ProductDetail_A.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * ListView初始化方法
	 */
	private void initListView()
	{
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < 15; i++)
		{
			items.add("这里是item " + i);
		}
		MyAdapter adapter = new MyAdapter(getActivity(), items);
		gridview.setAdapter(adapter);
		/*//listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Toast.makeText(getActivity(),
						" Click on " + parent.getAdapter().getItemId(position),
						Toast.LENGTH_SHORT).show();
			}
		});*/
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//第一次按下时的坐标
			fist_down_Y = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// 向上滑动，隐藏滚动框
			if (fist_down_Y - y > 100 && ll1.isShown()) {
				if (animationSet != null) {
					animationSet = null;
				}
				animationSet = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.up_out);
				ll1.startAnimation(animationSet);
				ll1.setY(-100);
				ll1.setVisibility(View.GONE);
			}
			// 向下滑动，显示滚动框
			if (y - fist_down_Y > 250 && !ll1.isShown()) {
				if (animationSet != null) {
					animationSet = null;
				}
				animationSet = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.down_in);
				ll1.startAnimation(animationSet);
				ll1.setY(0);
				ll1.setVisibility(View.VISIBLE);
			}
			break;

		}
		return false;

	}

	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onLoadMore() {
		
	}
	
}

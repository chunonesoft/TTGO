package com.chunsoft.ttgo.home;

import java.util.ArrayList;
import java.util.List;

import com.chunsoft.ab.view.AbOnItemClickListener;
import com.chunsoft.ab.view.AbSlidingPlayView;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.util.MyAdapter;
import com.chunsoft.ttgo.util.MyListener;
import com.chunsoft.view.PullToRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Home_F extends Fragment implements OnClickListener{
	private ImageView iv_search;
	/**首页轮播*/
	private AbSlidingPlayView viewPager;
	/**存储首页轮播的界面*/
	private ArrayList<View> allListView;
	/**首页轮播的界面的资源*/
	private int[] resId = {R.drawable.menu_viewpager_1, R.drawable.menu_viewpager_2, R.drawable.menu_viewpager_3, R.drawable.menu_viewpager_4, R.drawable.menu_viewpager_5 };
	
	ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_f, null);
		findView(view);
		onClick();
		initView();
		initListView();
		return view;
	}
	/**对象实例化*/
	private void findView(View view)
	{
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
		((PullToRefreshLayout) view.findViewById(R.id.refresh_view))
		.setOnRefreshListener(new MyListener());
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
		viewPager.setSleepTime(3000);
		initViewPager();
		
		
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
		for (int i = 0; i < 30; i++)
		{
			items.add("这里是item " + i);
		}
		MyAdapter adapter = new MyAdapter(getActivity(), items);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Toast.makeText(
						getActivity(),
						"LongClick on "
								+ parent.getAdapter().getItemId(position),
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});
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
		});
	}
}

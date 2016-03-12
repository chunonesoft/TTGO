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
	/**��ҳ�ֲ�*/
	private AbSlidingPlayView viewPager;
	/**�洢��ҳ�ֲ��Ľ���*/
	private ArrayList<View> allListView;
	/**��ҳ�ֲ��Ľ������Դ*/
	private int[] resId = {R.drawable.menu_viewpager_1, R.drawable.menu_viewpager_2, R.drawable.menu_viewpager_3, R.drawable.menu_viewpager_4, R.drawable.menu_viewpager_5 };
	
	private AnimationSet animationSet;
	/**��һ�ΰ�����Ļʱ��Y����*/
	float fist_down_Y = 0;
	/**�������ݵ�ҳ��*/
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
	
	/**����ʵ����*/
	private void findView(View view)
	{
		ll1 = (LinearLayout) view.findViewById(R.id.ll1);
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		gridview = (GridView) view.findViewById(R.id.myGridView);
		viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
		//((PullToRefreshLayout) view.findViewById(R.id.refresh_view))
		//.setOnRefreshListener(new MyListener());
	}
	/**�¼�����*/
	private void onClick()
	{
		iv_search.setOnClickListener(this);
	}
	
	/**��ʼ������*/
	private void initView()
	{
		//���ò��ŷ�ʽΪ˳�򲥷�
		viewPager.setPlayType(1);
		//���ò��ż��ʱ��
		viewPager.setSleepTime(2000);
		initViewPager();
		gridview.setOnTouchListener(this);
		//listView.setOnTouchListener(this);
		//listView.setXListViewListener(this);
		// ���ÿ��Խ����������صĹ���
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
			//����ViewPager�Ĳ���
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.pic_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			imageView.setImageResource(resId[i]);
			allListView.add(view);
		}
		
		
		viewPager.addViews(allListView);
		//��ʼ�ֲ�
		viewPager.startPlay();
		viewPager.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				//��ת���������
				Intent intent = new Intent(getActivity(), ProductDetail_A.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * ListView��ʼ������
	 */
	private void initListView()
	{
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < 15; i++)
		{
			items.add("������item " + i);
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
			//��һ�ΰ���ʱ������
			fist_down_Y = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// ���ϻ��������ع�����
			if (fist_down_Y - y > 100 && ll1.isShown()) {
				if (animationSet != null) {
					animationSet = null;
				}
				animationSet = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.up_out);
				ll1.startAnimation(animationSet);
				ll1.setY(-100);
				ll1.setVisibility(View.GONE);
			}
			// ���»�������ʾ������
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

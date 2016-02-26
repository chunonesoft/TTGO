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
	/**��ҳ�ֲ�*/
	private AbSlidingPlayView viewPager;
	/**�洢��ҳ�ֲ��Ľ���*/
	private ArrayList<View> allListView;
	/**��ҳ�ֲ��Ľ������Դ*/
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
	/**����ʵ����*/
	private void findView(View view)
	{
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
		((PullToRefreshLayout) view.findViewById(R.id.refresh_view))
		.setOnRefreshListener(new MyListener());
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
		for (int i = 0; i < 30; i++)
		{
			items.add("������item " + i);
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

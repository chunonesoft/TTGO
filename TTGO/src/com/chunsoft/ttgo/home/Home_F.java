package com.chunsoft.ttgo.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.android.volley.Response;
import com.chunsoft.ab.view.AbOnItemClickListener;
import com.chunsoft.ab.view.AbSlidingPlayView;
import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ProBean;
import com.chunsoft.ttgo.bean.ProListBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.view.MyGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class Home_F extends Fragment implements OnClickListener,
		OnItemClickListener, OnRefreshListener2<ScrollView> {
	/**
	 * widget statement
	 */
	private ImageView iv_search;
	private PullToRefreshScrollView scrollview;
	private AbSlidingPlayView viewPager_menu;
	/**
	 * variable statement
	 */
	private JSONObject sendData;
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
	// MyAdapter adapter;
	List<String> items;
	private Context mContext;
	private ProAdapter adapters;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_f,
				null);
		mContext = getActivity();
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
		viewPager_menu = (AbSlidingPlayView) view
				.findViewById(R.id.viewPager_menu);
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
		viewPager_menu.requestFocus();
		viewPager_menu.setFocusable(true);
		viewPager_menu.setFocusableInTouchMode(true);

		scrollview.getLoadingLayoutProxy().setLastUpdatedLabel("�ϴ�ˢ��ʱ��");
		scrollview.getLoadingLayoutProxy().setPullLabel("����ˢ��");
		scrollview.getLoadingLayoutProxy().setRefreshingLabel("���ڼ��ظ���");
		scrollview.getLoadingLayoutProxy().setReleaseLabel("�ɿ�����ˢ��");

		// �����������趨
		scrollview.setMode(Mode.PULL_UP_TO_REFRESH);
		scrollview.setOnRefreshListener(this);

		getData("1", new VolleyDataCallback<ProBean>() {
			@Override
			public void onSuccess(ProBean datas) {
				viewPager_menu.requestFocus();
				viewPager_menu.setFocusable(true);
				viewPager_menu.setFocusableInTouchMode(true);
				adapters = new ProAdapter(mContext, datas.productList,
						R.layout.home_gv_item);
				gridview.setAdapter(adapters);
			}
		});

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
		// adapter = new MyAdapter(getActivity(), items);
		// gridview.setAdapter(adapter);
	}

	private void getMoreData() {
		adapters.notifyDataSetChanged();
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

	/**
	 * get Data about pro
	 */
	private void getData(String page, final VolleyDataCallback<ProBean> callback) {
		String URL = Constant.IP + Constant.getProInfo;
		sendData = new JSONObject();
		try {
			sendData.put("page", page);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<ProBean> request = new GsonRequest<>(URL,
				sendData.toString(), new Response.Listener<ProBean>() {
					@Override
					public void onResponse(ProBean arg0) {
						callback.onSuccess(arg0);
					}

				}, new AbstractVolleyErrorListener(mContext) {

					@Override
					public void onError() {

					}
				}, ProBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	class ProAdapter extends CommonAdapter<ProListBean> {

		public ProAdapter(Context context, List<ProListBean> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ProListBean t) {
			holder.setText(R.id.tv_content, t.name.toString());
		}
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// ��ȡListView��Ӧ��Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()�������������Ŀ
			View listItem = listAdapter.getView(i, null, listView);
			// ��������View �Ŀ��
			listItem.measure(0, 0);
			// ͳ������������ܸ߶�
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
		// params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�
		listView.setLayoutParams(params);
	}
}

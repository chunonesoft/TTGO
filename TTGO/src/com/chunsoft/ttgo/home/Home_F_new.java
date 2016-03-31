package com.chunsoft.ttgo.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ADInfo;
import com.chunsoft.ttgo.bean.ProBean;
import com.chunsoft.ttgo.bean.ProListBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.view.ImageCycleView;
import com.chunsoft.view.ImageCycleView.ImageCycleViewListener;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Home_F_new extends Fragment implements OnClickListener,
		OnItemClickListener, IXListViewListener {
	/**
	 * widget statement
	 */
	private ImageView iv_search;
	ImageCycleView mAdView;
	ProgressDialog dialog = null;

	/**
	 * variable statement
	 */
	private List<ProListBean> productList = new ArrayList<ProListBean>();
	private JSONObject sendData;
	View cycleview;
	private int currentPage = 1;
	private int totalPage = 1;
	private XListView x_lv;
	// MyAdapter adapter;
	List<String> items;
	private Context mContext;
	private ProAdapter adapters;
	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
	private String[] imageUrls = {
			"http://pic16.nipic.com/20110831/5867329_155851694000_2.jpg",
			"http://pic65.nipic.com/file/20150423/1515468_133352223000_2.jpg",
			"http://pic14.nipic.com/20110530/6871773_151825944926_2.jpg",
			"http://tu.webps.cn/tb/img/4/TB1bz7AHXXXXXcAXpXXXXXXXXXX_%21%210-item_pic.jpg",
			"http://pic.58pic.com/58pic/11/18/41/50h58PICy7m.jpg" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.home_f_new, null);
		mContext = getActivity();
		findView(view);

		onClick();
		initView();
		return view;
	}

	/** 对象实例化 */
	private void findView(View view) {
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		x_lv = (XListView) view.findViewById(R.id.x_lv);
	}

	/** 事件监听 */
	private void onClick() {
		iv_search.setOnClickListener(this);
		x_lv.setOnItemClickListener(this);
	}

	/** 初始化界面 */
	private void initView() {
		cycleview = LayoutInflater.from(mContext).inflate(
				R.layout.image_cycle_view, null);
		mAdView = (ImageCycleView) cycleview.findViewById(R.id.ad_view);
		x_lv.addHeaderView(cycleview);
		x_lv.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		x_lv.setPullLoadEnable(true);
		x_lv.setPullRefreshEnable(true);
		for (int i = 0; i < imageUrls.length; i++) {
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("top-->" + i);
			infos.add(info);
		}
		mAdView.setImageResources(infos, mAdCycleViewListener);
		getData(String.valueOf(currentPage), new VolleyDataCallback<ProBean>() {
			@Override
			public void onSuccess(ProBean datas) {
				totalPage = Integer.valueOf(datas.totalpage);
				productList = datas.productList;
				adapters = new ProAdapter(mContext, productList,
						R.layout.home_gv_item);
				x_lv.setAdapter(adapters);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}
		});

	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			Toast.makeText(mContext, "content->" + info.getContent(),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			Intent intent = new Intent(getActivity(), Search_A.class);
			startActivity(intent);
			break;
		}

	}

	private void getMoreData() {
		if (currentPage < totalPage) {

		} else {

		}
		adapters.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 跳转到详情界面
		Intent intent = new Intent(getActivity(), ProductDetail_A.class);
		startActivity(intent);
	}

	/**
	 * get Data about pro
	 */
	private void getData(String page, final VolleyDataCallback<ProBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(mContext, "", "正在加载...");
			dialog.show();
		}
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
			holder.setText(R.id.tv_content, t.proIntro.toString());
			holder.setText(R.id.tv_price, "¥" + t.proPrice.toString());
			holder.setText(R.id.tv_sale, t.saleNum.toString() + "人付款");
			// 使用ImageLoader对图片进行加装！
			// holder.setVolleyImage(R.id.iv, t.picPath, R.drawable.icon_empty,
			// R.drawable.icon_error);
			ImageView image = holder.getView(R.id.iv);
			ImageLoader.getInstance().displayImage(
					Constant.ImageUri + t.picPath, image);// 使用ImageLoader对图片进行加装！
		}
	}

	/*
	 * public void setListViewHeightBasedOnChildren(ListView listView) { //
	 * 获取ListView对应的Adapter ListAdapter listAdapter = listView.getAdapter(); if
	 * (listAdapter == null) { return; }
	 * 
	 * int totalHeight = 0; for (int i = 0, len = listAdapter.getCount(); i <
	 * len; i++) { // listAdapter.getCount()返回数据项的数目 View listItem =
	 * listAdapter.getView(i, null, listView); // 计算子项View 的宽高
	 * listItem.measure(0, 0); // 统计所有子项的总高度 totalHeight +=
	 * listItem.getMeasuredHeight(); }
	 * 
	 * ViewGroup.LayoutParams params = listView.getLayoutParams(); params.height
	 * = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -
	 * 1)); // listView.getDividerHeight()获取子项间分隔符占用的高度 //
	 * params.height最后得到整个ListView完整显示需要的高度 listView.setLayoutParams(params); }
	 */

	@Override
	public void onResume() {
		super.onResume();
		mAdView.startImageCycle();
	};

	@Override
	public void onPause() {
		super.onPause();
		mAdView.pushImageCycle();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAdView.pushImageCycle();
	}

	@Override
	public void onRefresh() {
		productList.clear();
		currentPage = 1;
		getData(String.valueOf(currentPage), new VolleyDataCallback<ProBean>() {
			@Override
			public void onSuccess(ProBean datas) {
				productList = datas.productList;
				totalPage = Integer.valueOf(datas.totalpage);
				currentPage++;
				adapters = new ProAdapter(mContext, productList,
						R.layout.home_gv_item);
				x_lv.setAdapter(adapters);

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}
		});
		// 停止刷新和加载
		onLoad();
	}

	@Override
	public void onLoadMore() {
		if (currentPage < totalPage) {
			currentPage++;
			getData(String.valueOf(currentPage),
					new VolleyDataCallback<ProBean>() {
						@Override
						public void onSuccess(ProBean datas) {
							for (int i = 0; i < datas.productList.size(); i++) {
								productList.add(datas.productList.get(i));
							}
							adapters.notifyDataSetChanged();
							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
								dialog = null;
							}

						}
					});
		} else {
			// 停止刷新和加载
			onLoad();
			ToastUtil.showShortToast(mContext, "没有更多数据了");
		}
	}

	/** 停止加载和刷新 */
	private void onLoad() {
		x_lv.stopRefresh();
		// 停止加载更多
		x_lv.stopLoadMore();
		// 设置最后一次刷新时间
		x_lv.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
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
}

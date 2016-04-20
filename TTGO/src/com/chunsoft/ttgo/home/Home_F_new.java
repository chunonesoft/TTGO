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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ADInfo;
import com.chunsoft.ttgo.bean.JoinListBean;
import com.chunsoft.ttgo.bean.ProBean;
import com.chunsoft.ttgo.bean.ProListBean;
import com.chunsoft.ttgo.bean.RecProListBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.view.AutoScrollTextView;
import com.chunsoft.view.ImageCycleView;
import com.chunsoft.view.ImageCycleView.ImageCycleViewListener;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Home_F_new extends Fragment implements OnClickListener,
		IXListViewListener {
	/**
	 * widget statement
	 */
	private ImageView iv_search;
	ImageCycleView mAdView;
	ProgressDialog dialog = null;
	AutoScrollTextView autoScrollTextView;

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
	}

	/** 初始化界面 */
	private void initView() {
		cycleview = LayoutInflater.from(mContext).inflate(
				R.layout.image_cycle_view, null);
		mAdView = (ImageCycleView) cycleview.findViewById(R.id.ad_view);
		autoScrollTextView = (AutoScrollTextView) cycleview
				.findViewById(R.id.tv_Notice);
		getJoinList(new VolleyDataCallback<JoinListBean>() {
			@Override
			public void onSuccess(JoinListBean datas) {
				String str = "";
				for (int i = 0; i < datas.JOININFO_LIST.size(); i++) {
					str += datas.JOININFO_LIST.get(i).CONTENT + '\n';
				}
				autoScrollTextView.setText(str);
				autoScrollTextView.init(getActivity().getWindowManager());
				autoScrollTextView.startScroll();
			}
		});
		autoScrollTextView.init(getActivity().getWindowManager());
		autoScrollTextView.startScroll();
		x_lv.addHeaderView(cycleview);
		x_lv.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		x_lv.setPullLoadEnable(true);
		x_lv.setPullRefreshEnable(true);
		/**
		 * show image on home page
		 */
		getRecommendPro(new VolleyDataCallback<RecProListBean>() {
			@Override
			public void onSuccess(RecProListBean datas) {
				for (int i = 0; i < datas.PIC_LIST.size(); i++) {
					ADInfo info = new ADInfo();
					info.setUrl(Constant.ImageUri
							+ datas.PIC_LIST.get(i).PRO_PIC);
					info.setId(datas.PIC_LIST.get(i).PRO_ID);
					infos.add(info);
				}
				mAdView.setImageResources(infos, mAdCycleViewListener);
			}
		});
		getData(String.valueOf(currentPage), new VolleyDataCallback<ProBean>() {
			@Override
			public void onSuccess(final ProBean datas) {
				totalPage = Integer.valueOf(datas.totalpage);
				productList = datas.productList;
				adapters = new ProAdapter(mContext, productList);
				x_lv.setAdapter(adapters);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
				x_lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent();

						intent.putExtra("proID", datas.productList
								.get((int) parent.getAdapter().getItemId(
										position)).proID);

						intent.setClass(mContext, ProductDetail_A.class);
						startActivity(intent);
					}
				});
			}
		});

	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			Intent intent = new Intent();
			intent.putExtra("proID", info.getId());
			Log.e("proID", info.getId());
			intent.setClass(mContext, ProductDetail_A.class);
			startActivity(intent);
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
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				}, ProBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	public class ProAdapter extends BaseAdapter {
		List<ProListBean> datas;
		Context mContext;

		public ProAdapter(Context mContext, List<ProListBean> datas) {
			this.mContext = mContext;
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.home_gv_item, null);
				holder = new ViewHolder(view);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			bindListItem(holder, datas.get(position));
			return view;
		}
	}

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
				adapters = new ProAdapter(mContext, productList);
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

			ToastUtil.showShortToast(mContext, "没有更多数据了");
		}
		// 停止刷新和加载
		onLoad();
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

	private void getJoinList(final VolleyDataCallback<JoinListBean> callback) {
		String URL = Constant.IP + Constant.getJoinList;
		GsonRequest<JoinListBean> req = new GsonRequest<JoinListBean>(URL, "",
				new Response.Listener<JoinListBean>() {

					@Override
					public void onResponse(JoinListBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(getActivity()) {
					@Override
					public void onError() {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				}, JoinListBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}

	private void getRecommendPro(
			final VolleyDataCallback<RecProListBean> callback) {
		String URL = Constant.IP + Constant.getProRecommendPro;
		GsonRequest<RecProListBean> req = new GsonRequest<RecProListBean>(URL,
				"", new Response.Listener<RecProListBean>() {

					@Override
					public void onResponse(RecProListBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(getActivity()) {
					@Override
					public void onError() {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				}, RecProListBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}

	static class ViewHolder {
		@Bind(R.id.tv_content)
		TextView tv_content;
		@Bind(R.id.tv_price)
		TextView tv_price;
		@Bind(R.id.tv_sale)
		TextView tv_sale;
		@Bind(R.id.iv)
		ImageView image;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	private void bindListItem(ViewHolder holder, ProListBean data) {

		holder.tv_content.setText(data.name);
		holder.tv_price.setText("¥" + data.proPrice);
		holder.tv_sale.setText(data.saleNum + "人付款");
		holder.image.setTag(data.picPath);
		String imageUri = data.picPath;
		if (imageUri.equals(holder.image.getTag())) {
			ImageLoader.getInstance().displayImage(
					Constant.ImageUri + data.picPath, holder.image);
		} else {
			holder.image.setImageResource(R.drawable.icon_empty);
		}

	}
}

package com.chunsoft.ttgo.myself;

import java.io.Serializable;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.home.MyApplication;
import com.chunsoft.ttgo.util.PreferencesUtils;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Return_sqz extends Fragment implements IXListViewListener {
	@Bind(R.id.xlv_all)
	XListView xlv_all;
	ProgressDialog dialog;

	private int currentPage = 1;
	private int totalPage = 1;
	private String userId;
	private String Token;
	private String typeID = "4";
	private OrderDataAdapter adapter;
	private Context mContext;
	List<OrderConListBean> orderConList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.order_all, null);
		ButterKnife.bind(this, view);
		init();
		getData();
		return view;
	}

	private void init() {
		mContext = getActivity();
		xlv_all.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		xlv_all.setPullLoadEnable(true);
		xlv_all.setPullRefreshEnable(true);
	}

	private void getData() {
		userId = PreferencesUtils.getSharePreStr(getActivity(), "userId");
		Token = PreferencesUtils.getSharePreStr(getActivity(), "Token");
		orderConList = new ArrayList<OrderConListBean>();
		getOrderData(currentPage, userId, typeID, Token,
				new VolleyDataCallback<OrderDataBean>() {
					@Override
					public void onSuccess(final OrderDataBean datas) {
						currentPage++;
						totalPage = datas.totalPage;
						for (int i = 0; i < datas.orderConList.size(); i++) {
							String statusName = datas.orderConList.get(i).statusName;
							if (statusName.equals("退货审核中")) {
								orderConList.add(datas.orderConList.get(i));
							}
						}
						adapter = new OrderDataAdapter(mContext, orderConList,
								R.layout.order_item1);
						xlv_all.setAdapter(adapter);
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
						xlv_all.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Intent intent = new Intent();
								intent.setClass(getActivity(),
										OrderDetail_A.class);
								intent.putExtra("detailData",
										(Serializable) datas.orderConList
												.get(position));
								startActivity(intent);
							}
						});
					}
				});
	}

	@Override
	public void onRefresh() {
		currentPage = 1;
		getOrderData(currentPage, userId, typeID, Token,
				new VolleyDataCallback<OrderDataBean>() {
					@Override
					public void onSuccess(OrderDataBean datas) {
						currentPage++;
						totalPage = datas.totalPage;
						orderConList = new ArrayList<OrderConListBean>();
						for (int i = 0; i < datas.orderConList.size(); i++) {
							String statusName = datas.orderConList.get(i).statusName;
							if (statusName.equals("退货审核中")) {
								orderConList.add(datas.orderConList.get(i));
							}
						}
						adapter = new OrderDataAdapter(mContext, orderConList,
								R.layout.order_item1);
						xlv_all.setAdapter(adapter);
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				});
		onLoad();
	}

	@Override
	public void onLoadMore() {
		if (currentPage <= totalPage) {
			getOrderData(currentPage, userId, typeID, Token,
					new VolleyDataCallback<OrderDataBean>() {
						@Override
						public void onSuccess(OrderDataBean datas) {
							currentPage++;
							for (int i = 0; i < datas.orderConList.size(); i++) {
								String statusName = datas.orderConList.get(i).statusName;
								if (statusName.equals("退货审核中")) {
									orderConList.add(datas.orderConList.get(i));
								}
							}
							adapter.notifyDataSetChanged();
							xlv_all.setAdapter(adapter);
							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
								dialog = null;
							}
						}
					});
		} else {
			ToastUtil.showShortToast(mContext, "没有更多数据~");
		}
		onLoad();
	}

	private void getOrderData(int currentPage, String userId, String typeID,
			String token, final VolleyDataCallback<OrderDataBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(getActivity(), "", "正在加载...");
			dialog.show();
		}
		JSONObject sendData;
		sendData = new JSONObject();
		try {
			sendData.put("currentPage", currentPage + "");
			sendData.put("userId", userId);
			sendData.put("typeID", typeID);
			sendData.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String URL = Constant.IP + Constant.getOrderInfo;
		GsonRequest<OrderDataBean> req = new GsonRequest<OrderDataBean>(URL,
				sendData.toString(), new Response.Listener<OrderDataBean>() {
					@Override
					public void onResponse(OrderDataBean arg0) {
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
				}, OrderDataBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}

	class OrderDataAdapter extends CommonAdapter<OrderConListBean> {

		public OrderDataAdapter(Context context, List<OrderConListBean> datas,
				int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, OrderConListBean t) {
			int totalnum = 0;
			Log.e("Order_Num", t.orderNo);
			ImageView iv_image = holder.getView(R.id.iv_image);
			holder.getView(R.id.iv_image).setTag(t.productList.get(0).path);
			iv_image.setImageResource(R.drawable.icon_empty);
			if (t.productList.get(0).path.equals(holder.getView(R.id.iv_image)
					.getTag())) {
				ImageLoader.getInstance()
						.displayImage(
								Constant.ImageUri + t.productList.get(0).path,
								iv_image);
			}
			holder.setText(R.id.tv_order_time, t.orderTime);
			holder.setText(R.id.tv_order_state, t.statusName);
			holder.setText(R.id.tv_name, t.productList.get(0).proName + "等"
					+ t.productList.size() + "种衣服");
			holder.setText(R.id.tv_color, "颜色：" + t.productList.size() + "种");
			holder.setText(R.id.tv_size, "尺码：" + t.productList.size() + "种");
			for (int i = 0; i < t.productList.size(); i++) {
				totalnum += Integer.valueOf(t.productList.get(i).num);
			}
			holder.setText(R.id.tv_all_num, "共" + totalnum + "件商品");
			holder.setText(R.id.tv_all_price, "合计：¥" + t.proTotalPrice);
		}
	}

	/** 停止加载和刷新 */
	private void onLoad() {
		xlv_all.stopRefresh();
		// 停止加载更多
		xlv_all.stopLoadMore();
		// 设置最后一次刷新时间
		xlv_all.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
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

package com.chunsoft.ttgo.myself;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MyOrder_All extends Fragment implements IXListViewListener {
	@Bind(R.id.xlv_all)
	XListView xlv_all;
	ProgressDialog dialog;

	private int currentPage = 1;
	private int totalPage = 1;
	private String userId;
	private String Token;
	private String typeID = "1";
	private OrderDataAdapter adapter;
	private Context mContext;

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
		// ���ÿ��Խ����������صĹ���
		xlv_all.setPullLoadEnable(true);
		xlv_all.setPullRefreshEnable(true);
	}

	private void getData() {
		userId = PreferencesUtils.getSharePreStr(getActivity(), "userId");
		Token = PreferencesUtils.getSharePreStr(getActivity(), "Token");
		getOrderData(currentPage, userId, typeID, Token,
				new VolleyDataCallback<OrderDataBean>() {
					@Override
					public void onSuccess(OrderDataBean datas) {
						currentPage++;
						totalPage = datas.totalPage;
						Log.e("datas.totalCount---->", datas.totalCount);
						adapter = new OrderDataAdapter(mContext,
								datas.orderConList, R.layout.order_item1);
						xlv_all.setAdapter(adapter);
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
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
						adapter = new OrderDataAdapter(mContext,
								datas.orderConList, R.layout.order_item1);
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
							adapter.notifyDataSetChanged();
							xlv_all.setAdapter(adapter);
							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
								dialog = null;
							}
						}
					});
		} else {
			ToastUtil.showShortToast(mContext, "û�и�������~");
		}
		onLoad();
	}

	private void getOrderData(int currentPage, String userId, String typeID,
			String token, final VolleyDataCallback<OrderDataBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(getActivity(), "", "���ڼ���...");
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
			holder.setText(R.id.tv_order_id, t.orderNo);
		}
	}

	/** ֹͣ���غ�ˢ�� */
	private void onLoad() {
		xlv_all.stopRefresh();
		// ֹͣ���ظ���
		xlv_all.stopLoadMore();
		// �������һ��ˢ��ʱ��
		xlv_all.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
	}

	/** �򵥵�ʱ���ʽ */
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"MM-dd HH:mm");

	public static String getCurrentTime(long time) {
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}
}

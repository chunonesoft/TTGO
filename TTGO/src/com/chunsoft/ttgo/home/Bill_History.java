package com.chunsoft.ttgo.home;

import java.text.SimpleDateFormat;
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
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response;
import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.BillBean;
import com.chunsoft.ttgo.bean.BillItemBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.util.PreferencesUtils;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;

public class Bill_History extends Fragment implements IXListViewListener {
	@Bind(R.id.xlv_bill_current)
	XListView xlv_bill;

	@Bind(R.id.all_price)
	TextView all_price;

	ProgressDialog dialog = null;

	private int totalPrice = 0;
	private BillAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.bill_current, null);
		ButterKnife.bind(this, view);
		init();

		getBillData(new VolleyDataCallback<BillBean>() {
			@Override
			public void onSuccess(final BillBean datas) {
				if (datas.bill.size() == 0) {
					all_price.setText("本月账单未到");
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
						dialog = null;
					}
				} else {
					adapter = new BillAdapter(getActivity(), datas.bill,
							R.layout.billitem);
					xlv_bill.setAdapter(adapter);
					for (int i = 0; i < datas.bill.size(); i++) {
						totalPrice += Integer.valueOf(datas.bill.get(i).puCost);
					}
					all_price.setText("上月账单合计：¥" + totalPrice);

					xlv_bill.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							String orderNo = datas.bill.get(position - 1).orderNo;
							Intent intent = new Intent();
							intent.setClass(getActivity(), Bill_Detail_A.class);
							intent.putExtra("orderNo", orderNo);
							startActivity(intent);
						}
					});
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
						dialog = null;
					}
				}

			}
		});
		return view;
	}

	private void init() {
		xlv_bill.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		xlv_bill.setPullLoadEnable(true);
		xlv_bill.setPullRefreshEnable(true);
	}

	private void getBillData(final VolleyDataCallback<BillBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(getActivity(), "", "正在加载...");
			dialog.show();
		}
		String userId = PreferencesUtils
				.getSharePreStr(getActivity(), "userId");
		String Token = PreferencesUtils.getSharePreStr(getActivity(), "Token");
		String URL = Constant.IP + Constant.GetBill;
		JSONObject sendData;
		sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("token", Token);
			Log.e("senData--->", sendData.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<BillBean> request = new GsonRequest<BillBean>(URL,
				sendData.toString(), new Response.Listener<BillBean>() {

					@Override
					public void onResponse(BillBean arg0) {
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
				}, BillBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	class BillAdapter extends CommonAdapter<BillItemBean> {

		public BillAdapter(Context context, List<BillItemBean> datas,
				int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, BillItemBean t) {
			holder.setText(R.id.tv_orederNo, "订单编号：" + t.orderNo);
			holder.setText(R.id.tv_orederTime, "下单时间：" + t.orderCreateTime);
			holder.setText(R.id.tv_saleTime, "售出时间：" + t.saleTime);
			holder.setText(R.id.tv_money, "¥" + t.puCost);
		}
	}

	@Override
	public void onRefresh() {
		onLoad();
	}

	@Override
	public void onLoadMore() {
		onLoad();
	}

	/** 停止加载和刷新 */
	private void onLoad() {
		xlv_bill.stopRefresh();
		// 停止加载更多
		xlv_bill.stopLoadMore();
		// 设置最后一次刷新时间
		xlv_bill.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
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

package com.chunsoft.ttgo.home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.OrderDetailBean;
import com.chunsoft.ttgo.bean.ProRowsBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.util.PreferencesUtils;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;

public class Bill_Detail_A extends Activity implements IXListViewListener {
	ProgressDialog dialog = null;
	private String orderNo;
	BillAdapter adapter;
	private XListView xlv_detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill_detail);
		orderNo = getIntent().getStringExtra("orderNo");
		Log.e("orderNo----->yanzheng", orderNo);
		xlv_detail = (XListView) findViewById(R.id.xlv_bill_detail);
		xlv_detail.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		xlv_detail.setPullLoadEnable(true);
		xlv_detail.setPullRefreshEnable(true);
		GetBillDetail(new VolleyDataCallback<OrderDetailBean>() {
			@Override
			public void onSuccess(OrderDetailBean datas) {
				adapter = new BillAdapter(Bill_Detail_A.this, datas.rows,
						R.layout.bill_detail_item);
				xlv_detail.setAdapter(adapter);
				Log.e("orderNo----->yanzheng", "nihao" + datas.rows.size());
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}
		});
	}

	private void GetBillDetail(
			final VolleyDataCallback<OrderDetailBean> callback) {
		String userId = PreferencesUtils.getSharePreStr(Bill_Detail_A.this,
				"userId");
		String token = PreferencesUtils.getSharePreStr(Bill_Detail_A.this,
				"Token");
		if (dialog == null) {
			dialog = ProgressDialog.show(Bill_Detail_A.this, "", "正在加载...");
			dialog.show();
		}
		JSONObject sendData;
		sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("orderNo", orderNo);
			sendData.put("token", token);
			Log.e("sendData----->", sendData.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String URL = Constant.IP + Constant.getBillDetatil;
		GsonRequest<OrderDetailBean> req = new GsonRequest<OrderDetailBean>(
				URL, sendData.toString(),
				new Response.Listener<OrderDetailBean>() {
					@Override
					public void onResponse(OrderDetailBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(Bill_Detail_A.this) {
					@Override
					public void onError() {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				}, OrderDetailBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}

	class BillAdapter extends CommonAdapter<ProRowsBean> {

		public BillAdapter(Context context, List<ProRowsBean> datas,
				int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ProRowsBean t) {
			holder.setText(R.id.tv_name, t.productName);
			holder.setText(R.id.tv_color, "颜色：" + t.color + ";尺寸：" + t.size);
			holder.setText(R.id.tv_price, "单价：¥" + t.price);
			holder.setText(R.id.tv_num, "数量：" + t.num);
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
		xlv_detail.stopRefresh();
		// 停止加载更多
		xlv_detail.stopLoadMore();
		// 设置最后一次刷新时间
		xlv_detail.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
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

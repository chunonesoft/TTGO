package com.chunsoft.ttgo.myself;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.Constant;
import com.chunsoft.ttgo.R;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrderDetail_A extends Activity implements IXListViewListener {
	private XListView xlv_detail;
	ProgressDialog dialog = null;
	View view1, view2;
	TextView tv_order_state, tv_order_name, tv_order_phone, tv_order_address;
	OrderConListBean getData;
	private OrderDetailAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		Intent intent = getIntent();
		getData = (OrderConListBean) intent.getSerializableExtra("detailData");
		initView();
		setData();
	}

	private void setData() {
		Log.e("多少条数据---->", getData.productList.size() + "");
		adapter = new OrderDetailAdapter(OrderDetail_A.this,
				getData.productList, R.layout.order_item1);
		xlv_detail.setAdapter(adapter);

	}

	private void initView() {
		xlv_detail = (XListView) findViewById(R.id.xlv_detail);
		View view1 = LayoutInflater.from(OrderDetail_A.this).inflate(
				R.layout.order_detail1, null);
		View view2 = LayoutInflater.from(OrderDetail_A.this).inflate(
				R.layout.order_detail2, null);
		TextView tv_order_state = (TextView) view1
				.findViewById(R.id.tv_order_state);
		TextView tv_order_name = (TextView) view1
				.findViewById(R.id.tv_order_name);
		TextView tv_order_phone = (TextView) view1
				.findViewById(R.id.tv_order_phone);
		TextView tv_order_address = (TextView) view1
				.findViewById(R.id.tv_order_address);
		TextView tv_orederNo = (TextView) view2.findViewById(R.id.tv_orederNo);
		TextView tv_saleTime = (TextView) view2.findViewById(R.id.tv_saleTime);
		TextView tv_allmoney = (TextView) view2.findViewById(R.id.tv_allmoney);
		TextView tv_orederTime = (TextView) view2
				.findViewById(R.id.tv_orederTime);
		xlv_detail.addHeaderView(view1);
		xlv_detail.addFooterView(view2);
		tv_order_address.setText("收货地址：" + getData.receiveAddress);
		tv_order_name.setText("收件人：" + getData.receiveName);
		tv_order_phone.setText(getData.receiveMobile);
		tv_order_state.setText("订单状态：" + getData.statusName);
		tv_orederNo.setText("订单编号：" + getData.orderNo);
		tv_allmoney.setText("合计：¥" + getData.proTotalPrice);
		tv_orederTime.setText("成交时间" + getData.statusName);

		xlv_detail.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		xlv_detail.setPullLoadEnable(true);
		xlv_detail.setPullRefreshEnable(true);
	}

	@Override
	public void onRefresh() {
		onLoad();
	}

	@Override
	public void onLoadMore() {
		onLoad();
	}

	class OrderDetailAdapter extends CommonAdapter<ProductListBean> {

		public OrderDetailAdapter(Context context, List<ProductListBean> datas,
				int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ProductListBean t) {
			ImageView iv_image = holder.getView(R.id.iv_image);
			ImageLoader.getInstance().displayImage(Constant.ImageUri + t.path,
					iv_image);
			holder.setText(R.id.tv_order_time, "2016-04-19");
			holder.setText(R.id.tv_order_state, "已发货");
			holder.setText(R.id.tv_name, t.proName);
			holder.setText(R.id.tv_color, "颜色：" + t.style);
			holder.setText(R.id.tv_size, "尺码：" + t.style);
			holder.setText(R.id.tv_all_num, "共" + t.num + "件商品");
			holder.setText(R.id.tv_all_price, "合计：¥" + t.proPrice);
		}

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

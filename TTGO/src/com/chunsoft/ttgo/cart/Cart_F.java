package com.chunsoft.ttgo.cart;

import com.chunsoft.adapter.CartAdapter;
import com.chunsoft.net.Data;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.OrderBean;
import com.chunsoft.ttgo.util.IBtnCallListener;
import com.chunsoft.view.PullToRefreshListView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

public class Cart_F extends Fragment implements IBtnCallListener,OnCheckedChangeListener,OnClickListener{
	IBtnCallListener btnCallListener;
	//控件声明
	
	
	CartAdapter adapter;
	
	OrderBean bean;
	ListView lv;
	private PullToRefreshListView content_view;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.cart_f, null);
		FindView(view);
		initView();
		initView();
		return view;
	}
	
	private void initView()
	{
		// 如果购物车中有数据，那么就显示数据，否则显示默认界面
		//if (Data.arrayList_cart != null && Data.arrayList_cart.size() != 0) {
			//adapter = new Adapter_ListView_cart(getActivity(), Data.arrayList_cart);
			adapter = new CartAdapter(getActivity(), Data.arrayList_cart, R.layout.cart_item);
			//adapter.setOnCheckedChanged(this);
			//listView_cart.setAdapter(adapter);
			lv.setAdapter(adapter);
		//} 
	}
	/**
	 * 控件实例化
	 * @param view
	 */
	private void FindView(View view)
	{
		content_view = (PullToRefreshListView) view.findViewById(R.id.content_view);
		lv = content_view.getRefreshableView();
	}
	

	@Override
	public void transferMsg() {
		// 这里响应在FragmentActivity中的控件交互
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		btnCallListener = (IBtnCallListener) activity;
		super.onAttach(activity);
	}

}

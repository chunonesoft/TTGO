package com.chunsoft.ttgo.cart;

import java.util.ArrayList;
import java.util.List;

import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.OrderBean;
import com.chunsoft.view.PullToRefreshListView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Cart_F extends Fragment {
	List<OrderBean> datas = new ArrayList<>();
	OrderBean bean;
	ListView lv;
	private PullToRefreshListView content_view;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.cart_f, null);
		init();
		CartData adapter = new CartData(getActivity(), datas, R.layout.cart_item);
		content_view = (PullToRefreshListView) view.findViewById(R.id.content_view);
		lv = content_view.getRefreshableView();
		lv.setAdapter(adapter);
		return view;
	}
	
	private void init()
	{
		for(int i = 0;i <5;i++)
		{
			bean = new OrderBean();
			bean.Color_Size = "1";
			datas.add(bean);
		}
	}
	
	class CartData extends CommonAdapter<OrderBean>
	{

		public CartData(Context context, List<OrderBean> datas, int layoutId) {
			super(context, datas, R.layout.cart_item);
			
		}

		@Override
		public void convert(ViewHolder holder, OrderBean t) {
			
		}
		
	}
}

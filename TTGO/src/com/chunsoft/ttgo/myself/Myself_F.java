package com.chunsoft.ttgo.myself;

import java.util.ArrayList;
import java.util.List;

import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.OrderBean;
import com.chunsoft.view.MyGridView;

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
import android.widget.LinearLayout;

public class Myself_F extends Fragment implements OnClickListener,OnItemClickListener{
	private LinearLayout ll_myorder,ll_set,ll_person,ll_adress,ll_count;
	private Intent intent;
	private MyGridView gv_user;
	GridAdapter adapter;
	
	OrderBean bean;
	List<OrderBean> datas = new ArrayList<>();
	//资源文件
	private int[] pic_path={R.drawable.user_3,R.drawable.user_4,R.drawable.user_5,R.drawable.user_6,R.drawable.user_7};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.myself_f, null);
		FindView(view);
		initview();
		Click();
		
		return view;
	}

	private void initview()
	{
		//initData
		for(int i = 0;i < pic_path.length;i++)
		{
			bean = new OrderBean();
			bean.image = pic_path[i];
			datas.add(bean);
		}
		adapter = new GridAdapter(getActivity(), datas, R.layout.griditem);
		gv_user.setAdapter(adapter);
	}
	private void FindView(View view)
	{
		ll_myorder = (LinearLayout) view.findViewById(R.id.ll_myorder);
		ll_set = (LinearLayout) view.findViewById(R.id.ll_set);
		ll_person = (LinearLayout) view.findViewById(R.id.ll_person);
		ll_adress = (LinearLayout) view.findViewById(R.id.ll_adress);
		ll_count = (LinearLayout) view.findViewById(R.id.ll_count);
		gv_user = (MyGridView) view.findViewById(R.id.gv_user);
	}
	private void Click()
	{
		ll_myorder.setOnClickListener(this);
		ll_set.setOnClickListener(this);
		ll_person.setOnClickListener(this);
		ll_adress.setOnClickListener(this);
		ll_count.setOnClickListener(this);
		gv_user.setOnItemClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {		
		case R.id.ll_myorder:
			intent = new Intent(getActivity(),MyOrder.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	class GridAdapter extends CommonAdapter<OrderBean>
	{

		public GridAdapter(Context context, List<OrderBean> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, OrderBean t) {
			holder.setImageResouce(R.id.iv_grid_pic,t.image);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
		case 1:
		case 2:
			intent = new Intent(getActivity(),MyOrder.class);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(getActivity(),Ph_A.class);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(getActivity(),Tk_A.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}

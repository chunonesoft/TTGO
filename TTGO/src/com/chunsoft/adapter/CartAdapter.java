package com.chunsoft.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ProductBean;

public class CartAdapter extends CommonAdapter<ProductBean>{
	private onCheckedChanged listener;
	//衣服增加每次递增
	private final int ADDORREDUCE = 1;
	private TextView tv_num;
	public CartAdapter(Context context, ArrayList<ProductBean> datas, int layoutId) {
		super(context, datas, R.layout.cart_item);
	}

	@Override
	public void convert(final ViewHolder holder, ProductBean t) {
		//holder.setText(R.id.tv_num, String.valueOf(t.ProductNum));
		
		holder.getView(R.id.tv_add).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_num = holder.getView(R.id.tv_num);
				String num = Integer.valueOf(tv_num.getText().toString())+ADDORREDUCE+"";
				tv_num.setText(num);
			}
		});
		
		holder.getView(R.id.tv_reduce).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv_num = holder.getView(R.id.tv_num);
				String num = Integer.valueOf(tv_num.getText().toString())-ADDORREDUCE+"";
				tv_num.setText(num);
			}
		});
	}
	public interface onCheckedChanged{
		
		public void getChoiceData(int position,boolean isChoice);
	}
	
	public void setOnCheckedChanged(onCheckedChanged listener){
		this.listener=listener;
	}


}

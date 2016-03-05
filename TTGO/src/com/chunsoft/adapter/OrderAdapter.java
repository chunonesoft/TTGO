package com.chunsoft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.TextView;
import android.widget.Toast;

import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.OrderBean;
import com.chunsoft.ttgo.home.Popwindow;

public class OrderAdapter extends CommonAdapter<OrderBean>{
	private TextView all_num,all_money;
	//衣服增加每次递增
	private final int ADDORREDUCE=1;
	private TextView pop_num,tv_money;
	private Context mContext;
	public OrderAdapter(Context context, List<OrderBean> datas, int layoutId) {
		
		super(context, datas, R.layout.cart_popview_item);	
		mContext = context;
	}

	@Override
	public void convert(final ViewHolder holder, OrderBean t) {
		holder.getView(R.id.pop_add).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				pop_num = holder.getView(R.id.pop_num);
				String num_add = Integer.valueOf(pop_num.getText().toString())+ADDORREDUCE+"";
				pop_num.setText(num_add);
				tv_money = holder.getView(R.id.tv_money);
				tv_money.setText("¥"+Integer.valueOf(num_add)*129+"");
			}
		});
		holder.getView(R.id.pop_reduce).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop_num = holder.getView(R.id.pop_num);
				if(!pop_num.getText().toString().equals("0"))
				{
					String num_reduce = Integer.valueOf(pop_num.getText().toString())-ADDORREDUCE+"";
					pop_num.setText(num_reduce);
					tv_money = holder.getView(R.id.tv_money);
					tv_money.setText("¥"+Integer.valueOf(num_reduce)*129);
				}				
			}
		});
	}

}

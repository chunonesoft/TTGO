package com.chunsoft.ttgo.home;

import java.util.ArrayList;
import java.util.List;

import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.Data;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.OrderBean;
import com.chunsoft.ttgo.bean.ProductBean;
import com.chunsoft.view.MyListView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class Popwindow implements OnDismissListener, OnClickListener{

	private Context context;
	TextView all_money,all_num,tv_all;
	int price = 120;
	int totalPrice = 0; // 商品总价
	int totalNum = 0;   //商品总件数
	private TextView pop_ok;
	private ImageView pop_del;
	private MyListView mylv;
	OrderAdapter adapter;
	ProductBean bean;
	ArrayList<ProductBean> testData = new ArrayList<ProductBean>();
	Data datas = new Data();
	
	
	private PopupWindow popupWindow;
	private OnItemClickListener listener;
	public Popwindow (Context context)
	{
		
		this.context=context;
		View view=LayoutInflater.from(context).inflate(R.layout.popwindow, null);
		findView(view);
		popupWindow=new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//设置popwindow的动画效果
		popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
		Click();
		//LinearLayout layout = (LinearLayout) mylv.getChildAt(1);
		//TextView pop_num = (TextView) layout.findViewById(R.id.pop_num);
		//TextView pop_num = (TextView) layout.getChildAt(1);
		//Toast.makeText(context, pop_num.getText().toString(), Toast.LENGTH_SHORT).show();
		adapter = new OrderAdapter(context, testData, R.layout.cart_popview_item);
		mylv.setAdapter(adapter);
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.pop_del:
			listener.onClickOKPop();
			dissmiss();
			
			break;
		case R.id.pop_ok:
			listener.onClickOKPop();
			
			/*if (str_color.equals("")) {
				Toast.makeText(context, "亲，你还没有选择颜色哟~", Toast.LENGTH_SHORT).show();
			}else if (str_type.equals("")) {
				Toast.makeText(context, "亲，你还没有选择类型哟~",Toast.LENGTH_SHORT).show();
			}else {
				HashMap<String, Object> allHashMap=new HashMap<String,Object>();
				
				allHashMap.put("color",str_color);
				allHashMap.put("type",str_type);
				allHashMap.put("num",pop_num.getText().toString());
				allHashMap.put("id",Data.arrayList_cart_id+=1);
				
				Data.arrayList_cart.add(allHashMap);
				setSaveData();*/
				dissmiss();
				break;
		default:
			break;
		}
	}

	@Override
	public void onDismiss() {
		
	}
	
	public interface OnItemClickListener{
		/** 设置点击确认按钮时监听接口 */
		public void onClickOKPop();
	}

	/**设置监听*/
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener=listener;
	}
	
	/**弹窗显示的位置*/  
	public void showAsDropDown(View parent){
		popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}
	
	/**消除弹窗*/
	public void dissmiss(){
		popupWindow.dismiss();
	}
	
	public void findView(View view)
	{
		mylv = (MyListView) view.findViewById(R.id.mylv);
		pop_ok=(TextView) view.findViewById(R.id.pop_ok);
		pop_del=(ImageView) view.findViewById(R.id.pop_del);
		all_money = (TextView) view.findViewById(R.id.all_money);
		all_num = (TextView) view.findViewById(R.id.all_num);
	}
	public void Click()
	{
		for(int i = 0;i < 9;i++)
		{
			bean = new ProductBean();
			bean.ProductKind = "白色XL";
			testData.add(bean);
		}
		pop_ok.setOnClickListener(this);
		pop_del.setOnClickListener(this);
	}
	
	/**保存购物车的数据*/
	private void setSaveData(){
		SharedPreferences sp= context.getSharedPreferences("SAVE_CART", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("ArrayCart_size", Data.arrayList_cart.size());
		for (int i = 0; i < Data.arrayList_cart.size(); i++) {
			editor.remove("ArrayCart_ProductKind_"+i);
			editor.remove("ArrayCart_ProductNum_"+i);
			editor.remove("ArrayCart_ProductId_"+i);
			editor.putString("ArrayCart_ProductKind_"+i, Data.arrayList_cart.get(i).ProductKind.toString());
			editor.putInt("ArrayCart_ProductNum_"+i, Data.arrayList_cart.get(i).ProductNum);
			editor.putInt("ArrayCart_ProductId_"+i, Data.arrayList_cart.get(i).ProductId);	
		}
	}
	
	class OrderAdapter extends CommonAdapter<ProductBean>{
		//衣服增加每次递增
		private final int ADDORREDUCE = 1;
		private TextView pop_num,tv_money;
		public OrderAdapter(Context context, List<ProductBean> datas, int layoutId) {
			super(context, datas, R.layout.cart_popview_item);	
		}

		@Override
		public void convert(final ViewHolder holder, ProductBean t) {
			holder.getView(R.id.pop_add).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					pop_num = holder.getView(R.id.pop_num);
					String num_add = Integer.valueOf(pop_num.getText().toString())+ADDORREDUCE+"";
					pop_num.setText(num_add);
					totalNum += 1;
					totalPrice = totalNum*price;
					all_num.setText("总计："+totalNum+"件");
					all_money.setText("总金额："+totalPrice+"元");
					tv_money = holder.getView(R.id.tv_money);
					tv_money.setText("¥"+Integer.valueOf(num_add)*129+"");
				}
			});
			pop_num = holder.getView(R.id.pop_num);
			pop_num.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					int position = holder.getPosition(); 
					if(isExists(holder.getPosition()) != -1)
					{
						if(pop_num.getText().toString().equals("0"))
						{
							datas.arrayList_cart.remove(position);
							position--;
						}
						else{ 
							datas.arrayList_cart.get(position).ProductNum = Integer.valueOf(pop_num.getText().toString());
						}
					}
					else
					{
						bean = new ProductBean();
						bean.ProductId = position;
						bean.ProductKind = "白色X" + position;
						bean.ProductPrice = 120;
						bean.ProductNum = Integer.valueOf(pop_num.getText().toString());
						bean.ProductName = "这是一件美丽的衣服" + position;
						datas.arrayList_cart.add(bean);
						//datas.arrayList_cart.get(position).ProductId = position;
						
						//datas.arrayList_cart.get(position).ProductNum =  Integer.valueOf(pop_num.getText().toString());
					}
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
						totalNum -= 1;
						totalPrice = totalNum*price;
						all_num.setText("总计："+totalNum+"件");
						all_money.setText("总金额："+totalPrice+"元");
						tv_money = holder.getView(R.id.tv_money);
						tv_money.setText("¥"+Integer.valueOf(num_reduce)*129);
					}				
				}
			});
			
			/**
			 * getData
			 */
		}
		
		public int isExists(int id)
		{
			for(int i = 0;i < datas.arrayList_cart.size();i ++)
			{
				if(datas.arrayList_cart.get(i).ProductId == id)
				{
					return id;
				}
			}
			return -1;
		}

	}
}

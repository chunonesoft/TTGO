package com.chunsoft.ttgo.home;

import com.chunsoft.ttgo.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
	private TextView pop_choice_16g,pop_choice_32g,pop_choice_16m,pop_choice_32m,pop_choice_black,pop_choice_white,pop_add,pop_reduce,pop_num,pop_ok;
	private ImageView pop_del;
	
	private PopupWindow popupWindow;
	//衣服增加每次递增
	private final int ADDORREDUCE=1;
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
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_add:
			if (!pop_num.getText().toString().equals("750")) {
				
				String num_add=Integer.valueOf(pop_num.getText().toString())+ADDORREDUCE+"";
				pop_num.setText(num_add);
			}else {
				Toast.makeText(context, "不能超过最大产品数量", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.pop_reduce:
			if (!pop_num.getText().toString().equals("1")) {
				String num_reduce=Integer.valueOf(pop_num.getText().toString())-ADDORREDUCE+"";
				pop_num.setText(num_reduce);
			}else {
				Toast.makeText(context, "购买数量不能低于1件", Toast.LENGTH_SHORT).show();
			}
			break;
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
		pop_choice_16g=(TextView) view.findViewById(R.id.pop_choice_16g);
		pop_choice_32g=(TextView) view.findViewById(R.id.pop_choice_32g);
		pop_choice_16m=(TextView) view.findViewById(R.id.pop_choice_16m);
		pop_choice_32m=(TextView) view.findViewById(R.id.pop_choice_32m);
		pop_choice_black=(TextView) view.findViewById(R.id.pop_choice_black);
		pop_choice_white=(TextView) view.findViewById(R.id.pop_choice_white);
		pop_add=(TextView) view.findViewById(R.id.pop_add);
		pop_reduce=(TextView) view.findViewById(R.id.pop_reduce);
		pop_num=(TextView) view.findViewById(R.id.pop_num);
		pop_ok=(TextView) view.findViewById(R.id.pop_ok);
		pop_del=(ImageView) view.findViewById(R.id.pop_del);
	}
	public void Click()
	{
		pop_choice_16g.setOnClickListener(this);
		pop_choice_32g.setOnClickListener(this);
		pop_choice_16m.setOnClickListener(this);
		pop_choice_32m.setOnClickListener(this);
		pop_choice_black.setOnClickListener(this);
		pop_choice_white.setOnClickListener(this);
		pop_add.setOnClickListener(this);
		pop_reduce.setOnClickListener(this);
		pop_ok.setOnClickListener(this);
		pop_del.setOnClickListener(this);
	}

}

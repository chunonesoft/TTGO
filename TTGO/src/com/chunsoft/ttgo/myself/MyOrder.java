package com.chunsoft.ttgo.myself;

import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.home.Home_F;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MyOrder extends FragmentActivity implements OnClickListener{
	private TextView bt_cart_all, bt_cart_dfk, bt_cart_dfh, bt_cart_dsh, bt_cart_ysh;
	private View show_cart_all, show_cart_dfk,show_cart_dfh, show_cart_dsh, show_cart_stock, show_cart_ysh;
	MyOrder_All all;
	MyOrder_dfk dfk;
	MyOrder_dfh dfh;
	MyOrder_dsh dsh;
	MyOrder_ysh ysh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order);
		FindView();
		Click();
		initView();
	}
	
	/**控件实例化*/
	private void FindView()
	{
		bt_cart_all = (TextView) findViewById(R.id.bt_cart_all);
		bt_cart_dfk = (TextView) findViewById(R.id.bt_cart_dfk);
		bt_cart_dfh = (TextView) findViewById(R.id.bt_cart_dfh); 
		bt_cart_dsh = (TextView) findViewById(R.id.bt_cart_all);
		bt_cart_ysh = (TextView) findViewById(R.id.bt_cart_all);
		show_cart_all = findViewById(R.id.show_cart_all);
		show_cart_dfk = findViewById(R.id.show_cart_dfk);
		show_cart_dfh = findViewById(R.id.show_cart_dfh);
		show_cart_dsh = findViewById(R.id.show_cart_dsh);
		show_cart_ysh = findViewById(R.id.show_cart_ysh);
	}
	private void Click()
	{
		bt_cart_all.setOnClickListener(this);
		bt_cart_dfk.setOnClickListener(this);
		bt_cart_dfh.setOnClickListener(this);
		bt_cart_dsh.setOnClickListener(this);
		bt_cart_ysh.setOnClickListener(this);	
	}
	
	private void initView()
	{	//设置默认界面
		if(all == null)
		{
			all = new MyOrder_All();
			addFragment(all);
			showFragment(all);
		}
		else
		{
			if(all.isHidden())
			{
				showFragment(all);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_cart_all:
			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Black));
			show_cart_dfk.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dfh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_ysh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dsh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			break;
		case R.id.bt_cart_dfk:
			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dfk.setBackgroundColor(getResources().getColor(R.color.bg_Black));
			show_cart_dfh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_ysh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dsh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			break;
		case R.id.bt_cart_dfh:
			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dfk.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dfh.setBackgroundColor(getResources().getColor(R.color.bg_Black));
			show_cart_ysh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dsh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			break;
		case R.id.bt_cart_dsh:
			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dfk.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dfh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_ysh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dsh.setBackgroundColor(getResources().getColor(R.color.bg_Black));
			break;
		case R.id.bt_cart_ysh:
			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dfk.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_dfh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_ysh.setBackgroundColor(getResources().getColor(R.color.bg_Black));
			show_cart_dsh.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			break;
	
		default:
			break;
		}
	}
	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.show_cart_view, fragment);
		ft.commit();
	}
	
	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}
	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		// 设置Fragment的切换动画
		ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if (all != null) {
			ft.hide(all);
		}
		if (dfk != null) {
			ft.hide(dfk);
		}
		if (dfh != null) {
			ft.hide(dfh);
		}
		if (ysh != null) {
			ft.hide(ysh);
		}
		if (dsh != null) {
			ft.hide(dsh);
		}
		ft.show(fragment);
		ft.commitAllowingStateLoss();

	}

}

package com.chunsoft.ttgo.home;


import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.cart.Cart_F;
import com.chunsoft.ttgo.group.Group_F;
import com.chunsoft.ttgo.myself.Myself_F;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Main_FA extends FragmentActivity implements OnClickListener{

	//底面菜单按钮
	private ImageView[] bt_menu = new ImageView[5];
	//地面菜单按钮id
	private int[] bt_menu_id = {R.id.iv_menu_0,R.id.iv_menu_0,R.id.iv_menu_1,R.id.iv_menu_2,R.id.iv_menu_3};
	
	// 界面底部的选中菜单按钮资源
	private int[] select_on = { R.drawable.guide_home_on, R.drawable.guide_tfaccount_on, R.drawable.guide_cart_on, R.drawable.guide_account_on};
	// 界面底部的未选中菜单按钮资源
	private int[] select_off = { R.drawable.bt_menu_0_select, R.drawable.bt_menu_1_select, R.drawable.bt_menu_2_select, R.drawable.bt_menu_3_select};
	
	/*主页界面*/
	private Home_F home_F; 
	/*社区界面*/
	private Group_F group_F;
	/*购物车界面*/
	private Cart_F cart_F;
	/*我界面*/
	private Myself_F myself_F;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.mainfa);
		initView();
	}
	
	//初始化组件
	private void initView()
	{
		for(int i = 0;i < 4;i++)
		{
			bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
			bt_menu[i].setOnClickListener(this);
		}
		
		//设置默认界面
		if(home_F == null)
		{
			home_F = new Home_F();
			addFragment(home_F);
			showFragment(home_F);
		}
		else
		{
			if(home_F.isHidden())
			{
				showFragment(home_F);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		
	}
	
	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.show_layout, fragment);
		ft.commit();
	}
	
	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		// 设置Fragment的切换动画
		ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if (home_F != null) {
			ft.hide(home_F);
		}
		if (group_F != null) {
			ft.hide(group_F);
		}
		if (cart_F != null) {
			ft.hide(cart_F);
		}
		if (myself_F != null) {
			ft.hide(myself_F);
		}

		ft.show(fragment);
		ft.commitAllowingStateLoss();

	}
}

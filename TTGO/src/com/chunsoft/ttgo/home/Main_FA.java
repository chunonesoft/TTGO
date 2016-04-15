package com.chunsoft.ttgo.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.cart.Cart_F_new1;
import com.chunsoft.ttgo.group.Group_F;
import com.chunsoft.ttgo.myself.Myself_F;
import com.chunsoft.ttgo.util.IBtnCallListener;
import com.chunsoft.ttgo.util.PreferencesUtils;

public class Main_FA extends FragmentActivity implements OnClickListener,
		IBtnCallListener {

	// 底面菜单按钮
	private ImageView[] bt_menu = new ImageView[4];
	// 地面菜单按钮id
	private int[] bt_menu_id = { R.id.iv_menu_0, R.id.iv_menu_1,
			R.id.iv_menu_2, R.id.iv_menu_3 };

	// 界面底部的选中菜单按钮资源
	private int[] select_on = { R.drawable.home_guide_11,
			R.drawable.home_guide_12, R.drawable.home_guide_13,
			R.drawable.home_guide_14 };
	// 界面底部的未选中菜单按钮资源
	private int[] select_off = { R.drawable.bt_menu_0_select,
			R.drawable.bt_menu_1_select, R.drawable.bt_menu_2_select,
			R.drawable.bt_menu_3_select };

	/* 主页界面 */
	private Home_F_new home_F;
	/* 社区界面 */
	private Group_F group_F;
	/* 购物车界面 */
	private Cart_F_new1 cart_F;
	/* 我界面 */
	private Myself_F myself_F;

	private Intent intent;
	private Context mContext;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_fa);
		initView();
	}

	// 初始化组件
	private void initView() {
		mContext = Main_FA.this;
		for (int i = 0; i < 4; i++) {
			bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
			bt_menu[i].setOnClickListener(this);
		}

		// 设置默认界面
		if (home_F == null) {
			home_F = new Home_F_new();
			addFragment(home_F);
			showFragment(home_F);
		} else {
			if (home_F.isHidden()) {
				showFragment(home_F);
			}
		}
		// 设置默认首页点击的图片
		bt_menu[0].setImageResource(select_on[0]);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 主页界面
		case R.id.iv_menu_0:
			if (home_F == null) {
				home_F = new Home_F_new();
				addFragment(home_F);
				showFragment(home_F);
			} else {
				if (home_F.isHidden()) {
					showFragment(home_F);
				}
			}
			break;
		// 社区界面
		case R.id.iv_menu_1:
			if (group_F == null) {
				group_F = new Group_F();
				if (!group_F.isHidden()) {
					addFragment(group_F);
					showFragment(group_F);
				}

			} else {
				if (group_F.isHidden()) {
					showFragment(group_F);
				}
			}
			break;
		// 购物车界面
		case R.id.iv_menu_2:
			if (cart_F != null) {
				removeFragment(cart_F);
				cart_F = null;
			}
			cart_F = new Cart_F_new1();
			// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
			addFragment(cart_F);
			showFragment(cart_F);
			break;
		// 我界面
		case R.id.iv_menu_3:
			if (PreferencesUtils.getSharePreStr(mContext, "userId").equals("")) {
				intent = new Intent(mContext, Login_A.class);
				startActivity(intent);
			} else {
				if (myself_F == null) {
					myself_F = new Myself_F();
					if (!myself_F.isHidden()) {
						addFragment(myself_F);
						showFragment(myself_F);
					}

				} else {
					if (myself_F.isHidden()) {
						showFragment(myself_F);
					}
				}
			}

			break;
		}
		// 设置按钮的选中和未选中资源
		for (int i = 0; i < bt_menu.length; i++) {
			bt_menu[i].setImageResource(select_off[i]);
			if (v.getId() == bt_menu_id[i]) {
				bt_menu[i].setImageResource(select_on[i]);
			}
		}
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.add(R.id.show_layout, fragment);
		ft.commit();
	}

	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
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

	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}

	/** 返回按钮的监听 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	/** Fragment的回调函数 */
	private IBtnCallListener btnCallListener;

	@Override
	public void onAttachFragment(Fragment fragment) {
		try {
			btnCallListener = (IBtnCallListener) fragment;
		} catch (Exception e) {
		}
		super.onAttachFragment(fragment);
	}

	/**
	 * 响应从Fragment中传过来的消息
	 */
	@Override
	public void transferMsg() {
		if (home_F == null) {
			home_F = new Home_F_new();
			addFragment(home_F);
			showFragment(home_F);
		} else {
			showFragment(home_F);
		}
		bt_menu[2].setImageResource(select_off[2]);
		bt_menu[0].setImageResource(select_on[0]);

		System.out.println("由Fragment中传送来的消息");
	}
}

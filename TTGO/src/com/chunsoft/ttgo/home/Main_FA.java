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

	//����˵���ť
	private ImageView[] bt_menu = new ImageView[5];
	//����˵���ťid
	private int[] bt_menu_id = {R.id.iv_menu_0,R.id.iv_menu_0,R.id.iv_menu_1,R.id.iv_menu_2,R.id.iv_menu_3};
	
	// ����ײ���ѡ�в˵���ť��Դ
	private int[] select_on = { R.drawable.guide_home_on, R.drawable.guide_tfaccount_on, R.drawable.guide_cart_on, R.drawable.guide_account_on};
	// ����ײ���δѡ�в˵���ť��Դ
	private int[] select_off = { R.drawable.bt_menu_0_select, R.drawable.bt_menu_1_select, R.drawable.bt_menu_2_select, R.drawable.bt_menu_3_select};
	
	/*��ҳ����*/
	private Home_F home_F; 
	/*��������*/
	private Group_F group_F;
	/*���ﳵ����*/
	private Cart_F cart_F;
	/*�ҽ���*/
	private Myself_F myself_F;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.mainfa);
		initView();
	}
	
	//��ʼ�����
	private void initView()
	{
		for(int i = 0;i < 4;i++)
		{
			bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
			bt_menu[i].setOnClickListener(this);
		}
		
		//����Ĭ�Ͻ���
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
	
	/** ���Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.show_layout, fragment);
		ft.commit();
	}
	
	/** ��ʾFragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		// ����Fragment���л�����
		ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

		// �ж�ҳ���Ƿ��Ѿ�����������Ѿ���������ô�����ص�
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

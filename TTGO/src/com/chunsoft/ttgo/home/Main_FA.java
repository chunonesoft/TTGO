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

	// ����˵���ť
	private ImageView[] bt_menu = new ImageView[4];
	// ����˵���ťid
	private int[] bt_menu_id = { R.id.iv_menu_0, R.id.iv_menu_1,
			R.id.iv_menu_2, R.id.iv_menu_3 };

	// ����ײ���ѡ�в˵���ť��Դ
	private int[] select_on = { R.drawable.home_guide_11,
			R.drawable.home_guide_12, R.drawable.home_guide_13,
			R.drawable.home_guide_14 };
	// ����ײ���δѡ�в˵���ť��Դ
	private int[] select_off = { R.drawable.bt_menu_0_select,
			R.drawable.bt_menu_1_select, R.drawable.bt_menu_2_select,
			R.drawable.bt_menu_3_select };

	/* ��ҳ���� */
	private Home_F_new home_F;
	/* �������� */
	private Group_F group_F;
	/* ���ﳵ���� */
	private Cart_F_new1 cart_F;
	/* �ҽ��� */
	private Myself_F myself_F;

	private Intent intent;
	private Context mContext;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_fa);
		initView();
	}

	// ��ʼ�����
	private void initView() {
		mContext = Main_FA.this;
		for (int i = 0; i < 4; i++) {
			bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
			bt_menu[i].setOnClickListener(this);
		}

		// ����Ĭ�Ͻ���
		if (home_F == null) {
			home_F = new Home_F_new();
			addFragment(home_F);
			showFragment(home_F);
		} else {
			if (home_F.isHidden()) {
				showFragment(home_F);
			}
		}
		// ����Ĭ����ҳ�����ͼƬ
		bt_menu[0].setImageResource(select_on[0]);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ��ҳ����
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
		// ��������
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
		// ���ﳵ����
		case R.id.iv_menu_2:
			if (cart_F != null) {
				removeFragment(cart_F);
				cart_F = null;
			}
			cart_F = new Cart_F_new1();
			// �жϵ�ǰ�����Ƿ����أ�������ؾͽ��������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
			addFragment(cart_F);
			showFragment(cart_F);
			break;
		// �ҽ���
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
		// ���ð�ť��ѡ�к�δѡ����Դ
		for (int i = 0; i < bt_menu.length; i++) {
			bt_menu[i].setImageResource(select_off[i]);
			if (v.getId() == bt_menu_id[i]) {
				bt_menu[i].setImageResource(select_on[i]);
			}
		}
	}

	/** ���Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.add(R.id.show_layout, fragment);
		ft.commit();
	}

	/** ��ʾFragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
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

	/** ɾ��Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}

	/** ���ذ�ť�ļ��� */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	/** Fragment�Ļص����� */
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
	 * ��Ӧ��Fragment�д���������Ϣ
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

		System.out.println("��Fragment�д���������Ϣ");
	}
}

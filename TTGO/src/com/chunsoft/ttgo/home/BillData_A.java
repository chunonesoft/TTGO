package com.chunsoft.ttgo.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chunsoft.ttgo.R;

@SuppressLint("ResourceAsColor")
public class BillData_A extends FragmentActivity implements OnClickListener {
	private TextView tv_current, tv_history, tv_title;
	private View v_current, v_history;
	Bill_Current current;
	Bill_History history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill);
		FindView();
		Click();
		initView();
	}

	/** 控件实例化 */
	private void FindView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_current = (TextView) findViewById(R.id.tv_current);
		tv_history = (TextView) findViewById(R.id.tv_history);
		v_current = findViewById(R.id.v_current);
		v_history = findViewById(R.id.v_history);
		tv_title.setText(getResources().getText(R.string.bill_detail));
	}

	private void Click() {
		tv_current.setOnClickListener(this);
		tv_history.setOnClickListener(this);
	}

	private void initView() { // 设置默认界面
		if (current == null) {
			current = new Bill_Current();
			addFragment(current);
			showFragment(current);
		} else {
			if (current.isHidden()) {
				showFragment(current);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_current:
			if (current == null) {
				current = new Bill_Current();
				addFragment(current);
				showFragment(current);
			} else {
				showFragment(current);
			}
			tv_current
					.setTextColor(getResources().getColor(R.color.text_click));
			tv_history.setTextColor(getResources()
					.getColor(R.color.text_normal));

			v_current.setBackgroundColor(getResources().getColor(
					R.color.text_click));
			v_history.setBackgroundColor(getResources().getColor(
					R.color.line_normal));

			break;
		case R.id.tv_history:
			if (history == null) {
				history = new Bill_History();
				addFragment(history);
				showFragment(history);
			} else {
				showFragment(history);
			}
			tv_current.setTextColor(getResources()
					.getColor(R.color.text_normal));

			tv_history
					.setTextColor(getResources().getColor(R.color.text_click));

			v_current.setBackgroundColor(getResources().getColor(
					R.color.line_normal));
			v_history.setBackgroundColor(getResources().getColor(
					R.color.text_click));

			break;

		default:
			break;
		}
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.add(R.id.show_bill_view, fragment);
		ft.commit();
	}

	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}

	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		// 设置Fragment的切换动画
		ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if (current != null) {
			ft.hide(current);
		}
		if (history != null) {
			ft.hide(history);

			ft.show(fragment);
			ft.commitAllowingStateLoss();

		}
	}
}

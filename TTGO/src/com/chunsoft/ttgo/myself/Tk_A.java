package com.chunsoft.ttgo.myself;

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
public class Tk_A extends FragmentActivity implements OnClickListener {
	private TextView tv_sqz, tv_thz, tv_yth, tv_title;
	private View v_sqz, v_thz, v_yth;
	Return_sqz sqz;
	Return_thz thz;
	Return_yth yth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tk_a);
		FindView();
		Click();
		initView();
	}

	/** 控件实例化 */
	private void FindView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_sqz = (TextView) findViewById(R.id.tv_sqz);
		tv_thz = (TextView) findViewById(R.id.tv_thz);
		tv_yth = (TextView) findViewById(R.id.tv_yth);
		v_sqz = findViewById(R.id.v_sqz);
		v_thz = findViewById(R.id.v_thz);
		v_yth = findViewById(R.id.v_yth);
		tv_title.setText(getResources().getText(R.string.return_pro));
	}

	private void Click() {
		tv_sqz.setOnClickListener(this);
		tv_thz.setOnClickListener(this);
		tv_yth.setOnClickListener(this);
	}

	private void initView() { // 设置默认界面
		if (sqz == null) {
			sqz = new Return_sqz();
			addFragment(sqz);
			showFragment(sqz);
		} else {
			if (sqz.isHidden()) {
				showFragment(sqz);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sqz:
			if (sqz == null) {
				sqz = new Return_sqz();
				addFragment(sqz);
				showFragment(sqz);
			} else {
				showFragment(sqz);
			}
			tv_sqz.setTextColor(getResources().getColor(R.color.text_click));
			tv_yth.setTextColor(getResources().getColor(R.color.text_normal));
			tv_thz.setTextColor(getResources().getColor(R.color.text_normal));

			v_sqz.setBackgroundColor(getResources()
					.getColor(R.color.text_click));
			v_thz.setBackgroundColor(getResources().getColor(
					R.color.line_normal));
			v_yth.setBackgroundColor(getResources().getColor(
					R.color.line_normal));

			break;
		case R.id.tv_thz:
			if (thz == null) {
				thz = new Return_thz();
				addFragment(thz);
				showFragment(thz);
			} else {
				showFragment(thz);
			}
			tv_sqz.setTextColor(getResources().getColor(R.color.text_normal));
			tv_yth.setTextColor(getResources().getColor(R.color.text_normal));
			tv_thz.setTextColor(getResources().getColor(R.color.text_click));

			v_sqz.setBackgroundColor(getResources().getColor(
					R.color.line_normal));
			v_thz.setBackgroundColor(getResources()
					.getColor(R.color.text_click));
			v_yth.setBackgroundColor(getResources().getColor(
					R.color.line_normal));

			break;
		case R.id.tv_yth:
			if (yth == null) {
				yth = new Return_yth();
				addFragment(yth);
				showFragment(yth);
			} else {
				showFragment(yth);
			}

			tv_sqz.setTextColor(getResources().getColor(R.color.text_normal));
			tv_yth.setTextColor(getResources().getColor(R.color.text_click));
			tv_thz.setTextColor(getResources().getColor(R.color.text_normal));

			v_sqz.setBackgroundColor(getResources().getColor(
					R.color.line_normal));
			v_thz.setBackgroundColor(getResources().getColor(
					R.color.line_normal));
			v_yth.setBackgroundColor(getResources()
					.getColor(R.color.text_click));
			break;
		default:
			break;
		}
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.add(R.id.show_return_view, fragment);
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
		if (sqz != null) {
			ft.hide(sqz);
		}
		if (thz != null) {
			ft.hide(thz);
		}
		if (yth != null) {
			ft.hide(yth);
		}
		ft.show(fragment);
		ft.commitAllowingStateLoss();

	}

}

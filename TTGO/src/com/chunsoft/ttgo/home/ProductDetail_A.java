package com.chunsoft.ttgo.home;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.home.Popwindow.OnItemClickListener;
import com.chunsoft.ttgo.myself.AdressList;
import com.chunsoft.view.ScaleView.HackyViewPager;

public class ProductDetail_A extends Activity implements OnItemClickListener,
		OnClickListener {
	private Context mContext;
	private ImageView iv_back;
	private ImageView put_in;
	private ImageView buy_now;
	private ImageView iv_kf;

	/** 用于设置背景暗淡 */
	private LinearLayout all_choice_layout = null;

	/** 判断是否点击的立即购买按钮 */
	boolean isClickBuy = false;
	private ArrayList<View> allListView;
	private HackyViewPager viewPager;
	private Popwindow popWindow;
	/** ViewPager当前显示页的下标 */
	private int position = 0;
	/** 是否添加收藏 */
	private static boolean isCollection = false;
	private ImageView iv_baby_collection;
	private int[] resId = { R.drawable.detail_show_1, R.drawable.detail_show_2,
			R.drawable.detail_show_3, R.drawable.detail_show_4,
			R.drawable.detail_show_5, R.drawable.detail_show_6 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productdetail_a);
		findView();
		init();
		// 得到保存的收藏信息
		getSaveCollection();
		initViewPager();
		Click();
		popWindow = new Popwindow(this);
		popWindow.setOnItemClickListener(this);
	}

	private void findView() {
		all_choice_layout = (LinearLayout) findViewById(R.id.all_choice_layout);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_kf = (ImageView) findViewById(R.id.iv_kf);
		put_in = (ImageView) findViewById(R.id.put_in);
		buy_now = (ImageView) findViewById(R.id.buy_now);
		iv_baby_collection = (ImageView) findViewById(R.id.iv_baby_collection);
	}

	private void init() {
		mContext = ProductDetail_A.this;
	}

	private void initViewPager() {

		if (allListView != null) {
			allListView.clear();
			allListView = null;
		}
		allListView = new ArrayList<View>();

		for (int i = 0; i < resId.length; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.pic_item,
					null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			imageView.setImageResource(resId[i]);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 挑战到查看大图界面
					Intent intent = new Intent(mContext, ShowBigPictrue.class);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});
			allListView.add(view);
		}

		viewPager = (HackyViewPager) findViewById(R.id.iv_baby);
		ViewPagerAdapter adapter = new ViewPagerAdapter();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				position = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		viewPager.setAdapter(adapter);
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return allListView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (View) arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = allListView.get(position);
			container.addView(view);
			return view;
		}

	}

	/** 注册监听 */
	private void Click() {
		iv_back.setOnClickListener(this);
		iv_baby_collection.setOnClickListener(this);
		put_in.setOnClickListener(this);
		buy_now.setOnClickListener(this);
		iv_kf.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_baby_collection:
			// 收藏
			if (isCollection) {
				// 提示是否取消收藏
				cancelCollection();
			} else {
				isCollection = true;
				setSaveCollection();
				// 如果已经收藏，则显示收藏后的效果
				iv_baby_collection
						.setImageResource(R.drawable.second_2_collection);
				Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.put_in:
			// 添加购物车
			isClickBuy = false;
			setBackgroundBlack(all_choice_layout, 0);
			popWindow.showAsDropDown(v);
			break;
		case R.id.buy_now:
			// 立即购买
			/*
			 * isClickBuy = true; setBackgroundBlack(all_choice_layout, 0);
			 * popWindow.showAsDropDown(v);
			 */
			Intent intent = new Intent(mContext, AdressList.class);
			startActivity(intent);
			break;
		case R.id.iv_kf:
			// 联系客服
			AlertDialog.Builder builder = new Builder(ProductDetail_A.this);
			// builder.setMessage("联系客服");
			builder.setTitle("18868448198");
			builder.setPositiveButton("联系客服",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + "18868448198"));
							startActivity(intent);
						}
					});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			break;
		default:
			break;
		}
	}

	/** 保存是否添加收藏 */
	private void setSaveCollection() {
		SharedPreferences sp = getSharedPreferences("SAVECOLLECTION",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isCollection", isCollection);
		editor.commit();
	}

	/** 得到保存的是否添加收藏标记 */
	private void getSaveCollection() {
		SharedPreferences sp = getSharedPreferences("SAVECOLLECTION",
				Context.MODE_PRIVATE);
		isCollection = sp.getBoolean("isCollection", false);

	}

	/** 取消收藏 */
	private void cancelCollection() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("是否取消收藏");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				isCollection = false;
				// 如果取消收藏，则显示取消收藏后的效果
				iv_baby_collection.setImageResource(R.drawable.second_2);
				setSaveCollection();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.create().show();

	}

	@Override
	public void onClickOKPop() {
		setBackgroundBlack(all_choice_layout, 1);

		if (isClickBuy) {
			// 如果之前是点击的立即购买，那么就跳转到立即购物界面
			// Intent intent = new Intent(mContext, BuynowActivity.class);
			// startActivity(intent);
		} else {
			Toast.makeText(this, "添加到购物车成功", Toast.LENGTH_SHORT).show();
		}
	}

	/** 控制背景变暗 0变暗 1变亮 */
	public void setBackgroundBlack(View view, int what) {
		switch (what) {
		case 0:
			view.setVisibility(View.VISIBLE);
			break;
		case 1:
			view.setVisibility(View.GONE);
			break;
		}
	}

}

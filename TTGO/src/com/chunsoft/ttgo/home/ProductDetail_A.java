package com.chunsoft.ttgo.home;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ProDetailBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.cart.Submit_Order_FA;
import com.chunsoft.ttgo.home.Popwindow.OnItemClickListener;
import com.chunsoft.ttgo.util.PreferencesUtils;
import com.chunsoft.view.ScaleView.HackyViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProductDetail_A extends Activity implements OnItemClickListener,
		OnClickListener {
	/**
	 * widget statement
	 */
	@Bind(R.id.tv_name)
	TextView tv_name;

	@Bind(R.id.tv_price)
	TextView tv_price;

	@Bind(R.id.tv_salenum)
	TextView tv_salenum;

	@Bind(R.id.tv_storenum)
	TextView tv_storenum;

	@Bind(R.id.iv_back)
	ImageView iv_back;

	@Bind(R.id.put_in)
	ImageView put_in;

	@Bind(R.id.buy_now)
	ImageView buy_now;

	@Bind(R.id.iv_kf)
	ImageView iv_kf;

	@Bind(R.id.iv_baby_collection)
	ImageView iv_baby_collection;
	/** 用于设置背景暗淡 */
	@Bind(R.id.all_choice_layout)
	LinearLayout all_choice_layout;

	ProgressDialog dialog = null;

	ViewPagerAdapter adapter;

	/**
	 * variable statement
	 */
	private Context mContext;
	/** 判断是否点击的立即购买按钮 */
	boolean isClickBuy = false;
	private ArrayList<View> allListView;
	private HackyViewPager viewPager;
	private Popwindow popWindow;
	/** ViewPager当前显示页的下标 */
	private int position = 0;

	/** 是否添加收藏 */
	private static boolean isCollection = false;
	private int[] resId = { R.drawable.detail_show_1, R.drawable.detail_show_2,
			R.drawable.detail_show_3, R.drawable.detail_show_4,
			R.drawable.detail_show_5, R.drawable.detail_show_6 };
	private String proID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productdetail_a);
		ButterKnife.bind(this);
		Intent intent = getIntent();
		proID = intent.getStringExtra("proID");
		Log.e("proID--->", proID);
		init();
		// 得到保存的收藏信息
		getSaveCollection();
		Click();

		getDetailData(proID, new VolleyDataCallback<ProDetailBean>() {
			@Override
			public void onSuccess(ProDetailBean datas) {
				tv_name.setText(datas.proName);
				tv_price.setText("¥" + datas.proPrice + "");

				initViewPager(datas.picPath);

				popWindow = new Popwindow(mContext, datas, proID);
				popWindow.setOnItemClickListener(ProductDetail_A.this);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}
		});

	}

	private void init() {
		mContext = ProductDetail_A.this;
	}

	private void initViewPager(final String picPath) {

		if (allListView != null) {
			allListView.clear();
			allListView = null;
		}
		allListView = new ArrayList<View>();

		Log.e("picPath---->", picPath);

		for (int i = 0; i < stringNumbers(picPath); i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.pic_item,
					null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			// imageView.setImageResource(resId[i]);
			final int position = i;
			ImageLoader.getInstance().displayImage(
					Constant.ImageUri + getPicPath(picPath)[i], imageView);// 使用ImageLoader对图片进行加装！
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 挑战到查看大图界面
					Intent intent = new Intent(mContext, ShowBigPictrue.class);
					intent.putExtra("picPath", getPicPath(picPath));
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});
			allListView.add(view);
		}

		viewPager = (HackyViewPager) findViewById(R.id.iv_baby);
		ViewPagerAdapter adapter = new ViewPagerAdapter(allListView);
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
		private ArrayList<View> allListView;

		public ViewPagerAdapter(ArrayList<View> allListView) {
			this.allListView = allListView;
		}

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
			if (PreferencesUtils.getSharePreStr(mContext, "Token").equals("")) {
				ToastUtil.showShortToast(mContext, "请先登录");
			} else {
				// 添加购物车
				isClickBuy = false;
				setBackgroundBlack(all_choice_layout, 0);
				popWindow.showAsDropDown(v);
			}

			break;
		case R.id.buy_now:
			if (PreferencesUtils.getSharePreStr(mContext, "Token").equals("")) {
				ToastUtil.showShortToast(mContext, "请先登录");
			} else {
				// 立即购买
				isClickBuy = true;
				setBackgroundBlack(all_choice_layout, 0);
				popWindow.showAsDropDown(v);
			}

			// Intent intent = new Intent(mContext, AdressList.class);
			// startActivity(intent);
			break;
		case R.id.iv_kf:
			// 联系客服
			AlertDialog.Builder builder = new Builder(ProductDetail_A.this);

			builder.setTitle("联系客服");
			builder.setMessage("我们一直为您服务！");
			builder.setPositiveButton("电话联系",
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
			builder.setNeutralButton("微信联系",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							try {
								ComponentName cmp = new ComponentName(
										"com.tencent.mm",
										"com.tencent.mm.ui.LauncherUI");
								intent.setAction(Intent.ACTION_MAIN);
								intent.addCategory(Intent.CATEGORY_LAUNCHER);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setComponent(cmp);
								startActivityForResult(intent, 0);
							} catch (Exception e) {
							}

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
	public void onClickOKPop(String sendData) {
		setBackgroundBlack(all_choice_layout, 1);
		if (isClickBuy) {
			// 如果之前是点击的立即购买，那么就跳转到立即购物界面
			if (!sendData.equals("")) {
				Intent intent = new Intent(mContext, Submit_Order_FA.class);
				intent.putExtra("sendData", sendData);
				startActivity(intent);
			}

		} else {
			// Toast.makeText(this, "添加到购物车成功", Toast.LENGTH_SHORT).show();
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
		default:
			view.setVisibility(View.GONE);
			break;
		}
	}

	private void getDetailData(String proID,
			final VolleyDataCallback<ProDetailBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(mContext, "", "正在加载...");
			dialog.show();
		}
		JSONObject sendData;
		String URL;
		URL = Constant.IP + Constant.getProDetailInfo;
		Log.e("URL---->", URL);
		sendData = new JSONObject();
		try {
			sendData.put("proID", proID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<ProDetailBean> req = new GsonRequest<>(URL,
				sendData.toString(), new Response.Listener<ProDetailBean>() {

					@Override
					public void onResponse(ProDetailBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(mContext) {

					@Override
					public void onError() {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				}, ProDetailBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}

	public static int stringNumbers(String str) {
		int counter = 1;
		if (str.indexOf(",") == -1) {
			return 1;
		} else if (str.indexOf(",") != -1) {
			counter++;
			stringNumbers(str.substring(str.indexOf(",") + 1));
			return counter;
		}
		return 1;
	}

	public String[] getPicPath(String str) {
		String[] strs = new String[stringNumbers(str)];
		Log.e("[stringNumbers(str)", stringNumbers(str) + "");
		int start = 0;
		int end = str.indexOf(",");
		if (strs.length == 1) {
			strs[0] = str;
			return strs;
		} else {
			for (int i = 0; i < strs.length; i++) {
				if (i != strs.length - 1) {
					strs[i] = str.substring(start, end);
					start = end + 1;
					str = str.substring(start);
					end = str.indexOf(",");
				} else {
					strs[i] = str;
				}
			}
			return strs;
		}
	}
}

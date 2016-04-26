package com.chunsoft.ttgo.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ProBean;
import com.chunsoft.ttgo.bean.ProListBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.util.IntentUti;
import com.chunsoft.ttgo.util.ParseDataUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Show_All_Pro_FA extends FragmentActivity implements
		OnRefreshListener2<GridView>, OnClickListener {
	@Bind(R.id.pull_grid)
	PullToRefreshGridView mPullRefresh;

	@Bind(R.id.pull_list)
	PullToRefreshListView mPullRefreshList;

	@Bind(R.id.ll_change_style)
	LinearLayout ll_change_style;

	@Bind(R.id.ll_back)
	LinearLayout ll_back;

	@Bind(R.id.btn_search)
	Button btn_search;

	@Bind(R.id.iv_change_style)
	ImageView iv_change_style;

	private ProAdapetr adapter;
	private ProAdapetr adapter1;
	private boolean style = true;
	private int type;
	private String name;
	private String proname;
	private int page = 1;
	private int totalPage = 1;
	private String smallTypeId;
	private ProgressDialog dialog = null;
	private Context mContext;
	private List<ProListBean> productList = new ArrayList<ProListBean>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.show_all_pro);
		ButterKnife.bind(this);
		init();
		initData();
		Listrefresh();
	}

	private void initData() {
		getData(proname, smallTypeId, page + "",
				new VolleyDataCallback<ProBean>() {
					@Override
					public void onSuccess(final ProBean datas) {
						page++;
						totalPage = Integer.valueOf(datas.totalpage);
						productList = datas.productList;
						if (productList.size() != 0) {

							adapter = new ProAdapetr(mContext, productList,
									R.layout.home_gv_item);
							mPullRefresh.setAdapter(adapter);

							adapter1 = new ProAdapetr(mContext, productList,
									R.layout.home_list_item);
							mPullRefreshList.setAdapter(adapter1);

						} else {
							ToastUtil.showShortToast(mContext, "没有此产品信息，换个姿势搜");
						}
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
						mPullRefresh
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										Intent intent = new Intent();

										intent.putExtra(
												"proID",
												datas.productList
														.get((int) parent
																.getAdapter()
																.getItemId(
																		position)).proID);
										intent.setClass(mContext,
												ProductDetail_A.class);
										startActivity(intent);
									}
								});

						mPullRefreshList
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										Intent intent = new Intent();

										intent.putExtra(
												"proID",
												datas.productList
														.get((int) parent
																.getAdapter()
																.getItemId(
																		position)).proID);
										intent.setClass(mContext,
												ProductDetail_A.class);
										startActivity(intent);
									}
								});
					}
				});
	}

	private void init() {
		mContext = Show_All_Pro_FA.this;
		type = getIntent().getIntExtra("type", 0);
		name = getIntent().getStringExtra("name");

		if (type != 10 && type != 11) {
			btn_search.setText("分类：" + name);
			proname = "";
		} else if (type == 10) {
			smallTypeId = "";
			proname = name;
			btn_search.setText(name);
		} else if (type == 11) {
			smallTypeId = "";
			proname = "";
			btn_search.setText(name);
		}

		switch (type) {
		case 0:
			smallTypeId = "2";
			break;
		case 1:
			smallTypeId = "3";
			break;
		case 2:
			smallTypeId = "4";
			break;
		case 3:
			smallTypeId = "5";
			break;
		case 4:
			smallTypeId = "6";
			break;
		case 5:
			smallTypeId = "7";
			break;
		case 6:
			smallTypeId = "8";
			break;
		case 7:
			smallTypeId = "9";
			break;
		case 8:
			smallTypeId = "10";
			break;
		default:
			break;
		}
		mPullRefreshList.setVisibility(View.GONE);

		mPullRefresh.setMode(Mode.BOTH);
		// 设置refreshListView的监听器
		mPullRefresh.setOnRefreshListener(this);

		mPullRefreshList.setMode(Mode.PULL_UP_TO_REFRESH);

		ll_back.setOnClickListener(this);
		ll_change_style.setOnClickListener(this);
		btn_search.setOnClickListener(this);
	}

	private void Listrefresh() {
		mPullRefreshList.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// page = 1;
				mPullRefreshList.onRefreshComplete();
				refreshView.onRefreshComplete();
				refreshView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if (page < totalPage) {
					getData(proname, smallTypeId, page + "",
							new VolleyDataCallback<ProBean>() {
								@Override
								public void onSuccess(ProBean datas) {
									page++;
									totalPage = Integer
											.valueOf(datas.totalpage);
									for (int i = 0; i < datas.productList
											.size(); i++) {
										productList.add(datas.productList
												.get(i));
									}
									adapter.notifyDataSetChanged();
									adapter1.notifyDataSetChanged();

									if (dialog != null && dialog.isShowing()) {
										dialog.dismiss();
										dialog = null;
									}
								}
							});
				} else {
					ToastUtil.showShortToast(mContext, "没有更多数据了");
				}
				// 完成刷新调用此方法才会有显示效果
				mPullRefreshList.onRefreshComplete();
			}
		});
		mPullRefreshList.onRefreshComplete();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		page = 1;
		initData();
		// 完成刷新调用此方法才会有显示效果
		mPullRefresh.onRefreshComplete();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		if (page < totalPage) {
			getData(proname, smallTypeId, page + "",
					new VolleyDataCallback<ProBean>() {
						@Override
						public void onSuccess(ProBean datas) {
							page++;
							totalPage = Integer.valueOf(datas.totalpage);
							for (int i = 0; i < datas.productList.size(); i++) {
								productList.add(datas.productList.get(i));
							}
							adapter.notifyDataSetChanged();
							adapter1.notifyDataSetChanged();

							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
								dialog = null;
							}
						}
					});
		} else {
			ToastUtil.showShortToast(mContext, "没有更多数据了");
		}
		// 完成刷新调用此方法才会有显示效果
		mPullRefresh.onRefreshComplete();
	}

	/**
	 * get Data about pro
	 */
	private void getData(String proname, String smallTypeId, String page,
			final VolleyDataCallback<ProBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(mContext, "", "正在加载...");
			dialog.show();
		}
		String URL = Constant.IP + Constant.getProSearchInfo;
		JSONObject sendData = new JSONObject();
		try {
			sendData.put("smallTypeId", smallTypeId);
			sendData.put("page", page);
			sendData.put("name", proname);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<ProBean> request = new GsonRequest<>(URL,
				sendData.toString(), new Response.Listener<ProBean>() {
					@Override
					public void onResponse(ProBean arg0) {
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
				}, ProBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	class ProAdapetr extends CommonAdapter<ProListBean> {
		public ProAdapetr(Context context, List<ProListBean> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ProListBean t) {
			holder.setText(R.id.tv_content, t.name);
			holder.setText(R.id.tv_price, "¥" + t.proPrice);
			holder.setText(R.id.tv_sale, t.saleNum + "人付款");
			ImageView image = holder.getView(R.id.iv);
			holder.getView(R.id.iv).setTag(t.picPath);
			image.setImageResource(R.drawable.icon_empty);
			if (t.picPath.equals(holder.getView(R.id.iv).getTag())) {
				ImageLoader
						.getInstance()
						.displayImage(
								Constant.ImageUri
										+ ParseDataUtil.getPicPath(t.picPath)[0],
								image);// 使用ImageLoader对图片进行加装！
			} else {
				image.setImageResource(R.drawable.icon_empty);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.ll_change_style:
			if (style) {
				iv_change_style.setImageResource(R.drawable.icon_disp_list);
				style = !style;
				mPullRefresh.setVisibility(View.GONE);
				mPullRefreshList.setVisibility(View.VISIBLE);
			} else {
				iv_change_style.setImageResource(R.drawable.icon_disp_icon);
				style = !style;
				mPullRefreshList.setVisibility(View.GONE);
				mPullRefresh.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_search:
			IntentUti.IntentTo(mContext, Search_A.class);
			finish();
		default:
			break;
		}

	}
}

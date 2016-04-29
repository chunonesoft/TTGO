package com.chunsoft.ttgo.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Search_A extends Activity implements OnClickListener,
		IXListViewListener {
	/**
	 * widget statement
	 */
	private ImageView iv_search;
	private XListView lv;
	private EditText et_searsh;
	private ImageView ivDeleteText;
	/**
	 * variable statement
	 */
	private String searchWord;
	private Context mContext;
	private ProBean bean;
	private SearchAdapter adapter;
	private JSONObject sendData;
	private String URL;
	private int page = 1;
	private int totalPage = 1;
	private List<ProListBean> datas = new ArrayList<ProListBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_a);
		findView();
		init();
		onClick();
	}

	/* 对象实例化 */
	private void findView() {
		ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
		et_searsh = (EditText) findViewById(R.id.et_Search);
		lv = (XListView) findViewById(R.id.lv);
		iv_search = (ImageView) findViewById(R.id.iv_search);
	}

	/* 初始化 */
	private void init() {
		mContext = Search_A.this;
		lv.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);

	}

	/* 事件监听 */
	private void onClick() {
		iv_search.setOnClickListener(this);
		ivDeleteText.setOnClickListener(this);
		et_searsh.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			searchWord = et_searsh.getText().toString();
			if (searchWord.equals("")) {
				ToastUtil.showShortToast(mContext, "搜索词不能为空");
			} else {
				page = 1;
				Intent intent = new Intent(Search_A.this, Show_All_Pro_FA.class);
				intent.putExtra("name", searchWord);
				intent.putExtra("type", 1);
				startActivity(intent);
				finish();
				// getSearchData(searchWord, String.valueOf(page),
				// new VolleyDataCallback<ProBean>() {
				//
				// @Override
				// public void onSuccess(final ProBean returnData) {
				// if (returnData.productList.size() == 0) {
				// ToastUtil
				// .showShortToast(mContext, "没有产品信息");
				// } else {
				// totalPage = Integer
				// .valueOf(returnData.totalpage);
				// datas = returnData.productList;
				// page++;
				// adapter = new SearchAdapter(mContext,
				// datas, R.layout.home_gv_item);
				// lv.setAdapter(adapter);
				// lv.setOnItemClickListener(new OnItemClickListener() {
				// @Override
				// public void onItemClick(
				// AdapterView<?> parent,
				// View view, int position, long id) {
				// Intent intent = new Intent();
				// intent.putExtra(
				// "proID",
				// returnData.productList
				// .get(position).proID);
				// intent.setClass(mContext,
				// ProductDetail_A.class);
				// startActivity(intent);
				// }
				// });
				// }
				//
				// }
				// });

			}
			break;
		case R.id.ivDeleteText:
			et_searsh.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		page = 1;
		searchWord = et_searsh.getText().toString();
		getSearchData(searchWord, String.valueOf(page),
				new VolleyDataCallback<ProBean>() {
					@Override
					public void onSuccess(ProBean returnDatas) {
						if (returnDatas.productList.size() == 0) {
							ToastUtil.showShortToast(mContext, "没有产品信息");
						} else {
							datas = returnDatas.productList;
							page++;
							totalPage = Integer.valueOf(returnDatas.totalpage);
							adapter.notifyDataSetChanged();
						}
					}
				});
		// 停止刷新和加载
		onLoad();
	}

	@Override
	public void onLoadMore() {
		searchWord = et_searsh.getText().toString();
		if (page <= totalPage) {
			getSearchData(searchWord, String.valueOf(page),
					new VolleyDataCallback<ProBean>() {
						@Override
						public void onSuccess(ProBean returnDatas) {
							for (int i = 0; i < returnDatas.productList.size(); i++) {
								datas.add(returnDatas.productList.get(i));
							}
							page++;
							totalPage = Integer.valueOf(returnDatas.totalpage);
							adapter.notifyDataSetChanged();
						}
					});
		} else {
			ToastUtil.showLongToast(mContext, "没有更多数据了");
		}
		onLoad();
	}

	/** 停止加载和刷新 */
	private void onLoad() {
		lv.stopRefresh();
		// 停止加载更多
		lv.stopLoadMore();
		// 设置最后一次刷新时间
		lv.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
	}

	/** 简单的时间格式 */
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"MM-dd HH:mm");

	public static String getCurrentTime(long time) {
		if (0 == time) {
			return "";
		}

		return mDateFormat.format(new Date(time));
	}

	public class SearchAdapter extends CommonAdapter<ProListBean> {

		public SearchAdapter(Context context, List<ProListBean> datas,
				int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ProListBean t) {
			if (!t.equals("")) {
				holder.setText(R.id.tv_content, t.name.toString());
				holder.setText(R.id.tv_price, "¥" + t.proPrice.toString());
				holder.setText(R.id.tv_sale, t.saleNum.toString() + "人付款");
				ImageView image = holder.getView(R.id.iv);
				ImageLoader.getInstance().displayImage(
						Constant.ImageUri + t.picPath, image);// 使用ImageLoader对图片进行加装！
			}
		}
	}

	/**
	 * get search data from server
	 */
	private void getSearchData(String name, String page,
			final VolleyDataCallback<ProBean> callback) {
		URL = Constant.IP + Constant.getProSearchInfo;
		sendData = new JSONObject();
		try {
			sendData.put("name", name);
			sendData.put("page", page);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<ProBean> request = new GsonRequest<ProBean>(URL,
				sendData.toString(), new Response.Listener<ProBean>() {
					@Override
					public void onResponse(ProBean arg0) {
						callback.onSuccess(arg0);
					}

				}, new AbstractVolleyErrorListener(mContext) {

					@Override
					public void onError() {

					}
				}, ProBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}
}

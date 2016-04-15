//package com.chunsoft.ttgo.cart;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.util.SparseArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//import com.android.volley.Response;
//import com.chunosft.utils.ToastUtil;
//import com.chunsoft.net.AbstractVolleyErrorListener;
//import com.chunsoft.net.Constant;
//import com.chunsoft.net.GsonRequest;
//import com.chunsoft.ttgo.R;
//import com.chunsoft.ttgo.bean.AccountDataBean;
//import com.chunsoft.ttgo.bean.CartContentBean;
//import com.chunsoft.ttgo.bean.CartListBean;
//import com.chunsoft.ttgo.bean.FeedbackBean;
//import com.chunsoft.ttgo.bean.ProShopList;
//import com.chunsoft.ttgo.bean.UploadCartDataBean;
//import com.chunsoft.ttgo.bean.VolleyDataCallback;
//import com.chunsoft.ttgo.home.MyApplication;
//import com.chunsoft.ttgo.util.IBtnCallListener;
//import com.chunsoft.ttgo.util.PreferencesUtils;
//import com.chunsoft.view.xListview.XListView;
//import com.chunsoft.view.xListview.XListView.IXListViewListener;
//import com.google.gson.Gson;
//
//public class Cart_F extends Fragment implements IBtnCallListener,
//		IXListViewListener {
//	IBtnCallListener btnCallListener;
//	/**
//	 * widget statement
//	 */
//	@Bind(R.id.tv_cart_total)
//	TextView tv_cart_total;
//
//	@Bind(R.id.tv_cart_select_num)
//	TextView tv_cart_select_num;
//
//	@Bind(R.id.tv_top_edit)
//	TextView tv_top_edit;
//	// 移到收藏夹
//	@Bind(R.id.tv_cart_move_favorite)
//	TextView mFavorite;
//	// 删除 结算
//	@Bind(R.id.tv_cart_buy_or_del)
//	TextView mDelete;
//
//	@Bind(R.id.check_box)
//	CheckBox mCheckAll; // 全选 全不选
//
//	@Bind(R.id.cart_rl_allprie_total)
//	RelativeLayout cart_rl_allprie_total;
//
//	@Bind(R.id.content_view)
//	XListView content_view;
//
//	ProgressDialog dialog = null;
//	/**
//	 * variable statement
//	 */
//	private CartAdapter adapter;
//	private int totalPrice = 0; // 商品总价
//	/** 批量模式下，用来记录当前选中状态 */
//	private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();
//	private boolean isBatchModel;// 是否可删除模式
//	private List<CartContentBean> datas;
//	private UploadCartDataBean upLoadDatas;
//	private String userId;
//	private String token;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = LayoutInflater.from(getActivity()).inflate(R.layout.cart_f,
//				null);
//		ButterKnife.bind(this, view);
//		initData();
//		content_view.setXListViewListener(this);
//		// 设置可以进行下拉加载的功能
//		content_view.setPullLoadEnable(true);
//		content_view.setPullRefreshEnable(true);
//		getCartListData(new VolleyDataCallback<CartListBean>() {
//			@Override
//			public void onSuccess(final CartListBean datas) {
//				adapter = new CartAdapter(getActivity(), datas.content);
//				content_view.setAdapter(adapter);
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//					dialog = null;
//				}
//				content_view.setOnItemClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						CartContentBean bean = datas.content.get((int) parent
//								.getAdapter().getItemId(position));
//						ViewHolder holder = (ViewHolder) view.getTag();
//						int _id = (int) parent.getAdapter().getItemId(position);
//						boolean selected = !mSelectState.get(_id, false);
//						holder.checkBox.toggle();
//						if (selected) {
//							mSelectState.put(_id, true);
//							totalPrice += bean.number * bean.price;
//						} else {
//							mSelectState.delete(_id);
//							totalPrice -= bean.number * bean.price;
//						}
//						tv_cart_select_num.setText("已选" + mSelectState.size()
//								+ "件商品");
//						tv_cart_total.setText("￥" + totalPrice + "");
//						if (mSelectState.size() == datas.content.size()) {
//							mCheckAll.setChecked(true);
//						} else {
//							mCheckAll.setChecked(false);
//						}
//					}
//				});
//				mCheckAll.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						if (mCheckAll.isChecked()) {
//							totalPrice = 0;
//							if (datas.content != null) {
//								mSelectState.clear();
//								int size = datas.content.size();
//								if (size == 0) {
//									return;
//								}
//								for (int i = 0; i < size; i++) {
//									int _id = i;
//									mSelectState.put(_id, true);
//									totalPrice += datas.content.get(i).number
//											* datas.content.get(i).price;
//								}
//								adapter.notifyDataSetChanged();
//								tv_cart_total.setText("￥" + totalPrice + "");
//								tv_cart_select_num.setText("已选"
//										+ datas.content.size() + "件商品");
//
//							}
//						} else {
//							if (adapter != null) {
//								totalPrice = 0;
//								mSelectState.clear();
//								adapter.notifyDataSetChanged();
//								tv_cart_total.setText("￥" + 0.00 + "");
//								tv_cart_select_num.setText("已选" + 0 + "件商品");
//							}
//						}
//					}
//				});
//				mDelete.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						if (isBatchModel) {
//							List<Integer> ids = getSelectedIds();
//							for (int i = 0; i < datas.content.size(); i++) {
//								long dataId = i;
//								for (int j = 0; j < ids.size(); j++) {
//									int deleteId = ids.get(j);
//									if (dataId == deleteId) {
//										datas.content.remove(i);
//										DeleteCartPro(
//												datas.content.get(i).proId,
//												new VolleyDataCallback<FeedbackBean>() {
//													@Override
//													public void onSuccess(
//															FeedbackBean datas) {
//														ToastUtil
//																.showShortToast(
//																		getActivity(),
//																		datas.retmsg);
//														if (dialog != null
//																&& dialog
//																		.isShowing()) {
//															dialog.dismiss();
//															dialog = null;
//														}
//													}
//												});
//										i--;
//										ids.remove(j);
//										j--;
//									}
//								}
//							}
//
//							adapter.notifyDataSetChanged();
//							mSelectState.clear();
//							totalPrice = 0;
//							tv_cart_select_num.setText("已选" + 0 + "件商品");
//							tv_cart_total.setText("￥" + 0.00 + "");
//							mCheckAll.setChecked(false);
//						} else {
//							// upLoadDatas = "";
//							Gson gson = new Gson();
//							String sendData = gson.toJson(upLoadDatas);
//							AccountPro(sendData,
//									new VolleyDataCallback<AccountDataBean>() {
//
//										@Override
//										public void onSuccess(
//												AccountDataBean datas) {
//
//										}
//
//									});
//						}
//
//					}
//				});
//			}
//		});
//		tv_top_edit.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				isBatchModel = !isBatchModel;
//				if (isBatchModel) {
//					tv_top_edit.setText("完成");
//					mDelete.setText("删除");
//					cart_rl_allprie_total.setVisibility(View.GONE);
//					mFavorite.setVisibility(View.VISIBLE);
//
//				} else {
//					tv_top_edit.setText("编辑");
//					mFavorite.setVisibility(View.GONE);
//					cart_rl_allprie_total.setVisibility(View.VISIBLE);
//					mDelete.setText("结算");
//				}
//			}
//		});
//		return view;
//	}
//
//	@Override
//	public void transferMsg() {
//		// 这里响应在FragmentActivity中的控件交互
//	}
//
//	private final List<Integer> getSelectedIds() {
//		ArrayList<Integer> selectedIds = new ArrayList<Integer>();
//		for (int index = 0; index < mSelectState.size(); index++) {
//			if (mSelectState.valueAt(index)) {
//				selectedIds.add(mSelectState.keyAt(index));
//			}
//		}
//		return selectedIds;
//	}
//
//	private void initData() {
//		userId = PreferencesUtils.getSharePreStr(getActivity(), "userId");
//		token = PreferencesUtils.getSharePreStr(getActivity(), "Token");
//		upLoadDatas = new UploadCartDataBean();
//		upLoadDatas.userId = userId;
//		upLoadDatas.token = token;
//		upLoadDatas.proShopList = new ArrayList<ProShopList>();
//	}
//
//	// 适配器
//	public class CartAdapter extends BaseAdapter {
//		private List<CartContentBean> data_adapter;
//		private Context mContext;
//
//		public CartAdapter(Context mContext, List<CartContentBean> datas) {
//			this.mContext = mContext;
//			this.data_adapter = datas;
//		}
//
//		@Override
//		public int getCount() {
//			return data_adapter.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return data_adapter.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(final int position, View convertView,
//				ViewGroup parent) {
//			ViewHolder holder = null;
//			View view = convertView;
//			if (view == null) {
//				view = LayoutInflater.from(getActivity()).inflate(
//						R.layout.cart_item, null);
//				holder = new ViewHolder(view);
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//			bindListItem(holder, data_adapter.get(position), position);
//
//			holder.tv_add.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					int _id = position;
//					boolean selected = mSelectState.get(_id, false);
//
//					data_adapter.get(position).number = data_adapter
//							.get(position).number + 1;
//
//					notifyDataSetChanged();
//
//					if (selected) {
//						totalPrice += data_adapter.get(position).price;
//						tv_cart_total.setText("￥" + totalPrice + "");
//					}
//				}
//			});
//
//			holder.tv_reduce.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (data_adapter.get(position).number == 1)
//						return;
//
//					int _id = position;
//
//					boolean selected = mSelectState.get(_id, false);
//					data_adapter.get(position).number = data_adapter
//							.get(position).number - 1;
//					notifyDataSetChanged();
//
//					if (selected) {
//						totalPrice -= data_adapter.get(position).price;
//						tv_cart_total.setText("￥" + totalPrice + "");
//					}
//
//				}
//			});
//			return view;
//		}
//	}
//
//	private void bindListItem(ViewHolder holder, CartContentBean data,
//			int position) {
//		holder.tv_intro.setText(data.proName);
//		holder.tv_price.setText("￥" + data.price);
//		holder.tv_num.setText(data.number + "");
//		holder.tv_kind.setText(data.color + data.size);
//		int _id = position;
//		boolean selected = mSelectState.get(_id, false);
//		holder.checkBox.setChecked(selected);
//	}
//
//	class ViewHolder {
//		CheckBox checkBox;
//		ImageView iv_pic;
//		TextView tv_intro;
//		TextView tv_price;
//		TextView tv_reduce;
//		TextView tv_num;
//		TextView tv_add;
//		TextView tv_kind;
//		RadioGroup radioGroup1;
//
//		public ViewHolder(View view) {
//			checkBox = (CheckBox) view.findViewById(R.id.check_box);
//			tv_intro = (TextView) view.findViewById(R.id.tv_intro);
//			iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
//			tv_price = (TextView) view.findViewById(R.id.tv_price);
//			tv_reduce = (TextView) view.findViewById(R.id.tv_reduce);
//			tv_num = (TextView) view.findViewById(R.id.tv_num);
//			tv_add = (TextView) view.findViewById(R.id.tv_add);
//			tv_kind = (TextView) view.findViewById(R.id.tv_kind);
//			radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
//		}
//	}
//
//	private void getCartListData(final VolleyDataCallback<CartListBean> callback) {
//		String URL = Constant.IP + Constant.getCartList;
//		if (dialog == null) {
//			dialog = ProgressDialog.show(getActivity(), "", "正在加载...");
//			dialog.show();
//		}
//
//		JSONObject sendData = new JSONObject();
//		try {
//			sendData.put("userId", userId);
//			sendData.put("token", token);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		GsonRequest<CartListBean> request = new GsonRequest<CartListBean>(URL,
//				sendData.toString(), new Response.Listener<CartListBean>() {
//
//					@Override
//					public void onResponse(CartListBean arg0) {
//						callback.onSuccess(arg0);
//					}
//				}, new AbstractVolleyErrorListener(getActivity()) {
//
//					@Override
//					public void onError() {
//						if (dialog != null && dialog.isShowing()) {
//							dialog.dismiss();
//							dialog = null;
//						}
//					}
//				}, CartListBean.class);
//		MyApplication.getInstance().addToRequestQueue(request, "getCartList");
//	}
//
//	@Override
//	public void onRefresh() {
//		onLoad();
//	}
//
//	@Override
//	public void onLoadMore() {
//		onLoad();
//	}
//
//	/** 简单的时间格式 */
//	public static SimpleDateFormat mDateFormat = new SimpleDateFormat(
//			"MM-dd HH:mm");
//
//	public static String getCurrentTime(long time) {
//		if (0 == time) {
//			return "";
//		}
//		return mDateFormat.format(new Date(time));
//	}
//
//	/** 停止加载和刷新 */
//	private void onLoad() {
//		content_view.stopRefresh();
//		// 停止加载更多
//		content_view.stopLoadMore();
//		// 设置最后一次刷新时间
//		content_view.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
//	}
//
//	private void AccountPro(String sendData,
//			final VolleyDataCallback<AccountDataBean> callback) {
//		String URL = Constant.IP + Constant.getAccountPro;
//		GsonRequest<AccountDataBean> request = new GsonRequest<AccountDataBean>(
//				URL, sendData, new Response.Listener<AccountDataBean>() {
//
//					@Override
//					public void onResponse(AccountDataBean arg0) {
//						callback.onSuccess(arg0);
//					}
//				}, new AbstractVolleyErrorListener(getActivity()) {
//
//					@Override
//					public void onError() {
//						if (dialog != null && dialog.isShowing()) {
//							dialog.dismiss();
//							dialog = null;
//						}
//					}
//				}, AccountDataBean.class);
//		MyApplication.getInstance().addToRequestQueue(request, "AccountPro");
//	}
//
//	private void DeleteCartPro(String proId,
//			final VolleyDataCallback<FeedbackBean> callback) {
//		JSONObject sendData;
//		sendData = new JSONObject();
//		try {
//			sendData.put("userId", userId);
//			sendData.put("token", token);
//			sendData.put("proId", proId);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		String URL = Constant.IP + Constant.deleteCartPro;
//		Log.e("deleteSenData----->", sendData.toString());
//		GsonRequest<FeedbackBean> request = new GsonRequest<FeedbackBean>(URL,
//				sendData.toString(), new Response.Listener<FeedbackBean>() {
//
//					@Override
//					public void onResponse(FeedbackBean arg0) {
//						callback.onSuccess(arg0);
//					}
//				}, new AbstractVolleyErrorListener(getActivity()) {
//
//					@Override
//					public void onError() {
//						if (dialog == null) {
//							dialog = ProgressDialog.show(getActivity(), "",
//									"正在加载...");
//							dialog.show();
//						}
//					}
//				}, FeedbackBean.class);
//		MyApplication.getInstance().addToRequestQueue(request);
//	}
// }

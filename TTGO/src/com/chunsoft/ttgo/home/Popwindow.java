package com.chunsoft.ttgo.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.AddCartBean;
import com.chunsoft.ttgo.bean.AddCartBean1;
import com.chunsoft.ttgo.bean.FeedbackBean;
import com.chunsoft.ttgo.bean.ProDetailBean;
import com.chunsoft.ttgo.bean.PropertyBean;
import com.chunsoft.ttgo.bean.Propery;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.util.PreferencesUtils;
import com.chunsoft.view.MyListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Popwindow implements OnDismissListener, OnClickListener {
	private Context context;
	/**
	 * widget statement
	 */
	@Bind(R.id.iv_pic)
	ImageView iv_pic;

	@Bind(R.id.tv_content)
	TextView tv_content;

	@Bind(R.id.tv_money)
	TextView tv_money;

	@Bind(R.id.tv_store)
	TextView tv_store;

	@Bind(R.id.all_money)
	TextView all_money;

	@Bind(R.id.all_num)
	TextView all_num;

	@Bind(R.id.pop_ok)
	TextView pop_ok;

	@Bind(R.id.pop_del)
	ImageView pop_del;

	@Bind(R.id.mylv)
	MyListView mylv;

	/**
	 * variable statement
	 */
	/** 批量模式下，用来记录当前选中状态 */
	private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();
	private int price = 0;
	int totalPrice = 0; // 商品总价
	int totalNum = 0; // 商品总件数
	private String proID;
	private OrderAdapter adapter;
	private AddCartBean1 cartData;
	private PropertyBean proBean;
	private AddCartBean uploadData;
	private Propery bean;
	private ProDetailBean mArrayList = new ProDetailBean();
	private PopupWindow popupWindow;
	private OnItemClickListener listener;

	public Popwindow(Context context, ProDetailBean mArrayList, String proID) {
		this.context = context;
		this.price = mArrayList.proPrice;
		this.proID = proID;
		View view = LayoutInflater.from(context).inflate(R.layout.popwindow,
				null);
		ButterKnife.bind(this, view);
		this.mArrayList.proProperty = new ArrayList<PropertyBean>();
		this.mArrayList = mArrayList;
		tv_content.setText(mArrayList.proName);
		tv_money.setText("¥" + mArrayList.proPrice);
		tv_store.setText("(库存" + (mArrayList.proPrice * 2 + 7) + "件)");
		ImageLoader.getInstance().displayImage(
				Constant.ImageUri + getPicPath(mArrayList.picPath)[0], iv_pic);// 使用ImageLoader对图片进行加装！

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 设置popwindow的动画效果
		popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
		cartData = new AddCartBean1();
		cartData.proPropery = new ArrayList<PropertyBean>();
		cartData.proID = proID;
		cartData.token = PreferencesUtils.getSharePreStr(context, "Token");
		cartData.userId = PreferencesUtils.getSharePreStr(context, "userId");
		for (int i = 0; i < mArrayList.proProperty.size(); i++) {
			proBean = new PropertyBean();
			proBean.proNum = "0";
			proBean.color = mArrayList.proProperty.get(i).color;
			proBean.size = mArrayList.proProperty.get(i).size;

			proBean.id = mArrayList.proProperty.get(i).id;
			cartData.proPropery.add(i, proBean);
		}
		mylv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PropertyBean bean = cartData.proPropery.get((int) parent
						.getAdapter().getItemId(position));
				ViewHolder holder = (ViewHolder) view.getTag();
				int _id = (int) parent.getAdapter().getItemId(position);
				boolean selected = !mSelectState.get(_id, false);
				holder.cb.toggle();
				if (selected) {
					mSelectState.put(_id, true);
					totalNum += Integer.valueOf(bean.proNum);
					totalPrice += Integer.valueOf(bean.proNum) * price;

				} else {
					mSelectState.delete(_id);
					totalNum -= Integer.valueOf(bean.proNum);
					totalPrice -= Integer.valueOf(bean.proNum) * price;
				}
				all_money.setText("总金额：" + totalPrice + "元");
				all_num.setText("总计：" + totalNum + "件");
			}
		});
		Click();

		adapter = new OrderAdapter(context, cartData.proPropery);
		mylv.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.pop_del:
			listener.onClickOKPop("");
			dissmiss();
			break;
		case R.id.pop_ok:
			uploadData = new AddCartBean();
			uploadData.propery = new ArrayList<Propery>();
			uploadData.proID = cartData.proID;
			uploadData.token = cartData.token;
			uploadData.userId = cartData.userId;
			Gson gson = new Gson();
			List<Integer> ids = getSelectedIds();
			for (int i = 0; i < cartData.proPropery.size(); i++) {
				int dataId = i;
				for (int j = 0; j < ids.size(); j++) {
					int deleteId = ids.get(j);
					if ((dataId == deleteId)
							&& cartData.proPropery.get(i).proNum != "0") {
						bean = new Propery();
						bean.proNum = cartData.proPropery.get(i).proNum;
						bean.styleId = cartData.proPropery.get(i).id;
						uploadData.propery.add(bean);
					}
				}
			}
			if (uploadData.propery.size() != 0) {
				final String sendData = gson.toJson(uploadData);

				AddCartData(sendData, new VolleyDataCallback<FeedbackBean>() {
					@Override
					public void onSuccess(FeedbackBean datas) {
						ToastUtil.showShortToast(context, datas.retmsg);
						listener.onClickOKPop(sendData);
					}
				});
			} else {
				listener.onClickOKPop("");
			}
			dissmiss();
			break;
		default:
			listener.onClickOKPop("");
			dissmiss();
			break;
		}
	}

	private final List<Integer> getSelectedIds() {
		ArrayList<Integer> selectedIds = new ArrayList<Integer>();
		for (int index = 0; index < mSelectState.size(); index++) {
			if (mSelectState.valueAt(index)) {
				selectedIds.add(mSelectState.keyAt(index));
			}
		}
		return selectedIds;
	}

	@Override
	public void onDismiss() {

	}

	public interface OnItemClickListener {
		/** 设置点击确认按钮时监听接口 */
		public void onClickOKPop(String data);
	}

	/** 设置监听 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	/** 弹窗显示的位置 */
	public void showAsDropDown(View parent) {
		popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}

	/** 消除弹窗 */
	public void dissmiss() {
		popupWindow.dismiss();
	}

	public void Click() {
		pop_ok.setOnClickListener(this);
		pop_del.setOnClickListener(this);
	}

	class OrderAdapter extends BaseAdapter {
		private Context mContext;
		private List<PropertyBean> data_adapter;

		public OrderAdapter(Context mContext, List<PropertyBean> datas) {
			this.mContext = mContext;
			this.data_adapter = datas;
		}

		@Override
		public int getCount() {
			return data_adapter.size();
		}

		@Override
		public Object getItem(int position) {
			return data_adapter.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.cart_popview_item, null);
				holder = new ViewHolder(view);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			bindListItem(holder, data_adapter.get(position), position);

			holder.pop_add.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int _id = position;

					boolean selected = mSelectState.get(_id, false);

					cartData.proPropery.get(position).proNum = String
							.valueOf(Integer.valueOf(cartData.proPropery
									.get(position).proNum) + 1);

					notifyDataSetChanged();

					if (selected) {
						totalPrice += price;
						totalNum += 1;
						all_money.setText("总金额：" + totalPrice + "元");
						all_num.setText("总计：" + totalNum + "件");
					}
				}
			});

			holder.pop_reduce.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (cartData.proPropery.get(position).proNum == "0")
						return;

					int _id = position;

					boolean selected = mSelectState.get(_id, false);
					cartData.proPropery.get(position).proNum = String
							.valueOf(Integer.valueOf(cartData.proPropery
									.get(position).proNum) - 1);
					notifyDataSetChanged();

					if (selected) {
						totalPrice -= price;
						totalNum -= 1;
						all_money.setText("总金额：" + totalPrice + "元");
						all_num.setText("总计：" + totalNum + "件");
					}

				}
			});
			return view;
		}
	}

	private void AddCartData(String sendData,
			final VolleyDataCallback<FeedbackBean> callback) {
		String URL = Constant.IP + Constant.addCartPro;
		GsonRequest<FeedbackBean> req = new GsonRequest<FeedbackBean>(URL,
				sendData, new Response.Listener<FeedbackBean>() {

					@Override
					public void onResponse(FeedbackBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(context) {
					@Override
					public void onError() {

					}
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}

	private void bindListItem(ViewHolder holder, PropertyBean data, int position) {
		holder.tv_size.setText(data.color + data.size);
		holder.pop_num.setText(data.proNum + "");
		holder.tv_money.setText("¥" + price * Integer.valueOf(data.proNum)
				+ "元");
		int _id = position;
		boolean selected = mSelectState.get(_id, false);
		holder.cb.setChecked(selected);
	}

	static class ViewHolder {
		@Bind(R.id.tv_size)
		TextView tv_size;

		@Bind(R.id.cb)
		CheckBox cb;

		@Bind(R.id.pop_reduce)
		TextView pop_reduce;

		@Bind(R.id.pop_num)
		TextView pop_num;

		@Bind(R.id.pop_add)
		TextView pop_add;

		@Bind(R.id.tv_money)
		TextView tv_money;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public static int stringNumbers(String str) {
		// int counter = 1;
		// if (str.indexOf(",") == -1) {
		// return 1;
		// } else if (str.indexOf(",") != -1) {
		// counter++;
		// stringNumbers(str.substring(str.indexOf(",") + 1));
		// return counter;
		// }
		// return 1;
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ',') {
				count++;
			}
		}
		return count + 1;
	}

	public String[] getPicPath(String str) {
		String[] strs = new String[stringNumbers(str)];
		Log.e("[stringNumbers(str)]", stringNumbers(str) + "");
		int start = 0;
		int end = str.indexOf(",");
		if (strs.length == 1) {
			strs[0] = str;
			return strs;
		} else {
			for (int i = 0; i < strs.length; i++) {
				if (i != strs.length - 1) {
					strs[i] = str.substring(start, end);
					str = str.substring(end + 1);
					end = str.indexOf(",");
				} else {
					strs[i] = str;
				}
			}
			return strs;
		}
	}
}

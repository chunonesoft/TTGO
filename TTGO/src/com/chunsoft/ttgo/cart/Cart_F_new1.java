package com.chunsoft.ttgo.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response;
import com.chunsoft.adapter.ShopcartExpandableListViewAdapter;
import com.chunsoft.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.chunsoft.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.CartListBean;
import com.chunsoft.ttgo.bean.GroupInfo;
import com.chunsoft.ttgo.bean.ProductInfo;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.home.MyApplication;
import com.chunsoft.ttgo.util.PreferencesUtils;

public class Cart_F_new1 extends Fragment implements CheckInterface,
		ModifyCountInterface, OnClickListener {
	/**
	 * widget statement
	 */
	@Bind(R.id.tv_total_price)
	TextView tv_total_price;
	// 删除
	@Bind(R.id.tv_delete)
	TextView tv_delete;
	// 删除 结算
	@Bind(R.id.tv_go_to_pay)
	TextView tv_go_to_pay;

	@Bind(R.id.all_chekbox)
	CheckBox cb_check_all; // 全选 全不选

	@Bind(R.id.exListView)
	ExpandableListView exListView;

	ProgressDialog dialog = null;
	private String userId;
	private String token;

	private double totalPrice = 0.00;// 购买的商品总价
	private int totalCount = 0;// 购买的商品总数量
	private Context context;

	private ShopcartExpandableListViewAdapter selva;
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();// 组元素数据列表
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();// 子元素数据列表
	List<ProductInfo> products;
	ModifyCountInterface xModifyCountInterface;
	CheckInterface xCheckInterface;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.cart_f_new, null);
		ButterKnife.bind(this, view);
		initView();
		xModifyCountInterface = this;
		xCheckInterface = this;
		initEvents();
		return view;
	}

	private void initEvents() {
		cb_check_all.setOnClickListener(this);
		tv_delete.setOnClickListener(this);
		tv_go_to_pay.setOnClickListener(this);
	}

	private void initView() {
		context = getActivity();
		userId = PreferencesUtils.getSharePreStr(getActivity(), "userId");
		token = PreferencesUtils.getSharePreStr(getActivity(), "Token");

		getCartListData(new VolleyDataCallback<CartListBean>() {
			@Override
			public void onSuccess(CartListBean datas) {
				ProductInfo bean;

				for (int i = 0; i < datas.contentList.size(); i++) {
					groups.add(new GroupInfo(i + "", "TTGO店招" + (i + 1) + "号产品"));
					products = new ArrayList<ProductInfo>();
					for (int j = 0; j < datas.contentList.get(i).content.size(); j++) {
						bean = new ProductInfo();
						bean.color = datas.contentList.get(i).content.get(j).color;
						bean.size = datas.contentList.get(i).content.get(j).size;
						bean.number = datas.contentList.get(i).content.get(j).number;
						bean.proId = datas.contentList.get(i).proId;
						bean.price = datas.contentList.get(i).price;
						bean.proName = datas.contentList.get(i).proName;
						bean.proPath = datas.contentList.get(i).proPath;
						bean.styleId = datas.contentList.get(i).content.get(j).styleId;
						products.add(bean);
					}
					children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
				}
				selva = new ShopcartExpandableListViewAdapter(groups, children,
						getActivity());
				selva.setCheckInterface(xCheckInterface);// 关键步骤1,设置复选框接口
				selva.setModifyCountInterface(xModifyCountInterface);// 关键步骤2,设置数量增减接口
				exListView.setAdapter(selva);
				for (int k = 0; k < selva.getGroupCount(); k++) {
					exListView.expandGroup(k);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
				}

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}
		});
		// virtualData();
	}

	@Override
	public void onClick(View v) {
		AlertDialog alert;
		switch (v.getId()) {
		case R.id.all_chekbox:
			doCheckAll();
			break;
		case R.id.tv_go_to_pay:
			if (totalCount == 0) {
				Toast.makeText(context, "请选择要支付的商品", Toast.LENGTH_LONG).show();
				return;
			}
			alert = new AlertDialog.Builder(context).create();
			alert.setTitle("操作提示");
			alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alert.show();
			break;
		case R.id.tv_delete:
			if (totalCount == 0) {
				Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
				return;
			}
			alert = new AlertDialog.Builder(context).create();
			alert.setTitle("操作提示");
			alert.setMessage("您确定要将这些商品从购物车中移除吗？");
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							doDelete();
						}
					});
			alert.show();
			break;
		}
	}

	/**
	 * 删除操作<br>
	 * 1.不要边遍历边删除，容易出现数组越界的情况<br>
	 * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
	 */
	protected void doDelete() {
		List<GroupInfo> toBeDeleteGroups = new ArrayList<GroupInfo>();// 待删除的组元素列表
		for (int i = 0; i < groups.size(); i++) {
			GroupInfo group = groups.get(i);
			if (group.isChoosed()) {

				toBeDeleteGroups.add(group);
			}
			List<ProductInfo> toBeDeleteProducts = new ArrayList<ProductInfo>();// 待删除的子元素列表
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				if (childs.get(j).isChoosed()) {
					toBeDeleteProducts.add(childs.get(j));
				}
			}
			childs.removeAll(toBeDeleteProducts);
		}

		groups.removeAll(toBeDeleteGroups);

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void doIncrease(int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
		ProductInfo product = (ProductInfo) selva.getChild(groupPosition,
				childPosition);
		int currentCount = product.number;
		currentCount++;
		product.number = currentCount;
		((TextView) showCountView).setText(currentCount + "");

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void doDecrease(int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
		ProductInfo product = (ProductInfo) selva.getChild(groupPosition,
				childPosition);
		int currentCount = product.number;
		if (currentCount == 1)
			return;
		currentCount--;

		product.number = currentCount;
		((TextView) showCountView).setText(currentCount + "");

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void checkGroup(int groupPosition, boolean isChecked) {
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		for (int i = 0; i < childs.size(); i++) {
			childs.get(i).setChoosed(isChecked);
		}
		if (isAllCheck())
			cb_check_all.setChecked(true);
		else
			cb_check_all.setChecked(false);
		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void checkChild(int groupPosition, int childPosition,
			boolean isChecked) {
		boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		for (int i = 0; i < childs.size(); i++) {
			if (childs.get(i).isChoosed() != isChecked) {
				allChildSameState = false;
				break;
			}
		}
		if (allChildSameState) {
			group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
		} else {
			group.setChoosed(false);// 否则，组元素一律设置为未选中状态
		}

		if (isAllCheck())
			cb_check_all.setChecked(true);
		else
			cb_check_all.setChecked(false);
		selva.notifyDataSetChanged();
		calculate();
	}

	private boolean isAllCheck() {

		for (GroupInfo group : groups) {
			if (!group.isChoosed())
				return false;

		}

		return true;
	}

	/**
	 * 模拟数据<br>
	 * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
	 * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
	 */
	// private void virtualData() {
	//
	// for (int i = 0; i < 6; i++) {
	//
	// groups.add(new GroupInfo(i + "", "第八号当铺" + (i + 1) + "号店"));
	//
	// List<ProductInfo> products = new ArrayList<ProductInfo>();
	// for (int j = 0; j <= i; j++) {
	//
	// products.add(new ProductInfo(j + "", "商品", "", groups.get(i)
	// .getName() + "的第" + (j + 1) + "个商品", 120.00 + i * j, 1));
	// }
	// children.put(groups.get(i).getId(), products);//
	// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
	// }
	// }

	/**
	 * 统计操作<br>
	 * 1.先清空全局计数器<br>
	 * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
	 * 3.给底部的textView进行数据填充
	 */
	private void calculate() {
		totalCount = 0;
		totalPrice = 0.00;
		for (int i = 0; i < groups.size(); i++) {
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				ProductInfo product = childs.get(j);
				if (product.isChoosed()) {
					totalCount++;
					totalPrice += product.price * product.number;
				}
			}
		}
		tv_total_price.setText("￥" + totalPrice);
		tv_go_to_pay.setText("去支付(" + totalCount + ")");
	}

	/** 全选与反选 */
	private void doCheckAll() {
		for (int i = 0; i < groups.size(); i++) {
			groups.get(i).setChoosed(cb_check_all.isChecked());
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				childs.get(j).setChoosed(cb_check_all.isChecked());
			}
		}
		selva.notifyDataSetChanged();
	}

	private void getCartListData(final VolleyDataCallback<CartListBean> callback) {

		String URL = Constant.IP + Constant.getCartList;
		if (dialog == null) {
			dialog = ProgressDialog.show(getActivity(), "", "正在加载...");
			dialog.show();
		}

		JSONObject sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("token", token);
			Log.e("senddata------->", sendData.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<CartListBean> request = new GsonRequest<CartListBean>(URL,
				sendData.toString(), new Response.Listener<CartListBean>() {

					@Override
					public void onResponse(CartListBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(getActivity()) {

					@Override
					public void onError() {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				}, CartListBean.class);
		MyApplication.getInstance().addToRequestQueue(request, "getCartList");
	}

}

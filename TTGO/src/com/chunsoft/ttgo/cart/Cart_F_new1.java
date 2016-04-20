package com.chunsoft.ttgo.cart;

import java.io.Serializable;
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
import android.content.Intent;
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
import com.chunosft.utils.ToastUtil;
import com.chunsoft.adapter.ShopcartExpandableListViewAdapter;
import com.chunsoft.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.chunsoft.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.CartListBean;
import com.chunsoft.ttgo.bean.FeedbackBean;
import com.chunsoft.ttgo.bean.GroupInfo;
import com.chunsoft.ttgo.bean.ProKindList;
import com.chunsoft.ttgo.bean.ProShopList;
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
	// ɾ��
	@Bind(R.id.tv_delete)
	TextView tv_delete;
	// ����
	@Bind(R.id.tv_go_to_pay)
	TextView tv_go_to_pay;

	@Bind(R.id.all_chekbox)
	CheckBox cb_check_all; // ȫѡ ȫ��ѡ

	@Bind(R.id.exListView)
	ExpandableListView exListView;

	ProgressDialog dialog = null;
	private String userId;
	private String token;
	private JSONObject sendData;
	private String URL;

	private double totalPrice = 0.00;// �������Ʒ�ܼ�
	private int totalCount = 0;// �������Ʒ������
	private Context context;

	private ShopcartExpandableListViewAdapter selva;
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();// ��Ԫ�������б�
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();// ��Ԫ�������б�
	List<ProductInfo> products;
	ModifyCountInterface xModifyCountInterface;
	CheckInterface xCheckInterface;

	List<ProShopList> proShopList;
	ProShopList pShopListBean;
	List<ProKindList> proKindList;
	ProKindList prokindBean;

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
					groups.add(new GroupInfo(i + "", "TTGO����" + (i + 1) + "�Ų�Ʒ"));
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
					children.put(groups.get(i).getId(), products);// ����Ԫ�ص�һ��Ψһֵ������ȡId����Ϊ��Ԫ��List��Key
				}
				selva = new ShopcartExpandableListViewAdapter(groups, children,
						getActivity());
				selva.setCheckInterface(xCheckInterface);// �ؼ�����1,���ø�ѡ��ӿ�
				selva.setModifyCountInterface(xModifyCountInterface);// �ؼ�����2,�������������ӿ�
				exListView.setAdapter(selva);
				for (int k = 0; k < selva.getGroupCount(); k++) {
					exListView.expandGroup(k);// �ؼ�����3,��ʼ��ʱ����ExpandableListView��չ���ķ�ʽ����
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
				Toast.makeText(context, "��ѡ��Ҫ֧������Ʒ", Toast.LENGTH_LONG).show();
				return;
			}
			alert = new AlertDialog.Builder(context).create();
			alert.setTitle("������ʾ");
			alert.setMessage("�ܼ�:\n" + totalCount + "����Ʒ\n" + totalPrice + "Ԫ");
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							return;
						}
					});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UpLoadData();
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									Submit_Order_FA.class);
							intent.putExtra("upData",
									(Serializable) proShopList);
							doDelete();
							startActivity(intent);
							return;
						}
					});
			alert.show();
			break;
		case R.id.tv_delete:
			if (totalCount == 0) {
				Toast.makeText(context, "��ѡ��Ҫ�Ƴ�����Ʒ", Toast.LENGTH_LONG).show();
				return;
			}
			alert = new AlertDialog.Builder(context).create();
			alert.setTitle("������ʾ");
			alert.setMessage("��ȷ��Ҫ����Щ��Ʒ�ӹ��ﳵ���Ƴ���");
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
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
	 * ɾ������<br>
	 * 1.��Ҫ�߱�����ɾ�������׳�������Խ������<br>
	 * 2.�ֽ�Ҫɾ���Ķ���Ž���Ӧ���б������У������������removeAll�ķ�ʽ����ɾ��
	 */
	protected void doDelete() {
		List<GroupInfo> toBeDeleteGroups = new ArrayList<GroupInfo>();// ��ɾ������Ԫ���б�
		for (int i = 0; i < groups.size(); i++) {
			GroupInfo group = groups.get(i);
			if (group.isChoosed()) {
				toBeDeleteGroups.add(group);
			}
			List<ProductInfo> toBeDeleteProducts = new ArrayList<ProductInfo>();// ��ɾ������Ԫ���б�
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				if (childs.get(j).isChoosed()) {
					toBeDeleteProducts.add(childs.get(j));
					DeleteCartById(childs.get(j).proId + "",
							childs.get(j).styleId + "",
							new VolleyDataCallback<FeedbackBean>() {
								@Override
								public void onSuccess(FeedbackBean datas) {
									if (datas.retcode.equals(0)) {
										ToastUtil.showShortToast(getActivity(),
												datas.retmsg);
									}
									if (dialog != null && dialog.isShowing()) {
										dialog.dismiss();
										dialog = null;
									}
								}
							});
				}
			}
			childs.removeAll(toBeDeleteProducts);
		}

		groups.removeAll(toBeDeleteGroups);

		selva.notifyDataSetChanged();
		calculate();
	}

	private void UpLoadData() {
		proShopList = new ArrayList<ProShopList>();// ��ɾ������Ԫ���б�
		for (int i = 0; i < groups.size(); i++) {
			GroupInfo group = groups.get(i);
			// if (group.isChoosed()) {
			//
			// toBeDeleteGroups.add(group);
			// }
			pShopListBean = new ProShopList();
			proKindList = new ArrayList<ProKindList>();
			// proShopList.get(i).proKindList = new ArrayList<ProKindList>();
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				if (childs.get(j).isChoosed()) {
					prokindBean = new ProKindList();
					pShopListBean.cash = "1";
					pShopListBean.proID = childs.get(j).proId + "";
					Log.e("pShopListBean.proID" + j, pShopListBean.proID);
					prokindBean.proNum = childs.get(j).number + "";
					prokindBean.styleId = childs.get(j).styleId + "";
					proKindList.add(prokindBean);
				}
			}
			if (proKindList.size() != 0) {
				pShopListBean.proKindList = proKindList;
				proShopList.add(pShopListBean);
			}
		}
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
		boolean allChildSameState = true;// �жϸ��������������Ԫ���Ƿ���ͬһ��״̬
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		for (int i = 0; i < childs.size(); i++) {
			if (childs.get(i).isChoosed() != isChecked) {
				allChildSameState = false;
				break;
			}
		}
		if (allChildSameState) {
			group.setChoosed(isChecked);// ���������Ԫ��״̬��ͬ����ô��Ӧ����Ԫ�ر���Ϊ����ͳһ״̬
		} else {
			group.setChoosed(false);// ������Ԫ��һ������Ϊδѡ��״̬
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
	 * ģ������<br>
	 * ��ѭ�������������б����ԭ����Ԫ�ر�����һ��List�У���Ӧ����Ԫ����Ͻ����Ԫ�ر�����Map�У�<br>
	 * �������Ԫ�ص�Id(ͨ����һ��Ψһָ����Ԫ����ݵ�ֵ)
	 */
	// private void virtualData() {
	//
	// for (int i = 0; i < 6; i++) {
	//
	// groups.add(new GroupInfo(i + "", "�ڰ˺ŵ���" + (i + 1) + "�ŵ�"));
	//
	// List<ProductInfo> products = new ArrayList<ProductInfo>();
	// for (int j = 0; j <= i; j++) {
	//
	// products.add(new ProductInfo(j + "", "��Ʒ", "", groups.get(i)
	// .getName() + "�ĵ�" + (j + 1) + "����Ʒ", 120.00 + i * j, 1));
	// }
	// children.put(groups.get(i).getId(), products);//
	// ����Ԫ�ص�һ��Ψһֵ������ȡId����Ϊ��Ԫ��List��Key
	// }
	// }

	/**
	 * ͳ�Ʋ���<br>
	 * 1.�����ȫ�ּ�����<br>
	 * 2.����������Ԫ�أ�ֻҪ�Ǳ�ѡ��״̬�ģ��ͽ�����صļ������<br>
	 * 3.���ײ���textView�����������
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
		tv_total_price.setText("��" + totalPrice);
		tv_go_to_pay.setText("ȥ֧��(" + totalCount + ")");
	}

	/** ȫѡ�뷴ѡ */
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

		URL = Constant.IP + Constant.getCartList;
		if (dialog == null) {
			dialog = ProgressDialog.show(getActivity(), "", "���ڼ���...");
			dialog.show();
		}

		sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("token", token);
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

	private void DeleteCartById(String proId, String styleId,
			final VolleyDataCallback<FeedbackBean> callback) {
		URL = Constant.IP + Constant.deleteCartPro;
		if (dialog == null) {
			dialog = ProgressDialog.show(getActivity(), "", "���ڼ���...");
			dialog.show();
		}
		sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("token", token);
			sendData.put("proId", proId);
			sendData.put("styleId", styleId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<FeedbackBean> request = new GsonRequest<FeedbackBean>(URL,
				sendData.toString(), new Response.Listener<FeedbackBean>() {

					@Override
					public void onResponse(FeedbackBean arg0) {
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
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}
}

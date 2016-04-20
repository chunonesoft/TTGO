package com.chunsoft.ttgo.cart;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.chunsoft.ttgo.bean.FeedbackBean;
import com.chunsoft.ttgo.bean.ProKindList;
import com.chunsoft.ttgo.bean.ProShopList;
import com.chunsoft.ttgo.bean.PuDetailBean;
import com.chunsoft.ttgo.bean.UploadCartDataBean;
import com.chunsoft.ttgo.bean.UserBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.home.MyApplication;
import com.chunsoft.ttgo.myself.Add_Adress;
import com.chunsoft.ttgo.util.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Submit_Order_FA extends FragmentActivity implements
		OnClickListener {
	@Bind(R.id.ll_modify)
	LinearLayout ll_modify;

	@Bind(R.id.tv_top_txtTitle)
	TextView tv_top_txtTitle;

	@Bind(R.id.tv_order_name)
	TextView tv_order_name;

	@Bind(R.id.tv_order_phone)
	TextView tv_order_phone;

	@Bind(R.id.tv_order_address)
	TextView tv_order_address;

	@Bind(R.id.all_order_num)
	TextView all_order_num;

	@Bind(R.id.all_order_money)
	TextView all_order_money;
	@Bind(R.id.all_ph_num)
	TextView all_ph_num;
	@Bind(R.id.all_ph_money)
	TextView all_ph_money;

	@Bind(R.id.btn_submit)
	Button btn_submit;

	ProgressDialog dialog = null;
	private String userId;
	private String token;
	private String URL;
	private JSONObject sendData;
	List<ProShopList> proShopList;
	private UploadCartDataBean uploadData;
	private String SData = "";
	private AddCartBean SDataBean;
	private ProShopList proshopBean = new ProShopList();
	private int type = -1;

	private String name;
	private String phone;
	private String adress;
	private String province;

	private int OrderNum = 0;
	private int OrderMoney = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.firm_order);
		ButterKnife.bind(this);
		Intent intent = getIntent();
		SData = intent.getStringExtra("sendData") + "";
		if (!SData.equals("null")) {
			type = 0;
			OrderNum = 0;
			OrderMoney = 0;
			Gson gson = new Gson();
			SDataBean = gson.fromJson(SData, new TypeToken<AddCartBean>() {
			}.getType());
			proshopBean.proID = SDataBean.proID;
			proshopBean.cash = "1";
			proshopBean.proKindList = new ArrayList<ProKindList>();
			for (int i = 0; i < SDataBean.propery.size(); i++) {
				ProKindList bean = new ProKindList();
				bean.proNum = SDataBean.propery.get(i).proNum;
				OrderNum += Integer.valueOf(bean.proNum);
				bean.styleId = SDataBean.propery.get(i).styleId;
				proshopBean.proKindList.add(bean);
			}
		} else {
			type = 1;
			OrderNum = 0;
			OrderMoney = 0;
			proShopList = (List<ProShopList>) intent
					.getSerializableExtra("upData");
			for (int i = 0; i < proShopList.size(); i++) {
				for (int j = 0; j < proShopList.get(i).proKindList.size(); j++) {
					OrderNum += Integer.valueOf(proShopList.get(i).proKindList
							.get(j).proNum);
				}
			}
		}
		all_order_num.setText(OrderNum + "件");
		init();
	}

	private void init() {
		tv_top_txtTitle.setText(getResources().getText(R.string.firm_order));
		userId = PreferencesUtils
				.getSharePreStr(Submit_Order_FA.this, "userId");
		token = PreferencesUtils.getSharePreStr(Submit_Order_FA.this, "Token");
		getInitData(new VolleyDataCallback<UserBean>() {
			@Override
			public void onSuccess(UserBean datas) {
				tv_order_name.setText("收件人：" + datas.userName);
				tv_order_phone.setText(datas.mobile);
				tv_order_address.setText("收货地址：" + datas.address);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}
		});
		getPuDetail(new VolleyDataCallback<PuDetailBean>() {
			@Override
			public void onSuccess(PuDetailBean datas) {
				all_ph_num.setText(datas.disNum + "件");
				all_ph_money.setText("¥" + datas.disPay + "元");
			}
		});
		btn_submit.setOnClickListener(this);
		ll_modify.setOnClickListener(this);
	}

	private void getInitData(final VolleyDataCallback<UserBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(Submit_Order_FA.this, "", "正在加载...");
			dialog.show();
		}
		URL = Constant.IP + Constant.getUserMessage;
		sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("token", token);
			Log.e("senData--->", sendData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GsonRequest<UserBean> request = new GsonRequest<UserBean>(URL,
				sendData.toString(), new Response.Listener<UserBean>() {

					@Override
					public void onResponse(UserBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(Submit_Order_FA.this) {

					@Override
					public void onError() {

					}
				}, UserBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	private void UploadData(final VolleyDataCallback<FeedbackBean> callback) {
		URL = Constant.IP + Constant.Submitpro;
		uploadData = new UploadCartDataBean();

		uploadData.receiveAddress = adress;
		uploadData.receiveMobile = phone;
		uploadData.receiveName = name;
		uploadData.receiveProvince = province;
		uploadData.userId = userId;
		uploadData.token = token;
		if (type == 0) {
			uploadData.proShopList = new ArrayList<ProShopList>();
			uploadData.proShopList.add(proshopBean);
		} else {
			uploadData.proShopList = proShopList;
		}

		Gson gson = new Gson();
		String sendData = gson.toJson(uploadData);
		Log.e("sendData--->>", sendData);
		GsonRequest<FeedbackBean> request = new GsonRequest<FeedbackBean>(URL,
				sendData, new Response.Listener<FeedbackBean>() {

					@Override
					public void onResponse(FeedbackBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(Submit_Order_FA.this) {

					@Override
					public void onError() {

					}
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			String names = tv_order_name.getText().toString();
			name = names.substring(names.indexOf(":") + 1);
			phone = tv_order_phone.getText().toString();
			String adresses = tv_order_address.getText().toString();
			adress = adresses.substring(adresses.indexOf(":") + 1);
			province = adresses.substring(0, adress.indexOf("省"));
			UploadData(new VolleyDataCallback<FeedbackBean>() {

				@Override
				public void onSuccess(FeedbackBean datas) {

					ToastUtil.showShortToast(Submit_Order_FA.this, "订单提交成功");
				}
			});
			finish();
			break;
		case R.id.ll_modify:
			Intent intent = new Intent(Submit_Order_FA.this, Add_Adress.class);
			startActivityForResult(intent, 0);
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == 0) {
			if (resultCode == 0) {
				Bundle data = intent.getExtras();
				tv_order_name.setText("收件人：" + data.getString("receiveName"));
				tv_order_phone.setText(data.getString("receiveMobile"));
				tv_order_address.setText("收货地址："
						+ data.getString("receiveAddress"));
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	private void getPuDetail(final VolleyDataCallback<PuDetailBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(Submit_Order_FA.this, "", "正在加载...");
			dialog.show();
		}
		URL = Constant.IP + Constant.getPuDetail;
		sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("token", token);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GsonRequest<PuDetailBean> request = new GsonRequest<PuDetailBean>(URL,
				sendData.toString(), new Response.Listener<PuDetailBean>() {

					@Override
					public void onResponse(PuDetailBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(Submit_Order_FA.this) {

					@Override
					public void onError() {

					}
				}, PuDetailBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}
}

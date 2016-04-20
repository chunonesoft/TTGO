package com.chunsoft.ttgo.myself;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.chunsoft.ttgo.bean.FeedbackBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.cart.Submit_Order_FA;
import com.chunsoft.ttgo.home.MyApplication;
import com.chunsoft.ttgo.util.PreferencesUtils;

public class Add_Adress extends BaseActivity implements OnClickListener,
		OnWheelChangedListener {
	@Bind(R.id.tv_adress)
	TextView tv_adress;

	@Bind(R.id.tv_title)
	TextView tv_title;

	@Bind(R.id.btn_save)
	Button btn_save;

	@Bind(R.id.ll_choose_adress)
	LinearLayout ll_choose_adress;

	@Bind(R.id.et_name)
	EditText et_name;

	@Bind(R.id.et_phone)
	EditText et_phone;

	@Bind(R.id.et_detailadress)
	EditText et_detailadress;

	@Bind(R.id.et_postalcode)
	EditText et_postalcode;

	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	String receiveName;
	String receiveAddress;
	String receiveMobile;
	String province;
	String userId;
	String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_adress);
		ButterKnife.bind(this);
		FindView();
		Click();
		SetData();
	}

	private void FindView() {
		tv_title.setText(getResources().getText(R.string.modify_adress));
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
	}

	private void SetData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				Add_Adress.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	private void Click() {
		btn_save.setOnClickListener(this);
		tv_adress.setOnClickListener(this);
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
			tv_adress.setText(mCurrentProviceName + mCurrentCityName
					+ mCurrentDistrictName + "");
		} else if (wheel == mViewCity) {
			updateAreas();
			tv_adress.setText(mCurrentProviceName + mCurrentCityName
					+ mCurrentDistrictName + "");
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			tv_adress.setText(mCurrentProviceName + mCurrentCityName
					+ mCurrentDistrictName + "");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_adress:
			ll_choose_adress.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_save:
			initData();
			ModifyInfo(new VolleyDataCallback<FeedbackBean>() {
				@Override
				public void onSuccess(FeedbackBean datas) {
					ToastUtil.showShortToast(Add_Adress.this, datas.retmsg);
				}
			});
			Intent intent = new Intent(Add_Adress.this, Submit_Order_FA.class);
			Bundle backData = new Bundle();
			backData.putString("receiveName", receiveName);
			backData.putString("receiveMobile", receiveMobile);
			backData.putString("receiveAddress", receiveAddress);
			intent.putExtras(backData);
			setResult(0, intent);
			finish();
			break;
		default:
			break;
		}
	}

	private void initData() {
		userId = PreferencesUtils.getSharePreStr(Add_Adress.this, "userId");
		token = PreferencesUtils.getSharePreStr(Add_Adress.this, "Token");
		receiveName = et_name.getText().toString();
		receiveMobile = et_phone.getText().toString();
		receiveAddress = tv_adress.getText().toString()
				+ et_detailadress.getText().toString();
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	private void ModifyInfo(final VolleyDataCallback<FeedbackBean> callback) {
		JSONObject sendData;
		sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("token", token);
			sendData.put("receiveMan", receiveName);
			sendData.put("receiveAddress", receiveAddress);
			sendData.put("realName", receiveName);
			sendData.put("receiveMobile", receiveMobile);
			String province = receiveAddress.substring(0,
					receiveAddress.indexOf("省"));
			sendData.put("province", province);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String URL = Constant.IP + Constant.ModifyReceiveInfo;
		GsonRequest<FeedbackBean> request = new GsonRequest<FeedbackBean>(URL,
				sendData.toString(), new Response.Listener<FeedbackBean>() {

					@Override
					public void onResponse(FeedbackBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(Add_Adress.this) {

					@Override
					public void onError() {

					}
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}
}

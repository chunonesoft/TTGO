package com.chunsoft.ttgo.home;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.LoginBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.util.PreferencesUtils;
import com.chunsoft.view.LoadingDialog;

public class Login_A extends Activity implements OnClickListener {
	/**
	 * widget statement
	 */
	private TextView tv_register, tv_wjmm;
	private EditText et_mobile;
	private EditText et_password;
	private Button btn_login;
	private Context mContext;
	private LoadingDialog loadDialog;

	/**
	 * variable statement
	 */
	private String mobile;
	private String password;
	private String URL;
	private JSONObject sendData;
	private LoginBean returnData;
	private String NET_TAG;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		FindView();
		init();
		Click();
	}

	private void init() {
		mContext = Login_A.this;
		loadDialog = new LoadingDialog(mContext);
		loadDialog.setTitle("正在登录...");
	}

	private void FindView() {
		tv_wjmm = (TextView) findViewById(R.id.tv_wjmm);
		tv_register = (TextView) findViewById(R.id.tv_register);
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
	}

	private void Click() {
		btn_login.setOnClickListener(this);
		tv_register.setOnClickListener(this);
		tv_wjmm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			boolean flag1 = false;
			boolean flag2 = false;
			mobile = et_mobile.getText().toString();
			password = et_password.getText().toString();
			Pattern p1 = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m1 = p1.matcher(mobile);
			if (m1.matches()) {
				flag1 = true;
			} else {
				ToastUtil.showShortToast(mContext, "手机号格式有误请重新输入");
			}
			if (!password.equals("")) {
				flag2 = true;
			} else {
				ToastUtil.showShortToast(mContext, "密码不能为空");
			}
			if (flag1 && flag2) {
				loadDialog.show();
				getJSONRequest(mobile, password,
						new VolleyDataCallback<LoginBean>() {
							@Override
							public void onSuccess(LoginBean datas) {
								if (datas.retcode.equals("1")) {
									ToastUtil.showShortToast(mContext,
											datas.retmsg + datas.Token);
									PreferencesUtils.putSharePre(mContext,
											"userId", datas.userId);
									PreferencesUtils.putSharePre(mContext,
											"Token", datas.Token);
									PreferencesUtils.putSharePre(mContext,
											"phonenum", mobile);
									Constant.userId = datas.userId;
									Constant.Token = datas.Token;
									Constant.phonenum = mobile;
								} else {
									ToastUtil.showShortToast(mContext,
											datas.retmsg);
								}
								loadDialog.cancel();
								finish();
							}
						});
			}
			break;
		case R.id.tv_register:
			intent = new Intent(mContext, Register_A.class);
			startActivity(intent);
			break;
		case R.id.tv_wjmm:
			intent = new Intent(mContext, Wjmm_A.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void getJSONRequest(String mobile, String password,
			final VolleyDataCallback<LoginBean> callback) {
		URL = Constant.IP + Constant.VerifyUser;

		Log.e("URL", URL);
		sendData = new JSONObject();
		try {
			sendData.put("mobile", mobile);
			sendData.put("password", password);
			Log.e("sendData", sendData.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<LoginBean> request = new GsonRequest<LoginBean>(URL,
				sendData.toString(), new Response.Listener<LoginBean>() {

					@Override
					public void onResponse(LoginBean arg0) {
						returnData = arg0;
						callback.onSuccess(returnData);
					}
				}, new AbstractVolleyErrorListener(Login_A.this) {

					@Override
					public void onError() {
						loadDialog.cancel();
						Log.e(NET_TAG, "----onError");
					}
				}, LoginBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}
}

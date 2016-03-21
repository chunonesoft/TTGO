package com.chunsoft.ttgo.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.LoginBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;

public class Login_A extends Activity implements OnClickListener {
	/**
	 * widget statement
	 */
	private EditText et_mobile;
	private EditText et_password;
	private Button btn_login;
	private Context mContext;

	/**
	 * variable statement
	 */
	private String mobile;
	private String password;
	private String URL;
	private JSONObject sendData;
	private LoginBean returnData;
	private String NET_TAG;

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
	}

	private void FindView() {
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
	}

	private void Click() {
		btn_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			mobile = et_mobile.getText().toString();
			password = et_password.getText().toString();
			getJSONRequest(mobile, password,
					new VolleyDataCallback<LoginBean>() {
						@Override
						public void onSuccess(LoginBean datas) {
							if (datas.retcode.equals("1")) {
								ToastUtil.showShortToast(mContext, datas.retmsg
										+ datas.Token);
							} else {
								ToastUtil.showShortToast(mContext, "没有数据");
							}
						}
					});
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
						Log.e(NET_TAG, "----onError");
					}
				}, LoginBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}
}

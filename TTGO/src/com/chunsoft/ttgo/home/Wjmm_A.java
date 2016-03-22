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
import com.chunsoft.ttgo.bean.FeedbackBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;

public class Wjmm_A extends Activity implements OnClickListener {
	/**
	 * widget statement
	 */
	private TextView tv_title;
	private EditText et_num, et_checknum, et_pwd, et_apwd;
	private Button btn_check, btn_next;

	/**
	 * variable statement
	 */
	private String phonenum;
	private String pin;
	private String pwd;
	private String apwd;
	private Context mContext;
	private Intent intent;
	private JSONObject sendData;
	private FeedbackBean returnData = new FeedbackBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wjmm);
		FindView();
		init();
		Click();
	}

	private void init() {
		mContext = Wjmm_A.this;
		tv_title.setText("找回密码");
	}

	private void Click() {
		btn_check.setOnClickListener(this);
		btn_next.setOnClickListener(this);
	}

	private void FindView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_num = (EditText) findViewById(R.id.et_num);
		et_checknum = (EditText) findViewById(R.id.et_checknum);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_apwd = (EditText) findViewById(R.id.et_apwd);
		btn_check = (Button) findViewById(R.id.btn_check);
		btn_next = (Button) findViewById(R.id.btn_next);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_check:
			phonenum = et_num.getText().toString();
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(phonenum);
			if (m.matches()) {
				getChecknum(phonenum, new VolleyDataCallback<FeedbackBean>() {

					@Override
					public void onSuccess(FeedbackBean datas) {
						if (datas.retcode.equals("1")) {
							ToastUtil.showShortToast(mContext, "验证码已发送");
							et_checknum.setText("1507");
						} else
							ToastUtil.showShortToast(mContext,
									datas.retmsg.toString());
					}
				});
			} else {
				ToastUtil.showShortToast(mContext, "手机号格式有误请重新输入");
			}

			break;
		case R.id.btn_next:
			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			phonenum = et_num.getText().toString();
			pin = et_checknum.getText().toString();
			pwd = et_pwd.getText().toString();
			apwd = et_apwd.getText().toString();
			Pattern p1 = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m1 = p1.matcher(phonenum);
			if (m1.matches()) {
				flag1 = true;
			} else {
				ToastUtil.showShortToast(mContext, "手机号格式有误请重新输入");
			}
			if (!pin.equals("")) {
				flag2 = true;
			} else {
				ToastUtil.showShortToast(mContext, "验证码不能为空");
			}
			if (pwd.equals(apwd)) {
				flag3 = true;
			} else {
				ToastUtil.showShortToast(mContext, "两次密码不一致！");
			}
			if (flag1 && flag2 && flag3) {
				findPassword(phonenum, pin, pwd,
						new VolleyDataCallback<FeedbackBean>() {
							@Override
							public void onSuccess(FeedbackBean datas) {
								if (datas.retcode.toString().equals("1")) {
									intent = new Intent(mContext, Login_A.class);
									startActivity(intent);
								} else
									ToastUtil.showShortToast(mContext,
											datas.retmsg.toString());
							}
						});
			}
			break;

		default:
			break;
		}
	}

	private void getChecknum(String num,
			final VolleyDataCallback<FeedbackBean> callback) {
		String URL = Constant.IP + Constant.requestPIN;
		sendData = new JSONObject();
		try {
			sendData.put("phonenum", num);
			sendData.put("pinflag", "2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<FeedbackBean> request = new GsonRequest<FeedbackBean>(URL,
				sendData.toString(), new Response.Listener<FeedbackBean>() {

					@Override
					public void onResponse(FeedbackBean arg0) {
						returnData = arg0;
						callback.onSuccess(returnData);
					}
				}, new AbstractVolleyErrorListener(mContext) {

					@Override
					public void onError() {
						Log.e("Wjmm_A", "Net_error");
					}
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	private void findPassword(String num, String pin, String newpassword,
			final VolleyDataCallback<FeedbackBean> callback) {
		String URL = Constant.IP + Constant.setPassword;
		sendData = new JSONObject();
		try {
			sendData.put("mobile", num);
			sendData.put("pin", pin);
			sendData.put("newpassword", newpassword);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<FeedbackBean> request = new GsonRequest<FeedbackBean>(URL,
				sendData.toString(), new Response.Listener<FeedbackBean>() {

					@Override
					public void onResponse(FeedbackBean arg0) {
						returnData = arg0;
						callback.onSuccess(returnData);
					}
				}, new AbstractVolleyErrorListener(mContext) {

					@Override
					public void onError() {
						Log.e("Wjmm_A", "Net_error");
					}
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}
}

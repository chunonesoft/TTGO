package com.chunsoft.ttgo.myself;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.chunsoft.ttgo.home.MyApplication;
import com.chunsoft.ttgo.util.PreferencesUtils;

public class Write_Adress extends FragmentActivity {
	EditText et_num, et_company, et_kdbh;
	Button btn_submit;
	TextView tv_title;

	private String num, company, kdbh;
	private String returnNum;
	ProgressDialog dialog = null;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_adress);
		initView();
		mContext = Write_Adress.this;
		returnNum = getIntent().getStringExtra("returnNum");
		et_num.setText(returnNum);
		tv_title.setText(getResources().getText(R.string.add_kddh));
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean flag1 = false, flag2 = false, flag3 = false;
				getData();
				if (!num.equals("")) {
					flag1 = true;
				} else {
					ToastUtil.showShortToast(mContext, "退货编号不能为空");
				}
				if (!company.equals("")) {
					flag2 = true;
				} else {
					ToastUtil.showShortToast(mContext, "快递公司不能为空");
				}
				if (!kdbh.equals("")) {
					flag3 = true;
				} else {
					ToastUtil.showShortToast(mContext, "快递单号不能为空");
				}
				if (flag1 && flag2 && flag3) {
					AddAdress(new VolleyDataCallback<FeedbackBean>() {

						@Override
						public void onSuccess(FeedbackBean datas) {
							ToastUtil.showShortToast(mContext, datas.retmsg);
							finish();
						}
					});
				}
			}
		});
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_num = (EditText) findViewById(R.id.et_num);
		et_company = (EditText) findViewById(R.id.et_company);
		et_kdbh = (EditText) findViewById(R.id.et_kdbh);
		btn_submit = (Button) findViewById(R.id.btn_submit);
	}

	private void getData() {
		num = et_num.getText().toString();
		company = et_company.getText().toString();
		kdbh = et_kdbh.getText().toString();
	}

	private void AddAdress(final VolleyDataCallback<FeedbackBean> callback) {
		String URL = Constant.IP + Constant.writeAdress;
		String userId = PreferencesUtils.getSharePreStr(mContext, "userId");
		String token = PreferencesUtils.getSharePreStr(mContext, "Token");
		JSONObject sendData;
		sendData = new JSONObject();
		try {
			sendData.put("userId", userId);
			sendData.put("returnNo", num);
			sendData.put("returnExpress", company);
			sendData.put("returnExpressNO", kdbh);
			sendData.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<FeedbackBean> req = new GsonRequest<FeedbackBean>(URL,
				sendData.toString(), new Response.Listener<FeedbackBean>() {
					@Override
					public void onResponse(FeedbackBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(mContext) {
					@Override
					public void onError() {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}
}

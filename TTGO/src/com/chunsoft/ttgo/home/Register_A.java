package com.chunsoft.ttgo.home;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.chunosft.utils.ToastUtil;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.FeedbackBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.util.MyUtils;
import com.chunsoft.view.CustomDialog;
import com.chunsoft.view.RoundImageView;

public class Register_A extends Activity implements OnClickListener {
	/**
	 * widget statement
	 */
	private TextView tv_title;
	private EditText et_num, et_checknum, et_pwd, et_apwd;
	private Button btn_check, btn_next;
	private RoundImageView iv_yyzz;
	private LinearLayout ll_yyzz;
	/**
	 * variable statement
	 */
	private static final int FROMGARREY = 0; // 从图库中选择图片
	private static final int TAKE_PHOTO = 1; // 用相机拍摄照片
	public static final int CROP_PHOTO = 2;// 剪切照片
	private Uri imageUri;
	private int mainW = 50, mainH = 50;
	private String phonenum;
	private String pwd;
	private String apwd;
	private String pin;
	private Context mContext;
	private Intent intent;
	private String URL;
	private JSONObject sendData;
	private FeedbackBean returnData = new FeedbackBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		FindView();
		init();
		Click();
	}

	private void init() {
		mContext = Register_A.this;
		tv_title.setText("TTGO账户注册");
	}

	private void Click() {
		ll_yyzz.setOnClickListener(this);
		btn_check.setOnClickListener(this);
		btn_next.setOnClickListener(this);
	}

	private void FindView() {
		ll_yyzz = (LinearLayout) findViewById(R.id.ll_yyzz);
		iv_yyzz = (RoundImageView) findViewById(R.id.iv_yyzz);
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
				registerUser(phonenum, pwd, pin,
						new VolleyDataCallback<FeedbackBean>() {
							@Override
							public void onSuccess(FeedbackBean datas) {
								if (datas.retcode.equals("1")) {
									ToastUtil.showShortToast(mContext, "注册成功");
									intent = new Intent(mContext, Login_A.class);
									startActivity(intent);
								} else
									ToastUtil.showShortToast(mContext,
											datas.retmsg.toString());
							}
						});
			}
			break;
		case R.id.ll_yyzz:
			showChoosePhotoDialog();
			break;
		default:
			break;
		}
	}

	private void getChecknum(String num,
			final VolleyDataCallback<FeedbackBean> callback) {
		URL = Constant.IP + Constant.requestPIN;
		sendData = new JSONObject();
		try {
			sendData.put("mobile", num);
			sendData.put("pinflag", "1");
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
						Log.e("Register_A", "Net_error");
					}
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	private void registerUser(String mobile, String password, String pin,
			final VolleyDataCallback<FeedbackBean> callback) {
		URL = Constant.IP + Constant.registerUser;
		sendData = new JSONObject();
		try {
			sendData.put("mobile", mobile);
			sendData.put("password", password);
			sendData.put("pin", pin);
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
						Log.e("Register_A", "Net_error");
					}
				}, FeedbackBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	private void showChoosePhotoDialog() {
		CharSequence[] items = { "相册", "相机" };

		CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
		builder.setTitle("选择图片来源");
		builder.setMessage("");
		builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				File outputImage = new File(Environment
						.getExternalStorageDirectory(), "output_image.jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, FROMGARREY);
			}
		});
		builder.create().show();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case FROMGARREY:
				if (data != null) {
					imageUri = data.getData();// 图片的uri
					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setDataAndType(imageUri, "image/*");
					intent.putExtra("scale", true);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, CROP_PHOTO);
				}
				break;
			case TAKE_PHOTO:
				if (resultCode == RESULT_OK) {
					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setDataAndType(imageUri, "image/*");
					intent.putExtra("scale", true);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, CROP_PHOTO);
				}
				break;
			case CROP_PHOTO:
				if (resultCode == RESULT_OK) {
					try {
						// Bitmap bitmap =
						// BitmapFactory.decodeStream(getContentResolver()
						// .openInputStream(imageUri));
						String url = MyUtils
								.getRealFilePath(mContext, imageUri);
						BitmapFactory.Options opts = new Options();
						opts.inJustDecodeBounds = true;
						Bitmap bm = BitmapFactory.decodeFile(url, opts);
						int imageH = opts.outHeight;
						int imageW = opts.outWidth;
						int scale = 1;
						int scaleX = imageW / mainW;
						int scaleY = imageH / mainH;
						if (imageH > imageW && imageW >= 1) {
							scale = scaleY;
						}
						if (imageW > imageH && imageH >= 1) {
							scale = scaleX;
						}
						opts.inJustDecodeBounds = false;
						opts.inSampleSize = scale;
						Bitmap bitmap = BitmapFactory.decodeFile(url, opts);
						iv_yyzz.setImageBitmap(bitmap);
						// 在这里获取到图片的file

						// imageUri = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
		}
	}
}

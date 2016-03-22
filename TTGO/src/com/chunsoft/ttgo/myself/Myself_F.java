package com.chunsoft.ttgo.myself;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.OrderBean;
import com.chunsoft.ttgo.home.Login_A;
import com.chunsoft.ttgo.home.Wjmm_A;
import com.chunsoft.ttgo.util.MyUtils;
import com.chunsoft.ttgo.util.PreferencesUtils;
import com.chunsoft.view.CustomDialog;
import com.chunsoft.view.MyGridView;
import com.chunsoft.view.RoundImageView;

public class Myself_F extends Fragment implements OnClickListener,
		OnItemClickListener {
	private LinearLayout ll_myorder, ll_set, ll_person, ll_adress, ll_count;
	private Intent intent;
	private MyGridView gv_user;
	private Button btn_exit;
	GridAdapter adapter;
	private Context mContext;
	private RoundImageView iv_person;

	OrderBean bean;
	List<OrderBean> datas = new ArrayList<>();
	// 资源文件
	private static final int FROMGARREY = 0; // 从图库中选择图片
	private static final int TAKE_PHOTO = 1; // 用相机拍摄照片
	public static final int CROP_PHOTO = 2;// 剪切照片
	private Uri imageUri;
	private int mainW = 45, mainH = 45;
	private int[] pic_path = { R.drawable.user_3, R.drawable.user_4,
			R.drawable.user_5, R.drawable.user_6, R.drawable.user_7 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.myself_f, null);
		FindView(view);
		initview();
		Click();

		return view;
	}

	private void initview() {
		mContext = getActivity();
		// initData
		for (int i = 0; i < pic_path.length; i++) {
			bean = new OrderBean();
			bean.image = pic_path[i];
			datas.add(bean);
		}
		adapter = new GridAdapter(getActivity(), datas, R.layout.griditem);
		gv_user.setAdapter(adapter);
	}

	private void FindView(View view) {
		iv_person = (RoundImageView) view.findViewById(R.id.iv_person);
		btn_exit = (Button) view.findViewById(R.id.btn_exit);
		ll_myorder = (LinearLayout) view.findViewById(R.id.ll_myorder);
		ll_set = (LinearLayout) view.findViewById(R.id.ll_set);
		ll_person = (LinearLayout) view.findViewById(R.id.ll_person);
		ll_adress = (LinearLayout) view.findViewById(R.id.ll_adress);
		ll_count = (LinearLayout) view.findViewById(R.id.ll_count);
		gv_user = (MyGridView) view.findViewById(R.id.gv_user);
	}

	private void Click() {
		iv_person.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		ll_myorder.setOnClickListener(this);
		ll_set.setOnClickListener(this);
		ll_person.setOnClickListener(this);
		ll_adress.setOnClickListener(this);
		ll_count.setOnClickListener(this);
		gv_user.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_myorder:
			intent = new Intent(getActivity(), MyOrder.class);
			startActivity(intent);
			break;
		case R.id.btn_exit:
			PreferencesUtils.clearSharePre(getActivity());
			intent = new Intent(mContext, Login_A.class);
			startActivity(intent);
			break;
		case R.id.ll_adress:
			intent = new Intent(mContext, AdressList.class);
			startActivity(intent);
			break;
		case R.id.ll_count:
			intent = new Intent(mContext, Wjmm_A.class);
			startActivity(intent);
			break;
		case R.id.iv_person:
			showChoosePhotoDialog();
			break;
		default:
			break;
		}
	}

	class GridAdapter extends CommonAdapter<OrderBean> {

		public GridAdapter(Context context, List<OrderBean> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, OrderBean t) {
			holder.setImageResouce(R.id.iv_grid_pic, t.image);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
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
				if (resultCode == getActivity().RESULT_OK) {
					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setDataAndType(imageUri, "image/*");
					intent.putExtra("scale", true);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, CROP_PHOTO);
				}
				break;
			case CROP_PHOTO:
				if (resultCode == getActivity().RESULT_OK) {
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
						iv_person.setImageBitmap(bitmap);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
		case 1:
		case 2:
			intent = new Intent(getActivity(), MyOrder.class);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(getActivity(), Ph_A.class);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(getActivity(), Tk_A.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}

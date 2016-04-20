package com.chunsoft.ttgo.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chunsoft.net.Constant;
import com.chunsoft.ttgo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ��ʾ��ͼ��ʵ�֣����ҿ��ԷŴ���С
 * 
 * @author chunsoft
 * 
 */
@SuppressLint("ValidFragment")
public class PictrueFragment extends Fragment {

	private String resId;

	@SuppressLint("ValidFragment")
	public PictrueFragment(String resId) {

		this.resId = resId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.scale_pic_item, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		ImageView imageView = (ImageView) view
				.findViewById(R.id.scale_pic_item);
		ImageLoader.getInstance().displayImage(Constant.ImageUri + resId,
				imageView);// ʹ��ImageLoader��ͼƬ���м�װ��
	}

}

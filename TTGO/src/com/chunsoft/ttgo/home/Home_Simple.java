package com.chunsoft.ttgo.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ADInfo;
import com.chunsoft.ttgo.bean.ProBean;
import com.chunsoft.ttgo.bean.ProListBean;
import com.chunsoft.ttgo.bean.RecProListBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.util.IntentUti;
import com.chunsoft.view.ImageCycleView;
import com.chunsoft.view.ImageCycleView.ImageCycleViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Home_Simple extends Fragment implements OnClickListener {

	@Bind(R.id.my_gridview_kind)
	GridView my_gridview_kind;

	@Bind(R.id.my_gridview_new)
	GridView my_gridview_new;

	@Bind(R.id.ll_search)
	LinearLayout ll_search;

	@Bind(R.id.ad_view)
	ImageCycleView mAdView;

	@Bind(R.id.tv_more)
	TextView tv_more;

	@Bind(R.id.tv_kind6)
	ImageView tv_kind6;

	@Bind(R.id.tv_kind7)
	ImageView tv_kind7;

	@Bind(R.id.tv_kind8)
	ImageView tv_kind8;

	@Bind(R.id.tv_kind9)
	ImageView tv_kind9;

	@Bind(R.id.tv_kind10)
	ImageView tv_kind10;

	private Adapter_Kind kind_adapter;
	private Adapter_New pro_adapter;

	private ArrayList<ADInfo> infos;

	private int kindImage[] = { R.drawable.kind1, R.drawable.kind2,
			R.drawable.kind3, R.drawable.kind4, R.drawable.kind5 };
	private int kindString[] = { R.string.kind1, R.string.kind2,
			R.string.kind3, R.string.kind4, R.string.kind5 };
	ProgressDialog dialog = null;
	Context mContext;
	private JSONObject sendData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.home,
				null);
		ButterKnife.bind(this, view);
		initView();
		return view;
	}

	private void initView() {
		mContext = getActivity();
		Click();
		kind_adapter = new Adapter_Kind(getActivity(), kindImage, kindString);
		my_gridview_kind.setAdapter(kind_adapter);
		my_gridview_kind.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), Show_All_Pro_FA.class);
				intent.putExtra("type", position + 2);
				intent.putExtra("name",
						getResources().getText(kindString[position]));
				startActivity(intent);
			}
		});
		/**
		 * show image on home page
		 */
		getRecommendPro(new VolleyDataCallback<RecProListBean>() {
			@Override
			public void onSuccess(RecProListBean datas) {
				infos = new ArrayList<ADInfo>();
				for (int i = 0; i < datas.PIC_LIST.size(); i++) {
					ADInfo info = new ADInfo();
					info.setUrl(Constant.ImageUri
							+ datas.PIC_LIST.get(i).PRO_PIC);
					info.setId(datas.PIC_LIST.get(i).PRO_ID);
					infos.add(info);
				}
				mAdView.setImageResources(infos, mAdCycleViewListener);
			}
		});
		getData("1", new VolleyDataCallback<ProBean>() {
			@Override
			public void onSuccess(final ProBean datas) {

				pro_adapter = new Adapter_New(mContext, datas.productList);
				my_gridview_new.setAdapter(pro_adapter);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
				my_gridview_new
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Intent intent = new Intent();

								intent.putExtra("proID", datas.productList
										.get((int) parent.getAdapter()
												.getItemId(position)).proID);

								intent.setClass(mContext, ProductDetail_A.class);
								startActivity(intent);
							}
						});
			}
		});
	}

	private void Click() {
		ll_search.setOnClickListener(this);
		tv_more.setOnClickListener(this);
		tv_kind6.setOnClickListener(this);
		tv_kind7.setOnClickListener(this);
		tv_kind8.setOnClickListener(this);
		tv_kind9.setOnClickListener(this);
		tv_kind10.setOnClickListener(this);
	}

	/**
	 * get Data about pro
	 */
	private void getData(String smallTypeId,
			final VolleyDataCallback<ProBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(mContext, "", "正在加载...");
			dialog.show();
		}
		String URL = Constant.IP + Constant.getNewPro;
		sendData = new JSONObject();
		try {
			sendData.put("smallTypeId", smallTypeId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		GsonRequest<ProBean> request = new GsonRequest<>(URL,
				sendData.toString(), new Response.Listener<ProBean>() {
					@Override
					public void onResponse(ProBean arg0) {
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
				}, ProBean.class);
		MyApplication.getInstance().addToRequestQueue(request);
	}

	class Adapter_Kind extends BaseAdapter {
		private Context context;
		private int[] data;
		private int[] text;

		public Adapter_Kind(Context context, int[] data, int[] text) {

			this.context = context;
			this.data = data;
			this.text = text;
		}

		@Override
		public int getCount() {
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			return data[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View currentView, ViewGroup arg2) {
			HolderView holderView = null;
			if (currentView == null) {
				holderView = new HolderView();
				currentView = LayoutInflater.from(context).inflate(
						R.layout.grid_home_kind_item, null);
				holderView.iv_pic = (ImageView) currentView
						.findViewById(R.id.iv_adapter_grid_pic);
				holderView.tv_kind_name = (TextView) currentView
						.findViewById(R.id.tv_kind_name);
				currentView.setTag(holderView);
			} else {
				holderView = (HolderView) currentView.getTag();
			}

			holderView.iv_pic.setImageResource(data[position]);
			holderView.tv_kind_name.setText(text[position]);
			return currentView;
		}

		public class HolderView {
			public ImageView iv_pic;
			public TextView tv_kind_name;
		}
	}

	private void getRecommendPro(
			final VolleyDataCallback<RecProListBean> callback) {
		String URL = Constant.IP + Constant.getProRecommendPro;
		GsonRequest<RecProListBean> req = new GsonRequest<RecProListBean>(URL,
				"", new Response.Listener<RecProListBean>() {

					@Override
					public void onResponse(RecProListBean arg0) {
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
				}, RecProListBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			Intent intent = new Intent();
			intent.putExtra("proID", info.getId());
			Log.e("proID", info.getId());
			intent.setClass(mContext, ProductDetail_A.class);
			startActivity(intent);
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
		}
	};

	public class Adapter_New extends BaseAdapter {
		List<ProListBean> datas;
		Context mContext;

		public Adapter_New(Context mContext, List<ProListBean> datas) {
			this.mContext = mContext;
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.home_gv_item, null);
				holder = new ViewHolder(view);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			bindListItem(holder, datas.get(position));
			return view;
		}
	}

	private void bindListItem(ViewHolder holder, ProListBean data) {

		holder.tv_content.setText(data.name);
		holder.tv_price.setText("¥" + data.proPrice);
		holder.tv_sale.setText(data.saleNum + "人付款");
		holder.image.setTag(data.picPath);
		holder.image.setImageBitmap(null);
		if (data.picPath.equals(holder.image.getTag())) {
			ImageLoader.getInstance().displayImage(
					Constant.ImageUri + getPicPath(data.picPath)[0],
					holder.image);// 使用ImageLoader对图片进行加装！
		} else {
			holder.image.setImageResource(R.drawable.icon_empty);
		}

	}

	static class ViewHolder {
		@Bind(R.id.tv_content)
		TextView tv_content;
		@Bind(R.id.tv_price)
		TextView tv_price;
		@Bind(R.id.tv_sale)
		TextView tv_sale;
		@Bind(R.id.iv)
		ImageView image;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public static int stringNumbers(String str) {
		// int counter = 1;
		// if (str.indexOf(",") == -1) {
		// return 1;
		// } else if (str.indexOf(",") != -1) {
		// counter++;
		// stringNumbers(str.substring(str.indexOf(",") + 1));
		// return counter;
		// }
		// return 1;
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ',') {
				count++;
			}
		}
		return count + 1;
	}

	public String[] getPicPath(String str) {
		String[] strs = new String[stringNumbers(str)];
		Log.e("[stringNumbers(str)]", stringNumbers(str) + "");
		int start = 0;
		int end = str.indexOf(",");
		if (strs.length == 1) {
			strs[0] = str;
			return strs;
		} else {
			for (int i = 0; i < strs.length; i++) {
				if (i != strs.length - 1) {
					strs[i] = str.substring(start, end);
					str = str.substring(end + 1);
					end = str.indexOf(",");
				} else {
					strs[i] = str;
				}
			}
			return strs;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_search:
			IntentUti.IntentTo(mContext, Search_A.class);
			break;
		case R.id.tv_more:
			Intent intent = new Intent(mContext, Show_All_Pro_FA.class);
			intent.putExtra("name", getResources().getText(R.string.all_pro)
					.toString());
			intent.putExtra("type", 0);
			startActivity(intent);
			break;
		case R.id.tv_kind6:
			IntentUti.IntentToH(mContext, Show_All_Pro_FA.class, getResources()
					.getText(R.string.kind6).toString(), 38);
			break;
		case R.id.tv_kind7:
			IntentUti.IntentToH(mContext, Show_All_Pro_FA.class, getResources()
					.getText(R.string.kind7).toString(), 7);
			break;
		case R.id.tv_kind8:
			IntentUti.IntentToH(mContext, Show_All_Pro_FA.class, getResources()
					.getText(R.string.kind8).toString(), 8);
			break;
		case R.id.tv_kind9:
			IntentUti.IntentToH(mContext, Show_All_Pro_FA.class, getResources()
					.getText(R.string.kind9).toString(), 9);
			break;
		case R.id.tv_kind10:
			IntentUti.IntentToH(mContext, Show_All_Pro_FA.class, getResources()
					.getText(R.string.kind10).toString(), 10);
			break;
		default:
			break;
		}

	}
}

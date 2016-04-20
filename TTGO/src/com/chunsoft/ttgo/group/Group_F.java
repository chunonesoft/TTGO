package com.chunsoft.ttgo.group;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.chunsoft.adapter.CommonAdapter;
import com.chunsoft.adapter.ViewHolder;
import com.chunsoft.net.AbstractVolleyErrorListener;
import com.chunsoft.net.Constant;
import com.chunsoft.net.GsonRequest;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.NoticeBean;
import com.chunsoft.ttgo.bean.RowsBean;
import com.chunsoft.ttgo.bean.VolleyDataCallback;
import com.chunsoft.ttgo.home.MyApplication;
import com.chunsoft.view.xListview.XListView;
import com.chunsoft.view.xListview.XListView.IXListViewListener;

public class Group_F extends Fragment implements IXListViewListener {
	@Bind(R.id.x_lv)
	XListView x_lv;
	@Bind(R.id.tv_title)
	TextView tv_title;
	ProgressDialog dialog;

	private int rows = 9;
	private int page = 1;
	private NoticeAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.notice,
				null);
		ButterKnife.bind(this, view);
		init();
		return view;
	}

	@Override
	public void onRefresh() {
		onLoad();
	}

	@Override
	public void onLoadMore() {
		onLoad();
	}

	private void init() {
		tv_title.setText(getResources().getText(R.string.notify));
		x_lv.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		x_lv.setPullLoadEnable(true);
		x_lv.setPullRefreshEnable(true);
		GetNoticeData(rows, page, new VolleyDataCallback<NoticeBean>() {
			@Override
			public void onSuccess(NoticeBean datas) {
				page++;
				adapter = new NoticeAdapter(getActivity(), datas.rows,
						R.layout.notice_item);
				x_lv.setAdapter(adapter);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}
		});
	}

	/** 停止加载和刷新 */
	private void onLoad() {
		x_lv.stopRefresh();
		// 停止加载更多
		x_lv.stopLoadMore();
		// 设置最后一次刷新时间
		x_lv.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
	}

	/** 简单的时间格式 */
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"MM-dd HH:mm");

	public static String getCurrentTime(long time) {
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}

	private void GetNoticeData(int rows, int page,
			final VolleyDataCallback<NoticeBean> callback) {
		if (dialog == null) {
			dialog = ProgressDialog.show(getActivity(), "", "正在加载...");
			dialog.show();
		}
		String URL = Constant.IP + Constant.getNotice + "?page=" + page
				+ "&rows=" + rows;
		GsonRequest<NoticeBean> req = new GsonRequest<NoticeBean>(Method.GET,
				URL, "", new Response.Listener<NoticeBean>() {
					@Override
					public void onResponse(NoticeBean arg0) {
						callback.onSuccess(arg0);
					}
				}, new AbstractVolleyErrorListener(getActivity()) {
					@Override
					public void onError() {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
					}
				}, NoticeBean.class);
		MyApplication.getInstance().addToRequestQueue(req);
	}

	class NoticeAdapter extends CommonAdapter<RowsBean> {

		public NoticeAdapter(Context context, List<RowsBean> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, RowsBean t) {
			holder.setText(R.id.tv_name, t.title);
			holder.setText(R.id.tv_time, t.createTime);
			holder.setText(R.id.tv_content, t.content);
		}

	}
}

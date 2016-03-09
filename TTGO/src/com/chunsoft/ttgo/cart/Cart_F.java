package com.chunsoft.ttgo.cart;

import java.util.ArrayList;
import java.util.List;

import com.chunsoft.net.Data;
import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.bean.ProductBean;
import com.chunsoft.ttgo.util.IBtnCallListener;
import com.chunsoft.view.PullToRefreshListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

public class Cart_F extends Fragment implements IBtnCallListener,OnClickListener{
	IBtnCallListener btnCallListener;
	//控件声明
	private CheckBox mCheckAll; // 全选 全不选
	/** 批量模式下，用来记录当前选中状态 */
	private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();
	private TextView tv_cart_total,tv_cart_select_num,tv_top_edit;
	ListView lv;
	private PullToRefreshListView content_view;	
	private boolean isBatchModel;// 是否可删除模式
	private TextView mFavorite; // 移到收藏夹
	private TextView mDelete; // 删除 结算
	private RelativeLayout cart_rl_allprie_total;
	private CartAdapter adapter;
	private int totalPrice = 0; // 商品总价
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.cart_f, null);
		FindView(view);
		Click();
		adapter = new CartAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(adapter);
		return view;
	}
	private void doDelete(List<Integer> ids)
	{
		for (int i = 0; i < Data.arrayList_cart.size(); i++)
		{
			long dataId = Data.arrayList_cart.get(i).ProductId;
			for (int j = 0; j < ids.size(); j++)
			{
				int deleteId = ids.get(j);
				if (dataId == deleteId)
				{
					Data.arrayList_cart.remove(i);
					i--;
					ids.remove(j);
					j--;
				}
			}
		}

		refreshListView();
		mSelectState.clear();
		totalPrice = 0;
		tv_cart_select_num.setText("已选" + 0 + "件商品");
		tv_cart_total.setText("￥" + 0.00 + "");
		mCheckAll.setChecked(false);
	}
	
	private void Click()
	{
		mCheckAll.setOnClickListener(this);
		mDelete.setOnClickListener(this);
		tv_top_edit.setOnClickListener(this);
	}
	
	/**
	 * 控件实例化
	 * @param view
	 */
	private void FindView(View view)
	{
		mCheckAll = (CheckBox) view.findViewById(R.id.check_box);
		content_view = (PullToRefreshListView) view.findViewById(R.id.content_view);
		lv = content_view.getRefreshableView();
		tv_top_edit = (TextView) view.findViewById(R.id.tv_top_edit);
		tv_cart_select_num = (TextView) view.findViewById(R.id.tv_cart_select_num);
		mDelete = (TextView) view.findViewById(R.id.tv_cart_buy_or_del);
		cart_rl_allprie_total = (RelativeLayout) view.findViewById(R.id.cart_rl_allprie_total);
		mFavorite = (TextView) view.findViewById(R.id.tv_cart_move_favorite);
		tv_cart_total = (TextView) view.findViewById(R.id.tv_cart_total);
	}
	

	@Override
	public void transferMsg() {
		// 这里响应在FragmentActivity中的控件交互
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_top_edit:
			isBatchModel = !isBatchModel;
			if (isBatchModel)
			{
				tv_top_edit.setText("完成");
				mDelete.setText("删除");
				cart_rl_allprie_total.setVisibility(View.GONE);
				mFavorite.setVisibility(View.VISIBLE);

			} else
			{
				tv_top_edit.setText("编辑");
				mFavorite.setVisibility(View.GONE);
				cart_rl_allprie_total.setVisibility(View.VISIBLE);
				mDelete.setText("结算");
			}
			break;
		case R.id.tv_cart_buy_or_del:
			if (isBatchModel)
			{
				List<Integer> ids = getSelectedIds();
				doDelete(ids);
			} else
			{
				Toast.makeText(getActivity(), "结算", 0).show();
			}
			break;
		case R.id.check_box:
			if (mCheckAll.isChecked())
			{

				totalPrice = 0;
				if (Data.arrayList_cart != null)
				{
					mSelectState.clear();
					int size = Data.arrayList_cart.size();
					if (size == 0)
					{
						return;
					}
					for (int i = 0; i < size; i++)
					{
						int _id = (int) Data.arrayList_cart.get(i).ProductId;
						mSelectState.put(_id, true);
						totalPrice += Data.arrayList_cart.get(i).ProductNum * Data.arrayList_cart.get(i).ProductPrice;
					}
					refreshListView();
					tv_cart_total.setText("￥" + totalPrice + "");
					tv_cart_select_num.setText("已选" + mSelectState.size() + "件商品");

				}
			} else
			{
				if (adapter != null)
				{
					totalPrice = 0;
					mSelectState.clear();
					refreshListView();
					tv_cart_total.setText("￥" + 0.00 + "");
					tv_cart_select_num.setText("已选" + 0 + "件商品");

				}
			}
			break;
		default:
			break;
		}
	}
	
	private void refreshListView()
	{
		if (adapter == null)
		{
			adapter = new CartAdapter();
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(adapter);

		} else
		{
			adapter.notifyDataSetChanged();

		}
	}
	
	private final List<Integer> getSelectedIds()
	{
		ArrayList<Integer> selectedIds = new ArrayList<Integer>();
		for (int index = 0; index < mSelectState.size(); index++)
		{
			if (mSelectState.valueAt(index))
			{
				selectedIds.add(mSelectState.keyAt(index));
			}
		}
		return selectedIds;
	}
	
	//适配器
	public class CartAdapter extends BaseAdapter implements OnItemClickListener
	{

		@Override
		public int getCount() {
			return Data.arrayList_cart.size();
		}

		@Override
		public Object getItem(int position) {
			return Data.arrayList_cart.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View view = convertView;
			if (view == null)
			{
				view = LayoutInflater.from(getActivity()).inflate(R.layout.cart_item, null);
				holder = new ViewHolder(view);
				view.setTag(holder);
			} else
			{
				holder = (ViewHolder) view.getTag();
			}

			ProductBean data = Data.arrayList_cart.get(position);
			bindListItem(holder, data);
			
			holder.tv_add.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					int _id = (int) Data.arrayList_cart.get(position).ProductId;

					boolean selected = mSelectState.get(_id, false);

					Data.arrayList_cart.get(position).ProductNum = Data.arrayList_cart.get(position).ProductNum+ 1;

					notifyDataSetChanged();

					if (selected)
					{
						totalPrice += Data.arrayList_cart.get(position).ProductPrice;
						tv_cart_total.setText("￥" + totalPrice + "");
					}
				}
			});
			
			holder.tv_reduce.setOnClickListener(new OnClickListener() {		
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (Data.arrayList_cart.get(position).ProductNum == 1)
						return;

					int _id = (int) Data.arrayList_cart.get(position).ProductId;

					boolean selected = mSelectState.get(_id, false);
					Data.arrayList_cart.get(position).ProductNum = Data.arrayList_cart.get(position).ProductNum - 1;
					notifyDataSetChanged();

					if (selected)
					{
						totalPrice -= Data.arrayList_cart.get(position).ProductPrice;
						tv_cart_total.setText("￥" + totalPrice + "");

					} 

				}
			});
			return view;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ProductBean bean = Data.arrayList_cart.get(position);

			ViewHolder holder = (ViewHolder) view.getTag();
			int _id = (int) bean.ProductId;

			boolean selected = !mSelectState.get(_id, false);
			holder.checkBox.toggle();
			if (selected)
			{
				mSelectState.put(_id, true);
				totalPrice += bean.ProductNum * bean.ProductPrice;
			} else
			{
				mSelectState.delete(_id);
				totalPrice -= bean.ProductNum * bean.ProductPrice;
			}
			tv_cart_select_num.setText("已选" + mSelectState.size() + "件商品");
			tv_cart_total.setText("￥" + totalPrice + "");
			if (mSelectState.size() == Data.arrayList_cart.size())
			{
				mCheckAll.setChecked(true);
			} else
			{
				mCheckAll.setChecked(false);
			}
		}
	}
	private void bindListItem(ViewHolder holder, ProductBean data)
	{

		holder.tv_intro.setText(data.ProductName);
		holder.tv_price.setText("￥" + data.ProductPrice);
		holder.tv_num.setText(data.ProductNum + "");
		holder.tv_kind.setText(data.ProductKind);
		int _id = data.ProductId;
		boolean selected = mSelectState.get(_id, false);
		
		holder.checkBox.setChecked(selected);

	}
	class ViewHolder
	{
		CheckBox checkBox;
		ImageView iv_pic;
		TextView tv_intro;
		TextView tv_price;
		TextView tv_reduce;
		TextView tv_num;
		TextView tv_add;
		TextView tv_kind;
		RadioGroup radioGroup1;
		public ViewHolder(View view)
		{
			checkBox = (CheckBox) view.findViewById(R.id.check_box);
			tv_intro = (TextView) view.findViewById(R.id.tv_intro);
			iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			tv_price = (TextView) view.findViewById(R.id.tv_price);
			tv_reduce = (TextView) view.findViewById(R.id.tv_reduce);
			tv_num = (TextView) view.findViewById(R.id.tv_num);
			tv_add = (TextView) view.findViewById(R.id.tv_add);
			tv_kind = (TextView) view.findViewById(R.id.tv_kind);
			radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
		}
	}
}

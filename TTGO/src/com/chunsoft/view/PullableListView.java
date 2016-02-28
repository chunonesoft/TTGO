package com.chunsoft.view;

import com.chunsoft.ttgo.util.Pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;
import android.widget.Toast;

public class PullableListView extends ListView implements Pullable
{

	private Context mContext;
	public PullableListView(Context context)
	{
		super(context);
		mContext = context;
	}

	public PullableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext = context;
	}

	@Override
	public boolean canPullDown()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// 滑到ListView的顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight());
				return true;
		}
		return false;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

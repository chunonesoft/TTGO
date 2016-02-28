package com.chunsoft.ttgo.myself;

import com.chunsoft.ttgo.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Myself_F extends Fragment implements OnClickListener{
	private TextView tv_myphorder;
	private Intent intent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.myself_f, null);
		FindView(view);
		Click();
		return view;
	}

	private void FindView(View view)
	{
		tv_myphorder = (TextView) view.findViewById(R.id.tv_myphorder);
	}
	private void Click()
	{
		tv_myphorder.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_myphorder:
			intent = new Intent(getActivity(),Ph_A.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}

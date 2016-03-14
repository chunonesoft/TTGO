package com.chunsoft.ttgo.myself;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.chunsoft.ttgo.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Add_Adress extends BaseActivity implements OnClickListener,OnWheelChangedListener{
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView tv_adress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_adress);
		FindView();
		Click();
		SetData();
	}

	private void FindView()
	{
		tv_adress = (TextView) findViewById(R.id.tv_adress);
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
	}
	
	private void SetData()
	{
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(Add_Adress.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}
	private void Click()
	{
		// 添加change事件
    		mViewProvince.addChangingListener(this);
    		// 添加change事件
    		mViewCity.addChangingListener(this);
    		// 添加change事件
    		mViewDistrict.addChangingListener(this);
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
			tv_adress.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName+"");
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			tv_adress.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName+"");
		}
	}

	@Override
	public void onClick(View v) {
		
	}
	
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
}

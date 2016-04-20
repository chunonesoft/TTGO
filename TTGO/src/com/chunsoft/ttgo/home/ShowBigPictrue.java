package com.chunsoft.ttgo.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chunsoft.ttgo.R;
import com.chunsoft.view.ScaleView.HackyViewPager;

/**
 * ��ʾ��ͼ����
 * 
 * @author chunsoft
 * 
 */
public class ShowBigPictrue extends FragmentActivity {

	private HackyViewPager viewPager;
	private int[] resId = { R.drawable.detail_show_1, R.drawable.detail_show_2,
			R.drawable.detail_show_3, R.drawable.detail_show_4,
			R.drawable.detail_show_5, R.drawable.detail_show_6 };
	/** �õ���һ��������ͼƬ��λ�� */
	private int position = 0;
	private String str[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_big_pictrue_a);
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		str = intent.getStringArrayExtra("picPath");
		initViewPager();
	}

	private void initViewPager() {

		viewPager = (HackyViewPager) findViewById(R.id.viewPager_show_bigPic);
		ViewPagerAdapter adapter = new ViewPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		// ��ת���ڼ�������
		viewPager.setCurrentItem(position);

	}

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			String show_resId = str[position];
			return new PictrueFragment(show_resId);
		}

		@Override
		public int getCount() {
			return str.length;
		}

	}

}

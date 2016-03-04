package com.chunsoft.ttgo.home;

import java.util.ArrayList;

import com.chunsoft.ttgo.R;
import com.chunsoft.ttgo.home.Popwindow.OnItemClickListener;
import com.chunsoft.view.ScaleView.HackyViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ProductDetail_A extends Activity implements OnItemClickListener,OnClickListener{
	private Context mContext;
	private ImageView iv_back;
	private ImageView put_in;
	private ImageView buy_now;
	private ImageView iv_kf;
	/** �������ñ������� */
	private LinearLayout all_choice_layout = null;
	
	/**�ж��Ƿ�������������ť*/
	boolean isClickBuy = false;
	private ArrayList<View> allListView;
	private HackyViewPager viewPager;
	private Popwindow popWindow;
	/**ViewPager��ǰ��ʾҳ���±�*/
	private int position=0;
	/**�Ƿ�����ղ�*/
	private static boolean isCollection=false; 
	private ImageView iv_baby_collection;
	private int[] resId = { R.drawable.detail_show_1, R.drawable.detail_show_2, R.drawable.detail_show_3, R.drawable.detail_show_4, R.drawable.detail_show_5, R.drawable.detail_show_6 };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productdetail_a);
		findView();
		init();
		//�õ�������ղ���Ϣ
		getSaveCollection();
		initViewPager();
		Click();
		popWindow = new Popwindow(this);
		popWindow.setOnItemClickListener(this);
	}
	
	private void findView()
	{
		all_choice_layout = (LinearLayout) findViewById(R.id.all_choice_layout);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_kf = (ImageView) findViewById(R.id.iv_kf);
		put_in = (ImageView) findViewById(R.id.put_in);
		buy_now = (ImageView) findViewById(R.id.buy_now);
		iv_baby_collection=(ImageView) findViewById(R.id.iv_baby_collection);
	}
	
	private void init()
	{
		mContext = ProductDetail_A.this;
	}
	
	private void initViewPager() {

		if (allListView != null) {
			allListView.clear();
			allListView = null;
		}
		allListView = new ArrayList<View>();

		for (int i = 0; i < resId.length; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.pic_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			imageView.setImageResource(resId[i]);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//��ս���鿴��ͼ����
					Intent intent = new Intent(mContext, ShowBigPictrue.class);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});
			allListView.add(view);
		}

		viewPager = (HackyViewPager) findViewById(R.id.iv_baby);
		ViewPagerAdapter adapter = new ViewPagerAdapter();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				position=arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		viewPager.setAdapter(adapter);
	}
	
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return allListView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (View) arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = allListView.get(position);
			container.addView(view);
			return view;
		}

	}

	/**ע�����*/
	private void Click()
	{
		iv_back.setOnClickListener(this);
		iv_baby_collection.setOnClickListener(this);
		put_in.setOnClickListener(this);
		buy_now.setOnClickListener(this);
		iv_kf.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_baby_collection:
			//�ղ�
			if (isCollection) {
				//��ʾ�Ƿ�ȡ���ղ�
				cancelCollection();
			}else {
				isCollection=true;
				setSaveCollection();
				//����Ѿ��ղأ�����ʾ�ղغ��Ч��
				iv_baby_collection.setImageResource(R.drawable.second_2_collection);
				Toast.makeText(this, "�ղسɹ�", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.put_in:
			//��ӹ��ﳵ
			isClickBuy = false;
			setBackgroundBlack(all_choice_layout, 0);
			popWindow.showAsDropDown(v);
			break;
		case R.id.buy_now:
			//��������
			isClickBuy = true;
			setBackgroundBlack(all_choice_layout, 0);
			popWindow.showAsDropDown(v);
			break;
		case R.id.iv_kf:
			//��ϵ�ͷ�
			AlertDialog.Builder builder = new Builder(ProductDetail_A.this);
			//builder.setMessage("��ϵ�ͷ�");
			builder.setTitle("18868448198");
			builder.setPositiveButton("��ϵ�ͷ�", new android.content.DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					 dialog.dismiss();
					 Intent intent = new Intent
							 (Intent.ACTION_CALL, Uri.parse("tel:"+ "18868448198"));
					 startActivity(intent);
				}
			});
			builder.setNegativeButton("ȡ��",new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
			break;
		default:
			break;
		}
	}
	
	/**�����Ƿ�����ղ�*/
	private void setSaveCollection(){
		SharedPreferences sp=getSharedPreferences("SAVECOLLECTION", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putBoolean("isCollection", isCollection);
		editor.commit();
	}
	/**�õ�������Ƿ�����ղر��*/
	private void getSaveCollection(){
		SharedPreferences sp=getSharedPreferences("SAVECOLLECTION", Context.MODE_PRIVATE);
		isCollection=sp.getBoolean("isCollection", false);
		
	}
	/**ȡ���ղ�*/
	private  void cancelCollection(){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("�Ƿ�ȡ���ղ�");
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				isCollection=false;
				//���ȡ���ղأ�����ʾȡ���ղغ��Ч��
				iv_baby_collection.setImageResource(R.drawable.second_2);
				setSaveCollection();
			}
		});
		dialog.setNegativeButton("ȡ��", null);
		dialog.create().show();
		
	}

	@Override
	public void onClickOKPop() {
		setBackgroundBlack(all_choice_layout, 1);

		if (isClickBuy) {
			//���֮ǰ�ǵ��������������ô����ת�������������
			//Intent intent = new Intent(mContext, BuynowActivity.class);
			//startActivity(intent);
		}else {
			Toast.makeText(this, "��ӵ����ﳵ�ɹ�", Toast.LENGTH_SHORT).show();
		}
	}
	
	/** ���Ʊ����䰵 0�䰵 1���� */
	public void setBackgroundBlack(View view, int what) {
		switch (what) {
		case 0:
			view.setVisibility(View.VISIBLE);
			break;
		case 1:
			view.setVisibility(View.GONE);
			break;
		}
	}


}

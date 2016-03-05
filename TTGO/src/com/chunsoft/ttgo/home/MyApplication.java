package com.chunsoft.ttgo.home;

import java.util.LinkedList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

public class MyApplication extends Application{
	/**
	 * Log or request TAG
	 */
	public static final String TAG = "VolleyPatterns";
	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;
	public static Context applicationContext;
	
List<Activity> activitylist = new LinkedList<Activity>();
	
	private static MyApplication instance;
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		applicationContext = this;
		//��ʼ��ͼƬ�������������
		initImageLoader(getApplicationContext());
	}
	public static synchronized MyApplication getInstance()
	{
		if(null == instance)
		{
			instance = new MyApplication();
		}
		return instance;
	}
	
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)// �����̵߳����ȼ�
				.denyCacheImageMultipleSizesInMemory()// ��ͬһ��Uri��ȡ��ͬ��С��ͼƬ�����浽�ڴ�ʱ��ֻ����һ����Ĭ�ϻỺ������ͬ�Ĵ�С����ͬͼƬ
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// ���û����ļ�������
				.discCacheFileCount(60)// �����ļ���������
				.tasksProcessingOrder(QueueProcessingType.LIFO)// ����ͼƬ���غ���ʾ�Ĺ�����������
				.build();
		ImageLoader.getInstance().init(config);
	}
	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			// 1
			// 2
			synchronized (MyApplication.class) {
				if (mRequestQueue == null) {
					mRequestQueue = Volley
							.newRequestQueue(getApplicationContext());
				}
			}
		}
		return mRequestQueue;
	}
	
	//���Activity
	public void addActivity(Activity activity)
	{
		activitylist.add(activity);
	}
	//Activity finish
	public void exit()
	{
		for(Activity activity:activitylist)
		{
			activity.finish();
		}
		System.exit(0);
	}
	/**
	 * Adds the specified request to the global queue, if tag is specified then
	 * it is used else Default TAG is used.
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important to
	 * specify a TAG so that the pending/ongoing requests can be cancelled.
	 * 
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

}

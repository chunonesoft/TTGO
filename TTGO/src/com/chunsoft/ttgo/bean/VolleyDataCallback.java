package com.chunsoft.ttgo.bean;

/**
 * interface callback
 * 
 * @author Administrator
 * 
 * @param <T>
 */
public interface VolleyDataCallback<T> {
	void onSuccess(T datas);
}

package com.chunsoft.net;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;

/** 
 * @ClassName:  LruImageCache   
 * @Description:æœ?¿‘æœ?¹…æœªä½¿ç”¨å›¾ç‰‡ç¼“å­˜æœºåˆ?  
 * @author: chunsoft 
 * @date:   2015-8-2 ä¸‹åˆ10:20:28
 */
public class LruImageCache implements ImageCache{

	private static LruCache<String, Bitmap> mMemoryCache;
	private static LruImageCache lruImageCache;
	
	public LruImageCache(){
		// Get the Max available memory
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap bitmap){
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};		
	}
	
	public static LruImageCache instance(){
		if(lruImageCache == null){
			lruImageCache = new LruImageCache();
		}
		return lruImageCache;
	}
	
	@Override
	public Bitmap getBitmap(String url) {		
		return mMemoryCache.get(url);	
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if(getBitmap(url) == null){
			mMemoryCache.put(url, bitmap);
		}		
	}
}

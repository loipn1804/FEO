package com.fareastorchid;

import android.app.Application;
import android.content.Context;
import com.fareastorchid.connection.RequestBackgroundWorker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class FloristApp extends Application {

	private static Context context;

	public static Context getAppContext() {
		return FloristApp.context;
	}

	public static void startWorker() {
		RequestBackgroundWorker.startWaitingForRequest();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setupImageLoader();
		//Mint.initAndStartSession(FloristApp.this, "dee4bc9c");

		FloristApp.context = getApplicationContext();
		startWorker();
	}

	private void setupImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 3)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();

		ImageLoader.getInstance().init(config);
	}
}

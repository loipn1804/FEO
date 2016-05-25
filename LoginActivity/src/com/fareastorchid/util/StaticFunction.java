package com.fareastorchid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.fareastorchid.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by USER on 4/22/2015.
 */
public class StaticFunction {

	public static final String NO_IMAGE_1 = "/No%20image.jpg";
	public static final String NO_IMAGE_2 = "/no-image.jpg";

	public static final String[] NO_IMAGE_STRINGS = { NO_IMAGE_1, NO_IMAGE_2 };

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public static ImageLoader getImageLoader() {
		return ImageLoader.getInstance();
	}

	public static DisplayImageOptions getImageLoaderOptions() {

		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_loadinghaft)
				.showImageOnFail(R.drawable.noimage).cacheInMemory(false)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		return options;
	}

	public static DisplayImageOptions getImageLoaderOptionsForBanner() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.sample_banner)
				.showImageOnFail(R.drawable.sample_banner).cacheInMemory(false)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public static String checkLink(String link) {
		for (int i = 0; i < NO_IMAGE_STRINGS.length; i++) {
			if (link.contains(NO_IMAGE_STRINGS[i])) {
				return "assets://noimage.jpg"; // link no image
			}
		}

		return link;

	}

	public static String checkLinkDetailActivity(String link) {
		for (int i = 0; i < NO_IMAGE_STRINGS.length; i++) {
			if (link.contains(NO_IMAGE_STRINGS[i])) {
				return "assets://noimagebig.jpg"; // link no image
			}
		}

		return link;

	}
}

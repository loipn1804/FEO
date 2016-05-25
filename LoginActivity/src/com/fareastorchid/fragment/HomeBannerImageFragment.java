package com.fareastorchid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.fareastorchid.R;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.model.Banner;
import com.fareastorchid.util.PQT;
import com.fareastorchid.util.StaticFunction;

public class HomeBannerImageFragment extends Fragment {

	private Banner banner;

	public static HomeBannerImageFragment newInstance(Banner _banner) {
		HomeBannerImageFragment fragment = new HomeBannerImageFragment();
		fragment.setBanner(_banner);

		return fragment;
	}

	private void setBanner(Banner _banner) {
		this.banner = _banner;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_home_banner_photo, container, false);
		ImageView imvFlowerPhoto = (ImageView) view.findViewById(R.id.view_image_photo_imv_flower_photo);
		
		if (PQT.isNotNull(banner)) {
			if (!banner.getFile_path().equalsIgnoreCase("")) {
				String imgUrl = UrlHelper.FLORIST_BANNER_IMG + banner.getFile_path();
				imgUrl = imgUrl.replace(" ", "%20");
				//UrlImageViewHelper.setUrlDrawable(imvFlowerPhoto, imgUrl, R.drawable.sample_banner);
				imgUrl = StaticFunction.checkLink(imgUrl);
				StaticFunction.getImageLoader().displayImage(imgUrl, imvFlowerPhoto, StaticFunction.getImageLoaderOptionsForBanner());
				Log.e("banner", imgUrl);
			}
		}
		return view;
	}
}

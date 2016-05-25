package com.fareastorchid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.util.PQT;
import com.fareastorchid.util.StaticFunction;
import com.polites.android.TouchImageView;

public class ImageActivity extends AbstractActivity {

    TouchImageView imageView;
    TextView tvTitle;

    boolean isImageFitToScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = (TouchImageView) findViewById(R.id.activity_imageView);
        tvTitle = (TextView) findViewById(R.id.activity_tv_title);

        Intent intent = getIntent();
        String imageUrl = intent.hasExtra("imageUrl") ? intent.getStringExtra("imageUrl") : "";
        String itemTitle = intent.hasExtra("itemTitle") ? intent.getStringExtra("itemTitle") : "";

        if (PQT.isStringDirty(imageUrl)) {
            if (!imageUrl.startsWith("/"))
                imageUrl = "/" + imageUrl;
            String imgUrl = UrlHelper.FLORIST_IMG + imageUrl;
            imgUrl = imgUrl.replace(" ", "%20");
            
            //UrlImageViewHelper.setUrlDrawable(imageView, imgUrl, R.drawable.ic_loading);
            
            imgUrl = StaticFunction.checkLink(imgUrl);
            StaticFunction.getImageLoader().displayImage(imgUrl, imageView, StaticFunction.getImageLoaderOptions());
        }

        tvTitle.setText(itemTitle);
    }
}
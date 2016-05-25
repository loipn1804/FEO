package com.fareastorchid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fareastorchid.R;
import com.fareastorchid.util.PQT;

public class FlowerDetailBannerImageFragment extends Fragment {

    private int photo;

    public static FlowerDetailBannerImageFragment newInstance(int _photo) {
        FlowerDetailBannerImageFragment fragment = new FlowerDetailBannerImageFragment();
        fragment.setPhoto(_photo);
        return fragment;
    }

    private void setPhoto(int photo) {
        this.photo = photo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(
                R.layout.view_flower_detail_banner_photo, container, false);

        final ImageView imvFlowerPhoto = (ImageView) view
                .findViewById(R.id.view_flower_detail_imv_flower_photo);

        if (PQT.isNotNull(photo)) {
            imvFlowerPhoto.setImageResource(photo);
        }

        return view;
    }
}

package com.fareastorchid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fareastorchid.fragment.FlowerDetailBannerImageFragment;

import java.util.ArrayList;
import java.util.List;

public class FlowerDetailBannerAdapter extends FragmentStatePagerAdapter {

    private List<Integer> lsPhotos;

    public FlowerDetailBannerAdapter(final FragmentManager fm,
                                     final ArrayList<Integer> lsPhotos) {
        super(fm);
        this.lsPhotos = lsPhotos;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        if (lsPhotos != null) {
            return lsPhotos.size();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(final int position) {
        return FlowerDetailBannerImageFragment.newInstance(lsPhotos
                .get(position));
    }


}

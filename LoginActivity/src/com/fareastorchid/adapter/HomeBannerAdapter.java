package com.fareastorchid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fareastorchid.fragment.HomeBannerImageFragment;
import com.fareastorchid.model.Banner;

import java.util.List;

public class HomeBannerAdapter extends FragmentStatePagerAdapter {

    private List<Banner> lsBanner;

    public HomeBannerAdapter(final FragmentManager fm) {
        super(fm);
    }

    public void setBannerList(List<Banner> lsBanner) {

        this.lsBanner = lsBanner;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        if (lsBanner != null) {
            return lsBanner.size();
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
        return HomeBannerImageFragment.newInstance(lsBanner.get(position));
    }
}

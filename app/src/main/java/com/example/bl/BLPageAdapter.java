package com.example.bl;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class BLPageAdapter extends FragmentStatePagerAdapter {
    int counttab;
    public BLPageAdapter(FragmentManager fm, int counttab) {
        super(fm);
        this.counttab = counttab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                Borrowed_fragment borrowed_fragment = new Borrowed_fragment();
                return borrowed_fragment;

            case 1:
                Lended_fragment lended_fragment = new Lended_fragment();
                return lended_fragment;

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return counttab;
    }
}

package com.example.greed54.weatherapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();

    public SliderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
        Log.d("FRAG", fragment.toString());
    }

    public void removeFragment(int position){
        fragmentList.remove(position);
    }
}

package com.etcomm.dcare.fragment;

import android.support.v4.app.Fragment;

import com.etcomm.dcare.R;

/**
 * Created by admin on 13-11-23.
 */
public class FragmentFactory {
    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case 1:
            case R.id.radio_suggest:
                fragment = new SuggestFragment();//头条
                break;
            case 2:
            case R.id.radio_around:
                fragment = new AroundFragment();//身边
                break;
            case 3:
            case R.id.radio_walk:
                fragment = new WalkPageFragment();//健步
                break;
            case 4:
            case R.id.radio_find:
                fragment = new FindFragment();//发现
                break;
            case 5:
            case R.id.radio_mine:
                fragment = new MineFragment();//我的
                break;
        }
        return fragment;
    }
}

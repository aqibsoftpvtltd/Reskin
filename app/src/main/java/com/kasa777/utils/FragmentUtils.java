package com.kasa777.utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kasa777.R;

public class FragmentUtils {
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;

    public FragmentUtils(FragmentManager fragmentManager) {
        FragmentUtils.fragmentManager = fragmentManager;

    }

    public void replaceFragment(Fragment fragment, String tag, int frameId) {
        fragmentManager.beginTransaction().replace(frameId, fragment, tag).commit();
    }

    public void replaceFragment(Fragment fragment, String tag, int frameId,String direction) {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (direction.equals("right")){
            fragmentTransaction.setCustomAnimations(R.anim.enter,0);
        }
        else {

            fragmentTransaction.setCustomAnimations(R.anim.exit, 0);
        }

        fragmentTransaction.replace(frameId, fragment, tag).commit();
    }
}

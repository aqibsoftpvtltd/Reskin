package com.kasa777.utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentUtils {
    public static FragmentManager fragmentManager;

    public FragmentUtils(FragmentManager fragmentManager) {
        FragmentUtils.fragmentManager = fragmentManager;
    }

    public void replaceFragment(Fragment fragment, String tag, int frameId) {
        fragmentManager.beginTransaction().replace(frameId, fragment, tag).commit();
    }
}

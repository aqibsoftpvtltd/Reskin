package com.kasa777.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import com.kasa777.R;
import com.kasa777.interfaces.FragmentCallBack;
import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    Activity activity;
    private static final int S_V2 = 32;
    ActivityResultLauncher<String[]> mPermissionResult;

    public PermissionUtils(Activity activity, ActivityResultLauncher<String[]> mPermissionResult) {
        this.activity = activity;
        this.mPermissionResult=mPermissionResult;
    }


    public boolean isStoragePhoneStateRecordingPermissionGranted()
    {
//        if (Build.VERSION.SDK_INT>S_V2)
//        {
//            int contactPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
//            int recordAudioPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
//            int mediaImagesPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES);
//            int mediaVideoPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO);
//            return (recordAudioPermission== PackageManager.PERMISSION_GRANTED &&
//                    contactPermission== PackageManager.PERMISSION_GRANTED &&
//                    mediaImagesPermission== PackageManager.PERMISSION_GRANTED && mediaVideoPermission== PackageManager.PERMISSION_GRANTED);
//
//        }
//        else
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.P)
        {
            int contactPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
//            int recordAudioPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            int readExternalStoragePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            return (
//                    recordAudioPermission== PackageManager.PERMISSION_GRANTED &&
                    contactPermission== PackageManager.PERMISSION_GRANTED &&
                    readExternalStoragePermission== PackageManager.PERMISSION_GRANTED);
        }
        else
        {
            int contactPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
//            int recordAudioPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            int readExternalStoragePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writeExternalStoragePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return (
//                    recordAudioPermission== PackageManager.PERMISSION_GRANTED &&
                    contactPermission== PackageManager.PERMISSION_GRANTED &&
                    readExternalStoragePermission== PackageManager.PERMISSION_GRANTED &&
                    writeExternalStoragePermission== PackageManager.PERMISSION_GRANTED );
        }
    }



    public void showStoragePermissionDailog(String message)
    {
        List<String> permissionStatusList=new ArrayList<>();
        String[] permissions ;
//        if (Build.VERSION.SDK_INT>S_V2)
//        {
//            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VIDEO};
//        }
//        else
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.P)
        {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        else
        {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        for (String keyStr:permissions)
        {
            permissionStatusList.add(Helper.getPermissionStatus(activity,keyStr));
        }

        if (permissionStatusList.contains("denied"))
        {
            Helper.showDoubleButtonAlert(activity, activity.getString(R.string.permission_alert),message,
                    activity.getString(R.string.cancel_), activity.getString(R.string.permission), false, new FragmentCallBack() {
                        @Override
                        public void onResponce(Bundle bundle) {
                            if (bundle.getBoolean("isShow",false))
                            {
                                takeStoragePermission();
                            }
                        }
                    });
            return;
        }
        takeStoragePermission();

    }

    public boolean isStoragePermissionGranted()
    {
//        if (Build.VERSION.SDK_INT>S_V2)
//        {
//
//            int mediaImagesPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES);
//            int mediaVideoPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO);
//            return (mediaImagesPermission== PackageManager.PERMISSION_GRANTED && mediaVideoPermission== PackageManager.PERMISSION_GRANTED);
//        }
//        else
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.P)
        {
            int readExternalStoragePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            return (readExternalStoragePermission== PackageManager.PERMISSION_GRANTED);
        }
        else
        {
            int readExternalStoragePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writeExternalStoragePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return (readExternalStoragePermission== PackageManager.PERMISSION_GRANTED && writeExternalStoragePermission== PackageManager.PERMISSION_GRANTED );
        }
    }

    public void takeStoragePermission()
    {

//        if (Build.VERSION.SDK_INT>S_V2)
//        {
//            String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VIDEO};
//            mPermissionResult.launch(permissions);
//        }
//        else
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.P)
        {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            mPermissionResult.launch(permissions);
        }
        else
        {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            mPermissionResult.launch(permissions);
        }
    }


    public boolean isLocationPermissionGranted()
    {
        int coursePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int finePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        return (coursePermission== PackageManager.PERMISSION_GRANTED && finePermission== PackageManager.PERMISSION_GRANTED);
    }

    public void showLocationPermissionDailog(String message)
    {
        List<String> permissionStatusList=new ArrayList<>();
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        for (String keyStr:permissions)
        {
            permissionStatusList.add(Helper.getPermissionStatus(activity,keyStr));
        }

        if (permissionStatusList.contains("denied"))
        {
            Helper.showDoubleButtonAlert(activity, activity.getString(R.string.permission_alert),message,
                    activity.getString(R.string.cancel_), activity.getString(R.string.permission), false, new FragmentCallBack() {
                        @Override
                        public void onResponce(Bundle bundle) {
                            if (bundle.getBoolean("isShow",false))
                            {
                                takeLocationPermission();
                            }
                        }
                    });
            return;
        }
        takeLocationPermission();

    }

    public void takeLocationPermission()
    {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        mPermissionResult.launch(permissions);
    }


    public void showCameraPermissionDailog(String message)
    {
        List<String> permissionStatusList=new ArrayList<>();
        String[] permissions = {Manifest.permission.CAMERA};
        for (String keyStr:permissions)
        {
            permissionStatusList.add(Helper.getPermissionStatus(activity,keyStr));
        }

        if (permissionStatusList.contains("denied"))
        {
            Helper.showDoubleButtonAlert(activity, activity.getString(R.string.permission_alert),message,
                    activity.getString(R.string.cancel_), activity.getString(R.string.permission), false, new FragmentCallBack() {
                        @Override
                        public void onResponce(Bundle bundle) {
                            if (bundle.getBoolean("isShow",false))
                            {
                                takeCameraPermission();
                            }
                        }
                    });
            return;
        }
        takeCameraPermission();

    }

    public boolean isCameraPermissionGranted()
    {
        int cameraPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        return (cameraPermission== PackageManager.PERMISSION_GRANTED);
    }

    public void takeCameraPermission()
    {
        String[] permissions = {Manifest.permission.CAMERA};
        mPermissionResult.launch(permissions);
    }


    public void takeRecordingPermission()
    {
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        mPermissionResult.launch(permissions);
    }


    public void showRecordingPermissionDailog(String message)
    {
        List<String> permissionStatusList=new ArrayList<>();
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        for (String keyStr:permissions)
        {
            permissionStatusList.add(Helper.getPermissionStatus(activity,keyStr));
        }

        if (permissionStatusList.contains("denied"))
        {
            Helper.showDoubleButtonAlert(activity, activity.getString(R.string.permission_alert),message,
                    activity.getString(R.string.cancel_), activity.getString(R.string.permission), false, new FragmentCallBack() {
                        @Override
                        public void onResponce(Bundle bundle) {
                            if (bundle.getBoolean("isShow",false))
                            {
                                takeRecordingPermission();
                            }
                        }
                    });
            return;
        }
        takeRecordingPermission();

    }

    public boolean isRecordingPermissionGranted()
    {
        int recordAudioPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        return (recordAudioPermission== PackageManager.PERMISSION_GRANTED );
    }


    public void takePhoneCallStatePermission()
    {
        String[] permissions = {Manifest.permission.READ_PHONE_STATE};
        mPermissionResult.launch(permissions);
    }

    public void showPhoneCallStatePermissionDailog(String message)
    {
        List<String> permissionStatusList=new ArrayList<>();
        String[] permissions = {Manifest.permission.READ_PHONE_STATE};
        for (String keyStr:permissions)
        {
            permissionStatusList.add(Helper.getPermissionStatus(activity,keyStr));
        }

        if (permissionStatusList.contains("denied"))
        {
            Helper.showDoubleButtonAlert(activity, activity.getString(R.string.permission_alert),message,
                    activity.getString(R.string.cancel_), activity.getString(R.string.permission), false, new FragmentCallBack() {
                        @Override
                        public void onResponce(Bundle bundle) {
                            if (bundle.getBoolean("isShow",false))
                            {
                                takePhoneCallStatePermission();
                            }
                        }
                    });
            return;
        }
        takePhoneCallStatePermission();
    }

    public boolean isPhoneCallStatePermissionGranted()
    {
        int contactPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        return (contactPermission== PackageManager.PERMISSION_GRANTED);
    }
}

package com.kasa777.ui.login_signup_pages;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kasa777.R;
import com.kasa777.constant.Constant;
import com.kasa777.ui.login_signup_pages.fragments.CreateProfileFragment;
import com.kasa777.ui.login_signup_pages.fragments.LoginFragment;
import com.kasa777.ui.login_signup_pages.fragments.MpinLoginFragment;
import com.kasa777.utils.AppPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginSignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private String firebaseToken = "";
    private FragmentManager fragmentManager;

    private Animation btnAnim;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigation();
        setContentView(R.layout.loginsignup_horizontal);

        if (AppPreference.getStringPreference(this, Constant.FIREBASE_TOKEN).isEmpty()) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("FirebaseMessageService", "getInstanceId failed", task.getException());
                } else {
                    firebaseToken = Objects.requireNonNull(task.getResult()).getToken();
                }
            });
        } else {
            firebaseToken = AppPreference.getStringPreference(this, Constant.FIREBASE_TOKEN);
        }

        fragmentManager = getSupportFragmentManager();
              btnAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);

        init();
    }


    private void init() {
        boolean isProfileCreated = AppPreference.getBooleanPreference(this,Constant.IS_PROFILE_CREATED);
        if (!isProfileCreated){
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLogin, new CreateProfileFragment(), Constant.CreateProfileFragment)
                    .addToBackStack(null)
                    .commit();

        }else {
            if (!AppPreference.getBooleanPreference(this,Constant.isLoginWithMpin)){
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLogin, new LoginFragment(), Constant.LoginFragment)
                        .addToBackStack(null)
                        .commit();
            }else {
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLogin, new MpinLoginFragment(), Constant.MpinLoginFragment)
                        .addToBackStack(null)
                        .commit();

            }
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }


    @Override
    public void onBackPressed() {
        Fragment mpinLoginFragment = fragmentManager.findFragmentByTag(Constant.MpinLoginFragment);
        Fragment createProfileFragment = fragmentManager.findFragmentByTag(Constant.CreateProfileFragment);
        Fragment loginFragment = fragmentManager.findFragmentByTag(Constant.LoginFragment);
       if (loginFragment != null){
           finish();
       }else if (mpinLoginFragment != null) {
                finish();
        } else if (createProfileFragment != null) {
               finish();
        } else {
            finish();
        }

    }



    // this will hide the bottom mobile navigation controll
    public void hideNavigation() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

    }


}

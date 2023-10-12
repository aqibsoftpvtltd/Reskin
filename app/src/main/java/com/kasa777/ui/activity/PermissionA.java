package com.kasa777.ui.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.kasa777.BuildConfig;
import com.kasa777.R;
import com.kasa777.constant.Constant;
import com.kasa777.databinding.ActivityPermissionBinding;
import com.kasa777.retrofit_provider.RetrofitApiClient;
import com.kasa777.retrofit_provider.RetrofitService;
import com.kasa777.retrofit_provider.WebResponse;
import com.kasa777.ui.login_signup_pages.LoginSignUpActivity;
import com.kasa777.utils.Alerts;
import com.kasa777.utils.AppPreference;
import com.kasa777.utils.ConnectionDetector;
import com.kasa777.utils.Helper;
import com.kasa777.utils.PermissionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class PermissionA extends AppCompatActivity {

    ActivityPermissionBinding binding;
    PermissionUtils takeRecordPermission,takeCallPermission,takeStoragePermission,takeAllPermission;
    ConnectionDetector cd = null;
    RetrofitApiClient retrofitApiClient = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_permission);

        initControl();
        actionControl();
    }

    private void actionControl() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.tabMice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeRecordPermission.takeRecordingPermission();
            }
        });
        binding.tabCallPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeCallPermission.takePhoneCallStatePermission();
            }
        });
        binding.tabStoragePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeStoragePermission.takeStoragePermission();
            }
        });
        binding.tabAllowAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takeAllPermission.isStoragePhoneStateRecordingPermissionGranted())
                {
                    createRequestBody();
                }
                else
                {
                    Toast.makeText(binding.getRoot().getContext(), binding.getRoot().getContext().getString(R.string.you_need_all_permission), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void createRequestBody() {
        String deviceImei = new Helper(this).getDeviceId();
        AppPreference.setStringPreference(
                this,
                Constant.DEVICE_ID,
                deviceImei
        );
        Log.e(
                "newDeviceID",
                deviceImei + "  " + AppPreference.getStringPreference(this, Constant.FIREBASE_TOKEN)
        );

        JSONObject mObject = new JSONObject();
        try {
            mObject.put("deviceId", deviceImei);
            mObject.put("appVersion", BuildConfig.VERSION_CODE);
            mObject.put("token", AppPreference.getStringPreference(this, Constant.FIREBASE_TOKEN));
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, mObject.toString());
        tokenApi(body);
    }

    private void tokenApi(RequestBody body) {
        if (cd != null && cd.isNetworkAvailable()) {
            RetrofitService.getServerResponse(this,
                    null,
                    retrofitApiClient.updateFirebaseToken(body),
                    new WebResponse() {
                        @Override
                        public void onResponseSuccess(Response<?> result) {
                            /*{"status":1,"message":"Token Updated Successfully","data":{"n":0,"nModified":0,"ok":1}}*/
                            ResponseBody responseBody = (ResponseBody) result.body();
                            try {
                                JSONObject jsonObject = new JSONObject(responseBody.string());
                                JSONObject jsonResponse = new JSONObject(jsonObject.toString());
                                Log.e("OnResponse", jsonResponse.toString());

                                int status = jsonObject.getInt("status");
                                if (status == 1) {
                                    String userToken = jsonObject.getString("token");
                                    String name = jsonObject.optString("name");
                                    int mpinGen = jsonObject.optInt("mpinGen");

                                    if (mpinGen == 0) {
                                        AppPreference.setBooleanPreference(
                                                binding.getRoot().getContext(),
                                                Constant.IsMpinGenerate,
                                                true
                                        );

                                        AppPreference.setBooleanPreference(
                                                binding.getRoot().getContext(),
                                                Constant.isLoginWithMpin,
                                                true
                                        );
                                        if (AppPreference.getBooleanPreference(
                                                binding.getRoot().getContext(),
                                                Constant.isLoginWithUsername
                                        )
                                        ) {
                                            AppPreference.setBooleanPreference(
                                                    binding.getRoot().getContext(),
                                                    Constant.isLoginWithMpin,
                                                    false
                                            );
                                        }
                                    } else {
                                        AppPreference.setBooleanPreference(
                                                binding.getRoot().getContext(),
                                                Constant.IsMpinGenerate,
                                                false
                                        );
                                        AppPreference.setBooleanPreference(
                                                binding.getRoot().getContext(),
                                                Constant.isLoginWithMpin,
                                                false
                                        );
                                    }

                                    AppPreference.setStringPreference(
                                            binding.getRoot().getContext(),
                                            Constant.userLoginUserName,
                                            name
                                    );

                                    AppPreference.setStringPreference(
                                            binding.getRoot().getContext(),
                                            Constant.USER_LOGIN_TOKEN,
                                            userToken
                                    );
                                    Constant.USER_TOKEN = userToken;

                                    AppPreference.setBooleanPreference(
                                            binding.getRoot().getContext(),
                                            Constant.IS_PROFILE_CREATED,
                                            true
                                    );

                                    initSignUp();
                                } else if (status == 4) {
                                    // update
                                    Alerts.AlertDialogUpdateApp(
                                            binding.getRoot().getContext(),
                                            PermissionA.this,
                                            jsonObject.optString("message"),
                                            jsonObject.optString("appLink"),
                                            jsonObject.optBoolean("forceStatus"),
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Alerts.getDialog().dismiss();

                                                    String name = jsonObject.optString("name");
                                                    int mpinGen = jsonObject.optInt("mpinGen");

                                                    if (name != null) {
                                                        if (mpinGen == 0) {
                                                            AppPreference.setBooleanPreference(
                                                                    binding.getRoot().getContext(),
                                                                    Constant.IsMpinGenerate,
                                                                    true
                                                            );

                                                            AppPreference.setBooleanPreference(
                                                                    binding.getRoot().getContext(),
                                                                    Constant.isLoginWithMpin,
                                                                    true
                                                            );
                                                            if (AppPreference.getBooleanPreference(
                                                                    binding.getRoot().getContext(),
                                                                    Constant.isLoginWithUsername
                                                            )
                                                            ) {
                                                                AppPreference.setBooleanPreference(
                                                                        binding.getRoot().getContext(),
                                                                        Constant.isLoginWithMpin,
                                                                        false
                                                                );
                                                            }
                                                        } else {
                                                            AppPreference.setBooleanPreference(
                                                                    binding.getRoot().getContext(),
                                                                    Constant.IsMpinGenerate,
                                                                    false
                                                            );
                                                            AppPreference.setBooleanPreference(
                                                                    binding.getRoot().getContext(),
                                                                    Constant.isLoginWithMpin,
                                                                    false
                                                            );
                                                        }

                                                        AppPreference.setStringPreference(
                                                                binding.getRoot().getContext(),
                                                                Constant.userLoginUserName,
                                                                name
                                                        );

                                                        AppPreference.setBooleanPreference(
                                                                binding.getRoot().getContext(),
                                                                Constant.IS_PROFILE_CREATED,
                                                                true
                                                        );

                                                        initSignUp();
                                                    } else {
                                                        AppPreference.setBooleanPreference(
                                                                binding.getRoot().getContext(),
                                                                Constant.IS_PROFILE_CREATED,
                                                                false
                                                        );

                                                        initSignUp();
                                                    }
                                                }
                                            }
                                    );
                                } else if (status == 0) {
                                    AppPreference.setBooleanPreference(
                                            binding.getRoot().getContext(),
                                            Constant.IS_PROFILE_CREATED,
                                            false
                                    );

                                    initSignUp();
                                } else {
                                    Alerts.AlertDialogFailAutoClose(
                                            binding.getRoot().getContext(),
                                            PermissionA.this,
                                            "Application Is Under Maintenance\nPlease Come Back After Sometime\nThank You :)",""
                                    );
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponseFailed(String error) {
                            Alerts.serverError(binding.getRoot().getContext(), error);
                        }
                    });
        }
    }

    private void initSignUp() {
        //        Handler().postDelayed({
        if (AppPreference.getBooleanPreference(this, Constant.IS_LOGIN)) {
            AppPreference.setBooleanPreference(this, Constant.IS_LOGIN, false);
        }

        Intent intent = new Intent(this, LoginSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        //        }, 2000);
    }


    private ActivityResultLauncher<String[]> mAllPermissionDonePermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                    boolean allPermissionClear=true;
                    List<String> blockPermissionCheck=new ArrayList<>();
                    for (String key : result.keySet())
                    {
                        if (!(result.get(key)))
                        {
                            allPermissionClear=false;
                            blockPermissionCheck.add(Helper.getPermissionStatus(PermissionA.this,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked")) {
                        binding.tabAllowAccess.setAlpha(0.4f);

                    } else if (allPermissionClear) {
                        binding.tabAllowAccess.setAlpha(1f);
                    }
                }
            });


    private void initControl() {
        cd = new ConnectionDetector(binding.getRoot().getContext());
        retrofitApiClient = RetrofitService.getRetrofit();
        takeAllPermission=new PermissionUtils(PermissionA.this, mAllPermissionDonePermissionResult);
//        takeRecordPermission=new PermissionUtils(PermissionA.this, mRecordPermissionResult);
        takeCallPermission=new PermissionUtils(PermissionA.this, mPhoneStatePermissionResult);
        takeStoragePermission=new PermissionUtils(PermissionA.this, mStoragePermissionResult);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkRecordPermissions();
        checkPhoneStatePermissions();
        checkStoragePermissions();
        setupButtonStatus();
    }

    private void setupButtonStatus() {
        if (takeAllPermission.isStoragePhoneStateRecordingPermissionGranted()) {
            binding.tabAllowAccess.setAlpha(1f);
        } else {
            binding.tabAllowAccess.setAlpha(0.4f);
        }
    }

    private void checkStoragePermissions() {
        if (takeStoragePermission.isStoragePermissionGranted()) {
            activeStoragePermission();
        } else {
            deactiveStoragePermission();
        }
    }

    private void activeStoragePermission() {
        binding.ivStoragePermissionSelection.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_ractengle_selected));

    }
    private void deactiveStoragePermission() {
        binding.ivStoragePermissionSelection.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(),R.drawable.ic_ractengle_unselected));

    }

    private ActivityResultLauncher<String[]> mStoragePermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                    boolean allPermissionClear=true;
                    List<String> blockPermissionCheck=new ArrayList<>();
                    for (String key : result.keySet())
                    {
                        if (!(result.get(key)))
                        {
                            allPermissionClear=false;
                            blockPermissionCheck.add(Helper.getPermissionStatus(PermissionA.this,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked")) {
                        Helper.showPermissionSetting(
                                binding.getRoot().getContext(),
                                binding.getRoot().getContext().getString(R.string.storage_permission_description));
                    } else if (allPermissionClear) {
                        activeStoragePermission();
                        setupButtonStatus();
                    }
                }
            });



    private void checkPhoneStatePermissions() {
        if (takeCallPermission.isPhoneCallStatePermissionGranted()) {
            activePhoneStatePermission();
        } else {
            deactivePhoneStatePermission();
        }
    }

    private void activePhoneStatePermission() {
        binding.ivCallPermissionSelection.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_ractengle_selected));

    }
    private void deactivePhoneStatePermission() {
        binding.ivCallPermissionSelection.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(),R.drawable.ic_ractengle_unselected));
    }

    private ActivityResultLauncher<String[]> mPhoneStatePermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                    boolean allPermissionClear=true;
                    List<String> blockPermissionCheck=new ArrayList<>();
                    for (String key : result.keySet())
                    {
                        if (!(result.get(key)))
                        {
                            allPermissionClear=false;
                            blockPermissionCheck.add(Helper.getPermissionStatus(PermissionA.this,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked")) {
                        Helper.showPermissionSetting(
                                binding.getRoot().getContext(),
                                binding.getRoot().getContext().getString(R.string.call_permission_description));
                    } else if (allPermissionClear) {
                        activePhoneStatePermission();
                        setupButtonStatus();
                    }
                }
            });



    private void checkRecordPermissions() {
        if (takeRecordPermission.isRecordingPermissionGranted()) {
            activeRecordPermission();
        } else {
            deactiveRecordPermission();
        }
    }

    private void activeRecordPermission() {
        binding.ivMiceSelection.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_ractengle_selected));

    }
    private void deactiveRecordPermission() {
        binding.ivMiceSelection.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(),R.drawable.ic_ractengle_unselected));
    }
//    private ActivityResultLauncher<String[]> mRecordPermissionResult = registerForActivityResult(
//            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
//                @RequiresApi(api = Build.VERSION_CODES.M)
//                @Override
//                public void onActivityResult(Map<String, Boolean> result) {
//
//                    boolean allPermissionClear=true;
//                    List<String> blockPermissionCheck=new ArrayList<>();
//                    for (String key : result.keySet())
//                    {
//                        if (!(result.get(key)))
//                        {
//                            allPermissionClear=false;
//                            blockPermissionCheck.add(Helper.getPermissionStatus(PermissionA.this,key));
//                        }
//                    }
//                    if (blockPermissionCheck.contains("blocked")) {
//                        Helper.showPermissionSetting(
//                                binding.getRoot().getContext(),
//                                binding.getRoot().getContext().getString(R.string.record_permission_description));
//                    } else if (allPermissionClear) {
//                        activeRecordPermission();
//                        setupButtonStatus();
//                    }
//                }
//            });

}
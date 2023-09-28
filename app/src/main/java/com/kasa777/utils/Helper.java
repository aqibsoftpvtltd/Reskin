package com.kasa777.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.kasa777.R;
import com.kasa777.constant.Constant;
import com.kasa777.interfaces.FragmentCallBack;
import com.kasa777.modal.dashboard_gamelist.Result;
import com.kasa777.modal.history_data_modal.starlineresulthistory.Datum;
import com.kasa777.ui.activity.SplashActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Helper {
    Context context;

    public Helper(Context context) {
        this.context = context;
    }

    @SuppressLint("HardwareIds")
    public String getDeviceId() {
        return android.provider.Settings.Secure.getString(
                context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    public static boolean checkSmsPermision(final Context context) {
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
            return true;
        }
        Activity activity = (Activity) context;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.READ_PHONE_STATE")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle((CharSequence) "Premission Required");
            builder.setMessage((CharSequence) "Permisssion Required to Access your device Hardware to work Perfectly ");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_PHONE_STATE"}, 128);

                }
            });
            builder.create().show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_PHONE_STATE"}, 128);
        }
        return false;
    }
    @SuppressLint("MissingPermission")
    public String getDeviceId1(Activity context) {
        String device_id = "";
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return macAddress;
    }

    public String getDeviceIdold() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        String device_id = "";
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            device_id = "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            AppPreference.setStringPreference(
                    context,
                    Constant.DEVICE_ID,
                    device_id
            );
            // Go ahead and return the serial for api => 9
            return "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }
        Log.e("device_id:", "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString());
        device_id = "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        AppPreference.setStringPreference(
                context,
                Constant.DEVICE_ID,
                device_id
        );
        return "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public List<Result> getSortedListDashboard(List<Result> arrayList) {
        Collections.sort(arrayList, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    int  r= dateFormat.parse(o1.openBidTime).compareTo(dateFormat.parse(o2.openBidTime));

                    return r;
                } catch (ParseException e) {
                    return 0;

                }
            }
        });
        return arrayList;
    }

    public List<Result> getSortedListDashboard2(List<Result> arrayList) {
        Collections.sort(arrayList, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

                    int r =  o1.isClosed.compareTo(o2.isClosed);
//                    if(r == 0){
////                        r= dateFormat.parse(o1.openBidTime).compareTo(dateFormat.parse(o2.openBidTime));
//                    }
                    return r;
                } catch (Exception e) {
                    return 0;

                }
            }
        });
        return arrayList;
    }

    public List<com.kasa777.ui.fragment.startline_game_fragment.modal.Result> getSortedListStarline(List<com.kasa777.ui.fragment.startline_game_fragment.modal.Result> arrayList) {
        Collections.sort(arrayList, new Comparator<com.kasa777.ui.fragment.startline_game_fragment.modal.Result>() {
            @Override
            public int compare(com.kasa777.ui.fragment.startline_game_fragment.modal.Result o1, com.kasa777.ui.fragment.startline_game_fragment.modal.Result o2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    return dateFormat.parse(o1.openBidTime).compareTo(dateFormat.parse(o2.openBidTime));
                } catch (ParseException e) {
                    return 0;

                }
            }
        });
        return arrayList;
    }

    public List<com.kasa777.ui.fragment.jackpot_fragments.modal.Result> getSortedListJackpot(List<com.kasa777.ui.fragment.jackpot_fragments.modal.Result> arrayList) {
        Collections.sort(arrayList, new Comparator<com.kasa777.ui.fragment.jackpot_fragments.modal.Result>() {
            @Override
            public int compare(com.kasa777.ui.fragment.jackpot_fragments.modal.Result o1, com.kasa777.ui.fragment.jackpot_fragments.modal.Result o2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    return dateFormat.parse(o1.openBidTime).compareTo(dateFormat.parse(o2.openBidTime));
                } catch (ParseException e) {
                    return 0;

                }
            }
        });
        return arrayList;
    }

    public List<Datum> getSortedListStarlineResult(List<Datum> arrayList) {
        Collections.sort(arrayList, new Comparator<Datum>() {
            @Override
            public int compare(Datum o1, Datum o2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    return dateFormat.parse(o1.getProviderName()).compareTo(dateFormat.parse(o2.getProviderName()));
                } catch (ParseException e) {
                    return 0;

                }
            }
        });
        return arrayList;
    }

    public List<com.kasa777.modal.history_data_modal.jackpotresulthistory.Datum> getSortedListJackpotResult(List<com.kasa777.modal.history_data_modal.jackpotresulthistory.Datum> arrayList) {
        Collections.sort(arrayList, new Comparator<com.kasa777.modal.history_data_modal.jackpotresulthistory.Datum>() {
            @Override
            public int compare(com.kasa777.modal.history_data_modal.jackpotresulthistory.Datum o1, com.kasa777.modal.history_data_modal.jackpotresulthistory.Datum o2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    return dateFormat.parse(o1.getProviderName()).compareTo(dateFormat.parse(o2.getProviderName()));
                } catch (ParseException e) {
                    return 0;

                }
            }
        });
        return arrayList;
    }


    public void showNotification(Context context) {
        int currentTimeMillis = (int) System.currentTimeMillis();
        String str1 = "1";
        String str2 = "2";
        String str3 = "3";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_notification_layout);
        remoteViews.setTextViewText(R.id.title, str1);
        remoteViews.setTextViewText(R.id.text, str2);
        remoteViews.setTextViewText(R.id.time, format);
        if (Build.VERSION.SDK_INT >= 26) {
            if (notificationManager.getNotificationChannel(str1) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(str1, str2, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription(str3);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(notificationChannel);
            }
            Notification.Builder builder = new Notification.Builder(context, str1);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("flager", "notification");
            //intent.setFlags(603979776);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_SINGLE_TOP);
            builder.setContentTitle(str2).setSmallIcon(R.drawable.app_logo).setContentText(context.getString(R.string.app_name)).setDefaults(-1).setContentIntent(PendingIntent.getActivity(context, 0, intent, 0)).setContent(remoteViews).setTicker(str2).setVibrate(new long[]{100, 200, 300});
            Notification build = builder.build();
            build.defaults |= 1;
            notificationManager.notify(currentTimeMillis, build);
            return;
        }

        Uri defaultUri = RingtoneManager.getDefaultUri(2);
        Intent intent2 = new Intent(context, SplashActivity.class);
        intent2.putExtra("flager", "notification");
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Notification build2 = new Notification.Builder(context).setContentTitle(str1).setContentText(str2).setSmallIcon(R.drawable.ic_ks_logout).setContentIntent(PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)).setContent(remoteViews).setSound(defaultUri).setDefaults(-1).setPriority(Notification.PRIORITY_MAX).build();
        build2.flags |= 16;
        build2.defaults |= 1;
        build2.defaults |= 2;
        assert notificationManager != null;
        notificationManager.notify(currentTimeMillis, build2);
    }


    public void payWithGooglePay(Context context, String Amount) {
        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;
        if(isPackageExisted(context,GOOGLE_PAY_PACKAGE_NAME))
        {
            Uri uri = new Uri.Builder()
                    .scheme("upi")
                    .authority("pay")
                    .appendQueryParameter("pa", "shubh51512-1@okhdfcbank")
                    .appendQueryParameter("pn", "Shubham")
                    .appendQueryParameter("mc", "1234")
                    .appendQueryParameter("tr", "123456789")
                    .appendQueryParameter("tn", "test transaction note")
                    .appendQueryParameter("am", Amount)
                    .appendQueryParameter("cu", "INR")
                    .appendQueryParameter("url", "https://test.merchant.website")
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            ((Activity) context).startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        }else {
            Toast.makeText(context, "Please Install Google Pay in your device!", Toast.LENGTH_SHORT).show();
        }



    }

    public boolean isPackageExisted(Context context, String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    public static String getAppFolder(Context context) {
        try {
            return context.getExternalFilesDir(null).getAbsolutePath();
        }
        catch (Exception e)
        {
            return new File(context.getFilesDir() + "/data/files/").getAbsolutePath();
        }
    }


    public static DraweeController frescoImageLoad(int image, SimpleDraweeView draweeView)
    {
        ImageRequest request;
        request = ImageRequestBuilder.newBuilderWithResourceId(image)
                .build();
        DraweeController controller= Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .build();
        return controller;
    }

    public static DraweeController frescoImageLoad(String url,int placeholder, SimpleDraweeView draweeView)
    {
        ImageRequest request;
        request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .build();
        DraweeController controller;
        draweeView.getHierarchy().setPlaceholderImage(placeholder);
        draweeView.getHierarchy().setFailureImage(placeholder);

        controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .build();

        return controller;
    }

    public static SpannableString formatTimeString(String timeString) {
        // Split the time string into hours and period
        String[] parts = timeString.split(" ");
        String time = parts[0];
        String period = parts[1];

        // Create a SpannableString to hold the formatted text
        SpannableString formattedString = new SpannableString(timeString);

        // Set the color of the time (hours) to black
        int timeColor = 0xFF000000; // Black color
        int timeStartIndex = 0;
        int timeEndIndex = time.length();
        formattedString.setSpan(new ForegroundColorSpan(timeColor), timeStartIndex, timeEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the color of the period (am/pm) to the specified color ('165FB6')
        int periodColor = 0xFF165FB6; // '165FB6' color
        int periodStartIndex = time.length() + 1; // Add 1 to account for the space character
        int periodEndIndex = timeString.length();
        formattedString.setSpan(new ForegroundColorSpan(periodColor), periodStartIndex, periodEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Add a LineHeightSpan to create space between lines
        int lineHeight = 10; // Adjust this value as per your requirement
        formattedString.setSpan(new LineHeightSpan() {
            @Override
            public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm) {
                fm.descent += lineHeight;
                fm.bottom += lineHeight;
            }
        }, timeStartIndex, periodEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return formattedString;
    }

    public static String getSuffix(String value) {
        try {

            if (value != null && (!value.equals("") && !value.equalsIgnoreCase("null"))) {
                long count = Long.parseLong(value);
                if (count < 1000)
                    return "" + count;
                int exp = (int) (Math.log(count) / Math.log(1000));
                return String.format(Locale.ENGLISH,"%.1f %c",
                        count / Math.pow(1000, exp),
                        "kMBTPE".charAt(exp - 1));
            } else {
                return "0";
            }
        } catch (Exception e) {
            return value;
        }

    }

    public static String getPermissionStatus(Activity activity, String androidPermissionName) {
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)){
                return "blocked";
            }
            return "denied";
        }
        return "granted";
    }

    public static void showDoubleButtonAlert(Context context, String title, String message, String negTitle, String posTitle,boolean isCancelable, FragmentCallBack callBack)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(isCancelable);
        dialog.setContentView(R.layout.show_double_button_new_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView tvtitle,tvMessage,tvPositive,tvNegative;
        final RelativeLayout tabNegative,tabPositive;
        tvtitle=dialog.findViewById(R.id.tvtitle);
        tvMessage=dialog.findViewById(R.id.tvMessage);
        tvNegative=dialog.findViewById(R.id.tvNegative);
        tvPositive=dialog.findViewById(R.id.tvPositive);
        tabNegative=dialog.findViewById(R.id.tabNegative);
        tabPositive=dialog.findViewById(R.id.tabPositive);


        tvtitle.setText(title);
        tvMessage.setText(message);
        tvNegative.setText(negTitle);
        tvPositive.setText(posTitle);

        tabNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle bundle=new Bundle();
                bundle.putBoolean("isShow",false);
                callBack.onResponce(bundle);
            }
        });
        tabPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle bundle=new Bundle();
                bundle.putBoolean("isShow",true);
                callBack.onResponce(bundle);
            }
        });
        dialog.show();
    }


    //show permission setting screen
    public static void showPermissionSetting(Context context,String message) {
        showDoubleButtonAlert(context, context.getString(R.string.permission_alert),message,
                context.getString(R.string.cancel_), context.getString(R.string.settings), false, new FragmentCallBack() {
                    @Override
                    public void onResponce(Bundle bundle) {
                        if (bundle.getBoolean("isShow",false))
                        {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                        }
                    }
                });
    }

}

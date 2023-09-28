package com.kasa777.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.kasa777.modal.dashboard_gamelist.Result;
import com.google.android.material.snackbar.Snackbar;
import com.kasa777.R;
import com.kasa777.constant.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Alerts {

    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showSnack(View container, String msg) {
        Snackbar.make(container, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void withFeedback(View container, String msg, String btnTitle, View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(btnTitle, onClickListener);
        snackbar.show();
    }


    public static void SessionLogout(Context context, Activity activity) {

        Log.e("SessionLogout:", context.getClass().getName().toString() + " " + activity.getClass().getName());
        if (dialog != null && dialog.isShowing()) {

            dialog.dismiss();
        }
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_session_error);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {

                    dialog.dismiss();
                }
                logOutUser(context, activity);

            }
        });


        dialog.show();


    }


    private static boolean logOutUser(Context context, Activity activity) {
        boolean isAvailable = true;
        long loginTime = AppPreference.getLongPreference(context, Constant.USER_LOGIN_TIME);
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - loginTime;
        isAvailable = false;

        LogoutUser logoutUser = new LogoutUser(context, activity);
        logoutUser.logout();

        return isAvailable;
    }

    public static Dialog dialog;

    public static Dialog getDialog() {
        return dialog;
    }

    public static void AlertDialog(Context context, String message, View.OnClickListener onClickListener) {
        if (dialog != null) {
            dialog.dismiss();
        }

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);

        txtMessage.setText(message + "");

        btn_ok.setOnClickListener(onClickListener);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public static void AlertDialogDate(Context context, String message, View.OnClickListener onClickListener) {
        if (dialog != null) {
            dialog.dismiss();
        }

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_game_date);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        final RelativeLayout tabNegative,tabPositive;
        tabNegative=dialog.findViewById(R.id.tabNegative);
        tabPositive=dialog.findViewById(R.id.tabPositive);

        txtMessage.setText(message + "");

        tabNegative.setOnClickListener(onClickListener);

        tabPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }


    public static void AlertDialogWarning(Context context, String message) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        txtMessage.setText(message + "");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();


    }

    public static void AlertDialogSuccess(Context context, String message) {

        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_success
        );
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        txtMessage.setText(message + "");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public static void AlertDialogSuccessWithdraw(Context context, String message) {

        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_success
        );
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        txtMessage.setText(message + "");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();


    }

    public static void AlertDialogSuccessAutoClose(Context context, Activity activity, String message) {

        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_success
        );
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        txtMessage.setText(message + "");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    public static void AlertDialogFailAutoClose(Context context, Activity activity, String message) {

        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_yes_no
        );
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        txtMessage.setText(message + "");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    public static void AlertDialogUpdateApp(Context context, Activity activity, String message, String applink, Boolean forceCloseStatus, View.OnClickListener onClickListener) {

        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_update
        );
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtUpdate = (TextView) dialog.findViewById(R.id.txtUpdate);
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
        Button btn_continue = (Button) dialog.findViewById(R.id.btn_continue);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        txtMessage.setText(message + "");
        if (!forceCloseStatus) {
            iv_close.setVisibility(View.VISIBLE);
        } else {
            iv_close.setVisibility(View.GONE);
        }

        iv_close.setOnClickListener(onClickListener);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUpdate.setVisibility(View.VISIBLE);
                btn_ok.setText("Downloading...");
                btn_ok.setEnabled(false);
                new DownloadFileFromURL(context, activity, btn_ok, progressBar, btn_continue, txtUpdate, dialog).execute(applink);

            }
        });


        dialog.show();


    }


    public static void serverError(Context context, String error) {

        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_server_error);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);

        txtMessage.setText(error);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public static void internetError(Context context) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_internet_error);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }


    public static class DownloadFileFromURL extends AsyncTask<String, Integer, Boolean> {
        Button btn_ok;
        Button btn_continue;
        ProgressBar progressBar;
        Context context;
        Activity activity;
        TextView txtUpdate;
        Dialog dialog;

        public DownloadFileFromURL(Context context, Activity activity, Button btn_ok, ProgressBar progressBar, Button btn_continue, TextView txtUpdate, Dialog dialog) {
            this.btn_ok = btn_ok;
            this.progressBar = progressBar;
            this.btn_continue = btn_continue;
            this.context = context;
            this.activity = activity;
            this.txtUpdate = txtUpdate;
            this.dialog = dialog;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected Boolean doInBackground(String... f_url) {
            Boolean flag = false;
            try {
                URL url = new URL(f_url[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(false);
                c.connect();
                String PATH = Environment.getExternalStorageDirectory() + "/Download/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, "brand_name.apk");

                if (outputFile.exists()) {
                    outputFile.delete();
                }


                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();

                int total_size = c.getContentLength();//size of apk

                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded += len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }
                fos.close();
                is.close();
                OpenNewVersion(context, activity, PATH);
                flag = true;
            } catch (MalformedURLException e) {
                Log.e(TAG, "Update Error: " + e.getMessage());
                flag = false;
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return flag;

        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(Integer... progress) {
            // setting progress percentage
            progressBar.setProgress(progress[0]);

            txtUpdate.setText(progress[0] + " %");

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(Boolean file_url) {
            // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);
            btn_ok.setEnabled(true);
            // Displaying downloaded image into image view
            // Reading image path from sdcard
//            String file = Environment.getExternalStorageDirectory().toString() + "/brand_name.apk";
//            Log.e("file", file);
            if (dialog != null) {
                dialog.dismiss();
            }
            if (file_url) {

                Toast.makeText(context, "Update Done",
                        Toast.LENGTH_SHORT).show();
//                UnInstallApplication(activity,"com.brand_name");
            } else {

                Toast.makeText(context, "Error: Try Again",
                        Toast.LENGTH_SHORT).show();

            }


        }

    }


    private static void OpenNewVersion(Context context, Activity activity, String location) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(getUriFromFile(context, location),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
        activity.finish();

    }

    private static Uri getUriFromFile(Context context, String location) {

        if (Build.VERSION.SDK_INT < 24) {
            return Uri.fromFile(new File(location + "brand_name.apk"));
        } else {
            return FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",
                    new File(location + "brand_name.apk"));
        }
    }
    public static void UnInstallApplication(Context context, String packageName)// Specific package Name Uninstall.
    {
        Uri packageURI = Uri.parse("package:"+packageName);
//        Uri packageURI = Uri.parse(packageName.toString());
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }
    public static void infoDialog(Context context, Result result) {

        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_info);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView tv_openresulttime = (TextView) dialog.findViewById(R.id.tv_openresulttime);
        TextView tv_closeresulttime = (TextView) dialog.findViewById(R.id.tv_closeresulttime);
        TextView tv_gamename = (TextView) dialog.findViewById(R.id.tv_gamename);
        TextView tv_oblt = (TextView) dialog.findViewById(R.id.tv_oblt);
        TextView tv_cblt = (TextView) dialog.findViewById(R.id.tv_cblt);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);


        tv_gamename.setText(result.providerName);
        tv_openresulttime.setText(result.OpenBidResultTime);
        tv_closeresulttime.setText(result.CloseBidResultTime);
        tv_oblt.setText(result.openBidTime);
        tv_cblt.setText(result.closeBidTime);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }


    public static void AlertDialogDeviceUpdate(Context context, String message, View.OnClickListener onClickListener) {
        if (dialog != null) {
            dialog.dismiss();
        }

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_device_update);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);

        txtMessage.setText(message + "");

        btn_ok.setOnClickListener(onClickListener);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }


    public static void AlertDialogUPIApp(Context context, String message) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_device_upi);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView txtMessage = (TextView) dialog.findViewById(R.id.tv_message);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

        ImageView paytm = (ImageView) dialog.findViewById(R.id.paytm);
        Button gpay = (Button) dialog.findViewById(R.id.gpay);

        txtMessage.setText(message + "");


        paytm.setOnClickListener(v -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + "net.one97.paytm"));
            context.startActivity(webIntent);
        });
        gpay.setOnClickListener(v -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + "com.google.android.apps.nbu.paisa.user"));
            context.startActivity(webIntent);
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();


    }


}
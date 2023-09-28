package com.kasa777.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kasa777.R;
import com.kasa777.constant.Constant;
import com.kasa777.ui.activity.SplashActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    public Context mContext;

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }


    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "Refreshed token: " + s);
        Log.e(TAG, "Refreshed token: " + s);
        AppPreference.setStringPreference(this, Constant.FIREBASE_TOKEN, s);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        if(notification==null){
//            return;
//        }
        Log.d("FIREBASE_MESSAGE", "onMessageReceived: " + remoteMessage.getData().get("body"));
        Log.d("FIREBASE_MESSAGE", "onMessageReceived: " + remoteMessage.getData().get("title"));
        Log.d("FIREBASE_MESSAGE", "RemoteMessage: " + remoteMessage.getData());
            String title = remoteMessage.getData().get("body");
            String body = remoteMessage.getData().get("title");

        Map data = remoteMessage.getData();
        try {
            String type = (String) data.get("type");
//            Drawable drawable= ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
//            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            if(type==null){
                sendNewNotification(title, body);
            }
           else if (type.equalsIgnoreCase("otp")) {
                Intent broadcast1 = new Intent("Notification");
                broadcast1.putExtra("TYPE", "otp");
                broadcaster.sendBroadcast(broadcast1);
                sendOTPNotification(title, body);

            }
            else if (type.equalsIgnoreCase("Wallet")) {
                Intent broadcast1 = new Intent("Notification");
                broadcast1.putExtra("TYPE", "Wallet");
                broadcaster.sendBroadcast(broadcast1);
                sendWalletNotification(title, body);

            } else if (type.equalsIgnoreCase("Notification")) {
                Intent broadcast2 = new Intent("Notification");
                broadcast2.putExtra("TYPE", "Notification");
                broadcaster.sendBroadcast(broadcast2);
                sendNewNotification(title, body);

            } else if (type.equalsIgnoreCase("Result")) {
                sendCustomNotification(title, body);

            } else if (type.equalsIgnoreCase("broadcast")) {
                Intent broadcast3 = new Intent("Notification");
                broadcast3.putExtra("TYPE", "broadcast");
                broadcaster.sendBroadcast(broadcast3);
                sendNewNotification(title, body);
            } else {

                sendNewNotification(title, body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendWalletNotification(String title, String body) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "1234567890";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
//                        .setLargeIcon(bitmap)
//                        .setContentTitle(body)
                        .setContentTitle(body)
                        .setContentText(title)

                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.new_colorAccent))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(title))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int) System.currentTimeMillis() /* ID of notification */, notificationBuilder.build());
    }

    private void sendOTPNotification(String title, String body) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "1234567890";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
//                        .setLargeIcon(bitmap)
//                        .setContentTitle(body)
                        .setContentTitle(body)
                        .setContentText(title)
.setPriority(NotificationCompat.PRIORITY_MAX)
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.new_colorAccent))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(title))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNewNotification(String title, String body) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "1234567890";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
//                        .setLargeIcon(bitmap)
                        .setContentTitle(body)
                        .setContentText(title)

                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.new_colorAccent))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(title))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }


    private void sendCustomNotification(String title, String body) {
        String format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Calendar.getInstance().getTime());

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.my_notification_layout);

        contentView.setTextViewText(R.id.title, body);
        contentView.setTextViewText(R.id.text, title);
        contentView.setTextViewText(R.id.time, format);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "1234567890";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
                        .setContentTitle(body)
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.new_colorAccent))
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(title))
                        .setCustomContentView(contentView)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(this, channelId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_SINGLE_TOP);
            builder.setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle(body)
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.new_colorAccent))
//                    .setStyle(new Notification.BigTextStyle().bigText(title))
//                    .setStyle(new Notification.BigTextStyle().bigText(body))
                    .setContentIntent(pendingIntent)
                    .setCustomContentView(contentView);
            Notification build = builder.build();
            notificationManager.notify((int) System.currentTimeMillis(), build);
            return;
        }

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }


}
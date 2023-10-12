package com.kasa777.chat;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kasa777.R;
import com.kasa777.chat.model.MessageModal;
import com.kasa777.chat.ui.ChatBoardActivity;
import com.kasa777.chat.ui.VideoPlayerActivity;
import com.kasa777.chat.ui.ViewImagesActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageModalAdapter_old extends RecyclerView.Adapter<MessageModalAdapter_old.ChatViewHolder> implements Handler.Callback {

    private Context mContext;
    private List<MessageModal> chatList;

    private int holderPosition;

    private static final int MSG_UPDATE_SEEK_BAR = 1845;
    private MediaPlayer mediaPlayer;
    private Handler uiUpdateHandler;
    private int playingPosition;
    private ChatViewHolder playingHolder;

    private ChatBoardActivity activity;
    /*type
    1 text
    2 image
    3 image+text
    4 audio*/


    public MessageModalAdapter_old(Context mContext, ChatBoardActivity activity, ArrayList<MessageModal> chatList) {
        this.mContext = mContext;
        this.activity = activity;
        this.chatList = chatList;
        this.playingPosition = -1;

        uiUpdateHandler = new Handler(this);
    }


    @Override
    public ChatViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_chat_message, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NotNull ChatViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holderPosition = position;
        if (position == playingPosition) {
            playingHolder = holder;
            // this view holder corresponds to the currently playing audio cell
            // update its view to show playing progress
            updatePlayingView();
        } else {
            playingPosition = -1;
            // and this one corresponds to non playing
            updateNonPlayingView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewRecycled(@NotNull ChatViewHolder holder) {
        super.onViewRecycled(holder);
        if (playingPosition == holderPosition) {
            // view holder displaying playing audio cell is being recycled
            // change its state to non-playing
            updateNonPlayingView(playingHolder, holderPosition);
            playingHolder = null;
        }
    }

    /**
     * Changes the view to non playing state
     * - icon is changed to playnn1 arrow
     * - seek bar disabled
     * - remove seek bar updater, if needed
     *
     * @param holder ChatViewHolder whose state is to be chagned to non playing
     */
    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateNonPlayingView(ChatViewHolder holder, int hp) {
        //int pstn = holder.getAdapterPosition();
        if (holder == playingHolder) {
            uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
        }
    /*type
    1 text
    2 image
    3 image+text
    4 audio*/
        try {
            if (chatList.get(hp).messType == 1) {

                holder.llContainerOutgoingMessages.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        final CharSequence[] items = {"Copy text"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        builder.setTitle("Select Option : ");
                        builder.setItems(items, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int item) {

                                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setText(decodeFromB64(chatList.get(hp).androidMessage));
                                Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();


                            }

                        });

                        AlertDialog alert = builder.create();

                        alert.show();
                        return true;
                    }
                });

                holder.tvMessageTime.setText(getChatTime(chatList.get(hp).dateTime));
                holder.llContainerOutgoingMessages.setVisibility(View.VISIBLE);
                holder.llContainerIncomingMessages.setVisibility(View.GONE);

                if (chatList.get(hp).messStatus == 2) {
                    holder.ivMessageStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icf_double_tick_indicator_blue));
                } else if (chatList.get(hp).messStatus == 3) {

                } else {
                    holder.ivMessageStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icf_double_tick_indicator));
                }

                if (chatList.get(hp).messStatus == 3) {

                } else {
                    if (chatList.get(hp).msgContentType == 2 || chatList.get(hp).msgContentType == 3) {
                        holder.rlImageContainer.setVisibility(View.VISIBLE);
                        holder.ivChatFile.setVisibility(View.VISIBLE);
                        holder.rlMediaView.setVisibility(View.GONE);
                        holder.tvMessage.setVisibility(View.GONE);
                        holder.tvMediaProgressTime.setVisibility(View.GONE);
                        if (chatList.get(hp).androidMessage == null || chatList.get(hp).androidMessage.isEmpty()) {
                            holder.tvImgMessage.setVisibility(View.GONE);
                        } else {
                            holder.tvImgMessage.setVisibility(View.VISIBLE);
                            holder.tvImgMessage.setText(decodeFromB64(chatList.get(hp).androidMessage));
                            //                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //                            holder.tvImgMessage.setText(Html.fromHtml(decodeFromB64(chatList.get(hp).androidMessage), Html.FROM_HTML_MODE_COMPACT));
                            //                        } else {
                            //                            holder.tvImgMessage.setText(Html.fromHtml(decodeFromB64(chatList.get(hp).androidMessage)));
                            //                        }

                        }
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                        if (!TextUtils.isEmpty(chatList.get(hp).images)) {
                            Glide.with(mContext)
                                    .load(ChatConstants.mediaBaseUrl + chatList.get(hp).images)

                                    .apply(requestOptions)
                                    //                                .placeholder(R.drawable.image_placeholder)
                                    .into(holder.ivChatFile);
                        } else {
                            Glide.with(mContext)
                                    .load(chatList.get(hp).msgFile)

                                    //                                .placeholder(R.drawable.image_placeholder)
                                    .apply(requestOptions)
                                    .into(holder.ivChatFile);
                        }
                        holder.ivChatFile.setOnClickListener(v -> {

                            if (!TextUtils.isEmpty(chatList.get(hp).images)) {
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity.getActivity(), holder.ivChatFile, holder.ivChatFile.getTransitionName());

                                mContext.startActivity(new Intent(mContext, ViewImagesActivity.class)
                                        .putExtra("IMAGE_ID", chatList.get(hp).id)
                                        .putExtra("IMAGE_PATH", ChatConstants.mediaBaseUrl + chatList.get(hp).images),options.toBundle());
                            } else {
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity.getActivity(), holder.ivChatFile, holder.ivChatFile.getTransitionName());

                                mContext.startActivity(new Intent(mContext, ViewImagesActivity.class)
                                        .putExtra("IMAGE_ID", chatList.get(hp).id)
                                        .putExtra("IMAGE_PATH", chatList.get(hp).msgFile),options.toBundle());
                            }
                        });
                    }

                    if (chatList.get(hp).msgContentType == 4) {
                        holder.rlImageContainer.setVisibility(View.GONE);
                        holder.ivChatFile.setVisibility(View.GONE);
                        holder.tvMessage.setVisibility(View.GONE);
                        holder.rlMediaView.setVisibility(View.VISIBLE);
                        holder.sbMediaProgress.setEnabled(false);
                        holder.sbMediaProgress.setProgress(0);
                        holder.tvMediaProgressTime.setVisibility(View.VISIBLE);
                        holder.tvMediaProgressTime.setText(convertSecondsToHMmSs(chatList.get(hp).messAudioDuration));
                        holder.ivPlayPauseMedia.setImageResource(R.drawable.icf_play_button);
                    }

                    if (chatList.get(hp).msgContentType == 1) {
                        holder.rlImageContainer.setVisibility(View.GONE);
                        holder.ivChatFile.setVisibility(View.GONE);
                        holder.rlMediaView.setVisibility(View.GONE);
                        holder.tvMessage.setVisibility(View.VISIBLE);
                        holder.tvMediaProgressTime.setVisibility(View.GONE);
                        holder.tvMessage.setText(decodeFromB64(chatList.get(hp).androidMessage));

                        //                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //                        holder.tvMessage.setText(Html.fromHtml(decodeFromB64(chatList.get(hp).androidMessage), Html.FROM_HTML_MODE_COMPACT));
                        //                    } else {
                        //                        holder.tvMessage.setText(Html.fromHtml(decodeFromB64(chatList.get(hp).androidMessage)));
                        //                    }
                    }

                    if (!TextUtils.isEmpty(chatList.get(hp).replyMessage) || !TextUtils.isEmpty(chatList.get(hp).replyName)) {
                        holder.reply_layout.setVisibility(View.VISIBLE);
                        holder.reply_layout_in.setVisibility(View.GONE);
                        holder.txtQuotedMsg.setText(chatList.get(hp).replyMessage);
                        holder.txtQuotedUsername.setText(chatList.get(hp).replyName);
                    } else {
                        holder.reply_layout.setVisibility(View.GONE);
                        holder.reply_layout_in.setVisibility(View.GONE);
                    }
                }
            } else  if (chatList.get(hp).messType == 2 || chatList.get(hp).messType == 5){
                holder.tvInMessageTime.setText(getChatTime(chatList.get(hp).dateTime));
                holder.llContainerOutgoingMessages.setVisibility(View.GONE);
                holder.llContainerIncomingMessages.setVisibility(View.VISIBLE);

                holder.llContainerIncomingMessages.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        final CharSequence[] items = {"Copy text"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Select Option : ");
                        builder.setItems(items, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int item) {

                                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setText(decodeFromB64(chatList.get(hp).androidMessage));
                                Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();


                            }

                        });

                        AlertDialog alert = builder.create();

                        alert.show();
                        return true;
                    }
                });

                if (chatList.get(hp).messStatus == 3) {
                    holder.rlInChatImageContainer.setVisibility(View.GONE);
                    holder.rlInMediaView.setVisibility(View.GONE);
                    holder.tvInMessage.setVisibility(View.GONE);
                    holder.tvInMessageDeleted.setVisibility(View.VISIBLE);
                    holder.rlInChatVideoContainer.setVisibility(View.GONE);
                } else {
                    if (chatList.get(hp).msgContentType == 2 || chatList.get(hp).msgContentType == 3) {
                        holder.rlImageContainer.setVisibility(View.VISIBLE);
                        holder.rlInChatImageContainer.setVisibility(View.VISIBLE);
                        holder.ivInChatFile.setVisibility(View.VISIBLE);
                        holder.rlInMediaView.setVisibility(View.GONE);
                        holder.tvInMessage.setVisibility(View.GONE);
                        holder.tvInMessageDeleted.setVisibility(View.GONE);
                        holder.tvInImgMessage.setVisibility(View.GONE);
                        holder.tvInMediaProgressTime.setVisibility(View.GONE);
                        holder.rlInChatVideoContainer.setVisibility(View.GONE);
                        if (chatList.get(hp).androidMessage == null || chatList.get(hp).androidMessage.isEmpty()) {
                            holder.tvInImgMessage.setVisibility(View.GONE);
                        } else {
                            holder.tvInImgMessage.setVisibility(View.VISIBLE);
                            holder.tvInImgMessage.setText(decodeFromB64(chatList.get(hp).androidMessage));
                            //                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //                            holder.tvInImgMessage.setText(Html.fromHtml(decodeFromB64(chatList.get(hp).androidMessage), Html.FROM_HTML_MODE_COMPACT));
                            //                        } else {
                            //                            holder.tvInImgMessage.setText(Html.fromHtml(decodeFromB64(chatList.get(hp).androidMessage)));
                            //                        }
                        }
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                        Glide.with(mContext)
                                .load(ChatConstants.mediaBaseUrl + chatList.get(hp).images)

                                .apply(requestOptions)
                                //                            .placeholder(R.drawable.image_placeholder)
                                .into(holder.ivInChatFile);
                        holder.ivInChatFile.setOnClickListener(v -> {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity.getActivity(), holder.ivInChatFile, holder.ivInChatFile.getTransitionName());

                            mContext.startActivity(new Intent(mContext, ViewImagesActivity.class)
                                    .putExtra("IMAGE_ID", chatList.get(hp).id)
                                    .putExtra("IMAGE_PATH", ChatConstants.mediaBaseUrl + chatList.get(hp).images),options.toBundle());
                        });

                    }

                    if (chatList.get(hp).msgContentType == 6) {
                        holder.rlInChatImageContainer.setVisibility(View.GONE);
                        holder.ivInChatFile.setVisibility(View.GONE);
                        holder.rlImageContainer.setVisibility(View.GONE);
                        holder.tvInMessage.setVisibility(View.GONE);
                        holder.rlInChatVideoContainer.setVisibility(View.GONE);
                        holder.rlInMediaView.setVisibility(View.GONE);
                        holder.tvInMessageDeleted.setVisibility(View.GONE);
                        holder.sbInMediaProgress.setEnabled(false);
                        holder.sbInMediaProgress.setProgress(0);
                        holder.tvInMediaProgressTime.setVisibility(View.GONE);
                        holder.rlInChatVideoContainer.setVisibility(View.VISIBLE);

                        if (chatList.get(hp).androidMessage == null || chatList.get(hp).androidMessage.isEmpty()) {
                            holder.tvInVideoMessage.setVisibility(View.GONE);
                        } else {
                            holder.tvInVideoMessage.setVisibility(View.VISIBLE);
                            holder.tvInVideoMessage.setText(decodeFromB64(chatList.get(hp).androidMessage));
                        }

                        holder.rlInChatVideoContainer.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View view) {
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity.getActivity(), holder.video_placeholder, holder.video_placeholder.getTransitionName());

                                mContext.startActivity(new Intent(mContext, VideoPlayerActivity.class)
                                        .putExtra("VIDEO", chatList.get(hp).videos),options.toBundle());
                            }
                        });
                    }

                    if (chatList.get(hp).msgContentType == 4) {
                        holder.rlInChatVideoContainer.setVisibility(View.GONE);
                        holder.rlInChatImageContainer.setVisibility(View.GONE);
                        holder.ivInChatFile.setVisibility(View.GONE);
                        holder.rlImageContainer.setVisibility(View.GONE);
                        holder.tvInMessage.setVisibility(View.GONE);
                        holder.rlInMediaView.setVisibility(View.VISIBLE);
                        holder.tvInMessageDeleted.setVisibility(View.GONE);
                        holder.sbInMediaProgress.setEnabled(false);
                        holder.sbInMediaProgress.setProgress(0);
                        holder.tvInMediaProgressTime.setVisibility(View.VISIBLE);
                        holder.tvInMediaProgressTime.setText(convertSecondsToHMmSs(chatList.get(hp).messAudioDuration));
                        holder.ivInPlayPauseMedia.setImageResource(R.drawable.icf_play_button);
                    }

                    if (chatList.get(hp).msgContentType == 1) {
                        holder.rlInChatVideoContainer.setVisibility(View.GONE);
                        holder.rlInChatImageContainer.setVisibility(View.GONE);
                        holder.ivInChatFile.setVisibility(View.GONE);
                        holder.rlImageContainer.setVisibility(View.GONE);
                        holder.tvInMessageDeleted.setVisibility(View.GONE);
                        holder.rlInMediaView.setVisibility(View.GONE);
                        holder.tvInMessage.setVisibility(View.VISIBLE);
                        holder.tvInMediaProgressTime.setVisibility(View.GONE);
                        holder.tvInMessage.setText(decodeFromB64(chatList.get(hp).androidMessage));

                        //                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //                        holder.tvInMessage.setText(Html.fromHtml(decodeFromB64(chatList.get(hp).androidMessage), Html.FROM_HTML_MODE_COMPACT));
                        //                    } else {
                        //                        holder.tvInMessage.setText(Html.fromHtml(decodeFromB64(chatList.get(hp).androidMessage)));
                        //                    }
                    }

                    if (!TextUtils.isEmpty(chatList.get(hp).replyMessage) || !TextUtils.isEmpty(chatList.get(hp).replyName)) {
                        holder.reply_layout.setVisibility(View.GONE);
                        holder.reply_layout_in.setVisibility(View.VISIBLE);
                        holder.txtQuotedMsg_in.setText(chatList.get(hp).replyMessage);
                        holder.txtQuotedUsername_in.setText(chatList.get(hp).replyName);
                    } else {
                        holder.reply_layout.setVisibility(View.GONE);
                        holder.reply_layout_in.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the view to playing state
     * - icon is changed to pause
     * - seek bar enabled
     * - start seek bar updater, if needed
     */
    private void updatePlayingView() {
        Log.e( "updatePlayingView",mediaPlayer.getDuration()+"");
        try {
            playingHolder.sbMediaProgress.setMax(mediaPlayer.getDuration());
            playingHolder.sbMediaProgress.setProgress(mediaPlayer.getCurrentPosition());
            playingHolder.sbMediaProgress.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            playingHolder.sbInMediaProgress.setMax(mediaPlayer.getDuration());
            playingHolder.sbInMediaProgress.setProgress(mediaPlayer.getCurrentPosition());
            playingHolder.sbInMediaProgress.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (mediaPlayer.isPlaying()) {
            uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
            playingHolder.ivPlayPauseMedia.setImageResource(R.drawable.icf_pause);
            playingHolder.ivInPlayPauseMedia.setImageResource(R.drawable.icf_pause);
        } else {
            uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
            playingHolder.ivPlayPauseMedia.setImageResource(R.drawable.icf_play_button);
            playingHolder.ivInPlayPauseMedia.setImageResource(R.drawable.icf_play_button);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopPlayer() {
        try {
            if (mediaPlayer.isPlaying()) {
                releaseMediaPlayer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_SEEK_BAR: {
                Log.e("handleMessage:",mediaPlayer.getCurrentPosition()+"" );
                playingHolder.sbInMediaProgress.setProgress(mediaPlayer.getCurrentPosition());
                playingHolder.sbMediaProgress.setProgress(mediaPlayer.getCurrentPosition());

                uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
                return true;
            }
        }
        return false;
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        private TextView tvMessage, tvMessageTime, tvMediaProgressTime, tvInMessage,
                tvInMessageTime, tvInMediaProgressTime, tvImgMessage, tvInImgMessage,
                tvInMessageDeleted,tvInVideoMessage;
        private ImageView ivChatFile, ivPlayPauseMedia, ivInChatFile, ivInPlayPauseMedia, ivImageUpload, ivMessageStatus,video_placeholder;
        private RelativeLayout rlMediaView, rlInMediaView, rlImageNotUploaded, rlImageContainer, rlInChatImageContainer,rlInChatVideoContainer;
        private LinearLayout llContainerOutgoingMessages, llContainerIncomingMessages;
        private SeekBar sbMediaProgress, sbInMediaProgress;
        private ProgressBar progressImageUpload;

        RelativeLayout reply_layout, reply_layout_in;
        TextView txtQuotedMsg, txtQuotedMsg_in, txtQuotedUsername, txtQuotedUsername_in;

        public ChatViewHolder(View itemView) {
            super(itemView);

            llContainerOutgoingMessages = itemView.findViewById(R.id.llContainerOutgoingMessages);
            llContainerIncomingMessages = itemView.findViewById(R.id.llContainerIncomingMessages);

            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
            tvMediaProgressTime = itemView.findViewById(R.id.tvMediaProgressTime);
            tvInMessage = itemView.findViewById(R.id.tvInMessage);
            tvInMessageTime = itemView.findViewById(R.id.tvInMessageTime);
            tvInMediaProgressTime = itemView.findViewById(R.id.tvInMediaProgressTime);
            tvImgMessage = itemView.findViewById(R.id.tvImgMessage);
            tvInImgMessage = itemView.findViewById(R.id.tvInImgMessage);
            tvInVideoMessage = itemView.findViewById(R.id.tvInVideoMessage);
            tvInMessageDeleted = itemView.findViewById(R.id.tvInMessageDeleted);
            rlInChatVideoContainer = itemView.findViewById(R.id.rlInChatVideoContainer);
            video_placeholder = itemView.findViewById(R.id.video_placeholder);

            ivChatFile = itemView.findViewById(R.id.ivChatFile);
            ivPlayPauseMedia = itemView.findViewById(R.id.ivPlayPauseMedia);
            ivInChatFile = itemView.findViewById(R.id.ivInChatFile);
            ivInPlayPauseMedia = itemView.findViewById(R.id.ivInPlayPauseMedia);

            ivMessageStatus = itemView.findViewById(R.id.ivMessageStatus);

            rlMediaView = itemView.findViewById(R.id.rlMediaView);
            rlInMediaView = itemView.findViewById(R.id.rlInMediaView);

            rlImageContainer = itemView.findViewById(R.id.rlImageContainer);
            rlInChatImageContainer = itemView.findViewById(R.id.rlInChatImageContainer);



            sbMediaProgress = itemView.findViewById(R.id.sbMediaProgress);
            sbInMediaProgress = itemView.findViewById(R.id.sbInMediaProgress);


            reply_layout = itemView.findViewById(R.id.reply_layout);
            reply_layout_in = itemView.findViewById(R.id.reply_layout_in);
            txtQuotedMsg = itemView.findViewById(R.id.txtQuotedMsg);
            txtQuotedMsg_in = itemView.findViewById(R.id.txtQuotedMsg_in);
            txtQuotedUsername = itemView.findViewById(R.id.txtQuotedUsername);
            txtQuotedUsername_in = itemView.findViewById(R.id.txtQuotedUsername_in);

            ivPlayPauseMedia.setOnClickListener(this);
            ivInPlayPauseMedia.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ivPlayPauseMedia || v.getId() == R.id.ivInPlayPauseMedia) {
                if (getAdapterPosition() == playingPosition) {
                    // toggle between playnn1/pause of audio
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                    playingPosition = -1;
                } else {
                    // start another audio playback
                    playingPosition = getAdapterPosition();
                    if (mediaPlayer != null) {
                        if (null != playingHolder) {
                            updateNonPlayingView(playingHolder, playingHolder.getAdapterPosition());
                        }
                        mediaPlayer.release();
                    }
                    playingHolder = this;
                    try {
                        if (chatList.get(playingPosition).msgFile != null ) {
                            if(chatList.get(playingPosition).msgFile.isEmpty()){
                                startMediaPlayer(ChatConstants.CHAT_SERVER_URL + chatList.get(playingPosition).audios);
                            }else {
                                startMediaPlayer(chatList.get(playingPosition).msgFile);
                            }
                        } else {
                            startMediaPlayer(ChatConstants.CHAT_SERVER_URL + chatList.get(playingPosition).audios);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                updatePlayingView();
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startMediaPlayer(String mediaPath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mediaPath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp -> releaseMediaPlayer());
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                int duration = mediaPlayer.getDuration();
                if (duration <= 0) {

                }

                Log.i("time", duration + " ms " +mediaPath);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void releaseMediaPlayer() {
        if (playingHolder != null) {
            updateNonPlayingView(playingHolder, playingHolder.getAdapterPosition());
        }
        mediaPlayer.release();
        mediaPlayer = null;
        playingPosition = -1;
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        //long h = (seconds / (60 * 60)) % 24;
        return String.format("%02d:%02d", m, s);
    }

    private String decodeFromB64(String B64Msg) {
        try {
            return new String(Base64.decode(B64Msg, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getChatTime(String time) {
//        2020-01-09 03:58 pm
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date date = null;
        try {
            date = dateFormatter.parse(time);
            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
            String displayValue = timeFormatter.format(date);
            return displayValue;
        } catch (ParseException e) {

        }
        return "";

    }



}




package com.kasa777.chat.ui


import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.kasa777.R
import com.kasa777.chat.*
import com.kasa777.chat.model.GetOldMessages
import com.kasa777.chat.model.MediaFileModel
import com.kasa777.chat.model.MessageModal
import com.kasa777.chat.model.OnReceiveNewMessage
import com.kasa777.constant.Constant
import com.kasa777.modal.PaymentMode
import com.kasa777.modal.TransactionDetails
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.RetrofitServiceFileUpload
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.activity.NetBankingActivity
import com.kasa777.utils.Alerts
import com.kasa777.utils.AppPreference
import com.kasa777.utils.BaseActivity
import com.kasa777.utils.UpiBottomSheet
import com.kasa777.utils.view.AudioRecordView
import com.kasa777.utils.view.PageTransformer
import com.kbeanie.multipicker.api.CameraImagePicker
import com.kbeanie.multipicker.api.ImagePicker
import com.kbeanie.multipicker.api.Picker
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback
import com.kbeanie.multipicker.api.entity.ChosenFile
import com.kbeanie.multipicker.api.entity.ChosenImage
import com.kbeanie.multipicker.core.ImagePickerImpl
import com.shain.messenger.MessageSwipeController
import com.shain.messenger.SwipeControllerActions
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.ios.IosEmojiProvider
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.dialog_payment_mode.view.*
import kotlinx.android.synthetic.main.dialog_request_fund.view.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.btnOk
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.apache.commons.io.FileUtils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

class ChatBoardActivity : BaseActivity(), OnSocketListener {
    private lateinit var containerView: View
    private var recordingView: AudioRecordView? = null
    private lateinit var tv_add_fund: TextView
    private lateinit var tvUserStatus: TextView
    private lateinit var ll_loading1: LinearLayout
    private lateinit var iv_call: ImageView
    private lateinit var rvChatMessage: RecyclerView
    private var selectedApp: String = ""
    private var broMessId: String? = ""
    private var fundrequestamount: String? = ""
    private var adminUpiID: String = ""

    private lateinit var selectedMessageForReply: MessageModal
    private var lastmessage_id: String? = ""
    private var isAttachOptionOpen = false

    private var imagePicker: ImagePicker? = null

    private var cameraImagePicker: CameraImagePicker? = null
    private var pickerType: Int = 0
    private var pickerPath: String? = null

    private var time: Long = 0
    private var audioPath = ""

    private var myAudioRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var recordedAudioFile: String? = null
    private var emojiButton: ImageView? = null
    private var emojiPopup: EmojiPopup? = null

    private var chatMessageList: ArrayList<MessageModal> = ArrayList()
    private var recentChatMessageList: ArrayList<MessageModal> = ArrayList()
    private var chatAdapter: MessageModalAdapter? = null

    private var mSocket: Socket? = null
    private var dbHelper: DBHelper? = null

    private var userLoginId = ""
    private var userLoginName = ""
    private val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 101

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        EmojiManager.install(IosEmojiProvider())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        userLoginId =
            AppPreference.getStringPreference(this@ChatBoardActivity, Constant.USER_LOGIN_ID)
        userLoginName =
            AppPreference.getStringPreference(this@ChatBoardActivity, Constant.USER_LOGIN_USER_NAME)


        recordingView = AudioRecordView()
        // this is to make your layout the root of audio record view, root layout supposed to be empty..
        // this is to make your layout the root of audio record view, root layout supposed to be empty..
        // this is to make your layout the root of audio record view, root layout supposed to be empty..
        recordingView!!.initView(findViewById<View>(R.id.layoutMain) as FrameLayout)
        // this is to provide the container layout to the audio record view..
        // this is to provide the container layout to the audio record view..
        containerView = recordingView!!.setContainerView(R.layout.activity_chat_board)

        tv_add_fund = containerView!!.findViewById<TextView>(R.id.tv_add_fund)
        tvUserStatus = containerView!!.findViewById<TextView>(R.id.tvUserStatus)
        iv_call = containerView!!.findViewById<ImageView>(R.id.iv_call)
        rvChatMessage = containerView!!.findViewById<RecyclerView>(R.id.rvChatMessage)
        ll_loading1 = containerView!!.findViewById<LinearLayout>(R.id.ll_loading1)


        initMessageListing()
        initViews()
        tv_add_fund!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialogRequestFund()

            }
        })
        getUpiFromAdmin()

        iv_call.setOnClickListener {
            val string = AppPreference.getStringPreference(this@ChatBoardActivity, "PHONE")


            val permissionCheck: Int =
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(Manifest.permission.CALL_PHONE),
                    123
                )
            } else {
                startActivity(
                    Intent(Intent.ACTION_CALL)
                        .setData(Uri.parse("tel:$string"))
                )
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            123 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val string = AppPreference.getStringPreference(this@ChatBoardActivity, "PHONE")

                startActivity(
                    Intent(Intent.ACTION_CALL)
                        .setData(Uri.parse("tel:$string"))
                )
            } else {
                Log.d("TAG", "Call Permission Not Granted")
            }
            RECORD_AUDIO_PERMISSION_REQUEST_CODE->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The permission has been granted, you can proceed with using RECORD_AUDIO
                    // Your code for recording audio or any related functionality goes here
                } else {
                    // The permission has been denied, handle this situation (e.g., show an explanation or disable audio-related features)
                }
             else -> {
        }
        }
    }


    private fun initViews() {

        /*   llPickCamera.setOnClickListener {
               pickImageFromCamera()
               isAttachOptionOpen = false

           }

           llPickGallery.setOnClickListener {
               pickMultipleImages()
               isAttachOptionOpen = false

           }*/

//        rlNav.setOnClickListener { onBackPressed() }

        audioPath = this@ChatBoardActivity.externalCacheDir!!.absolutePath

        /****************************************
         * Manage Audio
         ***************************************/
//        chatMessageList = dbHelper!!.getAllMessageFromDB()


        initRecordView()
    }

    private fun initMessageListing() {

        dbHelper = DBHelper(this@ChatBoardActivity)
        chatMessageList = dbHelper!!.getAllMessageFromDB()
        if (chatMessageList != null)
            if (chatMessageList.size > 0) {
                for (i in 0..chatMessageList.size - 1) {
                    if (chatMessageList.get(i).messType == 5) {
                        broMessId = chatMessageList.get(i).id
                    } else {
                        lastmessage_id = chatMessageList.get(i).id
                    }
                }
            }
//

//        if (!lastmessage_id.equals(""))
        getUsersOldMessage()
//        else
//            finalizeMessageList()


    }

    private fun finalizeMessageList() {

        recordingView!!.cancelButton.setOnClickListener {
            hideReplyLayout()
        }

        val messageSwipeController = MessageSwipeController(this, object : SwipeControllerActions {
            override fun showReplyUI(position: Int) {
                showQuotedMessage(chatMessageList[position])
            }
        })

        val itemTouchHelper = ItemTouchHelper(messageSwipeController)
        itemTouchHelper.attachToRecyclerView(rvChatMessage)

        chatAdapter = MessageModalAdapter(this@ChatBoardActivity, this, chatMessageList)
        rvChatMessage.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this@ChatBoardActivity)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = false
        rvChatMessage.layoutManager = layoutManager
        rvChatMessage.adapter = chatAdapter
        chatAdapter!!.notifyDataSetChanged()

        if (chatMessageList.size > 0) {
            rvChatMessage.scrollToPosition(chatAdapter!!.itemCount - 1)
        }

        ll_loading1!!.visibility = View.GONE
    }

    private fun showQuotedMessage(message: MessageModal) {
        selectedMessageForReply = message;
        recordingView!!.messageView.requestFocus()
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.showSoftInput(
            recordingView!!.messageView,
            InputMethodManager.SHOW_IMPLICIT
        )
        recordingView!!.txtQuotedMsg.text = message.message
        recordingView!!.txtQuotedUsername.text = message.userName
        if (TextUtils.isEmpty(message.images)) {
            recordingView!!.imageQuote.visibility = View.GONE
            if (!TextUtils.isEmpty(message.msgFile)) {
                recordingView!!.imageQuote.visibility = View.VISIBLE
                Glide.with(this@ChatBoardActivity)
                    .load(message.msgFile)
                    .centerCrop()
                    .into(recordingView!!.imageQuote)
            }
        } else {
            recordingView!!.imageQuote.visibility = View.VISIBLE
            Glide.with(this@ChatBoardActivity)
                .load(ChatConstants.mediaBaseUrl + message.images)
                .centerCrop()
                .into(recordingView!!.imageQuote)
        }
        recordingView!!.reply_layout.visibility = View.VISIBLE

    }

    private fun hideReplyLayout() {
        recordingView!!.reply_layout.visibility = View.GONE
    }

    /*********************************************************
     * Used to return images from your phone's storage
     *********************************************************/
    private fun pickMultipleImages() {
        imagePicker = prepareImagePicker()
        imagePicker!!.allowMultiple()
        imagePicker!!.pickImage()
        pickerType = Picker.PICK_IMAGE_DEVICE
    }

    /*******************************************************
     * Used to return single image using your phone's camera
     *******************************************************/
    private fun pickImageFromCamera() {
        cameraImagePicker = prepareCameraImagePicker()
        pickerPath = cameraImagePicker!!.pickImage()
        pickerType = Picker.PICK_IMAGE_CAMERA
    }

    private fun prepareImagePicker(): ImagePicker {
        val imagePicker = ImagePicker(this)
        imagePicker.setImagePickerCallback(myImagePickerCallback)
        return imagePicker
    }

    private fun prepareCameraImagePicker(): CameraImagePicker {
        val imagePicker = CameraImagePicker(this)
        imagePicker.setImagePickerCallback(myImagePickerCallback)
        return imagePicker
    }

    private val myImagePickerCallback = object : ImagePickerCallback {
        override fun onImagesChosen(images: List<ChosenImage>?) {
            val array = ArrayList<ChosenImage>(images!!.size)
            array.addAll(images)
//            currentCompressedImageFile = null
            val chosenFile = images!![0] as ChosenFile
            val path = chosenFile.originalPath
            val ext = chosenFile.fileExtensionFromMimeTypeWithoutDot
            if (ext == "jpeg" || ext == "jpg" || ext == "png") {
                val intent = Intent(this@ChatBoardActivity, FullScreenImageActivity::class.java)
                intent.putExtra("FROM", "create msg")
                intent.putExtra("IMAGE_PATH", array);
                startActivityForResult(intent, 211);
            } else {
                showToast("Wrong file selected")
            }
        }

        override fun onError(message: String?) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_CAMERA || requestCode == Picker.PICK_IMAGE_DEVICE) {
                var imagePickerImpl: ImagePickerImpl? = null
                if (pickerType == Picker.PICK_IMAGE_DEVICE) {
                    if (imagePicker == null) {
                        imagePicker = prepareImagePicker()
                    }
                    imagePickerImpl = imagePicker
                } else if (pickerType == Picker.PICK_IMAGE_CAMERA) {
                    if (cameraImagePicker == null) {
                        cameraImagePicker = prepareCameraImagePicker()
                        cameraImagePicker!!.reinitialize(pickerPath)
                    }
                    imagePickerImpl = cameraImagePicker
                }
                imagePickerImpl!!.submit(data)
            } else if (requestCode == 211) {

                if (resultCode == Activity.RESULT_OK) {

                    try {
                        val typedMsg = data!!.getStringExtra("result").toString();
                        val compressedPath = data!!.getStringArrayListExtra("compressImagePath");
                        prepareImageToSend(typedMsg, compressedPath)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (resultCode == Activity.RESULT_CANCELED) {

                }

            }
        }

        when (requestCode) {
            UPI_PAYMENT -> /*if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $trxt")
                    val dataList = java.util.ArrayList<String>()
                    dataList.add(trxt)
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = java.util.ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                Log.d(
                    "UPI",
                    "onActivityResult: " + "Return data is null"
                ) //when user simply back without payment
                val dataList = java.util.ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }*/ {
                if (data != null) {
                    // Get Response from activity intent
                    val response = data.getStringExtra("response")
                    if (response == null) {
                        Alerts.AlertDialogWarning(
                            this@ChatBoardActivity,
                            "Payment cancelled by user."
                        )
                        Log.d(
                            "UPI",
                            "Response is null"
                        )
                    } else {
                        val transactionDetails =
                            getTransactionDetails(response)

                        // Transaction Completed
                        Log.d("UPI", transactionDetails.toString())
                        //Update Listener onTransactionCompleted()
//                        callbackTransactionComplete(transactionDetails)

                        //Check if success, submitted or failed
                        try {
                            if (transactionDetails!!.status!!.toLowerCase().equals("success")) {
                                if (transactionDetails.approvalRefNo != null) {
                                    autoPaymentApi(
                                        transactionDetails.approvalRefNo!!,
                                        transactionDetails.approvalRefNo!!,
                                        fundrequestamount!!, "Approved"
                                    )
                                } else {
                                    autoPaymentApi(
                                        transactionDetails.transactionRefId!!,
                                        transactionDetails.transactionRefId!!,
                                        fundrequestamount!!, "Approved"
                                    )
                                }
                            } else if (transactionDetails.status!!.toLowerCase()
                                    .equals("submitted")
                            ) {
//                                Alerts.AlertDialogWarning(
//                                    this@ChatBoardActivity,
//                                    "Transaction is Pending. Please Contact with support if payment is deducted"
//                                )
                                autoPaymentApi(
                                    transactionDetails.transactionRefId!!,
                                    transactionDetails.transactionRefId!!,
                                    fundrequestamount!!, "submitted"
                                )
                            } else if (transactionDetails.status!!.toLowerCase()
                                    .equals("pending")
                            ) {
//                                Alerts.AlertDialogWarning(
//                                    this@ChatBoardActivity,
//                                    "Transaction is Pending. Please Contact with support if payment is deducted"
//                                )
                                autoPaymentApi(
                                    transactionDetails.transactionRefId!!,
                                    transactionDetails.transactionRefId!!,
                                    fundrequestamount!!, "pending"
                                )
                            } else {
                                Alerts.AlertDialogWarning(
                                    this@ChatBoardActivity,
                                    "Transaction failed.Please try again"
                                )
                            }
                        } catch (e: java.lang.Exception) {
                            Log.e(
                                "UPI",
                                e.message.toString()
                            )
                            Alerts.AlertDialogWarning(
                                this@ChatBoardActivity,
                                "Transaction failed.Please try again"
                            )
                        }
                    }
                } else {
                    Log.e(
                        "UPI",
                        "Intent Data is null. User cancelled"
                    )
                    Alerts.AlertDialogWarning(
                        this@ChatBoardActivity,
                        "Payment cancelled by user."
                    )
                }
            }

            1000 -> {
                val response = data!!.getStringExtra("response")
                if (response.equals("success", true)) {
                    Alerts.AlertDialogWarning(
                        this@ChatBoardActivity,
                        "Transaction Success.\nPoints Added To Your Wallet"
                    )
                } else if (response.equals("failure", true)) {
                    Alerts.AlertDialogWarning(
                        this@ChatBoardActivity,
                        "Transaction failed.Please try again"
                    )
                } else {
                    Alerts.AlertDialogWarning(
                        this@ChatBoardActivity,
                        "Transaction Cancelled By User"
                    )
                }


            }
        }
    }

    private fun getQueryString(url: String): Map<String, String>? {
        val params = url.split("&").toTypedArray()
        val map: MutableMap<String, String> =
            HashMap()
        for (param in params) {
            val name = param.split("=").toTypedArray()[0]
            val value = param.split("=").toTypedArray()[1]
            map[name] = value
        }
        return map
    }

    // Make TransactionDetails object from response string
    private fun getTransactionDetails(response: String): TransactionDetails? {
        val map = getQueryString(response)
        val transactionId = map!!["txnId"]
        val responseCode = map["responseCode"]
        val approvalRefNo = map["ApprovalRefNo"]
        val status = map["Status"]
        val transactionRefId = map["txnRef"]
        return TransactionDetails(
            transactionId,
            responseCode,
            approvalRefNo,
            status,
            transactionRefId
        )
    }

    private fun prepareImageToSend(
        typedMsg: String,
        compressedPath: java.util.ArrayList<String>?
    ) {
        for (i in 0..compressedPath!!.size - 1) {/*type
   1 text
   2 image
   3 image+text
   4 audio*/
            val currentCompressedImageFile = File(compressedPath!!.get(i))
            val chatMessage = MessageModal()
            chatMessage.userId = userLoginId
            chatMessage.userName = userLoginName
            chatMessage.messType = 1
            if (i == compressedPath!!.size - 1) {
                chatMessage.msgContentType = 3
                chatMessage.androidMessage = typedMsg.encodeToB64()
                chatMessage.message = typedMsg
            } else {
                chatMessage.msgContentType = 2
                chatMessage.androidMessage = ""
                chatMessage.message = ""
            }
            chatMessage.messStatus = 1
            chatMessage.dateTimestamp = (System.currentTimeMillis() / 1000).toString()
            chatMessage.msgFile = currentCompressedImageFile!!.absolutePath
            chatMessage.dateTime = currentDateTime()

            chatMessageList.add(chatMessage)
            chatAdapter!!.notifyDataSetChanged()

            if (chatMessageList.size > 0) {
                rvChatMessage.scrollToPosition(chatAdapter!!.itemCount - 1)
            }

            uploadImageToServer(chatMessage, currentCompressedImageFile)
        }

    }

    private fun showToast(msg: String) {
        Toast.makeText(this@ChatBoardActivity, msg, Toast.LENGTH_LONG).show()
    }

    /*******************************************************
     *   Initialize Record Button
     *******************************************************/

    private fun initRecordView() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Check if the permission has been granted
            if (ContextCompat.checkSelfPermission(
                    this@ChatBoardActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // If not granted, request the permission
                ActivityCompat.requestPermissions(
                    this@ChatBoardActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    RECORD_AUDIO_PERMISSION_REQUEST_CODE
                )
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this@ChatBoardActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this@ChatBoardActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // If not granted, request the permission
                ActivityCompat.requestPermissions(
                    this@ChatBoardActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    RECORD_AUDIO_PERMISSION_REQUEST_CODE
                )
                return;
            }
        }
        recordingView!!.recordingListener = recordingViewListener

        recordingView!!.attachmentView.setOnClickListener {
            //            if (!isAttachOptionOpen) {
//                isAttachOptionOpen = true
            AttachImageDialog.ImageChooserDialog(
                this@ChatBoardActivity,
                "",
                object : AttachImageDialog.OnChooseImageSource {
                    override fun onCamera() {
                        pickImageFromCamera()
//                       isAttachOptionOpen = false
                    }

                    override fun onGAllery() {
                        pickMultipleImages()
//                       isAttachOptionOpen = false
                    }
                })
//            } else {
//                isAttachOptionOpen = false
////                cvAttachDocuments.visibility = View.GONE
//            }
        }

        recordingView!!.sendView.setOnClickListener {
            val msg = (recordingView!!.messageView.text.toString())
            recordingView!!.messageView.setText("")
            val chatMessage = MessageModal()

            if (recordingView!!.reply_layout.visibility == View.GONE) {
                chatMessage.userId = userLoginId
                chatMessage.userName = userLoginName
                chatMessage.messType = 1
                chatMessage.msgContentType = 1
                chatMessage.androidMessage = msg.encodeToB64()
                chatMessage.messStatus = 1
                chatMessage.dateTimestamp = (System.currentTimeMillis() / 1000).toString()
                chatMessage.dateTime = currentDateTime()
                chatMessage.message = msg
                chatMessage.images = ""
                chatMessage.audios = ""
            } else {

                chatMessage.userId = userLoginId
                chatMessage.userName = userLoginName
                chatMessage.messType = 1
                chatMessage.msgContentType = 1
                chatMessage.androidMessage = msg.encodeToB64()
                chatMessage.messStatus = 1
                chatMessage.dateTimestamp = (System.currentTimeMillis() / 1000).toString()
                chatMessage.dateTime = currentDateTime()
                chatMessage.message = msg
                chatMessage.images = ""
                chatMessage.audios = ""
                chatMessage.replyMessage = selectedMessageForReply.message
                chatMessage.replyName = selectedMessageForReply.userName
            }
            chatMessageList.add(chatMessage)
            recentChatMessageList.add(chatMessage)

            emitMessage(chatMessage)

            chatAdapter!!.notifyDataSetChanged()
            if (chatMessageList.size > 0) {
                rvChatMessage.scrollToPosition(chatAdapter!!.itemCount - 1)
            }

        }

        emojiButton = recordingView!!.emojiView as ImageView
        emojiButton!!.setOnClickListener { ignore -> emojiPopup!!.toggle() }
        setUpEmojiPopup()

    }

    private fun setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(recordingView!!.emojiView)
            .setOnEmojiBackspaceClickListener { ignore -> Log.d("TAG", "Clicked on Backspace") }
            .setOnEmojiClickListener { ignore, ignore2 -> Log.d("ATG", "Clicked on emoji") }
            .setOnEmojiPopupShownListener { emojiButton!!.setImageResource(R.drawable.icf_keyboard_key) }
            .setOnSoftKeyboardOpenListener { ignore -> Log.d("TAG", "Opened soft keyboard") }
            .setOnEmojiPopupDismissListener { emojiButton!!.setImageResource(R.drawable.icf_happy_emoji) }
            .setOnSoftKeyboardCloseListener { Log.d("TAG", "Closed soft keyboard") }
            .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
            .setPageTransformer(PageTransformer())
            .build(recordingView!!.messageView)
    }

    private val recordingViewListener = object : AudioRecordView.RecordingListener {

        override fun onRecordingStarted() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Check if the permission has been granted
                if (ContextCompat.checkSelfPermission(
                        this@ChatBoardActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // If not granted, request the permission
                    ActivityCompat.requestPermissions(
                        this@ChatBoardActivity,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        RECORD_AUDIO_PERMISSION_REQUEST_CODE
                    )
                    return;
                } else {
                }
            } else {
            }
            myAudioRecorder = MediaRecorder()
            myAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            myAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            myAudioRecorder!!.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)

            //showToast("started")
            val timeMillis = System.currentTimeMillis().toString()
            recordedAudioFile = "$audioPath/kaudio$timeMillis.mp3"
            myAudioRecorder!!.setOutputFile(recordedAudioFile)

            try {
                myAudioRecorder!!.prepare()
                try {
                    myAudioRecorder!!.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (ise: IllegalStateException) {
                // make something ...
            } catch (ioe: IOException) {
                // make something
            } catch (ioe: Exception) {
                // make something
            }


            time = System.currentTimeMillis() / 1000
        }

        override fun onRecordingLocked() {
            //showToast("locked")
        }

        override fun onRecordingCompleted() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Check if the permission has been granted
                if (ContextCompat.checkSelfPermission(
                        this@ChatBoardActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                return;
                }
            }
            if (myAudioRecorder != null) {
                myAudioRecorder!!.release()
                myAudioRecorder = null
            }

            val recordTime = (System.currentTimeMillis() / 1000 - time).toInt()

            if (recordTime > 1) {
                //messageAdapter.add(Message(recordTime))
                val chatMessage = MessageModal()
                chatMessage.userId = userLoginId
                chatMessage.userName = userLoginName
                chatMessage.messType = 1
                chatMessage.msgContentType = 4
//                chatMessage.file = "audio"
                chatMessage.androidMessage = ""
                chatMessage.messStatus = 1
                chatMessage.dateTimestamp = (System.currentTimeMillis() / 1000).toString()
                chatMessage.msgFile = recordedAudioFile
                chatMessage.messAudioDuration = recordTime
                chatMessage.dateTime = currentDateTime()

//                dbHelper!!.addMessageToDb(chatMessage)
                chatMessageList.add(chatMessage)
                chatAdapter!!.notifyDataSetChanged()

                if (chatMessageList.size > 0) {
                    rvChatMessage.scrollToPosition(chatAdapter!!.itemCount - 1)
                }

                uploadAudioToServer(chatMessage, File(recordedAudioFile))

            }
        }

        override fun onRecordingCanceled() {
//            showToast("canceled")
            if (myAudioRecorder != null) {
                myAudioRecorder!!.release()
                myAudioRecorder = null
            }
            //delete generated output file
            val rFile = File(recordedAudioFile!!)
            if (rFile.exists()) {
                FileUtils.forceDelete(rFile)
            }
        }


    }


    private fun currentDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm a");

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        return formatter.format(calendar.time)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public override fun onPause() {
        super.onPause()
        try {
            chatAdapter!!.stopPlayer()
        } catch (e: Exception) {
        }
        if (myAudioRecorder != null) {
            myAudioRecorder!!.release()
            myAudioRecorder = null
        }

        val jsonObject = JSONObject()
        jsonObject.put("userId", userLoginId)
        jsonObject.put("status", "Offline")
        mSocket!!.emit("userOnlineStatus", jsonObject)

        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mMessageReceiver)
    }


    private fun uploadImageToServer(
        chatMessage: MessageModal,
        currentCompressedImageFile: File
    ) {
        //currentCompressedImageFile
        val bodyProfilePicture = RequestBody.create(
            "image/*".toMediaTypeOrNull(),
            currentCompressedImageFile
        )
        val imageFilePart = MultipartBody.Part.createFormData(
            "productImage",
            currentCompressedImageFile!!.name, bodyProfilePicture
        )
        if (cd!!.isNetworkAvailable) {
            RetrofitServiceFileUpload.fileUploadResponse(this,
                retrofitApiClientFile!!.uploadMediaFile(imageFilePart), object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val mediaFileModel = result!!.body() as MediaFileModel
                        chatMessage.messFileUrl = mediaFileModel.filename
                        chatMessage.images = "chat/images/chat_images/" + mediaFileModel.filename
                        chatMessage.audios = ""

                        emitMessage(
                            chatMessage
                        )
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(mContext, error.toString())
                    }
                })
        }

    }

    private fun uploadAudioToServer(chatMessage: MessageModal, audioFile: File) {
        //currentCompressedImageFile
        val bodyProfilePicture = RequestBody.create("audio/*".toMediaTypeOrNull(), audioFile)
        val imageFilePart = MultipartBody.Part.createFormData(
            "productImage",
            audioFile.name, bodyProfilePicture
        )

        if (cd!!.isNetworkAvailable) {
            RetrofitServiceFileUpload.fileUploadResponse(null,
                retrofitApiClientFile!!.uploadMediaFile(imageFilePart), object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val mediaFileModel = result!!.body() as MediaFileModel
                        chatMessage.messFileUrl = mediaFileModel.filename
                        chatMessage.message = ""
                        chatMessage.images = ""
                        chatMessage.audios = "chat/images/chat_images/" + mediaFileModel.filename

                        emitMessage(
                            chatMessage
                        )
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(mContext, error.toString())
                    }
                })
        }

    }

    fun String.encodeToB64(): String {
        return android.util.Base64.encodeToString(
            this.toByteArray(charset("UTF-8")),
            android.util.Base64.DEFAULT
        )
    }


    //step socket for chat
    private fun initSocketSetup() {
        try {
            mSocket = (application as? ChatApplication)?.getSocket()
            if (mSocket!!.connected()) {
                mSocket!!.on(ChatConstants.KEY_ADMIN_ONLINE_STATUS_RETURN, adminOnlineStatus)
                //request current Admin status
                mSocket!!.emit(ChatConstants.KEY_ADMIN_ONLINE_STATUS, userLoginId)
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

    }


    private fun stopSocketConnections() {
        if (mSocket!!.connected()) {
            mSocket!!.off(ChatConstants.KEY_ADMIN_ONLINE_STATUS_RETURN, adminOnlineStatus)
            setMyOfflineStatus()
        }
    }

    private fun setMyOfflineStatus() {
        val jsonObject = JSONObject()
        jsonObject.put("userId", userLoginId)
        jsonObject.put("status", ChatConstants.MY_ONLINE_STATUS)
        mSocket!!.emit(ChatConstants.KEY_USER_ONLINE_STATUS_RETURN, jsonObject)
    }

    private val adminOnlineStatus = Emitter.Listener { args ->
        runOnUiThread {
            try {
                Log.e("SOCKET", "Getting current admin online status")
                val onlineStatus = args[0] as String
                Log.e("SOCKET", "Getting current admin online status" + onlineStatus)
                //Admin is online
                tvUserStatus.visibility = View.VISIBLE
                tvUserStatus.text = onlineStatus;
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        getUpiFromAdmin()
        (application as? ChatApplication)?.onSocketListener = this@ChatBoardActivity
        ChatConstants.MY_ONLINE_STATUS = "Online"
        initSocketSetup()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            (mMessageReceiver),
            IntentFilter("Notification")
        );

    }


    override fun onDestroy() {
        ChatConstants.MY_ONLINE_STATUS = "Offline"
        ChatConstants.CHAT_COUNTER = 0
        stopSocketConnections()
        (application as? ChatApplication)?.onSocketListener = null
        super.onDestroy()

    }

    override fun onReceiveMsg(msgData: MessageModal) {
//        Toast.makeText(this@ChatBoardActivity, msgData, Toast.LENGTH_SHORT).show()
        if (msgData != null) {
            if (msgData.messType == 2) {
                chatMessageList.add(msgData)

                chatAdapter!!.notifyDataSetChanged()
            }
            if (chatMessageList.size > 0) {
                rvChatMessage.scrollToPosition(chatAdapter!!.itemCount - 1)
            }
        }


    }

    override fun onDeleteUserMsg(msgData: String) {
//        Toast.makeText(this@ChatBoardActivity, msgData, Toast.LENGTH_SHORT).show()
        chatMessageList.clear();
        chatAdapter!!.notifyDataSetChanged()
    }

    override fun onDeleteAllMsg(msgData: String) {
//        Toast.makeText(this@ChatBoardActivity, msgData, Toast.LENGTH_SHORT).show()
        chatMessageList.clear();
        chatAdapter!!.notifyDataSetChanged()
    }

    override fun onDeleteSingleMsg(msgid: String) {

        for (i in 0..chatMessageList.size) {
            if (msgid.equals(chatMessageList.get(i).id)) {
                chatMessageList.get(i).messStatus = 3
                dbHelper!!.updateMessageInDbById(chatMessageList.get(i))
                chatAdapter!!.notifyItemChanged(i)
                Log.e("SOCKET", "chat view delete single")
            }
        }
    }

    /*Send message to socket*/
    private fun emitMessage(
        chatMessage: MessageModal
    ) {
        hideReplyLayout()

        val jsonObject = JSONObject()
        try {
            jsonObject.put("userIdMess", chatMessage.userId)
            jsonObject.put("userName", chatMessage.userName)
            jsonObject.put("adminMessage", chatMessage.message)
            jsonObject.put("imageUrl", chatMessage.images)
            jsonObject.put("audioUrl", chatMessage.audios)
            jsonObject.put("messType", chatMessage.messType)
            jsonObject.put("dateTime", chatMessage.dateTime)
            jsonObject.put("dateTimestamp", chatMessage.dateTimestamp)
            jsonObject.put("replyName", chatMessage.replyName)
            jsonObject.put("replyMessage", chatMessage.replyMessage)
            mSocket!!.emit(ChatConstants.KEY_SEND_MSG, jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }


    private fun getUsersOldMessage() {
        val mObject = JSONObject()
        mObject.put("messId", lastmessage_id)
        mObject.put("userId", userLoginId)
        mObject.put("broMessId", broMessId)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        ChatRetrofitService.getServerResponse(
            this@ChatBoardActivity,
            null,
            ChatRetrofitService.getRetrofit()?.getOldMessages(body),
            object : WebResponse {
                @SuppressLint("SetTextI18n")

                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    val jsonObject = JSONObject(response.string())
                    var gson = Gson()
                    var messageModallist =
                        gson.fromJson(jsonObject.toString(), GetOldMessages::class.java)
                    println("HELLOOO     :" + jsonObject.toString())
                    if (jsonObject.getBoolean("status")) {
                        for (msgData in messageModallist.messageList) {
                            if (!msgData!!.images.isEmpty() && !msgData!!.androidMessage.isEmpty()) {
                                Log.e("SOCKET", "image+text message")
                                msgData.msgContentType = 3; // image+text message
                            } else if (!msgData!!.images.isEmpty() && msgData!!.androidMessage.isEmpty() && msgData!!.audios.isEmpty()) {
                                Log.e("SOCKET", "image")
                                msgData.msgContentType = 2; // image
                            } else if (msgData!!.images.isEmpty() && !msgData!!.androidMessage.isEmpty() && msgData!!.audios.isEmpty() && msgData!!.videos.isNullOrEmpty()) {
                                Log.e("SOCKET", "text message")
                                msgData.msgContentType = 1; // text
                            } else if (msgData!!.images.isEmpty() && msgData!!.androidMessage.isEmpty() && !msgData!!.audios.isEmpty()) {
                                Log.e("SOCKET", "audio message")
                                msgData.msgContentType = 4; // audio
                            } else if (!msgData!!.videos.isNullOrEmpty()) {
                                Log.e("SOCKET", "videos message")
                                msgData.msgContentType = 6; // videos
                            }

                            if (msgData.messStatus != 3) {
                                chatMessageList.add(msgData)
                                dbHelper!!.addMessageToDb(msgData)
                            } else {
                                dbHelper!!.updateMessageInDbById(msgData)
                                for (i in 0..chatMessageList.size - 1) {
                                    if (msgData.id.equals(chatMessageList.get(i).id)) {
                                        chatMessageList.set(i, msgData)
                                    }
                                }
                            }


                        }
                        for (deletedmsgData in messageModallist.deleteMessageList) {
                            for (i in 0..chatMessageList.size - 1) {
                                if (deletedmsgData.id.equals(chatMessageList.get(i).id)) {
                                    chatMessageList.get(i).messStatus = 3
                                    dbHelper!!.updateMessageInDbById(chatMessageList.get(i))
                                }
                            }
                        }

                        if (messageModallist.allMessageListStatus) {
                            chatMessageList.clear();
                            dbHelper!!.deleteAllChatFromDb()
                        }
                        notifyBroadcastMessage(messageModallist.oldBroadcastMessage)
                        finalizeMessageList()

                    }

                }

                override fun onResponseFailed(error: String?) {
                    Log.e("RESPINSE", error.toString())
                    Alerts.serverError(this@ChatBoardActivity, error.toString())
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ChatConstants.CHAT_COUNTER = 0
    }


    fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {
        val timeStamp: String = "T-" + System.currentTimeMillis() / 1000
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .appendQueryParameter("tr", "")
            .appendQueryParameter("tid", "")
            .build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri
        val appNames = arrayListOf<String>()

        val appList = ChatApplication.context.packageManager.queryIntentActivities(upiPayIntent, 0)

        for (i in appList) {
            if (i.loadLabel(ChatApplication.context.packageManager).toString().toLowerCase()
                    .equals("google pay")
            )
                appNames.add(
                    i.loadLabel(ChatApplication.context.packageManager).toString().toLowerCase()
                )
        }

//        if (upiPayIntent.resolveActivity(requireContext().packageManager) != null)
        if (!appNames.isEmpty())
            startUpiBottomSheet(uri, appNames)
        else {
            Alerts.AlertDialogUPIApp(
                this@ChatBoardActivity!!,
                "No UPI app found,\nPlease Download Google-Pay app to continue"
            )
        }
        /* // will always show a dialog to user to choose an app
         val chooser = Intent.createChooser(upiPayIntent, "Pay with")

         // check if intent resolves
         if (null != chooser.resolveActivity(packageManager)) {
             startActivityForResult(chooser, UPI_PAYMENT)
         } else {
             Toast.makeText(
                 this@ChatBoardActivity,
                 "No UPI app found, please install one to continue",
                 Toast.LENGTH_SHORT
             ).show()
         }*/

    }

    private fun startUpiBottomSheet(
        uri: Uri,
        appNames: ArrayList<String>
    ) {

        val bundle = Bundle()
        bundle.putString("uri", uri.toString())
        bundle.putStringArrayList("ARG_UPI_APPS_LIST", appNames)

        val upiBottomSheet = UpiBottomSheet()
        upiBottomSheet.arguments = bundle
        upiBottomSheet.setListener(object : UpiBottomSheet.OnUpiTypeSelectedListener {
            override fun onUpiAppClosed() {

            }

            override fun onUpiAppSelected(data: ResolveInfo) {
                selectedApp = data.loadLabel(packageManager).toString().toLowerCase()
                val paymentIntent = Intent(Intent.ACTION_VIEW)
                paymentIntent.data = uri
                paymentIntent.setPackage(data.activityInfo.packageName)
                startActivityForResult(paymentIntent, UPI_PAYMENT)
            }
        })
        upiBottomSheet.isCancelable = false
        upiBottomSheet.show(supportFragmentManager, UpiBottomSheet::class.java.simpleName)
    }

    internal val UPI_PAYMENT = 0


    companion object {

        const val UPI_PAYMENT_REQUEST_CODE = 201
        const val ARG_UPI_APPS_LIST = "upi.apps.list"

        /**
         * default selected upi apps
         */
        @JvmStatic
        val UPI_APPS: ArrayList<String> =
            arrayListOf("google pay", "paytm", "imobile", "axis mobile")

        fun isConnectionAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val netInfo = connectivityManager.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected
                    && netInfo.isConnectedOrConnecting
                    && netInfo.isAvailable
                ) {
                    return true
                }
            }
            return false
        }
    }

    private fun getUpiFromAdmin() {

        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val upiLastId = AppPreference.getStringPreference(mContext, Constant.UPI_LAST_ID)
            val mObject = JSONObject()
            mObject.put("last_id", upiLastId)

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getUPI(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())

                        if (jsonresponse.optInt("status") == 1) {
                            val id = jsonresponse.optString("id")
                            adminUpiID = jsonresponse.optString("data")
                            AppPreference.setStringPreference(mContext, Constant.UPI_LAST_ID, id)
                            AppPreference.setStringPreference(
                                mContext,
                                Constant.UPI_Name,
                                adminUpiID
                            )
//                            tv_add_fund.visibility = View.VISIBLE
                        } else {
//                            Alerts.show(this@ChatBoardActivity, jsonresponse.optString("message"))
//                            tv_add_fund.visibility = View.GONE
                        }


                    }

                    override fun onResponseFailed(error: String?) {
//                        tv_add_fund.visibility = View.GONE
                        Alerts.serverError(this@ChatBoardActivity, error.toString())
                    }

                })
        }
    }


    private fun autoPaymentApi(
        transactiondId: String,
        reference_id: String,
        amount: String,
        paymentStatus: String
    ) {

        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("userId", userLoginId)
            mObject.put("transactiondId", transactiondId)
            mObject.put("reference_id", reference_id)
            mObject.put("amount", amount)
            mObject.put("transaction_mode", "UPI")
            mObject.put("UPI_name", AppPreference.getStringPreference(mContext, Constant.UPI_Name))
            mObject.put("UPI_id", AppPreference.getStringPreference(mContext, Constant.UPI_LAST_ID))
            mObject.put("payment_status", paymentStatus)
            mObject.put("app", selectedApp)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@ChatBoardActivity),
                retrofitApiClientAuth.autoPaymentUpi(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        fundrequestamount = ""
                        Alerts.AlertDialogSuccess(
                            this@ChatBoardActivity,
                            jsonresponse.optString("message")
                        )

                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@ChatBoardActivity, error.toString())
                        fundrequestamount = ""
                    }

                }
            )
        }
    }

//    private fun dialogBoxMessage(string: String, s: String) {
//        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
//        val inflater = this.layoutInflater
//        val dialogView = inflater.inflate(R.layout.dialog_view_toast_message, null)
//        dialogBuilder?.setView(dialogView)
//        if (s == "submit") {
//            dialogBuilder?.setCancelable(false)
//        } else {
//            dialogBuilder?.setCancelable(true)
//        }
//        val alertDialog = dialogBuilder?.create()
//        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
//        dialogView.txtMessage.text = string
//        val btnSubmit = dialogView.btnOk
//        btnSubmit.setOnClickListener {
//            if (s == "submit") {
//                alertDialog.dismiss()
//                finish()
//            } else {
//                alertDialog.dismiss()
//            }
//        }
//
//        alertDialog.show()
//    }


    private fun dialogRequestFund() {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_request_fund, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)
        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val btnSubmit = dialogView.btnOk
        val etRequestPoints = dialogView.et_fund
        btnSubmit.setOnClickListener {
            fundrequestamount = etRequestPoints.text.toString().trim()
            getMinMaxFundingAmount(etRequestPoints.text.toString().trim())


            alertDialog.dismiss()

        }

        alertDialog.show()
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }


    private fun getMinMaxFundingAmount(amount: String) {

//        getPaymentMode(amount)

        val userObject = JSONObject()
        userObject.put("amount", amount)

        if (cd!!.isNetworkAvailable) {
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (userObject).toString()
            )
            RetrofitService.getServerResponse(this,
                Dialog(this), retrofitApiClient!!.getMinMaxFunding(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        var msg: ResponseBody = result!!.body() as ResponseBody

                        try {
                            val responseObject = JSONObject(msg.string())
                            if (responseObject.getInt("status") == 1) {

                                getPaymentMode(amount)


                            } else
                                Alerts.AlertDialogWarning(
                                    this@ChatBoardActivity,

                                    responseObject.getString("message")
                                )
                        } catch (e: Exception) {
                            Log.e("ERROR", e.message.toString())
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@ChatBoardActivity, error.toString())
                    }
                })
        }
    }


    val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val type = intent!!.getStringExtra("TYPE")

            if (type.equals("broadcast")) {
                getUsersOldMessage()
            }

        }
    }


    private fun notifyBroadcastMessage(oldBroadcastMessage: List<MessageModal>) {
        for (i in 0..oldBroadcastMessage.size - 1) {
            runOnUiThread {
                try {
                    val my_userid =
                        AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
                    Log.e("SOCKET", "Receive Message")
                    Log.e("SOCKET", oldBroadcastMessage.get(i).id)
//                    {"_id":"5e4d1f37e46d280018b3dfc5","message":"tsetsete","dateTime":"2020-02-19 05:12 pm","dateTimestamp":"1582112567"}
                    var messageModallist = OnReceiveNewMessage()
                    val msgData = messageModallist!!.messageNew


                    if (!oldBroadcastMessage.get(i)!!.images.isEmpty() && !oldBroadcastMessage.get(i)!!.message.isEmpty()) {
                        Log.e("SOCKET", "image+text message")
                        msgData.msgContentType = 3; // image+text message
                    } else if (!oldBroadcastMessage.get(i)!!.images.isEmpty() && oldBroadcastMessage.get(
                            i
                        )!!.message.isEmpty() && oldBroadcastMessage.get(i)!!.audios.isEmpty()
                    ) {
                        Log.e("SOCKET", "image")
                        msgData.msgContentType = 2; // image
                    } else if (oldBroadcastMessage.get(i)!!.images.isEmpty() && !oldBroadcastMessage.get(
                            i
                        )!!.message.isEmpty() && oldBroadcastMessage.get(i)!!.audios.isEmpty() && oldBroadcastMessage.get(
                            i
                        )!!.videos.isNullOrEmpty()
                    ) {
                        Log.e("SOCKET", "text message")
                        msgData.msgContentType = 1; // text
                    } else if (oldBroadcastMessage.get(i)!!.images.isEmpty() && !oldBroadcastMessage.get(
                            i
                        )!!.audios.isEmpty()
                    ) {
                        Log.e("SOCKET", "audio message")
                        msgData.msgContentType = 4; // audio
                    } else if (!msgData!!.videos.isNullOrEmpty()) {
                        Log.e("SOCKET", "videos message")
                        msgData.msgContentType = 6; // videos
                    }

                    if (!oldBroadcastMessage.get(i)!!.audios.isEmpty()) {
                        msgData.msgContentType = 4;
                    }


                    msgData.id = oldBroadcastMessage.get(i).id
                    msgData.userId = my_userid
                    msgData.userName = "Brand Name"
                    msgData.messStatus = 0
                    msgData.messType = 5
                    msgData.androidMessage = oldBroadcastMessage.get(i).message!!.encodeToB64()
                    msgData.dateTime = oldBroadcastMessage.get(i).dateTime
                    msgData.dateTimestamp = oldBroadcastMessage.get(i).dateTimestamp
                    msgData.msgFile = null
                    msgData.message = oldBroadcastMessage.get(i).message
                    msgData.audios = oldBroadcastMessage.get(i).audios
                    msgData.images = oldBroadcastMessage.get(i).images
                    msgData.videos = oldBroadcastMessage.get(i).videos
                    msgData.replyMessage = ""
                    msgData.replyName = ""
                    msgData.messAudioDuration = 0
//                    msgData.msgContentType = 1
                    msgData.messFileUrl = ""
                    if (msgData.userId.equals(my_userid)) {
                        dbHelper!!.addMessageToDb(msgData)
                        chatMessageList.add(msgData)

                        /* if ((application as? ChatApplication)?.onSocketListener != null) {
                             onSocketListener = (application as? ChatApplication)?.onSocketListener
                             onSocketListener!!.onReceiveMsg(msgData)
                         }
                         if (msgData.messType == 5) {
                             ChatConstants.CHAT_COUNTER = ChatConstants.CHAT_COUNTER + 1;
                             manageChatCounter()
                         }*/
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("SOCKET", "Welcome Message : " + e.message)
                }
            }
        }
    }

    private fun getPaymentMode(amount: String) {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@ChatBoardActivity),
                retrofitApiClientAuth.paymentMode,
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())

                        if (jsonresponse.optInt("status") == 1) {

                            val paymentMode = Gson().fromJson(
                                jsonresponse.toString(),
                                PaymentMode::class.java
                            )

                            if (paymentMode.data.size == 2) {
                                dialogPaymentMode(paymentMode, amount)

                            } else if (paymentMode.data.size == 1) {
                                for (paymentTypes in 0 until paymentMode.data.size) {
                                    if (paymentMode.data.get(paymentTypes).getMode().contains(
                                            "upi",
                                            true
                                        )
                                    ) {
                                        payUsingUpi(
                                            amount,
                                            adminUpiID,
                                            "Brand Name",
                                            "Fund Request"
                                        )
                                    }
                                    if (paymentMode.data.get(paymentTypes).getMode().contains(
                                            "net banking",
                                            true
                                        )
                                    ) {
                                        val url =
                                            paymentMode.data.get(paymentTypes).redirectURL.toString()
                                        startActivityForResult(
                                            Intent(
                                                this@ChatBoardActivity,
                                                NetBankingActivity::class.java
                                            ).putExtra("URL", url), 1000
                                        )

                                    }
                                }

                            } else {
                                Alerts.AlertDialogWarning(
                                    this@ChatBoardActivity,
                                    jsonresponse.optString("message")
                                )
                            }


                        } else {
                            Alerts.AlertDialogWarning(
                                this@ChatBoardActivity,
                                jsonresponse.optString("message")
                            )

                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.show(this@ChatBoardActivity, "Server Error" + error!!)
                    }

                })
        }
    }

    private fun dialogPaymentMode(
        paymentmode: PaymentMode,
        amount: String
    ) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_payment_mode, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)

        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val ll_netbanking = dialogView.rb_netbanking
        val ll_upi = dialogView.rb_upi
        val iv_close = dialogView.iv_cancel

        ll_upi.visibility = View.GONE
        ll_netbanking.visibility = View.GONE

        var url = " "
        for (paymentTypes in 0 until paymentmode.data.size) {
            if (paymentmode.data.get(paymentTypes).getMode().contains("upi", true)) {
                ll_upi.visibility = View.VISIBLE
            }
            if (paymentmode.data.get(paymentTypes).getMode().contains("net banking", true)) {
                url = paymentmode.data.get(paymentTypes).redirectURL.toString()
                ll_netbanking.visibility = View.VISIBLE
            }
        }


        ll_upi.setOnClickListener {
            alertDialog.dismiss()
            payUsingUpi(
                amount,
                adminUpiID,
                "Brand Name",
                "Fund Request"
            )
        }
        ll_netbanking.setOnClickListener {
            alertDialog.dismiss()
            startActivityForResult(
                Intent(this@ChatBoardActivity, NetBankingActivity::class.java).putExtra(
                    "URL",
                    url
                ), 1000
            )

        }

        iv_close.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}

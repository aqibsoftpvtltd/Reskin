package com.kasa777.utils.sms_retriever

import android.app.Application


/**
 * Created by Chintan on 01-11-2017.
 */
class SMSRetrieverApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        /*Following will generate the hash code*/
        var appSignature = AppSignatureHelper(this)
        appSignature.appSignatures




    }



}
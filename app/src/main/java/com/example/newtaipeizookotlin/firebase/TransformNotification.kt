package com.example.newtaipeizookotlin.firebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.newtaipeizookotlin.MainActivity
import com.example.newtaipeizookotlin.tools.UtilCommonStr

class TransformNotification : BroadcastReceiver() {

    private var mIntent = Intent()
    private var mBundle = Bundle()

    override fun onReceive(context: Context?, intent: Intent?) {
        val iFirebasePageTitle = intent?.extras?.getString( UtilCommonStr.getInstance().mFirebasePageTitle) ?: ""
        val iFirebasePageCode = intent?.extras?.getInt("FirebasePageCode") ?: -1


        mIntent = Intent(context, MainActivity::class.java)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mBundle.putString("TransformNotificationFirebasePageTitle", iFirebasePageTitle)
        mBundle.putInt("TransformNotificationFirebasePageCode", iFirebasePageCode)
        mBundle.putBoolean("TransformNotificationFromFirebase", true)
        mIntent.putExtras(mBundle)
        context?.startActivity(mIntent)
    }
}
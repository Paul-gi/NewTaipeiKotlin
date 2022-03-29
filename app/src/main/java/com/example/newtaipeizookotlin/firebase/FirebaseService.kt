package com.example.newtaipeizookotlin.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.tools.UtilTools
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseService : FirebaseMessagingService() {

    //FCM

    //https://medium.com/@eason2019tw/%E9%80%8F%E9%81%8Epostman-%E5%82%B3%E9%80%81%E9%80%9A%E7%9F%A5-fcm-bd4ed84f4a70
    //tool -> fcm -> remote -> connect firebase

    //取得fcm token
    //FirebaseMessaging.getInstance().subscribeToTopic("news")
    //        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
    //            if (!task.isSuccessful) {
    //                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
    //                return@OnCompleteListener
    //            }
    //            // Get new FCM registration token
    //            val token = task.result
    //            Log.d("firebaseToken", token.toString())
    //        })
    //    }

    //Authorization key在 firebase專案設定的伺服器金鑰 ＡＡＡＡ.....

    //manifest 加入權限
    // <service
    //            android:name="com.example.newtaipeizookotlin.firebase.FirebaseService"
    //            android:exported="false">
    //            <intent-filter>
    //                <action android:name="com.google.firebase.MESSAGING_EVENT" />
    //            </intent-filter>
    //        </service>

    //走broadCast記得在 manifest加入<receiver android:name="com.example.newtaipeizookotlin.firebase.TransformNotification"/>




    private val mUtilTools = UtilTools()
    private val mChannelID = "notificationChannelID"
    private val mChannelName = "com.example.taipeizookotlin"
    private var mBundle = Bundle()
    private var mIntent = Intent()
    private lateinit var iPowerManager: PowerManager
    private var mFirebasePageTitle = ""
    private var mFirebasePageCode = -1


    override fun onNewToken(mToken: String) {
        super.onNewToken(mToken)
        Log.d(mToken, "getToken")
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(pRemoteMessage: RemoteMessage) {
        super.onMessageReceived(pRemoteMessage)

        mFirebasePageTitle = pRemoteMessage.data["Title"] ?: ""
        mFirebasePageCode = pRemoteMessage.data["PageCode"]?.toInt() ?: -1


        iPowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        mUtilTools.wakeUpPhone(iPowerManager)

        checkNotification(mFirebasePageTitle, mFirebasePageCode)
    }


    /**
     * mFirebaseHavePageCode = true 代表有PageCode跟TitleName 所以走詳細頁面
     * 如果沒有PageCode但是有TitleName 就走列表頁
     * 否則走一般的流程
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun checkNotification(pTitle: String, pPageCode: Int) {
        mIntent = Intent(this, TransformNotification::class.java)
        mBundle.putString("FirebasePageTitle", mFirebasePageTitle)
        mBundle.putInt("FirebasePageCode", mFirebasePageCode)
        mIntent.putExtras(mBundle)
        sendNotification(pTitle, pPageCode)
    }

    @SuppressLint("UnspecifiedImmutableFlag", "LaunchActivityFromNotification")
    private fun sendNotification(pTitle: String, pMessage: Int) {
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        /**
         * PendingIntent.getActivity 正常activity流程
         * PendingIntent.getBroadcast走廣播改造流程
         */
//        val pendingIntent =
//            PendingIntent.getActivity(
//                this,
//                0,
//                mIntent, PendingIntent.FLAG_UPDATE_CURRENT
//            )
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                mIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )


        /**
         * 兩種客製化版本
         */
//        var iNotification = NotificationCompat.Builder(this, mChannelID)
//            .setSmallIcon(R.drawable.logo)
////            .setContentTitle(pTitle)
////            .setContentText(pMessage.toString())
//            //.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.button_radius))
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//            .setCustomContentView(getRemoteView(pTitle, pMessage?.toString()))
//            .setCustomBigContentView(getRemoteView(pTitle, pMessage?.toString()))
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)

        val iNotification = NotificationCompat.Builder(applicationContext, mChannelID)
            //.setWhen(System.currentTimeMillis())
            .setContent(getRemoteView(pTitle, pMessage.toString()))
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(pTitle)
            .setContentText(pMessage.toString())
            //.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.button_radius))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)


        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val iNotificationChannel =
                NotificationChannel(mChannelID, mChannelName, NotificationManager.IMPORTANCE_HIGH)

            mNotificationManager.createNotificationChannel(iNotificationChannel)
        }
        mNotificationManager.notify(Random.nextInt(5), iNotification.build())
    }


    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(pTitle: String, pMessage: String?): RemoteViews {
        val iRemoteView = RemoteViews(packageName, R.layout.fcm_remoteview)
        iRemoteView.setTextViewText(R.id.mTitle, pTitle)
        iRemoteView.setTextViewText(R.id.mMessage, pMessage)
        iRemoteView.setImageViewResource(R.id.mSmallIcon, R.drawable.logo)
        return iRemoteView
    }
}
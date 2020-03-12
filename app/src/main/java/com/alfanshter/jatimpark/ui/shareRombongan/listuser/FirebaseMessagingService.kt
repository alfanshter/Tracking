package com.alfanshter.jatimpark.ui.shareRombongan.listuser

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.alfanshter.jatimpark.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val messageTitle = remoteMessage.notification!!.title
        val messageBody = remoteMessage.notification!!.body

        val click_action = remoteMessage.notification!!.clickAction

        val dataMessage = remoteMessage.data["message"]
        val dataFrom = remoteMessage.data["from_user_id"]


        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)

        val resultIntent = Intent(click_action)
        resultIntent.putExtra("message", dataMessage)
        resultIntent.putExtra("from_user_id", dataFrom)

        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT
        )

        mBuilder.setContentIntent(resultPendingIntent)


        val mNotificationId = System.currentTimeMillis().toInt()

        val mNotifyMgr =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotifyMgr.notify(mNotificationId, mBuilder.build())


    }

}
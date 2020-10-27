package id.co.gradien.tepav.app

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import id.co.gradien.tepav.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseMessagingServce"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var notificationTitle: String? = null
        var notificationBody: String? = null

        // Check if message contains a notification payload
        if (remoteMessage.notification != null) {
            Log.d(
                TAG, "Message Notification Body: " + remoteMessage.notification!!
                    .body
            )
            notificationTitle = remoteMessage.notification!!.title
            notificationBody = remoteMessage.notification!!.body
        }

        // If you want to fire a local notification (that notification on the top of the phone screen)
        // you should fire it from here
        sendLocalNotification(notificationTitle, notificationBody)
    }

    private fun sendLocalNotification(notificationTitle: String?, notificationBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, "channelMain")
            .setAutoCancel(true) //Automatically delete the notification
            .setSmallIcon(R.drawable.sym_def_app_icon) //Notification icon
            .setContentIntent(pendingIntent)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("NEW_TOKEN",p0)
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Token perangkat ini: $token")
    }

}
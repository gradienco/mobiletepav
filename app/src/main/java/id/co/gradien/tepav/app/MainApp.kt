package id.co.gradien.tepav.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import id.co.gradien.tepav.R

class MainApp: Application() {

    private val TAG = "MAIN APP"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()

        FirebaseMessaging.getInstance().subscribeToTopic("promotion")
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(baseContext, "FAILED to subscribe topic", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d(TAG, "Subscribe topic success")
                    }
                }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel1 = NotificationChannel(
                "channelMain",
                "Channel Main",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is Main Channel for Tepav"
            channel1.enableVibration(true)
            channel1.enableLights(true)
            channel1.canShowBadge()

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel1)
        }
    }
}
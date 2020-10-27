package id.co.gradien.tepav.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
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
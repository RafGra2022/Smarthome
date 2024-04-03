package itjet.android.smart.greenhouse

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import itjet.android.smart.R

class GreenhouseNotification(private val context: Context) {

    private val CHANNEL_ID : String = "1"
    private val NOTIFICATION_ID : Int = 1

    var hot = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(context.getString(R.string.greenhouse))
        .setContentText(context.getString(R.string.greenhouse_open_door))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "channel", importance).apply {
                description = context.getString(R.string.channel_for_greenhouse)
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun tooHotShow(){
        createNotificationChannel()
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            notify(NOTIFICATION_ID, hot.build())
        }
    }

}
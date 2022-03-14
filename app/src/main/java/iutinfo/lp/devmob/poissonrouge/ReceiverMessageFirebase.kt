package iutinfo.lp.devmob.poissonrouge

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ReceiverMessageFirebase: FirebaseMessagingService()  {
    private val CHANNEL_ID: String = "DEFAULT_CHANNEL"
    private var notificationId: Int = 0

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_desciption)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
          if (remoteMessage.data.isNotEmpty()){
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body, remoteMessage.data)
        }else {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("id", remoteMessage.data["id"])
            startActivity(intent)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(title: String?, body: String?, extra: MutableMap<String, String>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("id", extra["id"].toString())
        intent.putExtra("problem", extra["problem"].toString())
        intent.putExtra("temperature", extra["temperature"].toString())

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.icon_lancement)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId++, builder.build())
        }
    }
}
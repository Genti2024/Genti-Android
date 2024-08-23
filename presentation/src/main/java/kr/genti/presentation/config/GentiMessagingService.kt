package kr.genti.presentation.config

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.presentation.R
import kr.genti.presentation.main.MainActivity
import timber.log.Timber
import java.util.Random

@AndroidEntryPoint
class GentiMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Timber.tag("okhttp").d("NOTIFICATION RECEIVED : $message")

        message.notification?.let {
            sendNotification(
                mapOf(
                    MSG_TITLE to it.title.orEmpty(),
                    MSG_BODY to it.body.orEmpty(),
                ),
            )
        }
    }

    override fun handleIntent(intent: Intent?) {
        intent?.let {
            if (intent.getStringExtra(MSG_TITLE).isNullOrEmpty()) return
            sendNotification(
                mapOf(
                    MSG_TITLE to it.getStringExtra(MSG_TITLE).toString(),
                    MSG_BODY to it.getStringExtra(MSG_BODY).toString(),
                ),
            )
        }
    }

    private fun sendNotification(messageBody: Map<String, String>) {
        val notifyId = Random().nextInt()
        val intent =
            MainActivity.getIntent(this, TYPE_DEFAULT).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                notifyId,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE,
            )
        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId).apply {
                setSmallIcon(R.mipmap.ic_genti_launcher)
                setContentTitle(messageBody[MSG_TITLE])
                setContentText(messageBody[MSG_BODY])
                setAutoCancel(true)
                setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                setContentIntent(pendingIntent)
            }

        getSystemService<NotificationManager>()?.run {
            createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_HIGH,
                ),
            )
            notify(notifyId, notificationBuilder.build())
        }
    }

    companion object {
        private const val MSG_TITLE = "title"
        private const val MSG_BODY = "body"

        const val TYPE_DEFAULT = "TYPE_DEFAULT"
    }
}
